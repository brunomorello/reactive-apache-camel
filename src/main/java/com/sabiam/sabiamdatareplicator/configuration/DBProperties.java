package com.sabiam.sabiamdatareplicator.configuration;

import lombok.Data;

@Data
public abstract class DBProperties {

    private String tenantId = "Default";
    private String host;
    private int port;
    private String username;
    private String password;
    private String database;

}
