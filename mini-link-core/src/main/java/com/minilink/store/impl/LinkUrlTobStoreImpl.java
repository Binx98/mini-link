package com.minilink.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minilink.constant.RedisConstant;
import com.minilink.mapper.LinkUrlTobMapper;
import com.minilink.pojo.po.LinkUrlTob;
import com.minilink.store.LinkUrlTobStore;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-12-09
 */
@Service
public class LinkUrlTobStoreImpl extends ServiceImpl<LinkUrlTobMapper, LinkUrlTob> implements LinkUrlTobStore {
    @Override
    public Boolean saveLink(LinkUrlTob linkUrl) {
        return this.save(linkUrl);
    }

    @Override
    @Cacheable(value = RedisConstant.CACHE_LINK_URL_TOB, key = "#p0 + #p1 + #p2 + #p3", unless = "#result.records.isEmpty()")
    public Page<LinkUrlTob> getPage(Long accountId, Long groupId, Integer current, Integer size) {
        LambdaQueryWrapper<LinkUrlTob> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LinkUrlTob::getAccountId, accountId);
        wrapper.eq(LinkUrlTob::getGroupId, groupId);
        return this.page(new Page<>(current, size), wrapper);
    }

    @Override
    @Cacheable(value = RedisConstant.CACHE_LINK_URL_TOB, key = "#p0 + #p1", unless = "#result == null")
    public LinkUrlTob getLinkDetail(Long id, Long accountId) {
        return this.lambdaQuery()
                .eq(LinkUrlTob::getId, id)
                .eq(LinkUrlTob::getAccountId, accountId)
                .one();
    }

    @Override
    public Boolean deleteUrlById(Long id) {
        return this.removeById(id);
    }
}
