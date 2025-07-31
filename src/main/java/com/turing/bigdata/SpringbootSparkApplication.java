package com.turing.bigdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @descr 景旺电子数据赋能系统
 *
 * @author Tony
 * @date 2025-07
 */
@EnableAsync
@SpringBootApplication
public class SpringbootSparkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootSparkApplication.class, args);
	}

}
