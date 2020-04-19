package com.iterators.files.share.controller;


import com.iterators.files.share.entity.FileUploadResponse;
import com.iterators.files.share.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
}


/*
 * "/uploadFile"和main.js中的第18行：
 * xhr.open("POST", "/uploadFile"); // 上传单个文件
 * 是对应的。
 * */