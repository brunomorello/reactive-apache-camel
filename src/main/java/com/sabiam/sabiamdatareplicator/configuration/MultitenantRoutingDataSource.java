package com.sabiam.sabiamdatareplicator.configuration;

import io.r2dbc.spi.ConnectionFactoryMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public class MultitenantRoutingDataSource extends AbstractRoutingConnectionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultitenantRoutingDataSource.class);

    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        LOGGER.info("determineCurrentLookupKey: {}", TenantContext.getTenantId().map(id -> id).log());
        return Mono
                .deferContextual(Mono::just)
                .filter(it -> it.hasKey(TenantContext.TENANT_ID))
                .map(it -> it.get(TenantContext.TENANT_ID))
                .transform(m -> errorIfEmpty(m, () -> new RuntimeException(String.format("ContextView does not contain the Lookup Key '%s'", TenantContext.TENANT_ID))));
    }

    @Override
    public ConnectionFactoryMetadata getMetadata() {
        return PostgresqlConnectionFactoryMetadata.INSTANCE;
    }

    public static <R> Mono<R> errorIfEmpty(Mono<R> mono, Supplier<Throwable> throwableSupplier) {
        return mono.switchIfEmpty(Mono.defer(() -> Mono.error(throwableSupplier.get())));
    }
}
