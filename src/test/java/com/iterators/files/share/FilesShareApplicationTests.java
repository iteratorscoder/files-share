package com.iterators.files.share;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class FilesShareApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 生成一个二维码。
     *
     * @throws WriterException
     */
    @Test
    public void generateQRCode() throws WriterException, IOException {
        //一、定义一个json格式的字符串，这里使用fastJson，不仅可以创建json格式的字符串，还可以将json格式的字符串转换成json对象
        JSONObject jb = new JSONObject();//1、创建一个JSONObject对象
        jb.put("name", "Tony");  //2、给jb中存放数据
        jb.put("age", "18");
        jb.put("address", "China");
        String content = jb.toString();//3、将json对象转换成json格式的字符串
        log.info("content:" + content);
        //二、把content生成一个二维码。用到了谷歌提供的方法
        Map<EncodeHintType, Object> hints = new HashMap<>(); //创建Map集合
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");//CHARACTER_SET:编码集
        /*
        zxing将生成图形编码的方式抽象成了一个类com.google.zxing.Writer。Writer中有2个方法，其中一个是：
         BitMatrix encode(String var1, BarcodeFormat var2, int var3, int var4, Map<EncodeHintType, ?> var5)
        用于生成二维码。方法参数说明如下
        String contents：编码的内容
        BarcodeFormat format：编码的方式（二维码、条形码...）
        int width：首选的宽度
        int height：首选的高度
        Map<EncodeHintType,?> hints：编码时的额外参数
        */
        //创建一个矩阵对象。(核心的一句)
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
        //三、将矩阵对象生成一个图片.我们得到的是一个BitMatrix, 需要显示出来
        String filePath = "E:/IdeaProjects/files-share/uploads";
        String fileName = "QRCode.jpg";
        Path path = FileSystems.getDefault().getPath(filePath, fileName);//创建一个路径对象
        MatrixToImageWriter.writeToPath(bitMatrix, "jpg", path);// 输出图像
        log.info("生成二维码图片");
    }
}


