package com.turing.bigdata.controller.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Slf4j
@Api(tags = "文件操作", description = "文件操作接口")
@RestController
@RequestMapping("/file")
public class FileController {

    private static final String UPLOAD_DIR = "D:\\project\\springboot-spark\\docs";
//    private static final String UPLOAD_DIR = "/opt/project/springboot";

    @ApiOperation(value = "文件上传", notes = "文件上传接口", produces = "application/json")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // 获取文件名和路径
        String fileName = file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);

        // 存储文件到指定路径
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }

    @ApiOperation(value = "多文件上传", notes = "多文件上传接口", produces = "application/json")
    @PostMapping("/uploadMultiple")
    public ResponseEntity<String> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) {
        StringBuilder fileNames = new StringBuilder();

        // 处理每个文件
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            try {
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                fileNames.append(fileName).append(" ");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error uploading files: " + e.getMessage());
            }
        }

        return ResponseEntity.ok("Files uploaded successfully: " + fileNames.toString());
    }

    @ApiOperation(value = "文件下载", notes = "文件下载接口", produces = "application/json")
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Resource resource = new FileSystemResource(filePath.toFile());

        if (!resource.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // 返回文件流
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @ApiOperation(value = "文件删除", notes = "文件删除接口", produces = "application/json")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("filePath") String filePath) {
        try {
            // 安全检查：防止路径遍历攻击
            if (filePath.contains("../") || filePath.contains("..\\")) {
                return ResponseEntity.badRequest().body("非法文件路径");
            }
            // 构建完整文件路径
            File file = new File(UPLOAD_DIR + File.separator + filePath);
            // 验证文件是否存在
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            // 验证确实是文件（不是目录）
            if (!file.isFile()) {
                return ResponseEntity.badRequest().body("指定的路径不是文件");
            }
            // 删除文件
            if (file.delete()) {
                return ResponseEntity.ok("文件删除成功");
            } else {
                return ResponseEntity.unprocessableEntity().body("");
            }
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("没有删除权限");
        }
    }

}
