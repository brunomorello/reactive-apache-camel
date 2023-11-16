package com.sabiam.sabiamdatareplicator.policies;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.component.hazelcast.policy.HazelcastRoutePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class HazelcastPolicy {

    @Bean
    public HazelcastRoutePolicy getHazelcastPolicy() {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        HazelcastRoutePolicy hazelcastRoutePolicy = new HazelcastRoutePolicy(hazelcastInstance);

        hazelcastRoutePolicy.setLockMapName("testLockMap");
        hazelcastRoutePolicy.setLockKey("testLockKey");
        hazelcastRoutePolicy.setLockValue("testLockValue");

        hazelcastRoutePolicy.setTryLockTimeout(5, TimeUnit.SECONDS);

        return hazelcastRoutePolicy;
    }
}
