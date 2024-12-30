package com.minilink.app.func;

import com.minilink.pojo.VisitShortLinkWideLog;
import com.minilink.util.AMapUtil;
import org.apache.flink.api.common.functions.MapFunction;

import java.util.Map;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-30  11:17
 * @Description: DWM-设备信息
 * @Version: 1.0
 */
public class LocationMapFunction implements MapFunction<VisitShortLinkWideLog, VisitShortLinkWideLog> {
    @Override
    public VisitShortLinkWideLog map(VisitShortLinkWideLog wideLog) {
        Map<String, String> locationMap = AMapUtil.getLocationByIp(wideLog.getIp());
        wideLog.setProvince(locationMap.get("province"));
        wideLog.setCity(locationMap.get("city"));
        return wideLog;
    }
}
