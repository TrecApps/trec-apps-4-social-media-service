package com.trecapps.sm.profile.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@Document("Profile-Filter-${trecapps.sm.name}")
public class ProfileFilterList {

    @MongoId
    String id;

    List<ProfileFilter> filterList;


}
