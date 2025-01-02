package com.minilink.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minilink.adapter.LinkUrlAdapter;
import com.minilink.constant.RegexConstant;
import com.minilink.enums.BusinessCodeEnum;
import com.minilink.exception.BusinessException;
import com.minilink.interceptor.LoginInterceptor;
import com.minilink.pojo.dto.LinkUrlSaveDTO;
import com.minilink.pojo.po.LinkUrlTob;
import com.minilink.pojo.po.LinkUrlToc;
import com.minilink.pojo.vo.LinkUrlTobVO;
import com.minilink.service.LinkUrlTobService;
import com.minilink.store.LinkUrlTobStore;
import com.minilink.store.LinkUrlTocStore;
import com.minilink.util.LinkUrlUtil;
import com.minilink.util.SnowFlakeUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 徐志斌
 * @since 2024-12-09
 */
@Service
public class LinkUrlTobServiceImpl implements LinkUrlTobService {
    @Autowired
    private LinkUrlTocStore urlTocStore;
    @Autowired
    private LinkUrlTobStore urlTobStore;

    @Value("${mini-link.group-id}")
    private Long miniLinkGroupId;
    @Value("${mini-link.domain}")
    private String miniLinkDomain;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createShortLink(LinkUrlSaveDTO saveDTO) {
        String shortLinkCode = LinkUrlUtil.generate(SnowFlakeUtil.nextId() + "&" + saveDTO.getLongLink());
        if (!shortLinkCode.matches(RegexConstant.REGEX_SHORT_LINK_FORMAT)) {
            throw new BusinessException(BusinessCodeEnum.REGEX_SHORT_LINK_FORMAT_ERROR);
        }
        LinkUrlToc shortLinkPO = urlTocStore.getByShortLinkCode(shortLinkCode);
        if (ObjectUtils.isNotEmpty(shortLinkPO)) {
            this.createShortLink(saveDTO);
        }
        LinkUrlTob tobLinkPO = LinkUrlAdapter.buildLinkUrlTobPO(
                LoginInterceptor.threadLocal.get().getAccountId(),
                ObjectUtils.isNotEmpty(saveDTO.getGroupId()) ? saveDTO.getGroupId() : miniLinkGroupId,
                saveDTO.getTitle(),
                saveDTO.getIcon(),
                miniLinkDomain,
                shortLinkCode,
                miniLinkDomain + shortLinkCode,
                saveDTO.getLongLink(),
                saveDTO.getExpiredTime()
        );
        urlTobStore.saveLink(tobLinkPO);
        LinkUrlToc tocLinkPO = LinkUrlAdapter.buildLinkUrlTocPO(
                SnowFlakeUtil.nextId(),
                LoginInterceptor.threadLocal.get().getAccountId(),
                shortLinkCode,
                miniLinkDomain + shortLinkCode,
                saveDTO.getLongLink(),
                saveDTO.getExpiredTime()
        );
        urlTocStore.saveLink(tocLinkPO);
    }

    @Override
    public Map<String, Object> parseLink(String link) throws IOException {
        if (StringUtils.isBlank(link) || !link.matches(RegexConstant.REGEX_LONG_LINK_FORMAT)) {
            return null;
        }
        Document doc = Jsoup.connect(link).get();
        String title = doc.title();
        Element descriptionTag = doc.select("meta[name=description]").first();
        String description = descriptionTag != null ? descriptionTag.attr("content") : "";
        Element iconTag = doc.select("link[rel=icon], link[rel=shortcut icon]").first();
        String iconLink = iconTag != null ? iconTag.attr("href") : "";
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("title", title);
        resultMap.put("desc", description);
        resultMap.put("icon", iconLink);
        return resultMap;
    }

    @Override
    public Map<String, Object> getPageList(Long groupId, Integer current, Integer size) {
        Long accountId = LoginInterceptor.threadLocal.get().getAccountId();
        Page<LinkUrlTob> page = urlTobStore.getPage(accountId, groupId, current, size);
        List<LinkUrlTobVO> list = LinkUrlAdapter.buildLinkUrlTobVOList(page.getRecords());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", list);
        resultMap.put("total", page.getTotal());
        return resultMap;
    }

    @Override
    public LinkUrlTobVO detail(Long id) {
        Long accountId = LoginInterceptor.threadLocal.get().getAccountId();
        LinkUrlTob urlTob = urlTobStore.getLinkDetail(id, accountId);
        if (ObjectUtils.isEmpty(urlTob)) {
            return null;
        }
        return LinkUrlAdapter.buildLinkUrlTobVO(urlTob);
    }
}
