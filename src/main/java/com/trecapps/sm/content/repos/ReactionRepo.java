package com.trecapps.sm.content.repos;

import com.trecapps.sm.content.models.ReactionEntity;
import com.trecapps.sm.content.models.ReactionId;
import com.trecapps.sm.content.models.ReactionTypeCount;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactionRepo extends ReactiveCassandraRepository<ReactionEntity, ReactionId> {

    @Query(value = "select new com.trecapps.sm.content.models.ReactionTypeCount(type, count(re) )" +
            " from reactionEntry re where content_id = :contentId group by re.type")
    Flux<ReactionTypeCount> findCountByContentId(String contentId);

    @Query(value = "select * from reactionEntry where content_id = :contentId")
    Flux<ReactionEntity> findByContentId(String contentId, Pageable page);

    @Query(value = "select * from reactionEntry where content_id = :contentId and type = :type")
    Flux<ReactionEntity> findByContentIdAndType(String contentId, String type, Pageable page);

    @Query(value = "select * from reactionEntry where user_id = :userId")
    Flux<ReactionEntity> findByUserId(String userId, Pageable page);

    @Query(value = "select * from reactionEntry where content_id = :contentId and user_id = :userId")
    Mono<ReactionEntity> findByContentAndUserId(String contentId, String userId);

}
