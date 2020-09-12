package com.sample.springboot.cache.redis.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * MyBatis 配置
 */
@Configuration
@MapperScan(
        basePackages = {"com.sample.springboot.cache.redis.mapper"},
        markerInterface = com.sample.springboot.cache.redis.mapper.base.BaseMapper.class,
        sqlSessionFactoryRef = "sqlSessionFactory"
)
public class MyBatisConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setTypeAliasesPackage("com.sample.springboot.cache.redis.domain");
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:/mapper/*.xml"));
        tk.mybatis.mapper.session.Configuration configuration = new tk.mybatis.mapper.session.Configuration();
        sessionFactory.setConfiguration(configuration);
        return sessionFactory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource());
        return txManager;
    }

}
