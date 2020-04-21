package com.iterators.files.share.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建文件夹的响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class FolderResponse {
    private String folderName;
    private String ParentFolder;
    private String folderUrl;//拼接一个 http://localhost/dir/info?dirname=A  这种形式的字符串赋值给folderUrl就可以了

}


