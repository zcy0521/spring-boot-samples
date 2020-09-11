package com.sample.springboot.data.mybatis.config;

import com.sample.springboot.data.mybatis.handler.EnumsTypeHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
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
        basePackages = {"com.sample.springboot.data.mybatis.mapper.second"},
        markerInterface = com.sample.springboot.data.mybatis.mapper.base.BaseMapper.class,
        sqlSessionFactoryRef = "secondSqlSessionFactory"
)
public class SecondConfig {

    @Bean(name = "secondSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("secondDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        // dataSource
        sessionFactory.setDataSource(dataSource);
        // typeAliasesPackage
        sessionFactory.setTypeAliasesPackage("com.sample.springboot.data.mybatis.domain.second");
        // mapperLocations
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:/mapper/second/*.xml"));

        //tk.mybatis.mapper.session.Configuration
        tk.mybatis.mapper.session.Configuration configuration = new tk.mybatis.mapper.session.Configuration();
        sessionFactory.setConfiguration(configuration);

        // 自定义Enums处理
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        registry.register(EnumsTypeHandler.class);
        return sessionFactory.getObject();
    }

    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("secondDataSource") DataSource dataSource) {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(dataSource);
        return txManager;
    }

}
