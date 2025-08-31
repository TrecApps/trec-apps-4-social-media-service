package com.trecapps.sm.content.models;

import org.springframework.data.cassandra.core.mapping.*;

import java.time.OffsetDateTime;

@Table("reactionEntry")
public class ReactionEntity {

    @PrimaryKey
    ReactionId reactionId;

    String brandId;     // The brand id used when reacting to the content
    String type;        // The type of reaction
    String version;     // the version of the content being reacted to
    OffsetDateTime made;// When the Reaction was made
    boolean isStale;    // Whether the content has been updated since the reaction was made


}
