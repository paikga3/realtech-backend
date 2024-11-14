package realtech.config;

import java.io.IOException;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;



@Configuration
@EnableJpaRepositories(basePackages = "realtech.db", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
public class DataSourceConfig {
	@Primary
    @Bean
    @Qualifier("dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() throws IOException {
        HikariDataSource ds = DataSourceBuilder.create().type(HikariDataSource.class).build();
        return ds;
    }

    
    @Bean
    @Qualifier("entityManager")
    public EntityManager entityManager() throws IOException {
        EntityManagerFactory emf = entityManagerFactory().getObject();
        return emf.createEntityManager();
    }
    
    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws IOException {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "amas.db" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MariaDB103Dialect");
//        properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.format_sql", false);


        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager() throws IOException {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
