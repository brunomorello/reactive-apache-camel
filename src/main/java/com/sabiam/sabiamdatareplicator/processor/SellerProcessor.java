package com.sabiam.sabiamdatareplicator.processor;

import com.sabiam.sabiamdatareplicator.model.SellerInbound;
import com.sabiam.sabiamdatareplicator.model.Seller;
import com.sabiam.sabiamdatareplicator.repository.SellerRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class SellerProcessor implements Processor {

    @Autowired
    private SellerRepository repository;

    @Override
    public void process(Exchange exchange) throws Exception {
        SellerInbound body = exchange.getIn().getBody(SellerInbound.class);
        System.out.println(body);
        Seller seller = Seller.builder()
                .id(UUID.randomUUID())
                .publicId(UUID.randomUUID())
                .firstName(body.firstName())
                .lastName(body.lastName())
                .email(body.email())
                .phoneNumber(body.phoneNumber())
                .createdBy(body.createdBy())
                .createdDate(LocalDateTime.parse(body.createdAt()))
                .build();

        Mono.just(repository.save(seller))
                .log();
    }
}
