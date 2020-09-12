# MyBatis

## MyBatis3

[MyBatis3](https://mybatis.org/mybatis-3/)

[GitHub](https://github.com/mybatis/mybatis-3)

### Spring Boot 集成

[GitHub](https://github.com/mybatis/spring-boot-starter)

### 配置 `pom.xml`

[mybatis-starter maven](https://search.maven.org/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter)

[mybatis-test-starter maven](https://search.maven.org/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter-test)

[mybatis-jsr310 maven](https://search.maven.org/artifact/org.mybatis/mybatis-typehandlers-jsr310)

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.3</version>
</dependency>
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter-test</artifactId>
    <version>2.1.3</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-typehandlers-jsr310</artifactId>
    <version>1.0.2</version>
</dependency>
```

### 关闭自动配置

```java
@SpringBootApplication(exclude = {MybatisAutoConfiguration.class})
public class MyBatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBatisApplication.class, args);
    }

}
```

### 配置

- application.properties

```properties
# DATASOURCE
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://192.168.2.3:3306/spring_boot_samples
spring.datasource.username=root
spring.datasource.password=root

# Druid
```

- MyBatisConfig.java

[SpringBoot 多数据源配置](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-two-datasources)

[Druid 多数据源配置](https://github.com/alibaba/druid/blob/master/druid-spring-boot-starter/README_EN.md#how-to-configuration-multiple-datasource)

```java
@Configuration
@MapperScan(
        basePackages = {"com.sample.springboot.data.mybatis.mapper"},
        sqlSessionFactoryRef = "sqlSessionFactory"
)
public class MyBatisConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        // dataSource
        sessionFactory.setDataSource(dataSource);
        // typeAliasesPackage
        sessionFactory.setTypeAliasesPackage("com.sample.springboot.data.mybatis.domain");
        // mapperLocations
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:/mapper/*.xml"));
        // tk.mybatis.mapper.session.Configuration
        tk.mybatis.mapper.session.Configuration configuration = new tk.mybatis.mapper.session.Configuration();
        sessionFactory.setConfiguration(configuration);
        return sessionFactory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource);
        return txManager;
    }

}
```

## MyBatis Mapper

[XML 映射器](https://mybatis.org/mybatis-3/zh/sqlmap-xml.html)

[动态 SQL](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)

### 自定义BaseMapper

- 自定义BaseMapper接口`BaseMapper.java`

```java
public interface BaseMapper<T> extends Mapper<T>, IdsMapper<T> {
}
```

- 配置BaseMapper接口

```java
@Configuration
@MapperScan(
        basePackages = {"com.sample.springboot.data.mybatis.mapper"},
        markerInterface = com.sample.springboot.data.mybatis.mapper.base.BaseMapper.class
)
public class MyBatisConfig {
}
```

### `in`查询

- 定义接口方法

```java
public interface SampleMapper {
    List<SampleDO> selectByIds(@Param("ids") Set<Long> ids);
}
```

- 编写Mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.sample.springboot.mapper.SampleMapper">

  <select id="selectByIds" resultMap="BaseResultMap">
    select * from `dept`
    <choose>
      <when test="ids!=null and ids.size()>0">
        where `id` in
        <foreach collection="ids" item="item" index="index" open="(" separator="," close=")">
          #{item}
        </foreach>
      </when>
      <otherwise>
        where `id` is null
      </otherwise>
    </choose>
  </select>

</mapper>
```

### 1:1

- SampleDO.java

```java
public class SampleDO {
    private Long id;
    private Long oneToOneId;
    private OneToOne oneToOne;
}
```

- OneToOneDO.java

```java
public class OneToOneDO {
    private Long id;
}
```

- ServiceImpl.java

```java
public class ServiceImpl implements Service {
    
    private void handleSample(SampleDO sample) {
        // oneToOneId
        Long oneToOneId = sample.getOneToOneId();
        // 查询OneToOne对象
        OneToOne oneToOne = oneToOneMapper.selectByPrimaryKey(oneToOneId);
        
        sample.setOneToOne(oneToOne);
    }
    
    private void handleSamples(List<SampleDO> samples) {
        // oneToOneIds
        Set<Long> oneToOneIds = samples.stream().map(SampleDO::getOneToOneId).collect(Collectors.toSet());
        // 查询OneToOne集合
        List<OneToOne> oneToOnes = sampleMapper.selectByIds(oneToOneIds);
        Map<Long, OneToOne> oneToOneIdMap = oneToOnes.stream().collect(Collectors.toMap(
                OneToOne::getId,
                Function.identity(),
                (first, second) -> first
        ));
        
        samples.forEach(sample -> {
            sample.setOneToOne(oneToOneIdMap.get(sample.getOneToOneId()));
        });
    }
    
}
```

### 1:n

- SampleDO.java

```java
public class SampleDO {
    private Long id;
    private List<OneToMany> oneToManys;
}
```

- OneToManyDO.java

```java
public class OneToManyDO {
    private Long id;
    private Long sampleId;
}
```

- ServiceImpl.java

```java
public class ServiceImpl implements Service {
    
    private void handleSample(SampleDO sample) {
        // sampleId
        Long sampleId = sample.getId();
        // 查询OneToMany集合
        List<OneToMany> oneToManys = oneToManyMapper.selectBySampleId(sampleId);
        
        sample.setOneToManys(oneToManys);
    }
    
    private void handleSamples(List<SampleDO> samples) {
        // sampleIds
        Set<Long> sampleIds = samples.stream().map(SampleDO::getId).collect(Collectors.toSet());
        // 查询OneToMany集合
        List<OneToMany> oneToManys = oneToManyMapper.selectBySampleIds(sampleIds);
        Map<Long, List<OneToMany>> sampleIdOneToManysMap = oneToManys.stream().collect(Collectors.groupingBy(
                OneToMany::sampleId
        ));
        
        samples.forEach(sample -> {
            sample.setOneToManys(sampleIdOneToManysMap.get(sample.getId()));
        });
    }
    
}
```

### n:n

- SampleDO.java

```java
public class SampleDO {
    private Long id;
    private List<ManyToMany> manyToManys;
}
```

- ManyToManyDO.java

```java
public class ManyToManyDO {
    private Long id;
}
```

- SampleManyToManyDO.java

```java
public class SampleManyToManyDO {
    private Long id;
    private Long sampleId;
    private Long manyToManyId;
}
```

- ServiceImpl.java

```java
public class ServiceImpl implements Service {
    
    private void handleSample(SampleDO sample) {
        // sampleId
        Long sampleId = sample.getId();
        // 查询SampleManyToMany集合
        List<SampleManyToManyDO> sampleManyToManys = sampleManyToManyMapper.selectBySampleId(sampleId);
        // manyToManyIds
        Set<Long> manyToManyIds = sampleManyToManys.stream().map(SampleManyToManyDO::getManyToManyId).collect(Collectors.toSet());
        // 查询ManyToMany集合
        List<ManyToManyDO> manyToManys = manyToManyMapper.selectByIds(manyToManyIds);
        
        sample.setManyToManys(manyToManys);
    }
    
    private void handleSamples(List<SampleDO> samples) {
        // sampleIds
        Set<Long> sampleIds = samples.stream().map(SampleDO::getId).collect(Collectors.toSet());
        // 查询SampleManyToMany关系集合
        List<SampleManyToManyDO> sampleManyToManys = sampleManyToManyMapper.selectBySampleIds(userIds);
        Map<Long, Set<Long>> sampleIdManyToManyIdsMap = sampleManyToManys.stream().collect(Collectors.groupingBy(
                SampleManyToManyDO::getSampleId,
                Collectors.mapping(SampleManyToManyDO::getManyToManyId, Collectors.toSet())
        ));
        // manyToManyIds
        Set<Long> manyToManyIds = sampleIdManyToManyIdsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        // 查询ManyToMany集合
        List<ManyToManyDO> manyToManys = manyToMany.selectByIds(manyToManyIds);
        Map<Long, ManyToManyDO> manyToManyIdMap = manyToManys.stream().collect(Collectors.toMap(
                ManyToManyDO::getId,
                Function.identity(),
                (first, second) -> first
        ));
        Map<Long, List<ManyToManyDO>> sampleIdManyToManysMap = sampleIdManyToManyIdsMap.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream().map(manyToManyIdMap::get).collect(Collectors.toList())
        ));
        
        samples.forEach(sample -> {
            sample.setManyToManys(sampleIdManyToManysMap.get(sample.getId()));
        });
    }
    
}
```

### 枚举

- 定义枚举接口

```java
public interface Enums {
    Integer getValue();
}
```

- 枚举

```java
public enum SampleEnum implements Enums {

    ENUM_A(3),
    ENUM_B(6),
    ENUM_C(9),
    ENUM_UNKNOWN(0);

    private Integer value;

    SampleEnum(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
```

- 实现接口对应的类型处理器`EnumsTypeHandler.java`

```java
@MappedTypes(Enums.class)
public class EnumsTypeHandler<E extends Enums> extends BaseTypeHandler<E> {

    private Class<E> type;

    private Map<Integer, E> enumMap;

    public EnumsTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        E[] enums = type.getEnumConstants();
        if (enums != null) {
            this.enumMap = new HashMap<>(enums.length);
            for (E anEnum : enums) {
                this.enumMap.put(anEnum.getValue(), anEnum);
            }
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 根据columnName在结果集中获取结果
        int columnValue = rs.getInt(columnName);
        if (columnValue == 0 && rs.wasNull()) {
            return null;
        }
        return getEnum(columnValue);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 根据columnIndex在结果集中获取结果
        int columnValue = rs.getInt(columnIndex);
        if (columnValue == 0 && rs.wasNull()) {
            return null;
        }
        return getEnum(columnValue);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 根据columnIndex在结果集中获取结果
        int columnValue = cs.getInt(columnIndex);
        if (columnValue == 0 && cs.wasNull()) {
            return null;
        }
        return getEnum(columnValue);
    }

    /**
     * 根据value获取Enum
     * @param value 枚举值
     */
    private E getEnum(int value) {
        E anEnum = enumMap.get(value);
        if (null == anEnum) {
            throw new IllegalArgumentException("Cannot convert " + value + " to " + type.getSimpleName());
        }
        return anEnum;
    }

}
```

- 配置处理器

```java
@Configuration
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        // tk.mybatis.mapper.session.Configuration
        tk.mybatis.mapper.session.Configuration configuration = new tk.mybatis.mapper.session.Configuration();
        sessionFactory.setConfiguration(configuration);
        // 自定义Enums处理
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        registry.register(EnumsTypeHandler.class);
        return sessionFactory.getObject();
    }

}
```

## 通用Mapper

[GitHub](https://github.com/abel533/Mapper)

### Spring Boot 集成

[GitHub](https://github.com/abel533/Mapper/tree/master/spring-boot-starter)

- 配置 `pom.xml`

[starter maven](https://search.maven.org/artifact/tk.mybatis/mapper-spring-boot-starter)

```xml
<dependency>
  <groupId>tk.mybatis</groupId>
  <artifactId>mapper-spring-boot-starter</artifactId>
  <version>2.1.5</version>
</dependency>
```

## 分页PageHelper

[GitHub](https://github.com/pagehelper/Mybatis-PageHelper)

### Spring Boot 集成

[GitHub](https://github.com/pagehelper/pagehelper-spring-boot)

- 配置 `pom.xml`

[starter maven](https://search.maven.org/artifact/com.github.pagehelper/pagehelper-spring-boot-starter)

```xml
<dependency>
  <groupId>com.github.pagehelper</groupId>
  <artifactId>pagehelper-spring-boot-starter</artifactId>
  <version>1.3.0</version>
</dependency>
```

## MyBatis Generator

[MyBatis Generator](https://mybatis.org/generator/)

[GitHub](https://github.com/mybatis/generator)

### 配置`pom.xml`

- 配置文件生成路径

```xml
<properties>
    <!-- DAO 项目路径 -->
    <dao.target.dir>${basedir}/../mybatis-dao</dao.target.dir>
    <!-- Generator 生成文件路径 -->
    <targetJavaProject>${dao.target.dir}/src/main/java</targetJavaProject>
    <targetModelPackage>com.sample.springboot.data.mybatis.domain</targetModelPackage>
    <targetMapperPackage>com.sample.springboot.data.mybatis.mapper</targetMapperPackage>
    <targetResourcesProject>${dao.target.dir}/src/main/resources</targetResourcesProject>
    <targetXMLPackage>mapper</targetXMLPackage>
</properties>
```

- 配置`generator`插件

```xml
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.4.0</version>
    <configuration>
        <!-- 配置文件generatorConfig.xml的位置 -->
        <configurationFile>src/main/resources/generator/generatorConfig.xml</configurationFile>
        <!-- 是否覆盖同名文件 -->
        <overwrite>true</overwrite>
        <!-- 是否将生成过程输出至控制台 -->
        <verbose>true</verbose>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.21</version>
        </dependency>
        <dependency>
            <groupId>com.sample.springboot.data.mybatis</groupId>
            <artifactId>mybatis-dao</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>com.sample.springboot.data.mybatis</groupId>
            <artifactId>mybatis-generator</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
</plugin>
```

### 配置`generatorConfig.xml`

- 配置属性

    - `beginningDelimiter`: "`"
    - `endingDelimiter`: "`"

- 配置插件

    - `UnmergeableXmlMappersPlugin` 生成Mapper.xml时覆盖源文件，否则会在源文件中追加

- 配置注释

    - `suppressAllComments`: "true" 不生成注释
    - `suppressDate`: "true" 生成的注释不包含时间戳

- 配置jdbc连接

    - `nullCatalogMeansCurrent`: "true" [防止生成MySQL默认表(sys, information_schema, performance_schema, etc.)](http://www.mybatis.org/generator/usage/mysql.html)

- 配置model

    - `rootClass`: "com.xxx.BaseDO" BaseDO全类名

- 配置table

    - `domainObjectName` 生成的实体类类名
    - `mapperName` 生成的`Mapper.java`接口和`Mapper.xml`文件名

### 运行

- 安装`generator`模块 **否则生成代码时找不到Plugin**

```shell script
mvn clean install -Dmaven.test.skip=true
```

- 安装`BaseDO.java`所在模块 **否则生成代码时找不到rootClass**

```shell script
mvn clean install -Dmaven.test.skip=true
```

- 运行generator

```shell script
cd mybatis-web
mvn mybatis-generator:generate
```
