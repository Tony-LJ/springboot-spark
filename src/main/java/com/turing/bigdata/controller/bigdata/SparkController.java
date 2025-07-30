package com.turing.bigdata.controller.bigdata;

import com.alibaba.fastjson2.JSON;
import com.turing.bigdata.entity.SparkJobParam;
import com.turing.bigdata.pipeline.WordCount;
import com.turing.bigdata.service.ISparkSubmitService;
//import com.turing.bigdata.vo.DataBaseExtractorVo;
import com.turing.bigdata.vo.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

import com.turing.bigdata.pipeline.Count;
/**
 * @Author
 **/
@Slf4j
@Api(tags = "Spark计算引擎操作", description = "Spark计算引擎操作接口")
@RestController
@RequestMapping("/spark")
public class SparkController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Resource
	private ISparkSubmitService iSparkSubmitService;

	@Autowired
	private WordCount wordCount;

//	/**
//	 * 调用service进行远程提交spark任务
//	 * @param vo 页面参数
//	 * @return 执行结果
//	 */
//	@ResponseBody
//	@PostMapping("/extract/database")
//	public Object dbExtractAndLoad2Hdfs(@RequestBody DataBaseExtractorVo vo){
//		try {
//			return iSparkSubmitService.submitApplicationWithOtherParams(vo.getSparkApplicationParam(),
//					vo.getUrl(),
//					vo.getTable(),
//					vo.getUserName(),
//					vo.getPassword(),
//					vo.getTargetFileType(),
//					vo.getTargetFilePath());
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//			log.error("执行出错：{}", e.getMessage());
//			return Result.err(500, e.getMessage());
//		}
//	}

	/**
	 * 调用service进行远程提交spark任务2
	 * @param jobParam Spark Job传入参数
	 * @return 执行结果
	 */
	@ResponseBody
	@PostMapping("/pipelie/execute")
	public Object excuteSparkPipelineJob(@RequestBody SparkJobParam jobParam) {
		logger.info("Spark Job传入参数打印:{}", JSON.toJSONString(jobParam));
		try {
			return iSparkSubmitService.submitApplication(jobParam);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			logger.error("执行出错：{}", e.getMessage());
			return Result.err(500, e.getMessage());
		}
	}

	@ResponseBody
	@GetMapping("/pipelie/execute")
	public ResponseEntity<List<Count>> words() {
		return new ResponseEntity<>(wordCount.count(), HttpStatus.OK);
	}

}
