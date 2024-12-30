package com.minilink.app.dwm;

import cn.hutool.json.JSONUtil;
import com.minilink.app.func.DeviceMapFunction;
import com.minilink.app.func.LocationMapFunction;
import com.minilink.constant.KafkaConstant;
import com.minilink.pojo.VisitShortLinkLog;
import com.minilink.util.FlinkKafkaUtil;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-26  11:20
 * @Description: DWM-设备、地区信息补充
 * @Version: 1.0
 */
public class DwmLinkWideLogApp {
    public static final String SOURCE_TOPIC = KafkaConstant.DWD_CLICK_LINK_TOPIC;
    public static final String SINK_TOPIC = KafkaConstant.DWM_WIDE_LOG_TOPIC;
    public static final String GROUP = KafkaConstant.DWM_WIDE_LOG_GROUP;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        FlinkKafkaConsumer kafkaConsumer = FlinkKafkaUtil.getKafkaConsumer(SOURCE_TOPIC, GROUP);
        DataStreamSource jsonStrDS = env.addSource(kafkaConsumer);

        SingleOutputStreamOperator<VisitShortLinkLog> addDeviceDS = jsonStrDS.map(new DeviceMapFunction());
        addDeviceDS.print(">>>>>>>>DWM-addDeviceDS");

        SingleOutputStreamOperator<VisitShortLinkLog> addRegionDS = addDeviceDS.map(new LocationMapFunction());
        addRegionDS.print(">>>>>>>>DWM-addRegionDS");

        SingleOutputStreamOperator<String> jsonStr = addRegionDS.map(JSONUtil::toJsonStr);
        FlinkKafkaProducer kafkaProducer = FlinkKafkaUtil.getKafkaProducer(SINK_TOPIC);
        jsonStr.addSink(kafkaProducer);
        env.execute();
    }
}
