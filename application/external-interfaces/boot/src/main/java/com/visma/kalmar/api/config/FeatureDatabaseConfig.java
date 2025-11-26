package com.visma.kalmar.api.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.visma.feature.kalmar.api",
        entityManagerFactoryRef = "featureEntityManagerFactory",
        transactionManagerRef = "featureTransactionManager")
public class FeatureDatabaseConfig {
    @Value("${spring.jpa.database-platform}")
    private String dialect;

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties featureDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource featureDataSource() {
        return featureDataSourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "featureEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean featureEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        var em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(featureDataSource());
        em.setPackagesToScan("com.visma.feature.kalmar.api.**");
        var vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", dialect);
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager featureTransactionManager(
            final @Qualifier("featureEntityManagerFactory") LocalContainerEntityManagerFactoryBean
                    ondemandEntityManagerFactory) {

        return new JpaTransactionManager(
                Objects.requireNonNull(ondemandEntityManagerFactory.getObject()));
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        var factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]|"));
        return factory;
    }
}
