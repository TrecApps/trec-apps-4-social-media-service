package com.trecapps.sm.content.repos;

import com.trecapps.sm.content.models.ReactionEntity;
import com.trecapps.sm.content.models.ReactionId;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepo extends ReactiveCassandraRepository<ReactionEntity, ReactionId> {
}
