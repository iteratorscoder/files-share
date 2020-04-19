package com.iterators.files.share.util;
import lombok.extern.slf4j.Slf4j;
import java.io.FileOutputStream;
import java.io.IOException;
@Slf4j // 用于打印日志的注解，可以自动生成log对象

public class FileUtil {
    public static void uploadFileUtil(byte[] file, String filePath, String fileName) throws IOException {
        log.info("fileName:"+fileName);
        log.info("filePath:"+filePath);
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}