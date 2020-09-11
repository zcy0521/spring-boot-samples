package com.sample.springboot.data.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Redis 多连接配置
 */
@Configuration
public class ConnectionConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.redis.standalone")
    public RedisProperties standaloneProperties() {
        return new RedisProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.sentinel")
    public RedisProperties sentinelProperties() {
        return new RedisProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.cluster")
    public RedisProperties clusterProperties() {
        return new RedisProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    public RedisProperties.Pool lettucePoolProperties() {
        return new RedisProperties.Pool();
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
