package com.trecapps.sm.profile.pipeline;

import com.trecapps.sm.common.models.SocialMediaEvent;

@FunctionalInterface
public interface IEventHandler {
    boolean processEvent(SocialMediaEvent var1);
}
