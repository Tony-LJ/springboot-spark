package com.turing.bigdata.service;

//import com.turing.bigdata.entity.SparkApplicationParam;
import com.turing.bigdata.entity.SparkJobParam;

import java.io.IOException;

/**
 * @Author hrong
 * @description spark任务提交service
 **/
public interface ISparkSubmitService {

//	/**
//	 * 提交spark任务入口
//	 * @param sparkAppParams spark任务运行所需参数
//	 * @param otherParams 单独的job所需参数
//	 * @return 结果
//	 * @throws IOException          io
//	 * @throws InterruptedException 线程等待中断异常
//	 */
//	String submitApplicationWithOtherParams(SparkApplicationParam sparkAppParams, String... otherParams) throws IOException, InterruptedException;


	/**
	 * 提交spark任务入口
	 * @param jobParam spark任务运行所需参数
	 * @return 结果
	 * @throws IOException          io
	 * @throws InterruptedException 线程等待中断异常
	 */
	String submitApplication(SparkJobParam jobParam) throws IOException, InterruptedException;

}
