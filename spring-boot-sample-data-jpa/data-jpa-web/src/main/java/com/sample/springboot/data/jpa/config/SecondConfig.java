package com.sample.springboot.data.jpa.config;

import com.sample.springboot.data.jpa.repository.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * Spring Data Jpa 配置
 */
@Configuration
@EnableJpaRepositories(
        basePackages = {"com.sample.springboot.data.jpa.repository.second"},
        entityManagerFactoryRef = "secondEntityManagerFactory",
        repositoryBaseClass = BaseRepositoryImpl.class,
        transactionManagerRef = "secondTransactionManager"
)
public class SecondConfig {

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private JpaProperties jpaProperties;

    @Bean(name = "secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("secondDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        // dataSource
        factory.setDataSource(dataSource);
        // packagesToScan
        factory.setPackagesToScan("com.sample.springboot.data.jpa.domain.second");
        // jpaVendorAdapter
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        // jpaProperties
        factory.setJpaPropertyMap(jpaProperties.getProperties());
        return factory;
    }

    @Bean(name = "secondEntityManager")
    public EntityManager entityManager(@Qualifier("secondEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory){
        return factory.getObject().createEntityManager();
    }

    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("secondEntityManagerFactory") LocalContainerEntityManagerFactoryBean factory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(factory.getObject());
        return txManager;
    }

}
