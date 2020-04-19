package com.iterators.files.share.service;

import com.iterators.files.share.config.FileProperties;
import com.iterators.files.share.entity.FileUploadResponse;
import com.iterators.files.share.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j // 用于打印日志的注解，可以自动生成log对象
@Service
public class FileService {

    private final Path fileStorageLocation; // 文件在本地存储的地址

    //为什么会传递一个FileProperties进来？？？。下面这一串是在造地址？？？E:IdeaProjects_files-share_uploads
    @Autowired
    public FileService(FileProperties fileProperties) {
        this.fileStorageLocation = Paths.get(fileProperties.getUploadDir()).toAbsolutePath().normalize();
        log.info("fileStorageLocation.toString() = " + fileStorageLocation.toString());
        try {
            Path directories = Files.createDirectories(this.fileStorageLocation);
            log.info("directories.toString() = " + directories.toString());
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * 存储文件;
     * 1、获得文件地址
     * 2、文件名称
     *
     * @param file
     * @return
     */
    public FileUploadResponse storeFile(MultipartFile file) {
        // 规范化文件名称
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // 检查文件名是否包含不合法的字符
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            String filePath = fileStorageLocation.toString(); //文件的目录
            log.info("targetLocation.toString() = " + fileStorageLocation.toString());
            FileUtil.uploadFileUtil(file.getBytes(), filePath, fileName);
            String fileType = file.getContentType();//文件类型
            long fileSize = file.getSize();//文件大小
            //这个是什么语法、功能。
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString();
            return new FileUploadResponse(fileName, fileDownloadUri, fileType, fileSize);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!");
        }
    }


    @GetMapping("/downloadFile/{fileName:.+}")
    public void downloadFile(@PathVariable String fileName, HttpServletRequest httpServletRequest) {
//fileName:被下载文件的名称

        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        log.info("==================filePath:" + filePath);


    }
}