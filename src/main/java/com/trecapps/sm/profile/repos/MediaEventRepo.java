package com.trecapps.sm.profile.repos;

import com.trecapps.sm.profile.models.MediaEventId;
import com.trecapps.sm.profile.models.SocialMediaEvent;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaEventRepo extends ReactiveCassandraRepository<SocialMediaEvent, MediaEventId> {
}
