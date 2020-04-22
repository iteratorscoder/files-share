package com.iterators.files.share.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author iterators
 * @time 2020.04.22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FolderVO {

    /**
     * 文件夹名
     */
    private String dirName;

    /**
     * 父目录
     */
    private FolderVO parentFolder;

    /**
     * 查看当前文件夹内容的url: http://localhost/dir/info?dirname=A&pDir=parentName
     */
    private String dirUrl;

    /**
     * 当前目录包含子目录
     */
    private List<FolderVO> subDir;

    /**
     * 当前目录下的所有文件
     */
    private List<FileVO> files;
}
