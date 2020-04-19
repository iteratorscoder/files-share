package com.iterators.files.share.controller;

import com.iterators.files.share.entity.FileUploadResponse;
import com.iterators.files.share.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.UrlResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

/**
 * 处理文件上传和下载的逻辑，需要继续完成
 *
 * @author iterators
 * @link https://juejin.im/post/5c9e57e2f265da307a160328  参考文章
 * @time 2020.04.18
 */
@RestController
@Slf4j // 用于打印日志的注解，可以自动生成log对象
public class FileController {
    @Autowired
    private FileService fileService;

    public static final String MARKER = "FileController";

    /**
     * 上传单个文件
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadFile")
    public FileUploadResponse uploadSingleFile(@RequestParam("file") MultipartFile file) {
        log.info(MARKER + "receive a req for upload single file，name: {}", file.getOriginalFilename());
        FileUploadResponse response = fileService.storeFile(file);
        log.info(MARKER + "store file success with data: {}", response);
        return response;
    }

    //http://localhost:8080/downloadFile/A%20-%20%E5%89%AF%E6%9C%AC.png
    @GetMapping("/downloadFile/{fileName:.+}")//.+是个正则表达式。会把“A%20-%20%E5%89%AF%E6%9C%AC.png”都复制给fileName
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest httpServletRequest) throws MalformedURLException {
        /*
        fileName:被下载文件的名称
        方法：它把待下载文件的内容保存到了响应体里面。在浏览器的页面上渲染出来了，但是没有保存到本地。
        */
        ResponseEntity<Resource> resourceResponseEntity = fileService.loadFileAsResource(fileName, httpServletRequest);
        return resourceResponseEntity;
    }
}

/*
 * "/uploadFile"和main.js中的第18行：
 * xhr.open("POST", "/uploadFile"); // 上传单个文件
 * 是对应的。
 * */