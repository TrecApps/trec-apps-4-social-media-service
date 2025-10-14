package com.trecapps.sm.profile.models;

import com.trecapps.sm.common.models.SocialMediaEventType;
import lombok.Data;

@Data
public class ProfileFilter {

    String from;
    SocialMediaEventType type;
    double probability;

}
