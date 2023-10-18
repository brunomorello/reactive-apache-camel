package com.sabiam.sabiamdatareplicator.beans;

import com.sabiam.sabiamdatareplicator.configuration.TenantContext;
import com.sabiam.sabiamdatareplicator.model.SellerInbound;
import com.sabiam.sabiamdatareplicator.model.Seller;
import com.sabiam.sabiamdatareplicator.repository.SellerRepository;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Component("sellerBean")
public class SellerInboundStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(SellerInboundStream.class);

    @Autowired
    private SellerRepository repository;

    public Publisher<Seller> createSeller(Publisher<SellerInbound> sellerInboundPublisher) {
        return Mono.from(sellerInboundPublisher)
            .log()
            .map(sellerInbound -> {
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
