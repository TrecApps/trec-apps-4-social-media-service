package com.trecapps.sm.profile.repos;

import com.trecapps.sm.profile.models.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProfileRepoMongo extends ReactiveMongoRepository<Profile, String> {

    @Query("")
    Flux<Profile> findProfileByQuery(String query, Pageable page);
}
