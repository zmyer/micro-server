package com.oath.micro.server.spring.metrics.health;

import cyclops.companion.Semigroups;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import com.oath.micro.server.HealthStatusChecker;
import com.codahale.metrics.health.HealthCheckRegistry;

@Component
public class HealthCheckRunner implements HealthStatusChecker {

    private final HealthCheckRegistry healthChecks;
    private final long healthDelay;
    private volatile long lastRan = 0;
    private volatile boolean lastRanValue = true;

    public HealthCheckRunner(@Value("${metrics.health.delay:1000}") long healthDelay,
            HealthCheckRegistry healthChecks) {
        this.healthDelay = healthDelay;
        this.healthChecks = healthChecks;
    }

    @Override
    public boolean isOk() {
        if (System.currentTimeMillis() - healthDelay < lastRan)
            return lastRanValue;
        return healthChecks.runHealthChecks()
                           .values()
                           .stream()
                           .map(hc -> hc.isHealthy())
                           .reduce(true, Semigroups.booleanConjunction);
    }

}
