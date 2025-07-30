package com.turing.bigdata.controller.common;

import com.turing.bigdata.common.utils.SFTPUtil;
import com.turing.bigdata.entity.ApiResponse;
import com.turing.bigdata.service.SftpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Api(tags = "Lunix服务器操作", description = "Lunix服务器操作接口")
@RestController
@RequestMapping("/lunix")
public class LunixServerController {

    @Autowired
    private SftpService sftpService;

    /**
     * @param file 文件
     * */
    @ApiOperation(value = "文件上传", notes = "文件上传接口", produces = "application/json")
    @PostMapping("/upload")
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

    @GetMapping("/runShell")
    public String runShell() {
        System.out.println(" >>>>>>>>>>>>>>> 执行本地shell脚本start");
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec("sh /opt/module/spark-3.5.4/libs/run_spark_sql.sh");
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

}
