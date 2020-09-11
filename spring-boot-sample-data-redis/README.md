# Redis

[Redis](https://redis.io/)

[Redis Git](https://github.com/redis/redis)

[Spring Boot Data Redis Git](https://github.com/spring-projects/spring-data-redis)

[Spring Boot Data Redis Doc](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis)

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
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 关闭自动配置

```java
@SpringBootApplication(exclude = { RedisAutoConfiguration.class })
public class RedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

}
```

### Replica配置

[Replica](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:write-to-master-read-from-replica)

- application.properties

```properties
# REDIS CONNECTION
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.database=0
# REDIS LETTUCE POOL
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.max-wait=-1ms
spring.redis.lettuce.pool.min-idle=0
spring.redis.lettuce.pool.time-between-eviction-runs=100ms
```

- 配置`RedisConfig.java`

```java
@Configuration
@EnableRedisRepositories(
        basePackages = {"com.sample.springboot.data.redis.repository"}
)
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

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
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

### Sentinel配置

[Sentinel](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:sentinel)

- application.properties

```properties
# REDIS SENTINEL CONNECTION
spring.redis.sentinel.master=mymaster
spring.redis.sentinel.nodes[0]=192.168.3.2:26379
spring.redis.sentinel.nodes[1]=192.168.3.2:26380
spring.redis.sentinel.nodes[2]=192.168.3.2:26381
# REDIS LETTUCE POOL
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.max-wait=-1ms
spring.redis.lettuce.pool.min-idle=0
spring.redis.lettuce.pool.time-between-eviction-runs=100ms
```

- 配置`RedisConfig.java`

```java
@Configuration
@EnableRedisRepositories(
        basePackages = {"com.sample.springboot.data.redis.repository"}
)
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
        // sentinel nodes
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
        sentinelConfig.setMaster(redisProperties.getSentinel().getMaster());
        sentinelConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));
        List<String> sentinelNodes = redisProperties.getSentinel().getNodes();
        Set<RedisNode> nodes = new HashSet<>();
        sentinelNodes.forEach(address -> nodes.add(new RedisNode(
                address.split(":")[0].trim(),
                Integer.parseInt(address.split(":")[1])
        )));
        sentinelConfig.setSentinels(nodes);
        return new LettuceConnectionFactory(sentinelConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
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

### Cluster配置

[Cluster](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#cluster)

- application.properties

```properties
# REDIS CLUSTER CONNECTION
spring.redis.cluster.max-redirects=5
spring.redis.cluster.nodes[0]=192.168.3.2:7000
spring.redis.cluster.nodes[1]=192.168.3.2:7001
spring.redis.cluster.nodes[2]=192.168.3.2:7002
spring.redis.cluster.nodes[3]=192.168.3.2:7003
spring.redis.cluster.nodes[4]=192.168.3.2:7004
spring.redis.cluster.nodes[5]=192.168.3.2:7005
# REDIS LETTUCE POOL
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.max-wait=-1ms
spring.redis.lettuce.pool.min-idle=0
spring.redis.lettuce.pool.time-between-eviction-runs=100ms
```

- 配置`RedisConfig.java`

```java
@Configuration
@EnableRedisRepositories(
        basePackages = {"com.sample.springboot.data.redis.repository"}
)
public class ThirdConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    @Bean
    public RedisConnectionFactory thirdRedisConnectionFactory(RedisProperties redisProperties) {
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .readFrom(REPLICA_PREFERRED)
                .poolConfig(ConnectionConfig.getPoolConfig(redisProperties.getLettuce().getPool()))
                .build();
        // cluster nodes
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
        clusterConfig.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
        clusterConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));
        List<String> clusterNodes = redisProperties.getCluster().getNodes();
        Set<RedisNode> nodes = new HashSet<>();
        clusterNodes.forEach(address -> nodes.add(new RedisNode(
                address.split(":")[0].trim(),
                Integer.parseInt(address.split(":")[1])
        )));
        clusterConfig.setClusterNodes(nodes);
        return new LettuceConnectionFactory(clusterConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
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

## Redis Repositories

[Redis Repositories](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis.repositories)
