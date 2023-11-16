package com.sabiam.sabiamdatareplicator.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hazelcast.policy.HazelcastRoutePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BatchReadFileRoute extends RouteBuilder {

    @Autowired
    HazelcastRoutePolicy hazelcastRoutePolicy;

    @Override
    public void configure() throws Exception {
        from("timer:batch?period=60000")
                .routeId("batchMessageRouteId")
                .routePolicy(hazelcastRoutePolicy)
                .process(exchange -> {
                    exchange.getOut().setBody(UUID.randomUUID().toString(), String.class);
                })
                .log(LoggingLevel.INFO, "File Updated: ${body}")
                .to("file:D:/Users/bmo/tmp/?fileName=testBatch.txt&fileExist=append&appendChars=\\n")
        .end();
    }
}
