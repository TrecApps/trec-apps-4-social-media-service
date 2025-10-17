package com.trecapps.sm.profile.models;

import com.trecapps.sm.common.models.SocialMediaEventType;
import com.trecapps.sm.profile.pipeline.PresentProbabilityDefaults;
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

    public double getProbablity(String id, SocialMediaEventType type){
        if(filterList != null)
            for(ProfileFilter filter: filterList){
                if(id.equals(filter.from) && type.equals(filter.type))
                    return filter.getProbability();
            }

        return PresentProbabilityDefaults.getDefaultByType(type);
    }
}
