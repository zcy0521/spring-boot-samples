package com.sample.springboot.data.redis.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.lettuce.core.ReadFrom.REPLICA_PREFERRED;

/**
 * Redis Sentinel 配置
 *
 * @see org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
 */
@Configuration
@EnableRedisRepositories(
        basePackages = {"com.sample.springboot.data.redis.repository.sentinel"},
        redisTemplateRef = "sentinelRedisTemplate"
)
public class SentinelConfig {

    @Bean("sentinelRedisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory(@Qualifier("sentinelProperties") RedisProperties redisProperties, RedisProperties.Pool poolProperties) {
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .readFrom(REPLICA_PREFERRED)
                .poolConfig(ConnectionConfig.getPoolConfig(poolProperties))
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

    @Bean("sentinelRedisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(@Qualifier("sentinelRedisConnectionFactory") RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean("sentinelStringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(@Qualifier("sentinelRedisConnectionFactory") RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }

}
