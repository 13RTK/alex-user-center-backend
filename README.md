# 1. 技术栈
- 本项目采用SpringBoot、MyBatis-Plus
- 后端数据库采用MySQL8.0（5.7也可）


# 2. 部署
- 该项目推荐通过Dockerfile部署
- 通过Dockerfile生成镜像:
```dockerfile
docker build -t image-name:<image-version> <Dockerfile directory>
```

# 3. 说明
- 项目中接口和服务的异常返回信息都通过自定义封装的方式进行了处理
- 该项目来自鱼皮的知识星球直播: [编程导航](https://yupi.icu/)