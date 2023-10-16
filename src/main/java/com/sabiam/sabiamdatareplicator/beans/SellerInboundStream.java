package com.sabiam.sabiamdatareplicator.beans;

import com.sabiam.commons.response.AppResponse;
import com.sabiam.sabiamdatareplicator.configuration.TenantContext;
import com.sabiam.sabiamdatareplicator.model.SellerInbound;
import com.sabiam.sabiamdatareplicator.model.Seller;
import com.sabiam.sabiamdatareplicator.repository.SellerRepository;
import jakarta.annotation.PostConstruct;
import org.apache.camel.Configuration;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.eclipse.jetty.http.HttpStatus;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Component("sellerBean")
public class SellerInboundStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerInboundStream.class);

    @Autowired
    private SellerRepository repository;

    public Publisher<Seller> createSeller(Publisher<SellerInbound> sellerInboundPublisher) {
        return Flux.from(sellerInboundPublisher)
            .log()
            .map(sellerInbound -> {
                TenantContext.withTenantId(sellerInbound.country());
                Seller seller = Seller.builder()
                    .id(UUID.randomUUID())
                    .publicId(UUID.randomUUID())
                    .firstName(sellerInbound.firstName())
                    .lastName(sellerInbound.lastName())
                    .email(sellerInbound.email())
                    .phoneNumber(sellerInbound.phoneNumber())
                    .createdDate(LocalDateTime.parse(sellerInbound.createdAt()))
                    .createdBy(sellerInbound.createdBy())
                    .build();
                return seller;
            })
            .log()
            .flatMap(seller -> repository.save(seller)
                .onErrorResume(throwable -> {
                    LOGGER.error("Error to Create Seller {}", throwable.getMessage());
                    throw new IllegalArgumentException("Error to Create Seller");
                }));
    }

}
