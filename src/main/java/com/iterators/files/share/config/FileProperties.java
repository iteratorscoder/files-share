package com.iterators.files.share.config;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author iterators
 * @time 2020.04.18
 */
@ConfigurationProperties(prefix="file")
public class FileProperties {
    /**
     * 用于配置用户上传的文件在服务端的保存目录
     */
    private String uploadDir;
    public String getUploadDir() {

        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {

        this.uploadDir = uploadDir;
    }
}
