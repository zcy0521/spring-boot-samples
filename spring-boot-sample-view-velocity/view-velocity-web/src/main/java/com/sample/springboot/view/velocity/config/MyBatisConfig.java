package com.sample.springboot.view.velocity.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.sample.springboot.view.velocity.mybatis.handler.EnumsTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * MyBatis 配置
 */
@Configuration
@MapperScan(
        basePackages = {"com.sample.springboot.view.velocity.mapper"},
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
        sessionFactory.setTypeAliasesPackage("com.sample.springboot.view.velocity.domain");
        // tk.mybatis.mapper.session.Configuration
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        sessionFactory.setConfiguration(configuration);
        // 自定义Enums处理
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        registry.register(EnumsTypeHandler.class);
        return sessionFactory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource());
        return txManager;
    }

}
