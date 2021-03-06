# Velocity

[Apache Velocity](http://velocity.apache.org/)

[Spring View Technologies](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-view)

[Spring View Velocity](https://docs.spring.io/spring/docs/4.3.9.RELEASE/spring-framework-reference/htmlsingle/#view-velocity)

[Spring Boot Velocity](https://docs.spring.io/spring-boot/docs/1.4.7.RELEASE/reference/htmlsingle/#howto-customize-view-resolvers-velocity)

[Spring Boot Velocity Properties](https://docs.spring.io/spring-boot/docs/1.4.7.RELEASE/reference/html/common-application-properties.html)

[Alibaba Spring Boot Velocity](https://github.com/alibaba/velocity-spring-boot-project)

## Spring Boot 集成

### pom.xml

[maven](https://search.maven.org/artifact/com.alibaba.boot/velocity-spring-boot-starter)

```xml
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>velocity-spring-boot-starter</artifactId>
    <version>1.0.4.RELEASE</version>
</dependency>
```

### 配置

- application.properties

```properties
# Spring Velocity
spring.velocity.resource-loader-path=classpath:/templates/velocity/
spring.velocity.suffix=
spring.velocity.view-names=*.vm
spring.velocity.content-type=text/html; charset=UTF-8
spring.velocity.expose-request-attributes=true
spring.velocity.expose-session-attributes=true
spring.velocity.allow-request-override=true
spring.velocity.allow-session-override=true
spring.velocity.request-context-attribute=request

# Velocity Layout
spring.velocity.layout-url=/layout/default.vm

# Velocity Tools
spring.velocity.toolboxConfigLocation=/toolbox/tools.xml
```

## Velocity

[]()

[Bootstrap](https://getbootstrap.com/)

## Spring Security

[Spring Security](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)

### Headers

[Security HTTP Response Headers](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#headers)

### CSRF

[Cross Site Request Forgery (CSRF)](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#csrf)

## Spring Session

[]()

## 运行

[Running Your Application](https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-running-your-application)

### Jar运行

[Running as a Packaged Application](https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-running-as-a-packaged-application)

```shell script
mvn clean package -Dmaven.test.skip
java -jar target/myapplication-0.0.1-SNAPSHOT.jar
```

### Docker运行

[Spring Boot with Docker](https://spring.io/guides/gs/spring-boot-docker/)

[Dockerfile Maven](https://github.com/spotify/dockerfile-maven)

[Docker Hub Quickstart](https://docs.docker.com/docker-hub/)

- 编写 `Dockerfile`

```dockerfile
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.sample.springboot.view.velocity.VelocityApplication"]
```

- 打包并解压 `fat jar`

```shell script
mvn clean package -Dmaven.test.skip
mkdir -p target/dependency
cp <包含build模块的子项目>/target/*.jar target/
cd target/dependency; jar -xf ../*.jar
```

- 构建镜像

```shell script
sudo docker build -t <your_username>/<project.artifactId> .
```

- 本地运行

```shell script
sudo docker run -d --name app -p 8080:8080 <your_username>/<project.artifactId>
```

- 上传镜像

```shell script
sudo docker images
sudo docker tag <project.version> <your_username>/<project.artifactId>
sudo docker login
sudo docker push <your_username>/<project.artifactId>
```
