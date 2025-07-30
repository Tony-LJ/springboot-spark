package com.turing.bigdata.service.impl;

import com.turing.bigdata.service.SparkLauncherService;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Service
public class SparkLauncherServiceImpl implements SparkLauncherService {

    @Value("${hadoop.conf.dir}")
    private String hadoop_conf_dir;

    @Value("${spark.home.dir}")
    private String spark_home_dir;

    @Value("${spark.driver.memory}")
    private String spark_driver_memory;

    @Value("${spark.executor.memory}")
    private String spark_executor_memory;

    @Value("${spark.executor.cores}")
    private String spark_executor_cores;

    @Value("${spark.executor.instances}")
    private String spark_executor_instances;

    @Value("${spark.default.parallelism}")
    private String spark_default_parallelism;

    @Override
    public int submitYarn(String appName, String jarPath, String queue,String mainClass) throws IOException {
        int flag = 0;
        HashMap env = new HashMap();
        env.put("HADOOP_CONF_DIR", hadoop_conf_dir);

        SparkAppHandle handler = new SparkLauncher(env).setAppName(appName)
                .setSparkHome(spark_home_dir)
                .setMaster("yarn")
                .setConf("spark.driver.memory", spark_driver_memory)
                .setConf("spark.executor.memory", spark_executor_memory)
                .setConf("spark.executor.cores", spark_executor_cores)
                .setConf("spark.executor.instances", spark_executor_instances)
                .setConf("spark.default.parallelism", spark_default_parallelism)
                .setConf("spark.yarn.queue",queue)
                .setConf("spark.driver.allowMultipleContexts", "true")
                .setAppResource(jarPath)
                .setMainClass(mainClass)
                .setDeployMode("cluster")
                .startApplication(new SparkAppHandle.Listener() {
                    @Override
                    public void stateChanged(SparkAppHandle handle) {
//                        log.info("**********  state  changed  **********");
                    }

                    @Override
                    public void infoChanged(SparkAppHandle handle) {
//                        log.info("**********  info  changed  **********");
                    }
                });

        while(!"FINISHED".equalsIgnoreCase(handler.getState().toString()) && !"FAILED".equalsIgnoreCase(handler.getState().toString())){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
//                log.error(e.getMessage());
            }
        }
        if ("FINISHED".equalsIgnoreCase(handler.getState().toString())){
            flag = 1;
        } else if ("FAILED".equalsIgnoreCase(handler.getState().toString())){
            flag = -1;
        }

        return flag;
    }

    @Override
    public int submitLocal(String appName, String jarPath, String queue,String mainClass) throws IOException {
        int flag = 0;
        HashMap env = new HashMap();
        env.put("HADOOP_CONF_DIR", hadoop_conf_dir);

        SparkAppHandle handler = new SparkLauncher(env)
                .setAppName(appName)
                .setSparkHome(spark_home_dir)
                .setMaster("local[*]")
                .setConf("spark.driver.memory", spark_driver_memory)
                .setConf("spark.executor.memory", spark_executor_memory)
                .setConf("spark.executor.cores", spark_executor_cores)
                .setConf("spark.executor.instances", spark_executor_instances)
                .setConf("spark.default.parallelism", spark_default_parallelism)
                .setConf("spark.yarn.queue",queue)
                .setConf("spark.driver.allowMultipleContexts", "true")
                .setAppResource(jarPath)
                .setMainClass(mainClass)
                .setDeployMode("cluster")
                .startApplication(new SparkAppHandle.Listener() {
                    @Override
                    public void stateChanged(SparkAppHandle handle) {
//                        log.info("**********  state  changed  **********");
                    }

                    @Override
                    public void infoChanged(SparkAppHandle handle) {
//                        log.info("**********  info  changed  **********");
                    }
                });

        while(!"FINISHED".equalsIgnoreCase(handler.getState().toString()) && !"FAILED".equalsIgnoreCase(handler.getState().toString())){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
//                log.error(e.getMessage());
            }
        }
        if ("FINISHED".equalsIgnoreCase(handler.getState().toString())){
            flag = 1;
        } else if ("FAILED".equalsIgnoreCase(handler.getState().toString())){
            flag = -1;
        }

        return flag;
    }
}
