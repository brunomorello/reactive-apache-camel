package com.sabiam.sabiamdatareplicator.routes;

import com.sabiam.sabiamdatareplicator.model.CustomerInbound;
import com.sabiam.sabiamdatareplicator.model.SellerInbound;
import com.sabiam.sabiamdatareplicator.processor.CustomerProcessor;
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

        onException(NullPointerException.class)
                .routeId("errorDrools")
                .handled(true)
                .log(LoggingLevel.ERROR, "Error to find Drools xls with proper rules");

        restConfiguration()
                .component("jetty")
                .host("0.0.0.0")
                .port(8081)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true);

        rest("inboundData")
                .produces("application/json")
                .post("seller").type(SellerInbound.class)
                .routeId("inboundDataSeller")
                .type(SellerInbound.class)
                .to("direct:processSeller");

        rest("inboundData")
                .produces("application/json")
                .post("customer")
                .type(CustomerInbound.class)
                .routeId("inboundCustomer")
                .to("direct:processCustomer");

        from("direct:processSeller")
                .routeId("processSeller")
                .bean("sellerBean", "createSeller")
                .process(new UnwrapStreamProcessor())
                .log(LoggingLevel.INFO, "processing inbound seller");

        from("direct:processCustomer")
                .log(LoggingLevel.INFO, "Processing Customer: ${body}")
                .routeId("processCustomer")
                .process(new CustomerProcessor())
                .log(LoggingLevel.INFO, "Customer Processed: ${body}");
    }
}
