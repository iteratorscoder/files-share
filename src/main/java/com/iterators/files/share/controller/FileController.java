package com.iterators.files.share.controller;

import com.iterators.files.share.entity.FileUploadResponse;
import com.iterators.files.share.entity.FolderResponse;
import com.iterators.files.share.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.util.ArrayList;


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
     * @param fileName
     * @param response
     */
    @RequestMapping(value = "/qr")
    public void getQrCode(@RequestParam("file") String fileName, HttpServletResponse response) {
        fileService.getQrCodeService(fileName, 300, 300, response);
    }


    /**
     * @param folderName：文件夹名
     * @param parentFolder：父路径
     * @param response
     */
    @RequestMapping("/addFolder")
    public FolderResponse makeDirectrion(@RequestParam("folderName") String folderName, @RequestParam("parentFolder") String parentFolder, HttpServletResponse response) {
        FolderResponse folderResponse = fileService.makeDirectrionService(folderName, parentFolder, response);
        return folderResponse;
    }

    //显示某个文件夹下的所有文件
    @RequestMapping("/showFile")
    public ArrayList showFile(@RequestParam("folderName")String folderName,@RequestParam("parentFolder") String parentFolder) {
        ArrayList arrayList = fileService.showFileService(folderName, parentFolder);
        return arrayList;
    }
}


/*
 * "/uploadFile"和main.js中的第18行：
 * xhr.open("POST", "/uploadFile"); // 上传单个文件
 * 是对应的。
 * */