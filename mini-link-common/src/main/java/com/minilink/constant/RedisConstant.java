package com.minilink.constant;

/**
 * @Author 徐志斌
 * @Date: 2024/12/7 9:14
 * @Version 1.0
 * @Description: Redis Key常量
 */
public class RedisConstant {
    public static final String CAPTCHA_KEY = "account:captcha";
    public static final String EMAIL_CODE_KEY = "account:email-code";
    public static final String EMAIL_CHECK_KEY = "account:email-check";

    /**
     * Spring Cache Key
     */
    public static final String MINI_LINK_USER_KEY = "mini-link-user";
    public static final String MINI_LINK_URL_KEY = "mini-link-url";
}
