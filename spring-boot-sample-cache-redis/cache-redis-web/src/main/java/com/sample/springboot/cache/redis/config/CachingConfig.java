package com.sample.springboot.cache.redis.config;

import com.sample.springboot.cache.redis.common.CachingNames;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Caching Redis 配置
 */
@Configuration
@EnableCaching
public class CachingConfig {

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
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>(2);
        // DEPT_CACHE 缓存永不过期
        RedisCacheConfiguration deptConfig = createConfiguration(properties);
        deptConfig = deptConfig.entryTtl(DurationStyle.detectAndParse("0s"));
        cacheConfigurations.put(CachingNames.DEPT_CACHE, deptConfig);
        // ROLE_CACHE 缓存永不过期
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
