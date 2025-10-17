package com.trecapps.sm.profile.pipeline;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trecapps.sm.common.models.SocialMediaEvent;
import lombok.SneakyThrows;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class AzureServiceBusEventConsumer implements IEventConsumer {
    ServiceBusProcessorClient processorClient;
    IEventHandler handler;
    ObjectMapper objectMapper;

    AzureServiceBusEventConsumer(String queue, String connector, Jackson2ObjectMapperBuilder objectMapperBuilder1, boolean useConnectionString) {
        if (useConnectionString) {
            this.processorClient = (new ServiceBusClientBuilder())
                    .connectionString(connector)
                    .processor()
                    .queueName(queue)
                    .processMessage(this::processMessage)
                    .processError(this::processError)
                    .buildProcessorClient();
        } else {
            DefaultAzureCredential credential = (new DefaultAzureCredentialBuilder()).build();
            this.processorClient = (new ServiceBusClientBuilder()).fullyQualifiedNamespace(String.format("%s.servicebus.windows.net", connector)).credential(credential).processor().queueName(queue).processMessage(this::processMessage).processError(this::processError).buildProcessorClient();
        }

        this.objectMapper = objectMapperBuilder1.createXmlMapper(false).build();
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @SneakyThrows
    void processMessage(ServiceBusReceivedMessageContext context) {

            ServiceBusReceivedMessage message = context.getMessage();
            String messageStr = message.getBody().toString();
            SocialMediaEvent event = (SocialMediaEvent)this.objectMapper.readValue(messageStr, SocialMediaEvent.class);

            this.handler.processEvent(event).doOnNext((Boolean success) -> {
                if (success) {
                    context.complete();
                } else {
                    context.deadLetter();
                }
            }).subscribe();

    }

    void processError(ServiceBusErrorContext context) {
    }

    public void initialize(IEventHandler handler) {
        this.handler = handler;
        if (handler != null) {
            this.processorClient.start();
        }

    }
}
