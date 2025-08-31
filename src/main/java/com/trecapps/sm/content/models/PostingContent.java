package com.trecapps.sm.content.models;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PostingContent implements Comparable<PostingContent> {

    String content;
    OffsetDateTime made;
    String version;

    @Override
    public int compareTo(PostingContent o) {
        return made.compareTo(o.made);
    }
}
