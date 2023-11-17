package com.sabiam.sabiamdatareplicator.processor;

import com.sabiam.sabiamdatareplicator.model.CustomerInbound;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.Objects;

public class CustomerProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        CustomerInbound customerInbound = exchange.getIn().getBody(CustomerInbound.class);

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        KieSession kieSession = kieContainer.newKieSession("ksession-rule");

        kieSession.insert(customerInbound);

        kieSession.fireAllRules();
        kieSession.destroy();

        if (Objects.isNull(customerInbound.getCountry())) {
            System.out.println("Rule Not applied - ERROR!");
        }

    }
}
