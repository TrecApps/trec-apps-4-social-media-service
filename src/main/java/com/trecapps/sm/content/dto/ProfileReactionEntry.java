package com.trecapps.sm.content.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ProfileReactionEntry {

    String contentId;
    String brandId;
    String type;
    String version;
    boolean isPrivate;
    boolean isStale;
    OffsetDateTime made;

}
