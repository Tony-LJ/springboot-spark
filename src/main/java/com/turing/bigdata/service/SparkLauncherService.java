package com.turing.bigdata.service;

import java.io.IOException;

public interface SparkLauncherService {

    int submitYarn(String appName,String jarPath,String queue,String mainClass) throws IOException;

    int submitLocal(String appName,String jarPath,String queue,String mainClass) throws IOException;

}
