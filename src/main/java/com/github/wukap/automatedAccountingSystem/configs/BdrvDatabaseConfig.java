package com.github.wukap.automatedAccountingSystem.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration

public class BdrvDatabaseConfig {
    @Autowired
    private EntityManagerFactoryBuilder entityManagerFactoryBuilder;
    @Value("${bdrv.db.ip}")
    private String dbAddress;
    @Value("${bdrv.db.name}")
    private String dbName;

    @Value("${bdrv.db.username}")
    private String dbUsername;

    @Value("${bdrv.db.password}")
    private String dbPassword;
    @Value("${bdrv.db.connectionTimeout}")
    private int dbConnectionTimeout;

    @Value("${bdrv.db.idleTimeout}")
    private int dbIdleTimeout;
    @Value("${bdrv.db.maxLifetime}")
    private int dbMaxLifetime;

    @Bean
    public HikariDataSource bdrvDataSource() {
        return new HikariDataSource(new HikariConfig() {{
            setJdbcUrl("jdbc:sqlserver://" + dbAddress + ";databaseName=" + dbName);
            setUsername(dbUsername);
            setPassword(dbPassword);
            setMaximumPoolSize(Runtime.getRuntime().availableProcessors());
            setMaxLifetime(dbMaxLifetime);
            //setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
            setConnectionTimeout(dbConnectionTimeout);
            setIdleTimeout(dbIdleTimeout);
        }});
    }

    @Bean(name = "bdrvEntityManager")
    public LocalContainerEntityManagerFactoryBean bdrvEntityManager() {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        LocalContainerEntityManagerFactoryBean em = entityManagerFactoryBuilder.dataSource(bdrvDataSource()).packages("com.example.model.bdrv").persistenceUnit("bdrv").properties(properties).build();
        em.setEntityManagerFactoryInterface(jakarta.persistence.EntityManagerFactory.class);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean(name = "bdrvTransactionManager")
    public PlatformTransactionManager bdrvTransactionManager(@Qualifier("bdrvEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}