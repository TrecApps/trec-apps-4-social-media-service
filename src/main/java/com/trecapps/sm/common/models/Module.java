package com.trecapps.sm.common.models;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document("Module-${trecapps.sm.name}")
@Data
public class Module {

    @MongoId
    UUID id;

    @Indexed
    String name;

    String about;

    String creator;

    Set<String> moderators;

    OffsetDateTime created;

    Set<String> possibleTags;

    List<String> rules;

    List<ModuleCompare> similarModules;
}
