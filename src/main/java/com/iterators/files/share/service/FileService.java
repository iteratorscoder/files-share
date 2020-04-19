package com.iterators.files.share.service;

import com.iterators.files.share.config.FileProperties;
import com.iterators.files.share.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class FileService {
    @Autowired
  private   FileUtil fileUtil;

    private final Path fileStorageLocation; // 文件在本地存储的地址

    //为什么会传递一个FileProperties进来？？？
    public FileService(FileProperties fileProperties) {
        //这句的用法？？？作用？？？
        this.fileStorageLocation = Paths.get(fileProperties.getUploadDir()).toAbsolutePath().normalize();
        System.out.println(fileStorageLocation.toString());
        try {
            Path directories = Files.createDirectories(this.fileStorageLocation);
            System.out.println("directories.toString() = " + directories.toString());
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();//拿到文件名
        System.out.println("==========");
//文件的目录
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        String filePath = targetLocation.toString();

        System.out.println("targetLocation.toString() = " + targetLocation.toString());
        try {
            System.out.println("------------");
            fileUtil.uploadFileUtil(file.getBytes(), filePath, fileName);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!");
        }


    }
}
