package com.sample.springboot.data.redis.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
 * Redis Standalone 配置
 *
 * @see org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
 */
@Configuration
@EnableRedisRepositories(
        basePackages = {"com.sample.springboot.data.redis.repository.standalone"},
        redisTemplateRef = "standaloneRedisTemplate"
)
public class StandaloneConfig {

    @Bean("standaloneRedisConnectionFactory")
    @Primary
    public RedisConnectionFactory redisConnectionFactory(@Qualifier("standaloneProperties") RedisProperties redisProperties, RedisProperties.Pool poolProperties) {
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .readFrom(REPLICA_PREFERRED)
                .poolConfig(ConnectionConfig.getPoolConfig(poolProperties))
                .build();

        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setHostName(redisProperties.getHost());
        standaloneConfig.setPort(redisProperties.getPort());
        standaloneConfig.setDatabase(redisProperties.getDatabase());
        standaloneConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));
        return new LettuceConnectionFactory(standaloneConfig, clientConfig);
    }

    @Bean("standaloneRedisTemplate")
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(@Qualifier("standaloneRedisConnectionFactory") RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean("standaloneStringRedisTemplate")
    @Primary
    public StringRedisTemplate stringRedisTemplate(@Qualifier("standaloneRedisConnectionFactory") RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }

}
