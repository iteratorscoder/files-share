package com.iterators.files.share.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

class ShortUrlUtilsTest {

    @Test
    void createShortUrl() {
        String url = "http://localhost:8080/f/abcdef1234567/abd";
        String shortUrl = ShortUrlUtils.createShortUrl(url);
        System.out.println(shortUrl);

        String empty = "";
        ShortUrlUtils.createShortUrl(empty);
    }
}