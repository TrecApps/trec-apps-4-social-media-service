package com.trecapps.sm.common.models;

import lombok.Data;

@Data
public class SocialMediaEvent {

    String resourceId;
    String postId;
    SocialMediaEventType type;
    String module;
    String profile;
    String userId;
    String reaction;
    String category;
}
