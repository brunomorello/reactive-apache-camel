package com.sabiam.sabiamdatareplicator.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("spring.r2dbc.southafrica")
@Configuration
@Data
public class R2dbcConnectionPropertiesSouthAfrica extends DBProperties {
    private static final String TENANT_ID = "SouthAfrica";

    public R2dbcConnectionPropertiesSouthAfrica() {
        this.setTenantId(TENANT_ID);
    }
}
