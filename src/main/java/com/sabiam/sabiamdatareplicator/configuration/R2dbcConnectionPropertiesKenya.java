package com.sabiam.sabiamdatareplicator.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("spring.r2dbc.kenya")
@Configuration
@Data
public class R2dbcConnectionPropertiesKenya extends DBProperties {
    private static final String TENANT_ID = "Kenya";

    public R2dbcConnectionPropertiesKenya() {
        this.setTenantId(TENANT_ID);
    }
}
