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