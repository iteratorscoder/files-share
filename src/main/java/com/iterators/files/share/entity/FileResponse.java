package com.iterators.files.share.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * folder 和 file 的统一抽象, 参考，可以继续修改
 * @author iterators
 * @time 2020.04.22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    // 文件名：目录名或者文件名
    private String fileName;
    // 文件所在目录，或者是文件夹的父目录
    private String dirName;
    // 文件下载的url，或者是文件夹对应的 url
    // 文件夹这个值为 http://localhost/dir/info?dirname=A
    // 文件这个值为 http://localhost:8080/downloadFile/filename
    private String fileUrl;
    // 标记是否是文件夹: 对于文件这个值为true, 对于目录这个值是false
    private boolean isFile;
}
