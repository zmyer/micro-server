package com.oath.micro.server.health;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oath.micro.server.HealthStatusChecker;
import com.oath.micro.server.errors.ErrorBus;
import com.oath.micro.server.health.HealthStatus.State;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import lombok.Getter;

@Component
public class HealthCheck implements HealthStatusChecker {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HealthChecker healthCheckHelper;

    @Getter
    private volatile int maxSize;

    private final int hardMax;
    private final EventBus errorBus;

    @Autowired
    public HealthCheck(HealthChecker healthChecker, @Value("${health.check.error.list.size:25}") int maxSize,
            @Value("${health.check.max.error.list.size:2500}") int hardMax, EventBus errorBus) {
        this.healthCheckHelper = healthChecker;
        this.maxSize = maxSize;
        this.hardMax = hardMax;
        this.errorBus = errorBus;
    }

    final ConcurrentLinkedQueue<ErrorEvent> errors = new ConcurrentLinkedQueue<>();
    final ConcurrentLinkedQueue<ErrorEvent> fatalErrors = new ConcurrentLinkedQueue<>();

    @PostConstruct
    public void register() {
        errorBus.register(this);
        ErrorBus.setErrorBus(errorBus);
    }

    public void setMaxSize(int maxSize) {
        if (maxSize <= hardMax)
            this.maxSize = maxSize;
    }

    private Void handle(ErrorEvent e, ConcurrentLinkedQueue<ErrorEvent> queue) {
        while (queue.size() >= maxSize)
            queue.poll();
        queue.offer(e);
        return null;
    }

    @Subscribe
    public void onEvent(final ErrorEvent event) {
        event.visit(e -> handle(e, fatalErrors), e -> handle(e, errors));

    }

    public HealthStatus checkHealthStatus() {
        return healthCheckHelper.checkHealthStatus(errors, this.fatalErrors);
    }

    @Override
    public boolean isOk() {
        State state = checkHealthStatus().getGeneralProcessing();
        if (state.equals(State.Fatal))
            return false;
        return true;
    }

}
