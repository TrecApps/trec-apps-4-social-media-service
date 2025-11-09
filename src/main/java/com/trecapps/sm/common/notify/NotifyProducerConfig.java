package com.trecapps.sm.common.notify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
@Order(0)
public class NotifyProducerConfig {

    @Bean
    @ConditionalOnProperty(
            prefix = "trecapps.notify.producer",
            name = {"strategy"},
            havingValue = "azure-service-bus-entra"
    )
    ISMProducer getNotifyProducerServiceBusEntra(
            @Value("${trecapps.notify.producer.queue}") String queue,
            @Value("${trecapps.notify.producer.namespace}") String namespace,
            Jackson2ObjectMapperBuilder objectMapperBuilder) {
        return new ServiceBusSMProducer(queue, namespace, objectMapperBuilder, false);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "trecapps.notify.producer",
            name = {"strategy"},
            havingValue = "azure-service-bus-connection-string"
    )
    ISMProducer getNotifyProducerServiceBusConnString(
            @Value("${trecapps.notify.producer.queue}") String queue,
            @Value("${trecapps.notify.producer.connection}") String connection,
            Jackson2ObjectMapperBuilder objectMapperBuilder) {
        return new ServiceBusSMProducer(queue, connection, objectMapperBuilder, true);
    }
}
