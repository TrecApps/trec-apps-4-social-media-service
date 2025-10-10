package com.trecapps.sm.content.pipeline;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class InitiatorConfig {

    @Bean
    @ConditionalOnProperty(
            prefix = "trecapps.sminitiator",
            name = {"strategy"},
            havingValue = "azure-service-bus-entra"
    )
    IEventInitiator getServiceBusEntra(
            @Value("${trecapps.sminitiator.queue}") String queue,
            @Value("${trecapps.sminitiator.namespace}") String namespace,
            Jackson2ObjectMapperBuilder objectMapperBuilder) {
        return new AzureServiceBusEventInitiator(queue, namespace, objectMapperBuilder, false);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "trecapps.sminitiator",
            name = {"strategy"},
            havingValue = "azure-service-bus-connection-string"
    )
    IEventInitiator getServiceBusConnString(
            @Value("${trecapps.sminitiator.queue}") String queue,
            @Value("${trecapps.sminitiator.connection}") String connection,
            Jackson2ObjectMapperBuilder objectMapperBuilder) {
        return new AzureServiceBusEventInitiator(queue, connection, objectMapperBuilder, true);
    }
}
