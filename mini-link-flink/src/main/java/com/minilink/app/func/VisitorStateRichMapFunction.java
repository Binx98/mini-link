package com.minilink.app.func;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.minilink.enums.VisitorStateEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-24  17:19
 * @Description: DWD-新老访客
 * @Version: 1.0
 */
public class VisitorStateRichMapFunction extends RichMapFunction<JSONObject, String> {
    private ValueState<String> visitorState;

    @Override
    public void open(Configuration parameters) {
        ValueStateDescriptor<String> stateDescriptor = new ValueStateDescriptor<>("visitorState", String.class);
        StateTtlConfig ttlConfig = StateTtlConfig.newBuilder(Time.days(30)).build();
        stateDescriptor.enableTimeToLive(ttlConfig);
        visitorState = getRuntimeContext().getState(stateDescriptor);
    }

    @Override
    public String map(JSONObject jsonStr) throws Exception {
        String beforeTimeStr = visitorState.value();
        String nowTimeStr = jsonStr.getStr("visitTime");
        if (StringUtils.isNotEmpty(beforeTimeStr)) {
            jsonStr.set("visitorState", VisitorStateEnum.OLD.getCode());
        } else {
            jsonStr.set("visitorState", VisitorStateEnum.NEW.getCode());
            visitorState.update(nowTimeStr);
        }
        return JSONUtil.toJsonStr(jsonStr);
    }
}
