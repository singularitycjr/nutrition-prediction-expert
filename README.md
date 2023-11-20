## 综述

### 微服务架构

网关转发

食物一个微服务+运动一个微服务

公共模块可分可不分



## 中间件说明

- 注册中心和配置中心 ==> Nacos
- 缓存 ==> redis
- 短信服务 ==> 榛子云
- 静态文件存储系统 ==> minio

**说明**: 上述中间件的账号密码未放在远程仓库里，因为是我私人的资源，如有需要可以私法 



## 关键依赖包说明

- API测试文档 ==> knife4j
- 登录认证 ==> sa-token
- 工具包 ==> hutool
- 数据库持久层 ==> mybatis-plus



## 文件结构

---- nutritional-estimation

​    ----- common    公共模块，包括各种配置类，工具类

​    ----- gateway	网关微服务

​	----- model	模型，全是class类 dto vo pojo 放这里

​	----- service	业务服务

​		---- food-service	食物模块微服务