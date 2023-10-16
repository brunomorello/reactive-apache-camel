package com.sabiam.sabiamdatareplicator.configuration;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.sabiam.sabiamdatareplicator.repository", entityOperationsRef = "tenantEntityTemplate")
public class R2dbcConfiguration {

    private List<DBProperties> dbPropertiesList;

    @Autowired
    private R2dbcConnectionPropertiesNigeria r2DbcConnectionPropertiesNigeria;
    @Autowired
    private R2dbcConnectionPropertiesKenya r2dbcConnectionPropertiesKenya;
    @Autowired
    private R2dbcConnectionPropertiesSouthAfrica r2dbcConnectionPropertiesSouthAfrica;
    private final HashMap<Object, Object> tenantConnectionFactoriesMap = new HashMap<>();

    @Bean
    @Qualifier("tenantConnectionFactory")
    public ConnectionFactory tenantConnectionFactory() {
        var tenantConnectionFactory = new MultitenantRoutingDataSource();
        tenantConnectionFactory.setDefaultTargetConnectionFactory(tenantConnectionFactoriesMap.get("Nigeria"));
        tenantConnectionFactory.setTargetConnectionFactories(tenantConnectionFactoriesMap);
        return tenantConnectionFactory;
    }

    @Bean
    public R2dbcEntityOperations tenantEntityTemplate(@Qualifier("tenantConnectionFactory") ConnectionFactory connectionFactory) {

        R2dbcDialect dialect = DialectResolver.getDialect(connectionFactory);
        DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(dialect);
        DatabaseClient databaseClient = DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                .bindMarkers(dialect.getBindMarkersFactory())
                .build();

        return new R2dbcEntityTemplate(databaseClient, strategy);
    }

    @PostConstruct
    void setup() {
        dbPropertiesList = Arrays.asList(r2DbcConnectionPropertiesNigeria,
                r2dbcConnectionPropertiesKenya,
                r2dbcConnectionPropertiesSouthAfrica
        );

        dbPropertiesList.forEach(dbProperty -> {
            PostgresqlConnectionConfiguration postgresqlConnectionConfiguration = PostgresqlConnectionConfiguration.builder()
                    .host(dbProperty.getHost())
                    .port(dbProperty.getPort())
                    .username(dbProperty.getUsername())
                    .password(dbProperty.getPassword())
                    .build();
            tenantConnectionFactoriesMap.put(dbProperty.getTenantId(), new PostgresqlConnectionFactory(postgresqlConnectionConfiguration));
        });
    }


}
