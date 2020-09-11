# Spring Data JPA

[Spring Data JPA](https://spring.io/guides/gs/accessing-data-jpa/)

[GitHub](https://github.com/spring-projects/spring-data-jpa)

## Spring Boot 集成

### 配置 `pom`

[starter maven](https://search.maven.org/artifact/com.cooldatasoft/spring-boot-starter-parent)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### 关闭自动配置

```java
@SpringBootApplication(exclude = {JpaRepositoriesAutoConfiguration.class})
public class JpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

}
```

### 配置

- application.properties

```properties
# DATASOURCE
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://192.168.2.3:3306/jpa_samples
spring.datasource.username=root
spring.datasource.password=root

# Druid

# JPA
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.mapping.precedence=class,hbm
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.jpa.properties.hibernate.hbm2ddl.auto=update
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
```

- `JPAConfig.java`

[SpringBoot 多数据源配置](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-two-datasources)

[Druid 多数据源配置](https://github.com/alibaba/druid/blob/master/druid-spring-boot-starter/README_EN.md#how-to-configuration-multiple-datasource)

```java
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.sample.springboot.data.jpa.repository"},
        repositoryBaseClass = BaseRepositoryImpl.class
)
public class JPAConfig {

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private JpaProperties jpaProperties;

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource(){
        return DruidDataSourceBuilder.create().build();
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        // dataSource
        factory.setDataSource(dataSource);
        // packagesToScan
        factory.setPackagesToScan("com.sample.springboot.data.jpa.domain");
        // jpaVendorAdapter
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        // jpaProperties
        factory.setJpaPropertyMap(jpaProperties.getProperties());
        return factory;
    }

    @Bean
    public EntityManager entityManager(LocalContainerEntityManagerFactoryBean factory){
        return factory.getObject().createEntityManager();
    }

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean factory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(factory.getObject());
        return txManager;
    }

}
```

## JPA Repository

### 自定义BaseRepository

[自定义BaseRepository](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.customize-base-repository)

- 自定义BaseRepository接口`BaseRepository.java`

```java
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
}
```

- 自定义BaseRepository接口实现`BaseRepositoryImpl.java`

```java
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

}
```

- 配置自定义BaseRepository接口

```java
@Configuration
@EnableJpaRepositories(
        repositoryBaseClass = BaseRepositoryImpl.class
)
public class JPAConfig {
}
```

### 自定义Repository

[自定义Repository](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.single-repository-behavior)

- 定义自定义接口`CustomizedSampleRepository.java`

```java
public interface CustomizedSampleRepository {
    FirstDO searchById(Long id);
}
```

- 自定义接口实现`CustomizedSampleRepositoryImpl.java`

```java
public class CustomizedSampleRepositoryImpl implements CustomizedSampleRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public FirstDO searchById(Long id) {
        String sql = "SELECT s FROM SampleDO s WHERE s.id = :id";
        TypedQuery<FirstDO> query = entityManager.createQuery(sql, FirstDO.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

}
```

- 配置自定义接口

```java
public interface SampleRepository extends BaseRepository<SampleDO, Long>, CustomizedSampleRepository {
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

- 实现接口对应的类型处理器`EnumsConverter.java`

```java
public class EnumsConverter<E extends Enums> implements AttributeConverter<E, Integer> {

    private Class<E> type;

    private Map<Integer, E> enumMap;

    public EnumsConverter(Class<E> type) {
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
    public Integer convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public E convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        E anEnum = enumMap.get(dbData);
        if (null == anEnum) {
            throw new IllegalArgumentException("Cannot convert " + dbData + " to " + type.getSimpleName());
        }
        return anEnum;
    }

}
```

- 实现枚举类对应的类型处理器

```java
public static class Converter extends EnumsConverter<SampleEnum> {
    public Converter() {
        super(SampleEnum.class);
    }
}
```

- 配置处理器

```java
@Entity
@Table(name = "`sample`")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SampleDO extends BaseDO {

    @Column(name="`sample_enum`")
    @Convert(converter = SampleEnum.Converter.class)
    private SampleEnum sampleEnum;

}
```
