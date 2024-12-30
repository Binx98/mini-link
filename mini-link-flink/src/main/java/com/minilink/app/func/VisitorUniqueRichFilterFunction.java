package com.minilink.app.func;

import com.minilink.pojo.VisitShortLinkLog;
import com.minilink.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;

import java.io.IOException;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-30  11:20
 * @Description: DWM-独立访客UV
 * @Version: 1.0
 */
public class VisitorUniqueRichFilterFunction extends RichFilterFunction<VisitShortLinkLog> {
    private ValueState<String> uniqueState;

    @Override
    public void open(Configuration parameters) {
        ValueStateDescriptor<String> stateDescriptor = new ValueStateDescriptor<>("uniqueState", String.class);
        StateTtlConfig ttlConfig = StateTtlConfig.newBuilder(Time.days(1)).build();
        stateDescriptor.enableTimeToLive(ttlConfig);
        uniqueState = getRuntimeContext().getState(stateDescriptor);
    }

    @Override
    public boolean filter(VisitShortLinkLog wideLog) throws IOException {
        String visitTime = DateTimeUtil.format(wideLog.getVisitTime());
        String beforeTime = uniqueState.value();
        if (StringUtils.isEmpty(beforeTime)) {
            uniqueState.update(visitTime);
            return true;
        } else {
            return false;
        }
    }
}
