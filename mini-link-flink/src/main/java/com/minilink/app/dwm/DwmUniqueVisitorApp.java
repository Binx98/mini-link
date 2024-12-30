package com.minilink.app.dwm;

import cn.hutool.json.JSONUtil;
import com.minilink.app.func.VisitorUniqueRichFilterFunction;
import com.minilink.constant.KafkaConstant;
import com.minilink.pojo.VisitShortLinkLog;
import com.minilink.util.FlinkKafkaUtil;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-30  13:40
 * @Description: DWM-访客去重
 * @Version: 1.0
 */
public class DwmUniqueVisitorApp {
    public static final String SOURCE_TOPIC = KafkaConstant.DWM_WIDE_LOG_TOPIC;
    public static final String SINK_TOPIC = KafkaConstant.DWM_UNIQUE_VISITOR_TOPIC;
    public static final String GROUP = KafkaConstant.DWM_UNIQUE_VISITOR_GROUP;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        FlinkKafkaConsumer kafkaConsumer = FlinkKafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP);
        DataStreamSource jsonStrDS = env.addSource(kafkaConsumer);
        jsonStrDS.print(">>>>>>>>DWM-jsonStrDS");

        SingleOutputStreamOperator<VisitShortLinkLog> wideLogDS = jsonStrDS.map(
                new MapFunction<String, VisitShortLinkLog>() {
                    @Override
                    public VisitShortLinkLog map(String msg) throws Exception {
                        return JSONUtil.toBean(msg, VisitShortLinkLog.class);
                    }
                }
        );

        KeyedStream<VisitShortLinkLog, String> groupDS = wideLogDS.keyBy(
                new KeySelector<VisitShortLinkLog, String>() {
                    @Override
                    public String getKey(VisitShortLinkLog wideLog) {
                        return wideLog.getUserAgent();
                    }
                }
        );

        SingleOutputStreamOperator<VisitShortLinkLog> uniqueVisitorDS = groupDS.filter(new VisitorUniqueRichFilterFunction());
        uniqueVisitorDS.print(">>>>>>>>DWM-uniqueVisitorDS");

        SingleOutputStreamOperator<String> jsonStr = uniqueVisitorDS.map(JSONUtil::toJsonStr);
        FlinkKafkaProducer kafkaProducer = FlinkKafkaUtil.getKafkaProducer(SINK_TOPIC);
        jsonStr.addSink(kafkaProducer);
        env.execute();
    }
}
