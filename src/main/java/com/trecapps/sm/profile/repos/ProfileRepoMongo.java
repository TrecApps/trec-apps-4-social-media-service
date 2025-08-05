package com.trecapps.sm.profile.repos;

import com.trecapps.sm.profile.models.Profile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProfileRepoMongo extends ReactiveMongoRepository<Profile, String> {
}
