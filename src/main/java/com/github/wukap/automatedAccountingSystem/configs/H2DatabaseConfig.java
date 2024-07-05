package com.github.wukap.automatedAccountingSystem.configs;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@EnableJpaRepositories(basePackages = "com.github.wukap.automatedAccountingSystem", entityManagerFactoryRef = "h2EntityManager", transactionManagerRef = "h2TransactionManager")

@Configuration
public class H2DatabaseConfig {

    @Autowired
    private EntityManagerFactoryBuilder entityManagerFactoryBuilder;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;


    @Bean
    public HikariDataSource h2DataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setMinimumIdle(5);
        dataSource.setMaximumPoolSize(20);

        return dataSource;
    }

    @Bean(name = "h2EntityManager")
    public LocalContainerEntityManagerFactoryBean h2EntityManager() {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        LocalContainerEntityManagerFactoryBean em = entityManagerFactoryBuilder.dataSource(h2DataSource()).packages("com.github.wukap.automatedAccountingSystem").persistenceUnit("h2").properties(properties).build();
        em.setEntityManagerFactoryInterface(jakarta.persistence.EntityManagerFactory.class);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean(name = "h2TransactionManager")
    public PlatformTransactionManager h2TransactionManager(@Qualifier("h2EntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}