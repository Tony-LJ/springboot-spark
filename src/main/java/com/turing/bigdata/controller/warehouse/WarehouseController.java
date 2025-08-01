package com.turing.bigdata.controller.warehouse;

import com.turing.bigdata.entity.ApiResponse;
import com.turing.bigdata.entity.WarehouseMetastore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.turing.bigdata.dao.mapper.HiveMetastoreMapper;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Api(tags = "数仓相关操作", description = "数仓相关操作接口")
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Resource
    private HiveMetastoreMapper hiveMetastoreMapper;

    /**
     * @descr 根据字段中文名称查询
     * @param columnName
     * */
    @ApiOperation(value = "用字段名称查询数仓元数据", notes = "用字段名称查询数仓元数据接口", produces = "application/json")
    @PostMapping("/metastore/getWarehouseMetastoreInfosByColumnName")
    private ApiResponse<List<WarehouseMetastore>> getWarehouseMetastoreInfosByColumnName(@RequestParam("columnName") String columnName) {
        List<WarehouseMetastore> warehouseMetastoreInfos = hiveMetastoreMapper.getWarehouseMetastoreInfosByColumnName(columnName);
        return ApiResponse.success(warehouseMetastoreInfos);
    }

    /**
     * @descr 根据字段中文名称查询(精准查询)
     * @param comment
     * */
    @ApiOperation(value = "用字段中文名称精准查询数仓元数据", notes = "用字段中文名称精准查询数仓元数据接口", produces = "application/json")
    @PostMapping("/metastore/getWarehouseMetastoreInfosByComment")
    private List<WarehouseMetastore>  getWarehouseMetastoreInfosByComment(@RequestParam("comment") String comment) {
        List<WarehouseMetastore> warehouseMetastoreInfos =  hiveMetastoreMapper.getWarehouseMetastoreInfosByComment(comment);
        return warehouseMetastoreInfos;
    }

    /**
     * @descr 根据字段中文名称查询(模糊查询)
     * @param comment
     * */
    @ApiOperation(value = "用字段中文名称模糊查询数仓元数据", notes = "用字段中文名称模糊查询数仓元数据接口", produces = "application/json")
    @PostMapping("/metastore/fuzzyQueryWarehouseMetastoreInfosByComment")
    private List<WarehouseMetastore>  fuzzyQueryWarehouseMetastoreInfosByComment(@RequestParam("comment") String comment) {
        List<WarehouseMetastore> warehouseMetastoreInfos =  hiveMetastoreMapper.fuzzyQueryWarehouseMetastoreInfosByComment("%" + comment + "%");
        return warehouseMetastoreInfos;
    }

}
