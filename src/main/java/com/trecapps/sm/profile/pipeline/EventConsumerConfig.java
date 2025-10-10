package com.trecapps.sm.profile.pipeline;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class EventConsumerConfig {
    @Bean
    @ConditionalOnProperty(
            prefix = "trecapps.smconsumer",
            name = {"strategy"},
            havingValue = "azure-service-bus-entra"
    )
    IEventConsumer getServiceBusEntra(
            @Value("${trecapps.smconsumer.queue}") String queue,
            @Value("${trecapps.smconsumer.namespace}") String namespace,
            Jackson2ObjectMapperBuilder objectMapperBuilder) {
        return new AzureServiceBusEventConsumer(queue, namespace, objectMapperBuilder, false);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "trecapps.smconsumer",
            name = {"strategy"},
            havingValue = "azure-service-bus-connection-string"
    )
    IEventConsumer getServiceBusConnString(
            @Value("${trecapps.smconsumer.queue}") String queue,
            @Value("${trecapps.smconsumer.connection}") String connection,
            Jackson2ObjectMapperBuilder objectMapperBuilder) {
        return new AzureServiceBusEventConsumer(queue, connection, objectMapperBuilder, true);
    }
}
