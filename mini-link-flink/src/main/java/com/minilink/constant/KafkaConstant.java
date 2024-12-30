package com.minilink.constant;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-20  10:49
 * @Description: Kafka 常量
 * @Version: 1.0
 */
public class KafkaConstant {
    public static final String KAFKA_SERVER = "localhost:9092";

    /**
     * ODS
     */
    public static final String ODS_CLICK_LINK_TOPIC = "ods_click_link_topic";

    /**
     * DWD
     */
    public static final String DWD_CLICK_LINK_TOPIC = "dwd_click_link_topic";

    /**
     * DWM
     */
    public static final String DWM_WIDE_LOG_TOPIC = "dwm_wide_log_topic";
    public static final String DWM_UNIQUE_VISITOR_TOPIC = "dwm_unique_visitor_topic";

    /**
     * 消费组
     */
    public static final String DWD_CLICK_LINK_GROUP = "dwd_click_link_group";
    public static final String DWM_WIDE_LOG_GROUP = "dwm_wide_log_group";
    public static final String DWM_UNIQUE_VISITOR_GROUP = "dwm_unique_visitor_group";
    public static final String DWS_WIDE_LOG_GROUP = "dws_wide_log_group";
    public static final String DWS_UNIQUE_VISITOR_GROUP = "dws_unique_visitor_group";
}
