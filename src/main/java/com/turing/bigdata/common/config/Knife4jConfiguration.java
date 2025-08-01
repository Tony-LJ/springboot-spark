package com.turing.bigdata.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @descr Knife4j配置
 * @author Tony
 * */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi1")
    @Order(1)
    public Docket defaultApi1() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("通用API接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.turing.bigdata.controller.common"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean(value = "defaultApi2")
    @Order(2)
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("数据服务API接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.turing.bigdata.controller.bigdata"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean(value = "defaultApi3")
    @Order(3)
    public Docket defaultApi3() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("数仓操作API接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.turing.bigdata.controller.warehouse"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("景旺电子数据pipeline服务")  //标题
                .contact(new Contact("Tony","http://127.0.0.1","dataops_tony@163.com"))  //作者
                .description("景旺电子数据pipeline服务简介API文档")  //简介
                .termsOfServiceUrl("") //服务URL
                .version("1.0") //版本
                .build();
    }
}