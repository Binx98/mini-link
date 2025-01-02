package com.minilink.adapter;

import com.minilink.pojo.entity.VisitShortLinkMsg;
import com.minilink.util.IpUtil;

import java.time.LocalDateTime;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-20  11:20
 * @Description: Kafka 队列消息适配器
 * @Version: 1.0
 */
public class KafkaMsgAdapter {
    public static VisitShortLinkMsg buildVisitShortLinkMsg(Long accountId, String userAgentStr, String shortLinkCode) {
        VisitShortLinkMsg msg = new VisitShortLinkMsg();
        msg.setAccountId(accountId);
        msg.setIp(IpUtil.getIpAddr());
        msg.setUserAgent(userAgentStr);
        msg.setShortLinkCode(shortLinkCode);
        msg.setVisitTime(LocalDateTime.now());
        return msg;
    }
}
