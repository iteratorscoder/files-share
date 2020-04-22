package com.iterators.files.share.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * file 的统一抽象, 参考，可以继续修改
 * @author iterators
 * @time 2020.04.22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileVO {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件所在目录名
     */
    private FolderVO dir;

    /**
     * 文件下载url: http://localhost:8080/downloadFile/filename
     */
    private String fileUrl;

}
