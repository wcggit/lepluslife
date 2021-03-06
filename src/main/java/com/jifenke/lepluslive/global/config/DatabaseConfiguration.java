package com.jifenke.lepluslive.global.config;


import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = {"com.jifenke.lepluslive.*.repository"})
@EntityScan(basePackages = {"com.jifenke.lepluslive.*.domain.entities"})
@EnableTransactionManagement()
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Inject
    private Environment env;

//    @Inject
//    private EntityManagerFactory entityManagerFactory;
//
//    @Inject
//    private RequestMappingHandlerMapping requestMappingHandlerMapping;

//    @Inject
//    private ResourceUrlProviderExposingInterceptor resourceUrlProviderExposingInterceptor;

    @Autowired(required = false)
    private MetricRegistry metricRegistry;

    @Bean(destroyMethod = "close")
    @ConditionalOnExpression("#{!environment.acceptsProfiles('cloud') && !environment.acceptsProfiles('heroku')}")
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        log.debug("Configuring Datasource");
        if (dataSourceProperties.getUrl() == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                    " cannot start. Please check your Spring profile, current profiles are: {}",
                Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dataSourceProperties.getDriverClassName());
        config.addDataSourceProperty("url", dataSourceProperties.getUrl());
        if (dataSourceProperties.getUsername() != null) {
            config.addDataSourceProperty("user", dataSourceProperties.getUsername());
        } else {
            config.addDataSourceProperty("user", ""); // HikariCP doesn't allow null user
        }
        if (dataSourceProperties.getPassword() != null) {
            config.addDataSourceProperty("password", dataSourceProperties.getPassword());
        } else {
            config.addDataSourceProperty("password", ""); // HikariCP doesn't allow null password
        }
        config.setMaximumPoolSize(40);

        if (metricRegistry != null) {
            config.setMetricRegistry(metricRegistry);
        }
        return new HikariDataSource(config);
    }

//    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor(){
//        OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor =  );
//        openEntityManagerInViewInterceptor.setEntityManagerFactory(entityManagerFactory);
//
//        return openEntityManagerInViewInterceptor;
//    }

//    @Bean
//    public DefaultAnnotationHandlerMapping defaultAnnotationHandlerMapping() {
//        DefaultAnnotationHandlerMapping defaultAnnotationHandlerMapping =   new DefaultAnnotationHandlerMapping();
//        Object[] objects = new Object[1];
//        objects[0] = new OpenEntityManagerInViewInterceptor();
//        defaultAnnotationHandlerMapping.setInterceptors(objects);
//        return defaultAnnotationHandlerMapping;
//    }






    @Bean
    public Hibernate4Module hibernate4Module() {
        return new Hibernate4Module();
    }
}
