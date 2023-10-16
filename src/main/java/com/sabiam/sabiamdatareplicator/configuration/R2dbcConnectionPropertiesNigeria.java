package com.sabiam.sabiamdatareplicator.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("spring.r2dbc.nigeria")
@Configuration
@Data
public class R2dbcConnectionPropertiesNigeria extends DBProperties {
    private static final String TENANT_ID = "Nigeria";

    public R2dbcConnectionPropertiesNigeria() {
        this.setTenantId(TENANT_ID);
    }
}
