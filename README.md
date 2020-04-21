# files-share

## 项目功能

- [x] 文件的上传、下载
- 参考：

https://juejin.im/post/5c9e57e2f265da307a160328 


- [ ] 在线预览
- [x] 文件二维码分享
- 参考：

https://blog.csdn.net/fjcsdn/article/details/80633660

https://www.iteye.com/blog/kesun-shy-2154169

https://www.jianshu.com/p/6607e69b1121

https://www.bilibili.com/video/BV1ps411c7Lg?p=8

https://www.bilibili.com/video/BV1ps411c7Lg?p=9

https://www.jianshu.com/p/05e9ee773898
- [ ] 文件链接生成
- [ ] 文件的删除、重命名
- [ ] 文件的分类管理
- [ ] 文件夹的创建、删除、重命名
- [ ] 用户登录

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