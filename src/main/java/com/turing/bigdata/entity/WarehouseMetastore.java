package com.turing.bigdata.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

/**
 * @descr
 * */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Builder
public class WarehouseMetastore implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sdId;

    private String dbName;

    private String tableName;

    private String owner;

    private String location;

    private String comment;

    private String columnName;

    private String typeName;

    private String partName;
}
