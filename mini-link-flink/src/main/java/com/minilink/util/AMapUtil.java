package com.minilink.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-27  10:55
 * @Description: 高德地图工具类
 * @Version: 1.0
 */
public class AMapUtil {
    private static String AMAP_SECRET = "9269ad4b210d90de42f6104e14231bbf";
    private static String IP_LOCATION_API = "https://restapi.amap.com/v3/ip?key=" + AMAP_SECRET + "&ip=%s";

    public static Map<String, String> getLocationByIp(String ip) {
        String restApi = String.format(IP_LOCATION_API, ip);
        String locationStr = OkHttpUtil.get(restApi);
        JSONObject jsonObj = JSONUtil.toBean(locationStr, JSONObject.class);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("province", jsonObj.get("province") == null ? null : jsonObj.get("province").toString());
        resultMap.put("city", jsonObj.get("city") == null ? null : jsonObj.get("city").toString());
        return resultMap;
    }
}
