package com.sabiam.sabiamdatareplicator.model;

import java.time.LocalDateTime;

public record SellerInbound(String firstName, String lastName, String email, String phoneNumber, String createdAt, String createdBy, String country) {
}
