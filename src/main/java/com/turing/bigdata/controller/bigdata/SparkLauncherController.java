package com.turing.bigdata.controller.bigdata;

import com.turing.bigdata.entity.ApiResponse;
import com.turing.bigdata.service.SparkLauncherService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Api(tags = "Spark计算引擎操作", description = "Spark计算引擎操作接口")
@RestController
@RequestMapping("/spark")
public class SparkLauncherController {

    @Autowired
    private SparkLauncherService sparkLauncherService;

    /**
     * @descr 执行Spark Jar作业
     * */
    @PostMapping("/yarn/runSparkJarYarnJob")
    public ApiResponse runSparkJarYarnJob(String appName,
                                       String jarPath,
                                       String queue, String mainClass) throws IOException {
        int flag = sparkLauncherService.submitYarn(appName, jarPath,queue, mainClass);
        if (flag == -1) {
            return ApiResponse.fail(500,"request failed");
        }

        return ApiResponse.success();
    }

    /**
     * @descr 执行spark-sql脚本作业
     * */
    @PostMapping("/yarn/runSparkSqlYarnJob")
    public ApiResponse runSparkSqlYarnJob(String sqlPath) {
        try {
            String exeMsg = runShell(sqlPath);
            ApiResponse.success(exeMsg);
        } catch (Exception e) {
            ApiResponse.fail(500,"spark-sql脚本执行失败");
            e.printStackTrace();
        }

        return ApiResponse.success();
    }

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
