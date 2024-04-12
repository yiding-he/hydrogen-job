# hydrogen-job
**(注意：代码仍在开发中)**

## 介绍

_一个计划任务执行服务和框架_

- 支持各种定时任务的执行、启用、禁用等等相关功能；
- 支持对多个**产品**、产品下的多条**产线**、产线下的多个**服务**进行统一管理。如果企业将多个产品部署在一起，那么可以降低部署和运维成本；
- 要求 Java 17 或以上。

## 部署方式
- 一个关系型数据库；
- 一个或多个 `hydrogen-job-server` 服务，独立部署；
- 将 `hydrogen-job-client` 客户端库包含到要执行任务的微服务中；
- 在微服务中实现要执行的任务逻辑。

## 运行原理
- `hydrogen-job-server` 服务连接到数据库，管理任务的一切相关信息；
- `hydrogen-job-client` 客户端库向 `hydrogen-job-server` 服务注册，然后根据服务端派送的任务信息，执行任务。

## 管理界面
- `hydrogen-job-server` 提供可选的管理界面，当部署多个实例时，可以只启用其中的一个；
- 管理界面提供产线粒度的用户权限管理。