package com.trecapps.sm.profile.service;

import com.trecapps.sm.common.models.ResponseObj;
import com.trecapps.sm.profile.dto.Favorite;
import com.trecapps.sm.profile.dto.PostProfile;
import com.trecapps.sm.profile.dto.ProfileSearchResult;
import com.trecapps.sm.profile.dto.SkillPost;
import com.trecapps.sm.profile.models.Education;
import com.trecapps.sm.profile.models.Profile;
import com.trecapps.sm.profile.models.WorkExpHolder;
import com.trecapps.sm.profile.repos.ProfileRepoMongo;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MongoProfileService implements ProfileService {

    @Autowired
    ProfileRepoMongo profileRepo;



    @Override
    public Mono<ResponseObj> createProfile(String userId, @Nullable String brandId, PostProfile post) {



        return null;
    }

    @Override
    public Mono<List<ProfileSearchResult>> searchProfiles(String userId, String query, int page, int size) {
        return null;
    }

    @Override
    public Mono<Profile> getProfile(String userId, @Nullable String brandId, String profileId) {
        return null;
    }

    @Override
    public Mono<ResponseObj> updateFavorites(String userId, @Nullable String brandId, List<Favorite> favorites) {
        return null;
    }

    @Override
    public Mono<ResponseObj> setEducation(String userId, @Nullable String brandId, @Nullable String eduId, Education education) {
        return null;
    }

    @Override
    public Mono<ResponseObj> setWorkExperience(String userId, @Nullable String brandId, @Nullable String perspective, WorkExpHolder experience) {
        return null;
    }

    @Override
    public Mono<ResponseObj> setSkill(String userId, @Nullable String brandId, String name, SkillPost skillPost) {
        return null;
    }

    @Override
    public Mono<ResponseObj> removeEducation(String userId, @Nullable String brandId, String eduId) {
        return null;
    }

    @Override
    public Mono<ResponseObj> removeWorkExperience(String userId, @Nullable String brandId, String perspective) {
        return null;
    }

    @Override
    public Mono<ResponseObj> removeSkill(String userId, @Nullable String brandId, List<String> names) {
        return null;
    }
}
