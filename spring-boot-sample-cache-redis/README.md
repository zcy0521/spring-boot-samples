# Caching

[Caching Guides](https://spring.io/guides/gs/caching/)

[Spring Caching](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#cache)

[SpringBoot Caching](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-caching)

## Spring Boot 集成

### 配置`pom.xml`

[starter maven](https://search.maven.org/artifact/com.cooldatasoft/spring-boot-starter-parent)

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 关闭自动配置

```java
@SpringBootApplication(exclude = { CacheAutoConfiguration.class })
public class CacheRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheRedisApplication.class, args);
    }

}
```

### 配置

- application.properties

```properties
# REDIS CONNECTION
spring.redis.host=192.168.3.2
spring.redis.port=6379
spring.redis.database=0
# REDIS LETTUCE POOL
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.max-wait=-1ms
spring.redis.lettuce.pool.min-idle=0
spring.redis.lettuce.time-between-eviction-runs=100ms
# SPRING CACHE REDIS
spring.cache.redis.time-to-live=60s
spring.cache.redis.key-prefix=CacheRedisSample
spring.cache.redis.cache-null-values=true
spring.cache.redis.use-key-prefix=true
```

- RedisConfig.java

```java
@Configuration
public class RedisConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .readFrom(REPLICA_PREFERRED)
                .poolConfig(ConnectionConfig.getPoolConfig(redisProperties.getLettuce().getPool()))
                .build();
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setHostName(redisProperties.getHost());
        standaloneConfig.setPort(redisProperties.getPort());
        standaloneConfig.setDatabase(redisProperties.getDatabase());
        standaloneConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));
        return new LettuceConnectionFactory(standaloneConfig, clientConfig);
    }
    
    /*
     * (non-Javadoc)
     * @see org.springframework.boot.autoconfigure.data.redis.LettuceConnectionConfiguration.PoolBuilderFactory#getPoolConfig(Pool properties)
     */
    static GenericObjectPoolConfig<?> getPoolConfig(RedisProperties.Pool properties) {
        GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxIdle(properties.getMaxIdle());
        config.setMinIdle(properties.getMinIdle());
        if (properties.getTimeBetweenEvictionRuns() != null) {
            config.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRuns().toMillis());
        }
        if (properties.getMaxWait() != null) {
            config.setMaxWaitMillis(properties.getMaxWait().toMillis());
        }
        return config;
    }
    
}
```

- CachingConfig.java

```java
@Configuration
@EnableCaching
public class CachingConfig extends CachingConfigurerSupport {

    @Bean
    @ConfigurationProperties(prefix = "spring.cache")
    public CacheProperties cacheProperties() {
        return new CacheProperties();
    }
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        // standalone
        standaloneConfig.setHostName(redisProperties.getHost());
        standaloneConfig.setPort(redisProperties.getPort());
        standaloneConfig.setDatabase(redisProperties.getDatabase());
        standaloneConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));
        // 连接池配置
        RedisProperties.Pool pool = redisProperties.getLettuce().getPool();
        LettuceClientConfiguration clientConfig = getLettuceClientConfiguration(pool);
        return new LettuceConnectionFactory(standaloneConfig, clientConfig);
    }
    
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory, CacheProperties properties) {
        // 默认缓存规则
        RedisCacheConfiguration defaultConfig = createConfiguration(properties);
        // 定义各模块缓存规则
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>(2);
        // DEPT_CACHE 缓存永不过期
        RedisCacheConfiguration deptConfig = createConfiguration(properties);
        deptConfig = deptConfig.entryTtl(DurationStyle.detectAndParse("0s"));
        cacheConfigurations.put(DEPT_CACHE, deptConfig);
        // ROLE_CACHE 缓存永不过期
        RedisCacheConfiguration roleConfig = createConfiguration(properties);
        roleConfig = roleConfig.entryTtl(DurationStyle.detectAndParse("0s"));
        cacheConfigurations.put(ROLE_CACHE, roleConfig);
        return RedisCacheManager
                .builder(factory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration#RedisCacheConfiguration createConfiguration(CacheProperties cacheProperties, ClassLoader classLoader)
     */
    private RedisCacheConfiguration createConfiguration(CacheProperties cacheProperties) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // values序列化使用GenericJackson2JsonRedisSerializer
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

}
```
