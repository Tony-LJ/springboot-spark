package com.turing.bigdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * @descr Spark Job传入参数
 *
 * @author Tony
 * @date 2025-07-30
 * */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparkJobParam {

    /**
     * 任务的主类
     */
    private String mainClass;

    /**
     * jar包路径
     */
    private String jarPath;

    @Value("${spark.master:yarn}")
    private String master;

    @Value("${spark.deploy.mode:cluster}")
    private String deployMode;

    @Value("${spark.driver.memory:1g}")
    private String driverMemory;

    @Value("${spark.executor.memory:1g}")
    private String executorMemory;

    @Value("${spark.executor.cores:1}")
    private String executorCores;

    /**
     * 其他配置：传递给spark job的参数
     */
    private Map<String, String> otherConfParams;

}
