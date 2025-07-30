package com.turing.bigdata.controller.bigdata;

import com.turing.bigdata.entity.ApiResponse;
import com.turing.bigdata.service.SparkLauncherService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@Api(tags = "Spark计算引擎操作", description = "Spark计算引擎操作接口")
@RestController
@RequestMapping("/spark")
public class SparkLauncherController {

    @Autowired
    private SparkLauncherService sparkLauncherService;

    @PostMapping("/yarn/runSparkJob")
    public ApiResponse runSparkJob(String appName,
                                   String jarPath,
                                   String queue, String mainClass) throws IOException {

        int flag = sparkLauncherService.submitYarn(appName, jarPath,queue, mainClass);
        if (flag == -1) {
            return ApiResponse.fail(500,"request failed");
        }

        return ApiResponse.success();
    }

}
