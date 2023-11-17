package com.sabiam.sabiamdatareplicator.routes;

import com.sabiam.sabiamdatareplicator.model.Seller;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class BatchExportUsersRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:readDB?period=50000")
                .routeId("readDB")
                .bean("usersBean", "getUsers")
                .split(body())
                    .log("Processing User: ${body}")
                    .process(exchange -> {
                        final Seller seller = exchange.getIn().getBody(Seller.class);
                        exchange.getOut().setBody(seller.toString());
                    })
                    .to("file:D:/Users/bmo/tmp/?fileName=usersExport.txt&fileExist=append&appendChars=\\n")
                .end();
    }
}
