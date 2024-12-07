<div align="center">
    <a href="https://github.com/Binx98/mini-link"><img src="https://img.shields.io/badge/后端-项目地址-yellow.svg?style=plasticr"></a>
    <a href="https://github.com/Binx98/mini-link-front"><img src="https://img.shields.io/badge/前端-项目地址-blueviolet.svg?style=plasticr"></a>
    <a href="" target="_blank">
    <br>
    <h3>麻烦您帮忙点个Star⭐</h3>
</div>

# ✨系统介绍

基于 SpringBoot 3 + SpringCloud Alibaba 2023 等技术实现的高并发、高性能、海量数据短链接平台
<br>
通过 Flink 实现海量 PV UV 等行为数据实时计算、清洗、聚合、存储到 ClickHouse，提供多维度数据统计面板功能
<br>

# 🚀项目架构

## 模块划分

```
mini-link              ---   父工程
├── mini-link-common   ---   公共通用
├── mini-link-core     ---   短链接核心
├── mini-link-data     ---   大数据看板
├── mini-link-flink    ---   大数据实时计算
├── mini-link-gateway  ---   API网关
└── mini-link-user     ---   账户模块
```

## 服务端

| 技术                   | 说明        | 官网                                              |
|----------------------|-----------|-------------------------------------------------|
| JDK17                | Java开发工具  | https://spring.io/projects/spring-cloud         |
| Spring Boot          | 微服务框架     | https://spring.io/projects/spring-cloud         |
| Spring Cloud         | 微服务框架     | https://spring.io/projects/spring-cloud         |
| Spring Cloud Alibaba | 微服务框架     | https://github.com/alibaba/spring-cloud-alibaba |
| MySQL                | 关系型数据库    | http://www.mybatis.org/mybatis-3/zh/index.html  |
| Redis                | KV数据库     | https://redis.io/                               |
| Redisson             | 分布式缓存     | https://redis.io/                               |
| MyBatisPlus          | ORM框架     | http://www.mybatis.org/mybatis-3/zh/index.html  |
| XXL-JOB              | ORM框架     | http://www.mybatis.org/mybatis-3/zh/index.html  |
| Elasticsearch        | 搜索引擎      | https://github.com/elastic/elasticsearch        |
| Kafka                | 消息队列      | https://www.rabbitmq.com/                       |
| MinIO                | 对象存储      | https://github.com/minio/minio                  |
| ShardingSphere       | 分库分表      | https://github.com/minio/minio                  |
| Lombok               | 简化对象封装工具  | https://github.com/rzwitserloot/lombok          |
| Hutool               | 简化对象封装工具  | https://github.com/rzwitserloot/lombok          |
| Flink                | 大数据实时计算   | https://kubernetes.io/                          |
| HDFS                 | 分布式文件存储   | https://kubernetes.io/                          |
| ClickHouse           | 列式OLAP数据库 | https://github.com/jenkinsci/jenkins            |

## 前端

| 技术         | 说明           | 官网                             |
|------------|--------------|--------------------------------|
| Vue        | 前端框架         | https://vuejs.org/             |
| Vue-router | 路由框架         | https://router.vuejs.org/      |
| Vuex       | 全局状态管理框架     | https://vuex.vuejs.org/        |
| Element    | 前端UI框架       | https://element.eleme.io/      |
| TypeScript | 前端UI框架       | https://element.eleme.io/      |
| Axios      | HTTP请求库      | https://github.com/axios/axios |
| v-charts   | 基于Echarts的图表 | https://v-charts.js.org/       |

## 运维

| 技术            | 说明             | 官网                             |
|---------------|----------------|--------------------------------|
| CentOS 7.9    | Linux版本        | https://vuejs.org/             |
| Docker        | 镜像容器           | https://router.vuejs.org/      |
| Kubernetes    | 容器编排工具         | https://vuex.vuejs.org/        |
| Gitlab        | 代码仓库           | https://element.eleme.io/      |
| Harbor        | 镜像仓库           | https://github.com/axios/axios |
| Jenkins       | 基于Echarts的图表框架 | https://v-charts.js.org/       |
| Ingress-Nginx | 负载均衡器          | https://v-charts.js.org/       |
| Prometheus    | 数据采集           | https://v-charts.js.org/       |
| Grafana       | 数据展示           | https://v-charts.js.org/       |
| Rancher       | K8S图形化工具       | https://v-charts.js.org/       |

# 🎉贡献名单

<a href="https://github.com/Binx98/QuickChat/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Binx98/mini-link" />
</a>

| 姓名  |               Github               |      公司       |
|:---:|:----------------------------------:|:-------------:|
| 徐志斌 |     https://github.com/Binx98      | PARAVERSE 平行云 |
