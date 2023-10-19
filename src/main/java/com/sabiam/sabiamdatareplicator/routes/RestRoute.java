package com.sabiam.sabiamdatareplicator.routes;

import com.sabiam.sabiamdatareplicator.model.SellerInbound;
import com.sabiam.sabiamdatareplicator.processor.SellerProcessor;
import com.sabiam.sabiamdatareplicator.repository.SellerRepository;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.reactive.streams.util.UnwrapStreamProcessor;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestRoute extends RouteBuilder {

    @Autowired
    private SellerProcessor sellerProcessor;

    @Autowired
    private SellerRepository repository;

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("jetty")
                .host("0.0.0.0")
                .port(8080)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true);

        rest("inboundData")
                .produces("application/json")
                .post("seller").type(SellerInbound.class)
                .routeId("inboundDataSeller")
                .type(SellerInbound.class)
                .to("direct:processSeller");

        from("direct:processSeller")
                .routeId("processSeller")
                .bean("sellerBean", "createSeller")
                .process(new UnwrapStreamProcessor())
                .log(LoggingLevel.INFO, "processing inbound seller");
    }
}
