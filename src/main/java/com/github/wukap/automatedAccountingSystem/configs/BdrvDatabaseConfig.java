package com.github.wukap.automatedAccountingSystem.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.repository.bdrv",
        entityManagerFactoryRef = "bdrvEntityManager",
        transactionManagerRef = "bdrvTransactionManager"
)
public class BdrvDatabaseConfig
{
    @Autowired
    private EntityManagerFactoryBuilder entityManagerFactoryBuilder;
    @Value("${db.ip}")
    private String dbAddress;
    @Value("${db.name}")
    private String dbName;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;
    @Value("${db.connectionTimeout}")
    private int dbConnectionTimeout;

    @Bean
    public HikariDataSource bdrvDataSource()
    {
        return new HikariDataSource(
                new HikariConfig()
                {{
                    setJdbcUrl("jdbc:sqlserver://" + dbAddress + ";databaseName=" + dbName);
                    setUsername(dbUsername);
                    setPassword(dbPassword);
                    setMaximumPoolSize(Runtime.getRuntime().availableProcessors());
                    //setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
                    setConnectionTimeout(dbConnectionTimeout);
                    setIdleTimeout(10000);
                }}
        );
    }
    @Bean(name = "bdrvEntityManager")
    public LocalContainerEntityManagerFactoryBean bdrvEntityManager() {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        LocalContainerEntityManagerFactoryBean em = entityManagerFactoryBuilder
                .dataSource(bdrvDataSource())
                .packages("com.example.model.bdrv")
                .persistenceUnit("bdrv")
                .properties(properties)
                .build();
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