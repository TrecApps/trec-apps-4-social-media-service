package com.trecapps.sm.profile.repos;

import com.trecapps.sm.profile.models.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ProfileRepoMongo extends ReactiveMongoRepository<Profile, String> {
    
    @Query(value = "{\"$or\": [{ 'title' : { '$regex' : '?0', '$options' : 'i'}}, { 'aboutMeShort' : { '$regex' : '?0', '$options' : 'i'}}]}")
    Flux<Profile> findProfileByQuery(String query, List<String> blockers, Pageable page);
}
