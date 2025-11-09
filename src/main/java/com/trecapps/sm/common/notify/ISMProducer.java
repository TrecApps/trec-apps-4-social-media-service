package com.trecapps.sm.common.notify;

import reactor.core.publisher.Mono;

public interface ISMProducer {

    Mono<Boolean> sendNotification(NotificationPost post);

}
