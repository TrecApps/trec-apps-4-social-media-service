package com.trecapps.sm.profile.models;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.OffsetDateTime;

@Table("connectionEntry")
@Data
public class ConnectionEntry {
    @PrimaryKey
    ConnectionLink id;
    OffsetDateTime made;
    OffsetDateTime accepted;
    @Column("one_way") boolean oneWay;             // Set true when a Brand Profile is involved, false if both parties are User Profiles

}
