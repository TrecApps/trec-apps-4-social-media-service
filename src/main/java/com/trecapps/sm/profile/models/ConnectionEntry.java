package com.trecapps.sm.profile.models;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.time.OffsetDateTime;

@Table("connectionEntry")
@Data
public class ConnectionEntry {
    @PrimaryKey
    ConnectionLink id;
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    OffsetDateTime made;
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    OffsetDateTime accepted;
    @Column("one_way") boolean oneWay;             // Set true when a Brand Profile is involved, false if both parties are User Profiles

    public void setMade(Instant instant) {
        made = instant.atOffset(OffsetDateTime.now().getOffset());
    }

    public void setMade(OffsetDateTime made){
        this.made = made;
    }

    public void setAccepted(Instant instant) {
        accepted = instant.atOffset(OffsetDateTime.now().getOffset());
    }

    public void setAccepted(OffsetDateTime accepted){
        this.accepted = accepted;
    }
}
