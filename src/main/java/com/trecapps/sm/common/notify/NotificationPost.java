package com.trecapps.sm.common.notify;

import lombok.Data;

@Data
public class NotificationPost {

    String userId;      // The user this applies to
    String brandId;     // The brand this applies to
    String appId;       // The App that sent this

    String relevantId;  // The id of the content this is relevant to
    String relevantIdSecondary;

    // Image Information
    ImageEndpointType type = ImageEndpointType.REGULAR;
    String imageId;

    String message;
    String category;
}
