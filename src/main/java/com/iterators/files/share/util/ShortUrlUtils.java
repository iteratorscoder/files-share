package com.iterators.files.share.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.iterators.files.share.constants.HostConstants;
import com.iterators.files.share.constants.ShortUrlConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * 短链工具类
 * {@link Hashing} 可以用啦快速获取hash函数MurmurHash
 * @author iterators
 * @time 2020.04.25
 */
@Slf4j
public class ShortUrlUtils {

    private static final HashFunction HASH_FUNCTION = Hashing.murmur3_32();


    /**
     * 创建短链
     *
     * @param originalUrl 原始的长链url
     * @return
     */
    public static String createShortUrl(String originalUrl) {
        // 检查url是否为空，如果为空，快速失败，抛出异常
        Assert.hasLength(originalUrl, "url must not empty");
        // 计算url的hash值
        HashCode urlHashCode = HASH_FUNCTION.hashBytes(originalUrl.getBytes());
        // 转换成62进制
        String s = urlHashCodeToChars(urlHashCode.hashCode());
        return HostConstants.HOST + s;
    }

    private static String urlHashCodeToChars(int hashCode) {
        if (hashCode == 0) {
            return "0";
        }
        log.info("hash code: {}", hashCode);
        if (hashCode < 0) {
            // hash值可能为负数，所以使用这个确保hashcode永远是正数
            hashCode = hashCode & Integer.MAX_VALUE;
        }
        StringBuilder builder = new StringBuilder();
        while (hashCode != 0) {
            //6zH4pw
            int remainder = hashCode - (hashCode / 62) * 62;
            builder.append(ShortUrlConstants.CHARS_ARRAY[remainder]);
            hashCode /= 62;
        }
        return builder.toString();
    }

}
