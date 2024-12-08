package com.minilink.enums;

import lombok.Getter;

/**
 * @Author 徐志斌
 * @Date: 2024/12/4 10:53
 * @Version 1.0
 * @Description: 响应、异常状态码枚举
 */
@Getter
public enum BizCodeEnum {
    /**
     * 通用操作码
     */
    SUCCESS(20000, "操作成功"),
    FAIL(50000, "操作失败"),
    OPS_REPEAT(110001, "重复操作"),
    OPS_NETWORK_ADDRESS_ERROR(110002, "⽹络地址错误"),

    /**
     * 验证码
     */
    CODE_LIMITED(240002, "验证码发送过快"),
    CODE_EMAIL_ERROR(240003, "邮箱验证码错误"),
    CODE_CAPTCHA_ERROR(240101, "图形验证码错误"),

    /**
     * 账号
     */
    ACCOUNT_REPEAT(250001, "账号已经存在"),
    ACCOUNT_UNREGISTER(250002, "账号不存在"),
    ACCOUNT_PWD_ERROR(250003, "账号或密码错误"),
    ACCOUNT_NO_LOGIN(250004, "账号未登录"),
    PASSWORD_NO_EQUAL(250005, "两次密码输入不一致"),

    /**
     * 短链
     */
    SHORT_LINK_NOT_EXIST(260404, "短链不存在"),

    /**
     * 流控操作
     */
    CONTROL_FLOW(500101, "限流控制"),
    CONTROL_DEGRADE(500201, "降级控制"),
    CONTROL_AUTH(500301, "认证控制"),

    /**
     * 文件相关
     */
    FILE_UPLOAD_USER_IMG_FAIL(700101, "⽤户头像⽂件上传失败");


    private String msg;
    private int code;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}