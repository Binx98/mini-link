package com.minilink.app.func;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.minilink.pojo.VisitShortLinkLog;
import com.minilink.util.DateTimeUtil;
import com.minilink.util.UserAgentUtil;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-30  11:17
 * @Description: DWM-设备信息
 * @Version: 1.0
 */
public class DeviceMapFunction implements MapFunction<String, VisitShortLinkLog> {
    @Override
    public VisitShortLinkLog map(String msg) {
        JSONObject jsonObj = JSONUtil.toBean(msg, JSONObject.class);
        String userAgentStr = jsonObj.getStr("userAgent");
        String ip = jsonObj.getStr("ip");
        String browserType = UserAgentUtil.getBrowserType(userAgentStr);
        String osType = UserAgentUtil.getOsType(userAgentStr);
        String deviceType = UserAgentUtil.getDeviceType(userAgentStr);
        String visitTimeStamp = jsonObj.getStr("visitTime");
        String visitorState = jsonObj.getStr("visitorState");
        VisitShortLinkLog msgLog = new VisitShortLinkLog();
        msgLog.setIp(ip);
        msgLog.setUserAgent(userAgentStr);
        msgLog.setVisitorState(visitorState);
        msgLog.setBrowserType(browserType);
        msgLog.setOsType(osType);
        msgLog.setDeviceType(deviceType);
        msgLog.setVisitTime(DateTimeUtil.timeStampToLocalDateTime(Long.valueOf(visitTimeStamp)));
        return msgLog;
    }
}
