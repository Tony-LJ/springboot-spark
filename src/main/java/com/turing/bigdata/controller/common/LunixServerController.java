package com.turing.bigdata.controller.common;

import com.turing.bigdata.common.utils.ShellUtils;
import com.turing.bigdata.entity.ApiResponse;
import com.turing.bigdata.service.SftpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Api(tags = "Lunix服务器操作", description = "Lunix服务器操作接口")
@RestController
@RequestMapping("/lunix")
public class LunixServerController {

    @Autowired
    private SftpService sftpService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @ApiOperation(value = "本地执行shell脚本", notes = "本地执行shell脚本接口", produces = "application/json")
    @PostMapping("/local/command/shellLocalExecute")
    public ApiResponse<List<String>> shellLocalExecute(@RequestParam("filePath") String filePath) {
        logger.info("传入参数|filePath:{}",filePath);
        logger.info("本地shell命令开始执行...");
        List<String> execLogList = ShellUtils.exceShell(filePath);
        logger.info("本地shell命令结束执行...");

        return ApiResponse.success(execLogList);
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


}
