package com.turing.bigdata.controller.common;

import com.turing.bigdata.entity.ApiResponse;
import com.turing.bigdata.service.SftpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Slf4j
@Api(tags = "Lunix服务器操作", description = "Lunix服务器操作接口")
@RestController
@RequestMapping("/lunix")
public class LunixServerController {

    @Autowired
    private SftpService sftpService;

    /**
     * @param file 文件覆盖上传
     * */
    @ApiOperation(value = "文件上传", notes = "文件上传接口", produces = "application/json")
    @PostMapping("/uploadOverlayFile")
    public ApiResponse uploadOverlayFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.fail(500,"No file uploaded.");
        }
        try {
            // 指定Linux服务器上的文件路径
            String serverFilePath = "/opt/project/springboot";
            File dest = new File(serverFilePath);
            // 使用Apache Commons IO库覆盖文件
            FileUtils.writeByteArrayToFile(dest, file.getBytes());
            return ApiResponse.success("File uploaded and overwritten successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail(500,"Failed to upload file.");
        }
    }

    /**
     * @param file 文件下载
     * */
    @ApiOperation(value = "文件下载", notes = "文件上传接口", produces = "application/json")
    @GetMapping("/downloadFile")
    public ApiResponse downloadFile(@RequestParam("file") MultipartFile file) throws FileNotFoundException {
        Path filePath = Paths.get(baseDir, filename).normalize();
        File file = filePath.toFile();

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentLength(file.length())
                .contentType(getMediaTypeForFileName(filename))
                .body(resource);
        return ApiResponse.success("文件上传成功!");
    }

    /**
     * @param filename 文件下载
     * */
    @ApiOperation(value = "大文件下载", notes = "大文件下载接口", produces = "application/json")
    @GetMapping("/download-large")
    public ResponseEntity<StreamingResponseBody> downloadLargeFile(@RequestParam String filename) throws FileNotFoundException {

        Path filePath = validateAndGetPath(filename);
        File file = filePath.toFile();

        StreamingResponseBody body = outputStream -> {
            try (InputStream in = new FileInputStream(file)) {
                byte[] buffer = new byte[1024 * 8];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentLength(file.length())
                .body(body);
    }

    /**
     * @param filename 断点续传下载
     * */
    @ApiOperation(value = "断点续传下载", notes = "断点续传下载接口", produces = "application/json")
    @GetMapping("/download-resume")
    public ResponseEntity<Resource> downloadWithResume(
            @RequestParam String filename,
            @RequestHeader HttpHeaders headers) throws IOException {

        Path filePath = validateAndGetPath(filename);
        File file = filePath.toFile();

        long length = file.length();
        long start = 0;
        long end = length - 1;

        // 处理Range头（断点续传）
        if (headers.getRange().size() > 0) {
            Range range = headers.getRange().get(0);
            start = range.getRangeStart(length);
            end = range.getRangeEnd(length);
        }

        long contentLength = end - start + 1;
        InputStreamResource resource = new InputStreamResource(
                new FileInputStream(file));

        return ResponseEntity.status(start > 0 ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + length)
                .contentLength(contentLength)
                .body(resource);
    }

    /**
     * @param file 文件
     * */
    @ApiOperation(value = "文件上传", notes = "文件上传接口", produces = "application/json")
    @PostMapping("/uploadFile")
    public ApiResponse uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.fail(500,"文件为空,请重新上传文件");
        }
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            sftpService.uploadFile(inputStream, file.getOriginalFilename());
            return ApiResponse.success("文件上传成功,文件目录：/opt/project/springboot");
        } catch (IOException | JSchException | SftpException e) {
            return ApiResponse.fail(500,"文件上传失败"+e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param filePath 文件
     * */
    @ApiOperation(value = "指定目录文件查看", notes = "指定目录文件查看接口", produces = "application/json")
    @PostMapping("/listFiles")
    public List<String> listFiles(String filePath) {
        String host = "10.53.0.71";
        int port = 22;
        String username = "root";
        String password = "EEEeee111";
        String remoteDir = filePath;

        return sftpService.listFiles(host, port, username, password, remoteDir);
    }

    /**
     * @descr 执行shell文件
     *
     * @param shellPath shell文件
     * */
    @ApiOperation(value = "执行shell文件", notes = "执行shell文件接口", produces = "application/json")
    @GetMapping("/runShell")
    public String runShell(String shellPath) {
        System.out.println(" >>>>>>>>>>>>>>> 执行本地shell脚本start");
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("sh " + shellPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }

        return output.toString();
    }

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "pdf", "txt", "png", "jpg", "jpeg", "csv");

    private void validateFileExtension(String filename) throws FileNotFoundException {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new FileNotFoundException("File type not allowed");
        }
    }


}
