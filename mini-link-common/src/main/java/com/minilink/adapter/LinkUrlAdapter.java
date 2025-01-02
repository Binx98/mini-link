package com.minilink.adapter;

import com.minilink.pojo.po.LinkUrlTob;
import com.minilink.pojo.po.LinkUrlToc;
import com.minilink.pojo.vo.LinkUrlTobVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-13  13:21
 * @Description: 链接-适配器
 * @Version: 1.0
 */
public class LinkUrlAdapter {
    public static LinkUrlTob buildLinkUrlTobPO(Long accountId, Long groupId, String title, String icon,
                                               String domain, String shortLinkCode, String shortLink,
                                               String longLink, LocalDateTime expiredTime) {
        LinkUrlTob linkUrl = new LinkUrlTob();
        linkUrl.setAccountId(accountId);
        linkUrl.setGroupId(groupId);
        linkUrl.setTitle(title);
        linkUrl.setIcon(icon);
        linkUrl.setDomain(domain);
        linkUrl.setShortLinkCode(shortLinkCode);
        linkUrl.setShortLink(shortLink);
        linkUrl.setLongLink(longLink);
        linkUrl.setExpiredTime(expiredTime);
        return linkUrl;
    }

    public static LinkUrlToc buildLinkUrlTocPO(Long id, Long accountId, String shortLinkCode,
                                               String shortLink, String longLink, LocalDateTime expiredTime) {
        LinkUrlToc linkUrl = new LinkUrlToc();
        linkUrl.setId(id);
        linkUrl.setAccountId(accountId);
        linkUrl.setShortLinkCode(shortLinkCode);
        linkUrl.setShortLink(shortLink);
        linkUrl.setLongLink(longLink);
        linkUrl.setExpiredTime(expiredTime);
        return linkUrl;
    }

    public static LinkUrlTobVO buildLinkUrlTobVO(LinkUrlTob linkUrlTob) {
        LinkUrlTobVO linkUrl = new LinkUrlTobVO();
        linkUrl.setTitle(linkUrlTob.getTitle());
        linkUrl.setGroupId(linkUrlTob.getGroupId());
        linkUrl.setAccountId(linkUrlTob.getAccountId());
        linkUrl.setShortLink(linkUrlTob.getShortLink());
        linkUrl.setQrCode(linkUrlTob.getQrCode());
        linkUrl.setLongLink(linkUrlTob.getLongLink());
        linkUrl.setExpiredTime(linkUrlTob.getExpiredTime());
        return linkUrl;
    }

    public static List<LinkUrlTobVO> buildLinkUrlTobVOList(List<LinkUrlTob> urlTobList) {
        List<LinkUrlTobVO> result = new ArrayList<>();
        for (LinkUrlTob urlTob : urlTobList) {
            LinkUrlTobVO linkUrl = new LinkUrlTobVO();
            linkUrl.setTitle(urlTob.getTitle());
            linkUrl.setGroupId(urlTob.getGroupId());
            linkUrl.setAccountId(urlTob.getAccountId());
            linkUrl.setShortLink(urlTob.getShortLink());
            linkUrl.setQrCode(urlTob.getQrCode());
            linkUrl.setLongLink(urlTob.getLongLink());
            linkUrl.setExpiredTime(urlTob.getExpiredTime());
            result.add(linkUrl);
        }
        return result;
    }
}
