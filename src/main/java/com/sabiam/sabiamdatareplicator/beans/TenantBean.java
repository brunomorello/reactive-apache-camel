package com.sabiam.sabiamdatareplicator.beans;

import com.sabiam.sabiamdatareplicator.configuration.TenantContext;
import com.sabiam.sabiamdatareplicator.model.SellerInbound;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("tenantBean")
public class TenantBean {
    public Publisher<SellerInbound> setTenant(Publisher<SellerInbound> sellerInboundPublisher) {
        return Mono.from(sellerInboundPublisher)
                .flatMap(sellerInbound -> Mono.from(sellerInboundPublisher)
                        .contextWrite(context -> context.put(TenantContext.TENANT_ID, sellerInbound.country())));
    }
}
