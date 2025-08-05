package com.trecapps.sm.profile.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Index;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Data
@Document("Profile-${trecapps.sm.name}")
public class Profile {

    @MongoId
    String id;  // Prefix should be "User-" or "Brand-" and the UUID of the user/brand

    @Indexed
    String title;

    String aboutMe;

    List<ProfileLink> links = new ArrayList<>();

    String pronouns;

    // For Coffeeshop
    ObjectNode favorites;

    List<Education> education; // and Water Cooler

    Set<String> brandLikes;

    List<BrandDislike> brandDislikes;

    // Water Cooler

    List<WorkExpHolder> workExperiences;

    List<Skill> skills;


    // Validator methods

    boolean validatePronouns(){
        if(Constants.stringNullOrEmpty(pronouns)) return true;

        String tempPronouns = pronouns.toLowerCase(Locale.ROOT).replaceAll(" ", "");
        for(String validPronoun : Constants.VALID_PRONOUNS){
            if(validPronoun.equals(tempPronouns)) {
                pronouns = tempPronouns;
                return true;
            }
        }
        return false;
    }

}
