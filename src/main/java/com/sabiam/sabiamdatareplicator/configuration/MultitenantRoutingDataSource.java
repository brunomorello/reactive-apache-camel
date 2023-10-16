package com.sabiam.sabiamdatareplicator.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Mono;

public class MultitenantRoutingDataSource extends AbstractRoutingConnectionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultitenantRoutingDataSource.class);

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        LOGGER.info("determineCurrentLookupKey: {}", TenantContext.getTenantId().map(id -> id).log());
        return TenantContext.getTenantId().map(id -> id);
    }
}
