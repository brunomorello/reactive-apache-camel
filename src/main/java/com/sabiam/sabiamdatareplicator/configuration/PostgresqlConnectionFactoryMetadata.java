package com.sabiam.sabiamdatareplicator.configuration;

import io.r2dbc.spi.ConnectionFactoryMetadata;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostgresqlConnectionFactoryMetadata implements ConnectionFactoryMetadata {

    static final PostgresqlConnectionFactoryMetadata INSTANCE = new PostgresqlConnectionFactoryMetadata();
    public static final String NAME = "PostgreSQL";

    @Override
    public String getName() {
        return NAME;
    }
}
