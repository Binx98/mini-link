package com.minilink.service;

import com.minilink.pojo.dto.LinkUrlSaveDTO;
import com.minilink.pojo.vo.LinkUrlTobVO;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-12-09
 */
public interface LinkUrlTobService {
    /**
     * 生成短链接
     *
     * @param saveDTO 保存入参DTO
     */
    void createShortLink(LinkUrlSaveDTO saveDTO);

    /**
     * 获取目标链接 User-Agent 信息
     *
     * @param link 目标链接
     * @return User-Agent信息
     */
    Map<String, Object> parseLink(String link) throws IOException;

    /**
     * 分页查询短链接列表
     *
     * @param groupId 分组id
     * @param current 当前页
     * @param size    每页数据条数
     * @return
     */
    Map<String, Object> getPageList(Long groupId, Integer current, Integer size);

    /**
     * 根据 id 查询短链接详情
     *
     * @param id 主键id
     * @return 链接详情
     */
    LinkUrlTobVO detail(Long id);

    /**
     * 根据 id 删除短链接
     *
     * @param id 目标id
     */
    void deleteUrl(Long id);
}
