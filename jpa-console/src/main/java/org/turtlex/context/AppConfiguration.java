package org.turtlex.context;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.support.MergingPersistenceUnitManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@PropertySources({
        @PropertySource("classpath:db.properties"),
        @PropertySource("classpath:jpa.properties")
})
@EnableTransactionManagement
public class AppConfiguration {
    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(environment.getRequiredProperty("db.driver.class"));
        driverManagerDataSource.setUrl(environment.getRequiredProperty("db.url"));
        driverManagerDataSource.setUsername(environment.getRequiredProperty("db.username"));
        driverManagerDataSource.setPassword(environment.getRequiredProperty("db.password"));
        return driverManagerDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment environment) {

        LocalContainerEntityManagerFactoryBean pemf = new LocalContainerEntityManagerFactoryBean();
        pemf.setDataSource(dataSource);

        pemf.setPackagesToScan("org.turtlex.domain");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(Boolean.parseBoolean(environment.getRequiredProperty("jpa.showSql")));
        vendorAdapter.setDatabasePlatform(environment.getRequiredProperty("jpa.dialect"));
        pemf.setJpaVendorAdapter(vendorAdapter);

        pemf.setPersistenceProvider(new HibernatePersistenceProvider());

        MergingPersistenceUnitManager persistenceUnitManager = new MergingPersistenceUnitManager();
        persistenceUnitManager.setPersistenceXmlLocation("classpath*:META-INF/*persistence*.xml");
        persistenceUnitManager.setDefaultDataSource(dataSource);
        pemf.setPersistenceUnitManager(persistenceUnitManager);
        pemf.setPersistenceUnitName("spring-jpa");

        return pemf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        txManager.setJpaDialect(new HibernateJpaDialect());
        return txManager;
    }
}
