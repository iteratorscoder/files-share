package com.iterators.files.share.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传结果的响应对象
 * @author iterators
 * @time 2020.04.18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
