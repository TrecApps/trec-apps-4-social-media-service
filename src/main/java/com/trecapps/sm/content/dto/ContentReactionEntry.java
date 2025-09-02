package com.trecapps.sm.content.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ContentReactionEntry {

    String profileId;
    String type;
    OffsetDateTime made;

}
