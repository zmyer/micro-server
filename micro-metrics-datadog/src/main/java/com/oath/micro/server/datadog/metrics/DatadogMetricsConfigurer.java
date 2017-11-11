package com.oath.micro.server.datadog.metrics;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cyclops.control.Maybe;
import lombok.extern.slf4j.Slf4j;
import org.coursera.metrics.datadog.DatadogReporter;
import org.coursera.metrics.datadog.transport.HttpTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

import lombok.Getter;

@Configuration
@EnableMetrics
@Slf4j
public class DatadogMetricsConfigurer extends MetricsConfigurerAdapter {

    private String apiKey;
    private List<String> tags;
    private int period;
    private TimeUnit timeUnit;
    @Getter
    private EnumSet<DatadogReporter.Expansion> expansions;

    private final String host;

    @Autowired
    public DatadogMetricsConfigurer(@Value("${datadog.apikey:#{null}}") String apiKey,
                                    @Value("${datadog.tags:{\"stage:dev\"}}") String tags,
                                    @Value("${datadog.report.period:1}") int period,
                                    @Value("${datadog.report.timeunit:SECONDS}") TimeUnit timeUnit,
                                    @Value("${datadog.report.expansions:#{null}}") String expStr,
                                    @Value("${host.address:#{null}}") String host){
        this.apiKey = apiKey;
        this.tags = Arrays.asList(Optional.ofNullable(tags)
                                          .orElse("")
                                          .split(","));
        this.period = period;
        this.timeUnit = timeUnit;
        this.expansions = expansions(expStr);
        this.host = host;
    }

    private EnumSet<DatadogReporter.Expansion> expansions(String expStr) {
        return Maybe.just(Maybe.ofNullable(expStr)
                               .map(s -> s.split(","))
                               .stream()
                               .flatMap(Stream::of)
                               .map(String::trim)
                               .map(DatadogReporter.Expansion::valueOf)
                               .collect(Collectors.toCollection(() -> EnumSet.noneOf(DatadogReporter.Expansion.class))))
                    .filter(s -> !s.isEmpty())
                    .orElse(DatadogReporter.Expansion.ALL);
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        if (Objects.isNull(apiKey)) {
           log.error("The 'datadog.apikey' is null. Datadog reporting will be ignored.");
           return;
        }
        HttpTransport httpTransport = new HttpTransport.Builder().withApiKey(apiKey)
                                                                 .build();
        EnumSet<DatadogReporter.Expansion> expansions = DatadogReporter.Expansion.ALL;
        DatadogReporter.Builder builder = DatadogReporter.forRegistry(metricRegistry)
                                                  .withTransport(httpTransport)
                                                  .withExpansions(expansions)
                                                  .withTags(tags);
 
        DatadogReporter reporter = (Objects.nonNull(host) ? builder.withHost(host) : builder).build();
        reporter.start(period, timeUnit);
        registerReporter(reporter);
    }
}
