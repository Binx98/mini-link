package com.minilink.app.sink;

import com.minilink.pojo.VisitShortLinkLog;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-31  14:08
 * @Description: ClickHouse下游
 * @Version: 1.0
 */
public class ClickHouseSink {
    public static SinkFunction getJdbcSink(String sql) {
        JdbcStatementBuilder<VisitShortLinkLog> statementBuilder = (statement, param) -> {
            statement.setObject(1, param.getAccountId());
            statement.setObject(2, param.getShortLinkCode());
            statement.setObject(3, param.getIp());
            statement.setObject(4, param.getProvince());
            statement.setObject(5, param.getCity());
            statement.setObject(6, param.getBrowserType());
            statement.setObject(7, param.getDeviceType());
            statement.setObject(8, param.getOsType());
            statement.setObject(9, param.getVisitorState());
        };
        JdbcExecutionOptions executionOptions = new JdbcExecutionOptions.Builder()
                .withBatchSize(3)
                .build();
        JdbcConnectionOptions connectionOptions = new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl("jdbc:clickhouse://localhost:8123/default")
                .withDriverName("ru.yandex.clickhouse.ClickHouseDriver")
                .withUsername("default")
                .build();
        return JdbcSink.sink(sql, statementBuilder, executionOptions, connectionOptions);
    }
}
