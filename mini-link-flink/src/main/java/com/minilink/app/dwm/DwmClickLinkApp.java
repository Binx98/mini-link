package com.minilink.app.dwm;

import com.minilink.app.func.DeviceMapFunction;
import com.minilink.app.func.LocationMapFunction;
import com.minilink.constant.KafkaConstant;
import com.minilink.pojo.VisitShortLinkWideLog;
import com.minilink.util.FlinkKafkaUtil;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-26  11:20
 * @Description: 访问短链接埋点 DWM
 * @Version: 1.0
 */
public class DwmClickLinkApp {
    public static final String SOURCE_TOPIC = KafkaConstant.DWD_CLICK_LINK_TOPIC;
    public static final String SINK_TOPIC = KafkaConstant.DWM_CLICK_LINK_TOPIC;
    public static final String DWS_CLICK_LINK_GROUP = KafkaConstant.DWM_CLICK_LINK_GROUP;

    public static void main(String[] args) throws Exception {
        // 接收上游数据 DWD
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        FlinkKafkaConsumer kafkaConsumer = FlinkKafkaUtil.getKafkaConsumer(SOURCE_TOPIC, DWS_CLICK_LINK_GROUP);
        DataStreamSource jsonStrDS = env.addSource(kafkaConsumer);

        // 数据补齐：访问设备相关
        SingleOutputStreamOperator<VisitShortLinkWideLog> addDeviceDS = jsonStrDS.map(new DeviceMapFunction());
        addDeviceDS.print(">>>>>>>>DWM-addDeviceDS");

        // 数据补齐：访问地址相关
        SingleOutputStreamOperator<VisitShortLinkWideLog> addRegionDS = addDeviceDS.map(new LocationMapFunction());
        addRegionDS.print(">>>>>>>>DWM-addRegionDS");

        // 分组
        KeyedStream<VisitShortLinkWideLog, String> groupDS = addRegionDS.keyBy(
                new KeySelector<VisitShortLinkWideLog, String>() {
                    @Override
                    public String getKey(VisitShortLinkWideLog wideLog) {
                        return wideLog.getUserAgent();
                    }
                }
        );
        groupDS.print(">>>>>>>>DWM-groupDS");

//        // 过滤重复访问（拿到独立访客）
//        groupDS.filter(
//                new RichFilterFunction<VisitShortLinkWideLog>() {
//                    @Override
//                    public void open(Configuration parameters) throws Exception {
//                        super.open(parameters);
//                    }
//
//                    @Override
//                    public void close() throws Exception {
//                        super.close();
//                    }
//
//                    @Override
//                    public boolean filter(VisitShortLinkWideLog wideLog) throws Exception {
//                        return false;
//                    }
//                }
//        );


        // TODO 数据推送到下游 DWS

        env.execute();
    }
}
