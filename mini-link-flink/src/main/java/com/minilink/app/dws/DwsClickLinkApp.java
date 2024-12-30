package com.minilink.app.dws;

import cn.hutool.json.JSONUtil;
import com.minilink.constant.KafkaConstant;
import com.minilink.pojo.VisitShortLinkLog;
import com.minilink.util.DateTimeUtil;
import com.minilink.util.FlinkKafkaUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

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

    public static void main(String[] args) {
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

        // 水位线处理：3s，
        DataStream unionDS = wideLogDS.union(uniqueVisitorDS);
        unionDS.assignTimestampsAndWatermarks(WatermarkStrategy
                .<VisitShortLinkLog>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                .withTimestampAssigner((event, timestamp) -> DateTimeUtil.localDateTimeToTimeStamp(event.getVisitTime())));


    }
}
