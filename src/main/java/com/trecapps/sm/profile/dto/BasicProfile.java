package com.trecapps.sm.profile.dto;

import com.trecapps.sm.profile.models.Profile;
import lombok.Data;

@Data
public class BasicProfile {
    String id;
    String displayName;
    String shortAboutMe;
    String pronouns;

    public static BasicProfile getInstance(Profile profile) {
        BasicProfile ret = new BasicProfile();
        ret.setId(profile.getId());
        ret.setDisplayName(profile.getTitle());
        ret.setShortAboutMe(profile.getAboutMeShort());

        PronounVisibility vis = profile.getPronounVisibility();
        if(vis == PronounVisibility.SHOW_ALL || vis == PronounVisibility.SHOW_ON_POSTS){
            ret.setPronouns(profile.getPronouns());
        }
        return ret;
    }
}
