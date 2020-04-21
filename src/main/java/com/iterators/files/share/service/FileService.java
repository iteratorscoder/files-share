package com.iterators.files.share.service;

import com.iterators.files.share.config.FileProperties;
import com.iterators.files.share.entity.FileResponse;
import com.iterators.files.share.entity.FileUploadResponse;
import com.iterators.files.share.entity.FolderResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Slf4j // 用于打印日志的注解，可以自动生成log对象
@Service
public class FileService {

    private final Path fileStorageLocation; //文件在本地存储的地址

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
     *
     * @param content
     * @param width
     * @param height
     * @param response
     */
    public void getQrCodeService(String content, @RequestParam int width, @RequestParam int height, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            //getOutputStream方法得到的是一个输出流，
            // 服务端的Socket对象上的getOutputStream方法得到的输出流其实就是发送给客户端的数据。
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

    //创建子文件夹
    public FolderResponse makeDirectrionService(String folderName, String parentFolder, HttpServletResponse response) {
        log.info("folderName:" + folderName);
        String path = "";
        if (parentFolder == null) {
            // TODO: 可以继续优化下
            // "E:/IdeaProjects/files-share/uploads" + "/"
            // 这种名字写死的方式弊端在于，如果我们修改了 ./uploads 这个配置项，那么就必须回来再修改这里的代码， "E:/IdeaProjects/files-share/uploads"
            // 如果我们能够使用这个 resolve 解析的方式获取路径，那么即使我们修改了配置项 ./uploads，那么我们也不用修改这里的代码了，因为它就是根据我们的配置项来解析路径的
            // 此处我们需要的是在已有的目录的下创建一个目录，那么，resolve方法刚好是支持这样的操作的，
            // 合并两个路径的技术允许你先定义一个固定的根目录然后再附上局部的路径，在NIO.2 中，使用resolve() 方法来实现这一功能
            // 比如： Path newPath = this.fileStorageLocation.resolve(folderName);
            // 这里 this.fileStorageLocation 就是从我们的 ./uploads 目录解析出来的路径 E:/IdeaProjects/files-share/uploads
            // 然后再在这个基础上添加了folderName, 那么 newPath 的值就是我们所需要的 E:/IdeaProjects/files-share/uploads/folderName 了
            Path newPath = this.fileStorageLocation.resolve(folderName);
            path = "E:/IdeaProjects/files-share/uploads" + "/" + folderName;//在/uploads下面创建目录
            log.info("path:" + path.toString());
            File folder = new File(path);
        } else {
            // 此处的代码也要使用 resolve的方式来解析路径
            path = "E:/IdeaProjects/files-share/uploads" + parentFolder + "/" + folderName;
            log.info("path:" + path.toString());

        }
        File folder = new File(path);
        //创建目录。如果父目录不存在会连同父目录一起创建
        boolean flag = folder.mkdirs();
        //
        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/dir/info?dirname=").path(folderName).toUriString();
        log.info("fileUri:" + fileUri);
        return new FolderResponse(folderName, path, fileUri);
    }

    public ArrayList showFileService(String folderName, String parentFolder) {
        ArrayList<String> arrayList = new ArrayList<>();
        String path = "";
        if (parentFolder == null && folderName == null) {
            // TODO: 此处的代码也要使用 resolve的方式来解析路径
            path = "E:/IdeaProjects/files-share/uploads";
            log.info("path:" + path.toString());
            File folder = new File(path);
        }
        if (parentFolder != null && folderName != null) {
            // TODO: 此处的代码也要使用 resolve的方式来解析路径
            path = "E:/IdeaProjects/files-share/uploads" + parentFolder + "/" + folderName;
            log.info("path:" + path.toString());

        }
        File dir = new File(path);
        // TODO: 用于获取某个dir下的所有内容
        ArrayList<FileResponse> responses = new ArrayList<>();
        File[] files = dir.listFiles();
        for (File file : files) {
            String fileName = file.getName();
            String dirName = file.getParent();
            if (file.isDirectory()) {
                // 判断某个文件是否是目录，也就是可以找出path下的子目录
                String url = ""; // 需要拼接
                boolean isFile = false;
                responses.add(new FileResponse(fileName, dirName, url, isFile));
            }
            if (file.isFile()) {
                // 这个文件不是目录，也就是可以找出path下的文件
                String url = ""; // 需要拼接
                boolean isFile = true;
                responses.add(new FileResponse(fileName, dirName, url, isFile));
            }
        }
        //列出下一级名称
        String[] subNames = dir.list();
        for (String s : subNames) {
            System.out.println(s);
            arrayList.add(s);
        }
        return arrayList;
    }
}