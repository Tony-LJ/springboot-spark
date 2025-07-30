# 项目说明

---

```.text
{
"jarPath":"hdfs://sz-hadoopmaster-01:8020/jars/spark-common-core-1.0.1-SNAPSHOT-jar-with-dependencies.jar",
"mainClass":"org.apache.common.example.NoFenQuBiaoSync",
"deployMode":"cluster",
"master":"yarn",
"driverMemory":"1g",
"executorMemory":"1g",
"executorCores":"1g",
}
```

# springboot基于spark-launcher构建rest api远程提交spark任务
```.text

java -jar springboot-spark-1.0.1-SNAPSHOT.jar

http://localhost:8079/doc.html#/home
http://10.53.0.71:8079/doc.html#/home
```
