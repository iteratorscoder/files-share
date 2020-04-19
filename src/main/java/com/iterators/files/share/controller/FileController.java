package com.iterators.files.share.controller;


import com.iterators.files.share.entity.FileUploadResponse;
import com.iterators.files.share.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;

/**
 * 处理文件上传和下载的逻辑，需要继续完成
 * @link https://juejin.im/post/5c9e57e2f265da307a160328  参考文章
 * @author iterators
 * @time 2020.04.18
 */
@Controller
@Slf4j // 用于打印日志的注解，可以自动生成log对象
public class FileController {
@Autowired
    private FileService fileService;

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
/*
   String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();//拿到文件名
        System.out.println("fileName-->" + fileName);
        System.out.println("getContentType-->" + contentType);
// 项目根路径下的目录  -- SpringBoot static 目录相当于是根路径下（SpringBoot 默认）
        String IMG_PATH_PREFIX = "static/upload/imgs";
        // 构建上传文件的存放 "文件夹" 路径
        String fileDirPath = new String("src/main/resources/" + IMG_PATH_PREFIX);
        File fileDir = new File(fileDirPath);
        if(!fileDir.exists()){
            // 递归生成文件夹
            fileDir.mkdirs();
        }
        String filePath=fileDirPath;
        try {
            System.out.println("ffffffffffffffffffffffffffff");
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
*/

String fileName=fileService.storeFile(file);//文件名

//这个是什么用法
        String fileDownloadUri=ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString();
        String fileType = file.getContentType();//文件类型
        long fileSize = file.getSize();//文件大小
        return new FileUploadResponse(fileName,fileDownloadUri,fileType,fileSize);
    }
}
