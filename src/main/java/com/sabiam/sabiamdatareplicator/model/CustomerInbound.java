package com.sabiam.sabiamdatareplicator.model;

import lombok.Data;

@Data
public class CustomerInbound {
    private String firstName;
    private String lastName;
    private String countryCode;
    private String phoneNumber;
    private String country;
}
