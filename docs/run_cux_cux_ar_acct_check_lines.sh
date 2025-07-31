#!/bin/bash
# ################################
# descr ODS_CUX_CUX_AR_ACCT_CHECK_LINES同步脚本
# author Tony
# ################################
export LC_ALL=zh_CN.utf8
# 测试阶段日更，线上保持周更即可
DATA_DATE=$1
# 输入参数校验
if [[ ${DATA_DATE} -eq "" ]]; then
  DATA_DATE=$(date "+%Y%m%d")
fi
BIZ_DATE=`date +%Y%m%d -d "${DATA_DATE} -1 day"`
echo "脚本执行日期:${DATA_DATE}, 业务数据计算日期:${BIZ_DATE}"
echo ">>>数据同步开始"
spark-sql --master yarn --queue root --deploy-mode client --conf spark.debug.maxToStringFields=300 --conf spark.executor.instances=4 --conf spark.executor.memory=5g --conf spark.executor.cores=3 -e"
create table if not exists bi_tmp.ods_cux_cux_ar_acct_check_lines
USING org.apache.spark.sql.jdbc
OPTIONS (
    url "jdbc:oracle:thin:@ebsdb-scan.kinwong.com:1531/prod",
    dbtable "CUX.CUX_AR_ACCT_CHECK_LINES",
    driver "oracle.jdbc.driver.OracleDriver",
    user "hadoop",
    password "vSWnGLcdd8ch"
);

select * from bi_tmp.ods_cux_cux_ar_acct_check_lines limit 100;

insert overwrite table bi_data.dwd_oe_order_ds2 partition(pt_d)
select * from bi_data.dwd_oe_order_ds;
"
if [[ $? -eq 0 ]]; then
	echo ">>>数据同步结束，同步日期:${BIZ_DATE}"
else
	exit 1
fi


