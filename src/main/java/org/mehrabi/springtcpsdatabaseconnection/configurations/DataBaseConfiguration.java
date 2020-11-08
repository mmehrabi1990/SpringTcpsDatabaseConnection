/*
 * Copyright (c) 2020.
 * by Mohammad Mehrabi
 */

package org.mehrabi.springtcpsdatabaseconnection.configurations;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import oracle.security.pki.OraclePKIProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.security.Security;
import java.util.HashMap;
import java.util.Properties;

@Configuration
@ConfigurationProperties("spring.datasource")
@EnableJpaRepositories
public class DataBaseConfiguration {


    private int maximumPoolSize;


    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    @PostConstruct
    private void init() {

        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
        System.setProperty("javax.net.ssl.trustStorePassword", "trustStorePassword");
        System.setProperty("javax.net.ssl.trustStore", "trustStore");
        System.setProperty("javax.net.ssl.keyStore", "keyStore");
        System.setProperty("javax.net.ssl.keyStoreType", "JKS");
        System.setProperty("javax.net.ssl.keyStorePassword", "keyStorePassword");
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean userEntityManager() {

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(this.jpaVendorAdapter());
        factory.setPersistenceProvider(this.persistenceProvider());
        factory.setDataSource(userDataSource());
        HashMap<String, String> persistenceMap = new HashMap<>();
        persistenceMap.put("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
        persistenceMap.put("javax.persistence.validation.mode", "none");
        persistenceMap.put("hibernate.hbm2ddl.auto", "none");
        persistenceMap.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        persistenceMap.put("hibernate.show_sql", "true");
        Properties properties = new Properties();
        properties.putAll(persistenceMap);
        factory.setJpaProperties(properties);
        return factory;
    }

    @Bean
    public HibernatePersistenceProvider persistenceProvider() {
        return new HibernatePersistenceProvider();
    }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.Oracle12cDialect");
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        return hibernateJpaVendorAdapter;
    }

    @Primary
    @Bean
    public DataSource userDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("urltcps");
        config.setUsername("user");
        config.setPassword("password");
        config.setConnectionTestQuery("SELECT 1 FROM DUAL");
        Security.insertProviderAt(new OraclePKIProvider(), 3);
        config.setPoolName("hikariPoll");
        config.setMaximumPoolSize(maximumPoolSize);
        return new HikariDataSource(config);
    }


    @Primary
    @Bean
    public PlatformTransactionManager userTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                userEntityManager().getObject());
        return transactionManager;
    }
}
