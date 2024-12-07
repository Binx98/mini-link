package com.minilink.controller;

import com.minilink.enums.BizCodeEnum;
import com.minilink.pojo.dto.RegisterDTO;
import com.minilink.response.R;
import com.minilink.service.UserFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author 徐志斌
 * @Date: 2024/12/6 21:35
 * @Version 1.0
 * @Description: 账号表单相关控制器
 */
@Tag(name = "账号表单")
@RestController
public class UserFormController {
    @Autowired
    private UserFormService formService;

    @Operation(summary = "注册账号")
    @PostMapping("/register")
    public R register(@RequestBody RegisterDTO registerDTO) {
        formService.register(registerDTO);
        return R.out(BizCodeEnum.SUCCESS);
    }

    @Operation(summary = "登录账号")
    @PostMapping("/login")
    public R login(@RequestBody RegisterDTO registerDTO) {
        Map<String, Object> resultMap = formService.login(registerDTO);
        return R.out(BizCodeEnum.SUCCESS, resultMap);
    }

    @Operation(summary = "忘了密码")
    @PostMapping("/forget")
    public R forget() {
        return R.out(BizCodeEnum.SUCCESS);
    }

    @Operation(summary = "找回密码")
    @PostMapping("/find")
    public R findBack() {
        return R.out(BizCodeEnum.SUCCESS);
    }
}