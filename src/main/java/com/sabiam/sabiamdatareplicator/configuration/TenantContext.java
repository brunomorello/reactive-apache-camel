package com.sabiam.sabiamdatareplicator.configuration;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

public class TenantContext {

    public static final String TENANT_ID = TenantContext.class.getName() + ".TENANT_ID";

    public static Context withTenantId(String id) {
        return Context.of(TENANT_ID, Mono.just(id));
    }

    public static Mono<String> getTenantId() {
        return Mono.deferContextual(contextView -> contextView.hasKey(TENANT_ID) ? contextView.get(TENANT_ID) : Mono.empty());
    }

    public static Function<Context, Context> clearContext() {
        return (context) -> context.delete(TENANT_ID);
    }
}
