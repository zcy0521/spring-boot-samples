package com.sample.springboot.cache.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import static io.lettuce.core.ReadFrom.REPLICA_PREFERRED;

/**
 * Redis 配置
 */
@Configuration
@EnableRedisRepositories(
        basePackages = {"com.sample.springboot.cache.redis.repository"}
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
                .poolConfig(getPoolConfig(redisProperties.getLettuce().getPool()))
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
    private static GenericObjectPoolConfig<?> getPoolConfig(RedisProperties.Pool properties) {
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
