package com.sabiam.sabiamdatareplicator.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("spring.r2dbc.default")
@Configuration
@Data
public class R2dbcDefaultConnectionProperties extends DBProperties {
}
