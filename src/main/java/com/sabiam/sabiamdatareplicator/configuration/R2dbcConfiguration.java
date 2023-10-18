package com.sabiam.sabiamdatareplicator.configuration;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories
@RequiredArgsConstructor
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {

    @Autowired
    private R2dbcDefaultConnectionProperties r2dbcDefaultConnectionProperties;
    @Autowired
    private R2dbcConnectionPropertiesNigeria r2DbcConnectionPropertiesNigeria;
    @Autowired
    private R2dbcConnectionPropertiesKenya r2dbcConnectionPropertiesKenya;
    @Autowired
    private R2dbcConnectionPropertiesSouthAfrica r2dbcConnectionPropertiesSouthAfrica;

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        var routingConnectionFactory = createRoutingConnectionFactory();
        routingConnectionFactory.afterPropertiesSet();
        return routingConnectionFactory;
    }

    private AbstractRoutingConnectionFactory createRoutingConnectionFactory() {
        var routingConnectionFactory = new MultitenantRoutingDataSource();
        routingConnectionFactory.setDefaultTargetConnectionFactory(this.defaultConn());
        routingConnectionFactory.setTargetConnectionFactories(this.dbConnectionMap());
        return routingConnectionFactory;
    }

    private ConnectionFactory defaultConn() {
        return new PostgresqlConnectionFactory(
             PostgresqlConnectionConfiguration.builder()
                     .host(r2dbcDefaultConnectionProperties.getHost())
                     .port(r2dbcDefaultConnectionProperties.getPort())
                     .database(r2dbcDefaultConnectionProperties.getDatabase())
                     .username(r2dbcDefaultConnectionProperties.getUsername())
                     .password(r2dbcDefaultConnectionProperties.getPassword())
             .build()
        );
    }

    private Map<String, ConnectionFactory> dbConnectionMap() {
        return Map.ofEntries(
                Map.entry(r2DbcConnectionPropertiesNigeria.getTenantId(), createConnection(r2DbcConnectionPropertiesNigeria)),
                Map.entry(r2dbcConnectionPropertiesKenya.getTenantId(), createConnection(r2dbcConnectionPropertiesKenya)),
                Map.entry(r2dbcConnectionPropertiesSouthAfrica.getTenantId(), createConnection(r2dbcConnectionPropertiesSouthAfrica))
        );
    }

    private PostgresqlConnectionFactory createConnection(DBProperties dbProperty) {
        return new PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                    .host(dbProperty.getHost())
                    .port(dbProperty.getPort())
                    .database(dbProperty.getDatabase())
                    .username(dbProperty.getUsername())
                    .password(dbProperty.getPassword())
            .build()
        );
    }
}
