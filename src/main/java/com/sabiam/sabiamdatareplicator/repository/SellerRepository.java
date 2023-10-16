package com.sabiam.sabiamdatareplicator.repository;

import com.sabiam.sabiamdatareplicator.model.Seller;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SellerRepository extends ReactiveCrudRepository<Seller, UUID> {

    @Modifying
    @Query("insert into users(id, public_id, email, phone_number, first_name, last_name, created_date, created_by) " +
            "values (:#{#seller.id}, :#{#seller.publicId}, :#{#seller.email}, :#{#seller.phoneNumber}, :#{#seller.firstName}, :#{#seller.lastName}, :#{#seller.createdDate}, :#{#seller.createdBy})")
    Mono<Seller> save(final Seller seller);
}
