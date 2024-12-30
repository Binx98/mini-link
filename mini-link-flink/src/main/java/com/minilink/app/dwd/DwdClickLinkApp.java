package com.minilink.app.dwd;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.minilink.app.func.VisitorStateMapFunction;
import com.minilink.constant.KafkaConstant;
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
 * @CreateTime: 2024-12-23  15:30
 * @Description: 访问短链接埋点 DWD
 * @Version: 1.0
 */
public class DwdClickLinkApp {
    public static final String SOURCE_TOPIC = KafkaConstant.ODS_CLICK_LINK_TOPIC;
    public static final String SINK_TOPIC = KafkaConstant.DWD_CLICK_LINK_TOPIC;
    public static final String DWD_CLICK_LINK_GROUP = KafkaConstant.DWD_CLICK_LINK_GROUP;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        FlinkKafkaConsumer kafkaConsumer = FlinkKafkaUtil.getKafkaConsumer(SOURCE_TOPIC, DWD_CLICK_LINK_GROUP);
        DataStreamSource jsonStrDS = env.addSource(kafkaConsumer);
        jsonStrDS.print(">>>>>>>>DWD-jsonStrDS");

        SingleOutputStreamOperator<JSONObject> jsonObjDS = jsonStrDS.map(
                new MapFunction<String, JSONObject>() {
                    @Override
                    public JSONObject map(String msg) {
                        return JSONUtil.toBean(msg, JSONObject.class);
                    }
                }
        );

        KeyedStream keyedStream = jsonObjDS.keyBy(
                new KeySelector<JSONObject, String>() {
                    @Override
                    public String getKey(JSONObject jsonStr) {
                        return jsonStr.getStr("userAgent");
                    }
                }
        );

        SingleOutputStreamOperator<String> visitorStateDS = keyedStream.map(new VisitorStateMapFunction());
        FlinkKafkaProducer kafkaProducer = FlinkKafkaUtil.getKafkaProducer(SINK_TOPIC);
        visitorStateDS.addSink(kafkaProducer);
        visitorStateDS.print(">>>>>>>>DWD-visitorStateDS");
        env.execute();
    }
}
