package com.minilink.app.dws;

import cn.hutool.json.JSONUtil;
import com.minilink.app.sink.ClickHouseSink;
import com.minilink.constant.KafkaConstant;
import com.minilink.pojo.VisitShortLinkLog;
import com.minilink.util.DateTimeUtil;
import com.minilink.util.FlinkKafkaUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple9;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-30  14:55
 * @Description: DWS
 * @Version: 1.0
 */
public class DwsClickLinkApp {
    public static final String SOURCE_TOPIC_WIDE_LOG = KafkaConstant.DWM_WIDE_LOG_TOPIC;
    public static final String GROUP_WIDE_LOG = KafkaConstant.DWS_WIDE_LOG_GROUP;
    public static final String SOURCE_TOPIC_UNIQUE_VISITOR = KafkaConstant.DWM_UNIQUE_VISITOR_TOPIC;
    public static final String GROUP_UNIQUE_VISITOR = KafkaConstant.DWS_UNIQUE_VISITOR_GROUP;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        FlinkKafkaConsumer wideLogConsumer = FlinkKafkaUtil.getKafkaConsumer(SOURCE_TOPIC_WIDE_LOG, GROUP_WIDE_LOG);
        DataStreamSource wideLogJsonStr = env.addSource(wideLogConsumer);
        FlinkKafkaConsumer uniqueVisitorConsumer = FlinkKafkaUtil.getKafkaConsumer(SOURCE_TOPIC_UNIQUE_VISITOR, GROUP_UNIQUE_VISITOR);
        DataStreamSource uniqueVisitorJsonStr = env.addSource(uniqueVisitorConsumer);

        SingleOutputStreamOperator wideLogDS = wideLogJsonStr.map(
                new MapFunction<String, VisitShortLinkLog>() {
                    @Override
                    public VisitShortLinkLog map(String msg) throws Exception {
                        VisitShortLinkLog visitShortLinkLog = JSONUtil.toBean(msg, VisitShortLinkLog.class);
                        visitShortLinkLog.setPv(1L);
                        visitShortLinkLog.setUv(0L);
                        return visitShortLinkLog;
                    }
                }
        );

        SingleOutputStreamOperator uniqueVisitorDS = uniqueVisitorJsonStr.map(
                new MapFunction<String, VisitShortLinkLog>() {
                    @Override
                    public VisitShortLinkLog map(String msg) throws Exception {
                        VisitShortLinkLog visitShortLinkLog = JSONUtil.toBean(msg, VisitShortLinkLog.class);
                        visitShortLinkLog.setPv(0L);
                        visitShortLinkLog.setUv(1L);
                        return visitShortLinkLog;
                    }
                }
        );

        DataStream unionDS = wideLogDS.union(uniqueVisitorDS);

        // 水位线策略：超过3s视为乱序，少于3s允许进入窗口计算
        SingleOutputStreamOperator waterMarkDS = unionDS.assignTimestampsAndWatermarks(
                WatermarkStrategy
                        .<VisitShortLinkLog>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner((event, timestamp) -> DateTimeUtil.localDateTimeToTimeStamp(event.getVisitTime()))
        );

        KeyedStream keyedStreamDS = waterMarkDS.keyBy(
                new KeySelector<VisitShortLinkLog, Tuple9<Long, String, String, String, String, String, String, String, String>>() {
                    @Override
                    public Tuple9<Long, String, String, String, String, String, String, String, String> getKey(VisitShortLinkLog log) throws Exception {
                        return Tuple9.of(
                                log.getAccountId(), log.getShortLinkCode(),
                                log.getIp(), log.getProvince(), log.getCity(),
                                log.getBrowserType(), log.getDeviceType(),
                                log.getOsType(), log.getVisitorState()
                        );
                    }
                }
        );

        WindowedStream windowDS = keyedStreamDS.window(TumblingEventTimeWindows.of(Time.seconds(10)));

        SingleOutputStreamOperator reduceDS = windowDS.reduce(
                new ReduceFunction<VisitShortLinkLog>() {
                    @Override
                    public VisitShortLinkLog reduce(VisitShortLinkLog log1, VisitShortLinkLog log2) throws Exception {
                        log1.setPv(log1.getPv() + log2.getPv());
                        log1.setUv(log1.getUv() + log2.getUv());
                        return log1;
                    }
                },

                new ProcessWindowFunction<VisitShortLinkLog, Object, Tuple9<Long, String, String, String, String, String, String, String, String>, TimeWindow>() {
                    @Override
                    public void process(Tuple9<Long, String, String, String, String, String, String, String, String> tuple9,
                                        ProcessWindowFunction<VisitShortLinkLog, Object, Tuple9<Long, String, String, String, String, String, String, String, String>,
                                                TimeWindow>.Context context, Iterable<VisitShortLinkLog> iterable, Collector<Object> collector) throws Exception {
                        String startTime = DateTimeUtil.format(context.window().getStart());
                        String endTime = DateTimeUtil.format(context.window().getEnd());
                        for (VisitShortLinkLog log : iterable) {
                            log.setStartTime(startTime);
                            log.setEndTime(endTime);
                        }
                    }
                }
        );
        reduceDS.print(">>>>>>>>DWS-reduceDS");
        reduceDS.addSink(ClickHouseSink.getJdbcSink("insert into link_visit_stats(account_id, short_link_code, ip, province, city, " +
                "browser_type, device_type, os_type, visitor_state, pv, uv, start_time, end_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?)"));
        env.execute();
    }
}
