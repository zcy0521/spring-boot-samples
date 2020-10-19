package com.sample.springboot.cache.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.springboot.cache.redis.common.CachingNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Caching Redis 配置
 */
@Configuration
@EnableCaching
public class CachingConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    @ConfigurationProperties(prefix = "spring.cache")
    public CacheProperties cacheProperties() {
        return new CacheProperties();
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory factory, CacheProperties properties) {
        // 默认缓存规则
        RedisCacheConfiguration defaultConfig = createConfiguration(properties);

        // 定义各模块缓存规则
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>(1);
        // ROLE_CACHE
        RedisCacheConfiguration roleConfig = createConfiguration(properties);
        roleConfig = roleConfig.entryTtl(DurationStyle.detectAndParse("0s"));
        cacheConfigurations.put(CachingNames.ROLE_CACHE, roleConfig);

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

        // 序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));

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
