package com.trecapps.sm.profile.dto;

import com.trecapps.sm.common.models.SocialMediaEventType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostFilterRequest {

    @NotNull
    SocialMediaEventType type;
    String from;

    boolean decrease = true;

}
