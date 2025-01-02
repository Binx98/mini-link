package com.minilink.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.minilink.adapter.KafkaMsgAdapter;
import com.minilink.constant.KafkaConstant;
import com.minilink.constant.RegexConstant;
import com.minilink.pojo.entity.VisitShortLinkMsg;
import com.minilink.pojo.po.LinkUrlToc;
import com.minilink.service.LinkUrlTocService;
import com.minilink.store.LinkUrlTocStore;
import com.minilink.util.HttpServletUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-12-14
 */
@Service
public class LinkUrlTocServiceImpl implements LinkUrlTocService {
    @Autowired
    private LinkUrlTocStore urlTocStore;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public void redirect(String shortLinkCode) {
        HttpServletResponse response = HttpServletUtil.getResponse();
        if (!shortLinkCode.matches(RegexConstant.REGEX_SHORT_LINK_FORMAT)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        LinkUrlToc linkUrlPO = urlTocStore.getByShortLinkCode(shortLinkCode);
        if (ObjectUtils.isEmpty(linkUrlPO)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        if (ObjectUtils.isNotEmpty(linkUrlPO.getExpiredTime()) && linkUrlPO.getExpiredTime().isBefore(LocalDateTime.now())) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        VisitShortLinkMsg visitShortLinkMsg = KafkaMsgAdapter.buildVisitShortLinkMsg(
                linkUrlPO.getAccountId(),
                HttpServletUtil.getRequest().getHeader("User-Agent"),
                linkUrlPO.getShortLinkCode()
        );
        kafkaTemplate.send(KafkaConstant.ODS_CLICK_LINK_TOPIC, JSONUtil.toJsonStr(visitShortLinkMsg));

        response.setHeader("Location", linkUrlPO.getLongLink());
        response.setStatus(HttpStatus.FOUND.value());
    }
}
