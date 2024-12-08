package com.minilink.controller;

import com.minilink.pojo.po.MiniLinkUrl;
import com.minilink.store.MiniLinkUrlStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-03  16:37
 * @Description: 短链接
 * @Version: 1.0
 */
@Tag(name = "短链接相关")
@RestController
@RequestMapping("/link")
public class ShortLinkController {
    @Autowired
    private MiniLinkUrlStore urlService;

    @Operation(summary = "生成短链接")
    @PostMapping("/createLong/{longLink}")
    public void createShort(@PathVariable String longLink) {
        MiniLinkUrl url = new MiniLinkUrl();
        url.setShortLink("fdjisi");
        url.setAccountId("xuzhibin");
        url.setGroupId(123219939L);
        url.setLongLink(longLink);
        urlService.saveBatch(Arrays.asList(url, url, url));
    }

    @Operation(summary = "查询长链接")
    @GetMapping("/getLong/{shortLink}")
    public void getLongLink(@PathVariable String shortLink) {

    }

    @Operation(summary = "重定向跳转长连接")
    @GetMapping("/redirect/{shortLink}")
    public void route(@PathVariable String shortLink) {

    }
}
