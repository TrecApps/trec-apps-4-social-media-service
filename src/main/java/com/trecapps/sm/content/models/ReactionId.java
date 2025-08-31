package com.trecapps.sm.content.models;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@Data
@PrimaryKeyClass
public class ReactionId {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    @Column("content_id")
    String contentId;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 1)
    @Column("user_id")
    String userId;
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 2)
    String type;        // The type of reaction
}
