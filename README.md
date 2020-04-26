# files-share

## 项目功能

- [x] 文件的上传、下载
  - 参考：https://juejin.im/post/5c9e57e2f265da307a160328 
- [ ] 在线预览
- [x] 文件二维码分享
  - 参考：https://blog.csdn.net/fjcsdn/article/details/80633660
  - 参考：https://www.iteye.com/blog/kesun-shy-2154169
  - 参考：https://www.jianshu.com/p/6607e69b1121
  - 参考：https://www.bilibili.com/video/BV1ps411c7Lg?p=8
  - 参考：https://www.bilibili.com/video/BV1ps411c7Lg?p=9
  - 参考：https://www.jianshu.com/p/05e9ee773898
- [ ] 文件链接生成
- [ ] 文件的删除、重命名
- [ ] 文件的分类管理
- [ ] 文件夹的创建、删除、重命名
- [ ] 用户登录
- [ ] 文件下载链接转换成短链

## 短链系统设计

### 为什么要采用短链

- 在不同平台上对文字的长度有限制，当我们采用短链分享后，可以编辑的文字信息就会变长了
- 通过二维码分享时，采用长链时二维码密集难识别，但是如果采用短链的话就不存在这个问题，从而提高了二维码的识别率

### 短链工作原理

- 用户点击分享的短链，就会通过浏览器发送请求给我们的短链服务器
- 短链服务器收到请求后解析短链得到与短链所对应的的长链，将这个长链内容返回给浏览器，同时通过302状态码告诉浏览器临时重定向到这个长链所指向的地方
- 浏览器收到重定向请求后，重定向到长链所指向的地址，从而完成短链的跳转过程
  - 使用302重定向是为了让浏览器每次都去短链服务器请求得到短链所对应的最新的长链内容
  - 如果使用301状态码则浏览器只会在第一次跳转时请求短链服务器，之后都使用的是第一次请求得到的内容来跳转

### 短链的生成方法

- 通过**固定域名+长链映射后的一串字母**来构成一个短链
- 长链映射后的字母生成方式如下
  - 长链通过Hash算法得到一个hash值(推荐采用MurmurHash算法)
  - hash值进一步通过62进制(或者其他进制)转化得到更短的表示形式
  - 对于hash冲突问题采用长链+固定字段的形式后再hash来解决
- 长链和短链的映射关系保存在数据库中
- 短链是否在数据库中存在可以采用布隆过滤器进行过滤
  - 在将长短链的映射关系保存在数据库前，使用布隆过滤器验证短链是否存在，过滤器显示不存在则说明一定不存在；
  - 同时在数据库中给短链添加唯一索引的约束来保证短链不会重复，同时提高查询效率

## 需要了解的API

- `HttpServletRequest`
- `Resource`
- `httpServletRequest.getServletContext().getMimeType(resource.getFile().getAbsolutePath());`
- `ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                 .body(resource);`
- `Files.createDirectories(this.fileStorageLocation);`
- `StringUtils.cleanPath`
- `String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString();`
- `new UrlResource(filePath.toUri())`
- `MultipartFile`  
- `response.getOutputStream`


### 注释
    /**
     * 1、首先二维码保存一个信息（文件的链接），通过这个信息触发预览文件这个事情。
     * 2、客户点击“生成二维码”的功能，手机一扫二维码，就可以预览文件。手机一扫二维码，就可以预览文件。
     * 3、明确把哪一个文件生成二维码。
     * 4、写到浏览器里面预览
     * 5、提供一个把二维码生成一个图片保存到用户本地的功能（下载）
     * <p>
     * 用户提供一个文件的名字。用户怎么提供的呢？类似于下载图片那种写法。
     * 上传成功之后，会紧接着生成：main.js的第25行：<a href='" + response.fileDownloadUri + "，是一个可以允许用户下载的超链接
     * 同理，上传成功之后，也会生成一个类似的超链接，可以允许用户生成二维码
     *
     * @param fileName
     * @param response
     */
    @RequestMapping(value = "/qr")
    public void getQrCode(@RequestParam("file") String fileName, HttpServletResponse response) {
        fileService.getQrCodeService(fileName, 300, 300, response);
    }