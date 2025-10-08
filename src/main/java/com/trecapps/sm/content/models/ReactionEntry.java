package com.trecapps.sm.content.models;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.Instant;
import java.time.OffsetDateTime;

@Table("reaction_entry")
@Data
public class ReactionEntry {

    @PrimaryKey
    ReactionId reactionId;

    @Column("brand_id")
    String brandId;     // The brand id used when reacting to the content

    String version;     // the version of the content being reacted to
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    Instant made;// When the Reaction was made
    @Column("is_stale")
    boolean isStale;    // Whether the content has been updated since the reaction was made
    @Column("is_private")
    boolean isPrivate;  // Whether the id of the reactor should be private


    public void setMade(Instant instant) {
        this.made = instant;
    }

    public void setMade(OffsetDateTime made){
        this.made = made.toInstant();
    }
}
