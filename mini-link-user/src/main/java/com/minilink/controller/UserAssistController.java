package com.minilink.controller;

import com.minilink.annotation.NoLogin;
import com.minilink.enums.BusinessCodeEnum;
import com.minilink.service.UserAssistService;
import com.minilink.util.resp.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author 徐志斌
 * @Date: 2024/12/6 21:35
 * @Version 1.0
 * @Description: 账号辅助相关控制器
 */
@Tag(name = "账号辅助")
@RestController
public class UserAssistController {
    @Autowired
    private UserAssistService assistService;

    @NoLogin
    @Operation(summary = "图片验证码")
    @GetMapping("/captcha")
    public void captcha() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        assistService.captcha();
    }

    @NoLogin
    @Operation(summary = "发送邮件")
    @PostMapping("/email/{type}/{email}")
    public R sendEmail(@PathVariable Integer type, @PathVariable String email) {
        assistService.sendEmail(type, email);
        return R.out(BusinessCodeEnum.SUCCESS);
    }

    @Operation(summary = "上传文件")
    @PostMapping("/upload/{type}")
    public R uploadFile(@PathVariable Integer type, MultipartFile file) {
        assistService.uploadFile(type, file);
        return R.out(BusinessCodeEnum.SUCCESS);
    }
}
