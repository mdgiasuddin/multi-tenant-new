package org.example.multitenant.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.lang.Boolean.TRUE;

@Configuration
@Slf4j
public class DataSourceConfig {

    @Value("${tenant.default}")
    private String defaultTenant;

    @Value("${tenant.properties-location}")
    private String propertiesLocation;

    @Bean
    @ConfigurationProperties(prefix = "tenants")
    public DataSource dataSource() {
        File[] files = Paths.get(propertiesLocation).toFile().listFiles();
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        assert files != null;
        for (File propertyFile : files) {
            Properties tenantProperties = new Properties();
            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

            try (FileInputStream fileInputStream = new FileInputStream(propertyFile)) {

                tenantProperties.load(fileInputStream);
                String tenantId = tenantProperties.getProperty("name");

                dataSourceBuilder.driverClassName(tenantProperties.getProperty("datasource.driver-class-name"));
                dataSourceBuilder.username(tenantProperties.getProperty("datasource.username"));
                dataSourceBuilder.password(tenantProperties.getProperty("datasource.password"));
                dataSourceBuilder.url(tenantProperties.getProperty("datasource.url"));
                DataSource dataSource = dataSourceBuilder.build();
                resolvedDataSources.put(tenantId, dataSource);

                migrateFlyway(dataSource);
            } catch (IOException e) {
                log.error("Error loading tenant properties", e);
            }
        }

        AbstractRoutingDataSource multitenantDataSource = new MultitenantDataSource();
        multitenantDataSource.setDefaultTargetDataSource(resolvedDataSources.get(defaultTenant));
        multitenantDataSource.setTargetDataSources(resolvedDataSources);

        multitenantDataSource.afterPropertiesSet();

        return multitenantDataSource;
    }

    private void migrateFlyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .baselineOnMigrate(TRUE)
                .validateOnMigrate(TRUE)
                .dataSource(dataSource)
                .load();

        flyway.migrate();
    }
}
