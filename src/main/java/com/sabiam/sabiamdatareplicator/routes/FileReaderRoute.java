package com.sabiam.sabiamdatareplicator.routes;

import com.sabiam.sabiamdatareplicator.model.SellerInbound;
import com.sabiam.sabiamdatareplicator.processor.SellerProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class FileReaderRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file://D:/Users/bmo/tmp/")
            .routeId("inputFile")
            .log(LoggingLevel.INFO, "#### Starting File Route")
            .unmarshal().json(JsonLibrary.Jackson, SellerInbound.class)
            .process(new SellerProcessor());
    }
}
