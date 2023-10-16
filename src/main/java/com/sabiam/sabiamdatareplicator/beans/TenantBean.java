package com.sabiam.sabiamdatareplicator.beans;

import com.sabiam.sabiamdatareplicator.configuration.TenantContext;
import com.sabiam.sabiamdatareplicator.model.SellerInbound;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component("tenantBean")
public class TenantBean {
    public Publisher<SellerInbound> setTenant(Publisher<SellerInbound> sellerInboundPublisher) {
        return Flux.from(sellerInboundPublisher)
                .flatMap(sellerInbound -> {
                    TenantContext.withTenantId(sellerInbound.country());
                    return Flux.from(sellerInboundPublisher);
                });
    }
}
