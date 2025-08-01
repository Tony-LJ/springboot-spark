package com.turing.bigdata.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JdbcService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 执行查询 SQL，返回结果列表
     *
     * @param sql  SQL 语句
     * @return 查询结果（每行数据以 Map 形式存储）
     */
    @DS("PROD_MYSQL")
    public List<Map<String, Object>> query(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 执行更新 SQL（如 INSERT、UPDATE、DELETE）
     *
     * @param sql SQL 语句
     * @return 受影响的行数
     */
    @DS("PROD_MYSQL")
    public int update(String sql) {
        return jdbcTemplate.update(sql);
    }

    /**
     * 执行任意 SQL 语句
     *
     * @param sql SQL 语句
     */
    @DS("PROD_MYSQL")
    public void execute(String sql) {
        jdbcTemplate.execute(sql);
    }
}
