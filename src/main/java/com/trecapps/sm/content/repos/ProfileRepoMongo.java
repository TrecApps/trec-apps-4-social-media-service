package com.trecapps.sm.content.repos;

import com.trecapps.sm.profile.models.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ProfileRepoMongo extends ReactiveMongoRepository<Profile, String> {

    @Query("{$or: ['title': query, 'aboutMeShort': query]}")
    Flux<Profile> findProfileByQuery(String query, List<String> blockers, Pageable page);
}
