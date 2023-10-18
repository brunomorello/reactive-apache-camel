package com.sabiam.sabiamdatareplicator.configuration;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.function.Function;

public class TenantContext {

    public static final String TENANT_KEY = "TENANT_KEY";

    public static Context withTenantId(String id) {
        return Context.of(TENANT_KEY, Mono.just(id));
    }

    public static Mono<Object> getTenantKey() {
        return Mono.deferContextual(contextView -> contextView.hasKey(TENANT_KEY) ? contextView.get(TENANT_KEY) : Mono.empty());
    }

    public static Function<Context, Context> clearContext() {
        return (context) -> context.delete(TENANT_KEY);
    }
}
