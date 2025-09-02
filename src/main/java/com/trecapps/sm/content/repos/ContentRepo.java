package com.trecapps.sm.content.repos;

import com.trecapps.sm.content.models.Posting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ContentRepo extends ReactiveMongoRepository<Posting, String> {
    @Query("{$or: ['profilePoster': profileId, 'profileOwner': profileId]}")
    Flux<Posting> getContentByProfileId(String profileId, Pageable page);

    @Query("{'moduleId': moduleId}")
    Flux<Posting> getContentByModuleId(String moduleId, Pageable page);

    @Query("{'moduleId': moduleId, $or: ['profilePoster': profileId, 'profileOwner': profileId]}")
    Flux<Posting> getContentByModuleAndProfileId(String moduleId, String profileID, Pageable page);
}
