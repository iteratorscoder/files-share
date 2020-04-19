package com.iterators.files.share.controller;

import com.iterators.files.share.entity.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 处理文件上传和下载的逻辑，需要继续完成
 * @link https://juejin.im/post/5c9e57e2f265da307a160328  参考文章
 * @author iterators
 * @time 2020.04.18
 */
@Controller
@Slf4j // 用于打印日志的注解，可以自动生成log对象
public class FileController {

    public static final String MARKER = "FileController";
/*
* "/uploadFile"和main.js中的第18行：
* xhr.open("POST", "/uploadFile"); // 上传单个文件
* 是对应的。
* */
    @PostMapping(value = "/uploadFile")
    public FileUploadResponse uploadSingleFile(@RequestParam("file") MultipartFile file) {
        log.info(MARKER + "receive a req for upload single file，name: {}", file.getOriginalFilename());

//这里写文件的上传的逻辑


        return null;
    }
}
