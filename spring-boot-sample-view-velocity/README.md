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
mvn clean package install -Dmaven.test.skip
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

- 修改 `pom.xml`

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>dockerfile-maven-plugin</artifactId>
            <version>${dockerfile-maven.version}</version>
            <executions>
                <execution>
                    <id>default</id>
                    <goals>
                        <goal>build</goal>
                        <goal>push</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <tag>${project.version}</tag>
                <buildArgs>
                    <JAR_FILE>${project.build.finalName}.jar</JAR_FILE>
                </buildArgs>
            </configuration>
        </plugin>
    </plugins>
</build>
```

- 修改 `maven/conf/settings.xml`

```xml
<pluginGroups>
    <pluginGroup>com.spotify</pluginGroup>
</pluginGroups>
```

- 安装并推送镜像

```shell script
alias sudo='sudo -E env "PATH=$PATH"'
sudo mvn dockerfile:build -Ddockerfile.repository=<your_username>/<project.artifactId>
mvn deploy -Ddockerfile.repository=<your_username>/<project.artifactId>
```

- 本地运行

```shell script
sudo docker run <your_username>/<project.artifactId>
```

## 热部署

[Developer Tools](https://docs.spring.io/spring-boot/docs/current/reference/html/using-spring-boot.html#using-boot-devtools)

- 修改 `pom.xml`

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

- 配置 IntelliJ IDEA
    - 使用 `Ctrl+Alt+S` 打开 `Settings | Build, Execution, Deployment | Compiler` 并 ☑ `Build project automatically`
    - 使用 `Ctrl+Alt+Shift+/` 搜索 `Registry` 并 ☑ `complier.automark.allow.when.app.running`
