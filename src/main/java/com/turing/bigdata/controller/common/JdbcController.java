package com.turing.bigdata.controller.common;

import com.turing.bigdata.entity.ApiResponse;
import com.turing.bigdata.service.JdbcService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "JDBC操作", description = "JDBC操作接口")
@RestController
@RequestMapping("/jdbc")
public class JdbcController {

    @Autowired
    private JdbcService jdbcService;

    /**
     * 查询接口
     *
     * @param sql SQL 查询语句
     * @return 查询结果
     */
    @GetMapping("/mysql/query")
    public ApiResponse<List<Map<String, Object>>> query(@RequestParam String sql) {
        return ApiResponse.success(jdbcService.query(sql));
    }

//    /**
//     * 更新接口
//     *
//     * @param sql SQL 更新语句
//     * @return 受影响的行数
//     */
//    @GetMapping("/mysql/update")
//    public int update(@RequestParam String sql) {
//        return jdbcService.update(sql);
//    }
//
//    /**
//     * 执行任意 SQL 接口
//     *
//     * @param sql SQL 语句
//     * @return 执行结果
//     */
//    @GetMapping("/mysql/execute")
//    public String execute(@RequestParam String sql) {
//        jdbcService.execute(sql);
//        return "SQL 执行成功";
//    }
}
