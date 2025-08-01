package com.turing.bigdata.dao.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.turing.bigdata.entity.WarehouseMetastore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface HiveMetastoreMapper extends BaseMapper<WarehouseMetastore> {

    @DS("PROD_MYSQL")
    List<WarehouseMetastore> getWarehouseMetastoreInfos();

    @DS("PROD_MYSQL")
    List<WarehouseMetastore> getMetastoreInfosByColumnName(@Param("columnName") String columnName);

    /**
     * 根据字段中文名称精准查询
     * */
    @DS("PROD_MYSQL")
    List<WarehouseMetastore> getMetastoreInfosByComment(@Param("comment") String comment);

    /**
     * 根据字段中文名称模糊查询
     * */
    @DS("PROD_MYSQL")
    List<WarehouseMetastore> fuzzyQueryMetastoreInfosByComment(@Param("comment") String comment);
}
