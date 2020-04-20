package com.iterators.files.share.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.iterators.files.share.entity.FileUploadResponse;
import com.iterators.files.share.service.FileService;
import com.iterators.files.share.util.QRCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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
        方法：它把待下载文件的内容保存到了响应体里面。保存到本地了。
        */
        ResponseEntity<Resource> resourceResponseEntity = fileService.loadFileAsResource(fileName, httpServletRequest);
        return resourceResponseEntity;
    }


    /**
     * 1、首先二维码保存一个信息（文件的链接），通过这个信息触发预览文件这个事情。
     * 2、客户点击“生成二维码”的功能，手机一扫二维码，就可以预览文件。手机一扫二维码，就可以预览文件。
     * 3、明确把哪一个文件生成二维码。
     * 4、写到浏览器里面预览
     * 5、提供一个把二维码生成一个图片保存到用户本地的功能（下载）
     * <p>
     * 用户提供一个文件的名字。用户怎么提供的呢？类似于下载图片那种写法。
     * 上传成功之后，会紧接着生成：main.js的第25行：<a href='" + response.fileDownloadUri + "，是一个可以允许用户下载的超链接
     * 同理，上传成功之后，也会生成一个类似的超链接，可以允许用户生成二维码
     *
     * @param fileName
     * @param response
     */

    @RequestMapping(value = "/qr")
    public void getQrCode(@RequestParam("file") String fileName, HttpServletResponse response) {
        fileService.getQrCodeService(fileName, 300, 300, response);
    }
}
/*
 * "/uploadFile"和main.js中的第18行：
 * xhr.open("POST", "/uploadFile"); // 上传单个文件
 * 是对应的。
 * */