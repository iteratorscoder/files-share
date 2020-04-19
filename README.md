# files-share

## 项目功能

- [x] 文件的上传、下载
- [x] 文件链接生成
- [ ] 文件二维码分享
- [ ] 文件的删除、重命名
- [ ] 文件的分类管理
- [ ] 文件夹的创建、删除、重命名

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
                 
                 
                 