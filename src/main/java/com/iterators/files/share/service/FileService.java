package com.iterators.files.share.service;

import com.iterators.files.share.config.FileProperties;
import com.iterators.files.share.entity.FileUploadResponse;
import com.iterators.files.share.util.FileUtil;
import com.iterators.files.share.util.QRCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
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

    /**
     * 加载文件
     *
     * @param fileName 文件名
     * @return 文件
     */
    public ResponseEntity<Resource> loadFileAsResource(String fileName, HttpServletRequest httpServletRequest) throws MalformedURLException {
        Resource resource = null;
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();//文件路径

            log.info("this.fileStorageLocation.resolve(fileName);" + this.fileStorageLocation.resolve(fileName).toString());
            log.info("filePath;" + filePath.toString());

            resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                //尝试确定文件的内容类型。有什么用？？？？为了浏览器渲染???
                String contentType = null;
                try {
                    contentType = httpServletRequest.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                } catch (IOException ex) {
                    log.info("Could not determine file type.");
                }
                // Fallback to the default content type if type could not be determined
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                //这是再写响应报文吗？？好激动！！！效果看图1
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);

            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName);
        }

    }


    /**
     * 为社么浏览器就可以显示出二维码了呢？？？把二维码写到了哪里呢？二维码又是怎么拼出来的呢？？？？
     * @param content
     * @param width
     * @param height
     * @param response
     */
   public void getQrCodeService(String content, @RequestParam int width, @RequestParam int height, HttpServletResponse response){
    //   Path filePath = this.fileStorageLocation.resolve(content).normalize();//文件路径
    //   String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(content).toUriString();
       ServletOutputStream outputStream = null;
       try {
           outputStream = response.getOutputStream();
           QRCodeUtil.writeToStream("Hello !!!", outputStream, width, height);
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           if (outputStream != null) {
               try {
                   outputStream.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
   }
}