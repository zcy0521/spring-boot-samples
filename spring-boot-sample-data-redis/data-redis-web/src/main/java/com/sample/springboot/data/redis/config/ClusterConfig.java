package com.sample.springboot.data.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
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
 * Redis Cluster 配置
 *
 * @see org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
 */
@Configuration
@EnableRedisRepositories(
        basePackages = {"com.sample.springboot.data.redis.repository.cluster"},
        redisTemplateRef = "clusterRedisTemplate"
)
public class ClusterConfig {

    @Bean("clusterRedisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory(@Qualifier("clusterProperties") RedisProperties redisProperties, RedisProperties.Pool poolProperties) {
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .readFrom(REPLICA_PREFERRED)
                .poolConfig(ConnectionConfig.getPoolConfig(poolProperties))
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

    @Bean("clusterRedisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(@Qualifier("clusterRedisConnectionFactory") RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean("clusterStringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(@Qualifier("clusterRedisConnectionFactory") RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }

}
