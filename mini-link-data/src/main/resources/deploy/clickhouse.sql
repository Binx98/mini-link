CREATE TABLE default.visit_stats
(
    `account_id`      UInt64,
    `short_link_code` String,
    `ip`              String,
    `referer`         String,
    `province`        String,
    `city`            String,
    `browser_type`    String,
    `device_type`     String,
    `os_type`         String,
    `visitor_state`   UInt64,
    `pv`              UInt64,
    `uv`              UInt64,
    `start_time`      DateTime,
    `end_time`        DateTime,
    `create_time`     UInt64
) ENGINE = ReplacingMergeTree(create_time)
PARTITION BY toYYYYMMDD(start_time)
ORDER BY (
 start_time,
 end_time,
 short_link_code,
 province,
 city,
 referer,
 visitor_state,
 ip,
 browser_type,
 os_type,
 device_type);