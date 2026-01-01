package com.trecapps.sm.common.notify;


import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderAsyncClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class ServiceBusSMProducer implements ISMProducer {
    ServiceBusSenderAsyncClient serviceBusSenderClient;
    ObjectMapper objectMapper;

    ServiceBusSMProducer(String queueName, String connection, ObjectMapper objectMapper, boolean useConnectionString) {
        if (useConnectionString) {
            this.serviceBusSenderClient = (new ServiceBusClientBuilder()).connectionString(connection).sender().queueName(queueName).buildAsyncClient();
        } else {
            DefaultAzureCredential credential = (new DefaultAzureCredentialBuilder()).build();
            this.serviceBusSenderClient = (new ServiceBusClientBuilder()).fullyQualifiedNamespace(String.format("%s.servicebus.windows.net", connection)).credential(credential).sender().queueName(queueName).buildAsyncClient();
        }

        this.objectMapper = objectMapper;
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    @SneakyThrows
    public Mono<Boolean> sendNotification(NotificationPost post) {
        return this.serviceBusSenderClient.sendMessage(new ServiceBusMessage(this.objectMapper.writeValueAsString(post)))
                .thenReturn(true)
                .onErrorResume((Throwable thrown) -> {
                    log.error("Failed to send message {}, reason is: {}", post, thrown.getMessage());
                    return Mono.just(false);
                });
    }
}
