package com.trecapps.sm.content.models;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PostingContent {

    String content;
    OffsetDateTime made;

}
