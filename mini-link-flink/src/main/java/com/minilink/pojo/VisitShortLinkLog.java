package com.minilink.pojo;

import com.minilink.enums.VisitorStateEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-20  11:21
 * @Description: 访问短链接行为日志数据
 * @Version: 1.0
 */
@Data
public class VisitShortLinkLog {
    /**
     * 账号id
     */
    private Long accountId;

    /**
     * 短链接码
     */
    private String shortLinkCode;

    /**
     * ip
     */
    private String ip;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 浏览器指纹
     */
    private String userAgent;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 操作系统类型
     */
    private String osType;

    /**
     * 浏览器类型
     */
    private String browserType;

    /**
     * 访客状态
     *
     * @see VisitorStateEnum
     */
    private String visitorState;

    /**
     * 访问时间
     */
    private LocalDateTime visitTime;

    /**
     * pv 浏览量
     */
    private Long pv;

    /**
     * uv 访客量
     */
    private Long uv;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
