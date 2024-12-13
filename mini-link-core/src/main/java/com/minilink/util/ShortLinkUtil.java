package com.minilink.util;

import com.google.common.hash.Hashing;
import com.minilink.sharding.ShardingUtil;

/**
 * @Author: 徐志斌
 * @CreateTime: 2024-12-12  13:12
 * @Description: 短链接工具类：Murmurhash算法 + Base62编码
 * @Version: 1.0
 */
public class ShortLinkUtil {
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static long murmurHash32(String str) {
        return Hashing.murmur3_32_fixed().hashUnencodedChars(str).padToLong();
    }

    private static String encodeToBase62(long num) {
        StringBuffer sb = new StringBuffer();
        do {
            int i = (int) (num % CHARS.length());
            sb.append(CHARS.charAt(i));
            num = num / CHARS.length();
        } while (num > 0);
        return sb.reverse().toString();
    }

    /**
     * 生成短链接
     * ---------------------------------
     * 格式：库号表号-短链接
     * 例如：02-4s3sQA
     */
    public static String generate(String link) {
        long murmurHash32 = murmurHash32(link);
        StringBuffer sb = new StringBuffer();
        sb.append(ShardingUtil.getDatabaseCode());
        sb.append(ShardingUtil.getTableCode());
        sb.append("-");
        sb.append(encodeToBase62(murmurHash32));
        return sb.toString();
    }
}