package com.trecapps.sm.content.services;

import com.trecapps.auth.common.models.TcBrands;
import com.trecapps.auth.common.models.TcUser;
import com.trecapps.sm.common.functionality.ObjectResponseException;
import com.trecapps.sm.common.functionality.ProfileFunctionality;
import com.trecapps.sm.common.models.ReactionStats;
import com.trecapps.sm.common.models.ResponseObj;
import com.trecapps.sm.content.dto.ContentReactionEntry;
import com.trecapps.sm.content.dto.ProfileReactionEntry;
import com.trecapps.sm.content.dto.ReactionPosting;
import com.trecapps.sm.content.models.*;
import com.trecapps.sm.content.repos.ReactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
public class ReactionService {

    @Autowired
    ReactionRepo reactionRepo;

    @Autowired
    ContentService contentService;

    @Value("#{'${trecapps.sm.reaction-types}'.split(',')}")
    List<String> reactionTypes;

    public Mono<ResponseObj> postReaction(
            TcUser user,
            TcBrands brands,
            String contentId,
            ReactionPosting reactionPosting

    ) {
        if(!reactionTypes.contains(reactionPosting.getReactType()))
            return Mono.just(ResponseObj.getInstance(HttpStatus.BAD_REQUEST, "Type is not supported on this platform!"));

        return contentService.getPosting(user, brands, contentId)
                .map((ResponseObj responseObj) -> {
                    if(responseObj.getStatus() != 200)
                    {
                        // if status is not 200, then we don't have a posting and we need to error
                        throw new ObjectResponseException(HttpStatus.NOT_FOUND, "Content Not Found!");
                    }
                    return (Posting) responseObj.getData();
                })
                .flatMap((Posting posting) -> {

                    return reactionRepo.findByContentAndUserId(contentId, user.getId())
                            .doOnNext((ReactionEntity entity) -> {
                                reactionRepo.delete(entity);
                            }).thenReturn(new ReactionEntity())
                            .doOnNext((ReactionEntity entity) -> {
                                entity.setVersion(posting.getContents().last().getVersion());
                                entity.setStale(false);
                            });


                })
                .flatMap((ReactionEntity entity) -> {
                    ReactionId reactionId = new ReactionId();
                    reactionId.setContentId(contentId);
                    reactionId.setUserId(user.getId());
                    reactionId.setType(reactionPosting.getReactType());

                    entity.setReactionId(reactionId);
                    entity.setMade(OffsetDateTime.now());
                    entity.setPrivate(reactionPosting.isMakePrivate());
                    entity.setBrandId(brands == null ? null : brands.getId());

                    return reactionRepo.save(entity);
                })
                .flatMap((ReactionEntity entity) -> {
                    return getReactionCount(user, contentId);
                });
    }


    public Mono<ResponseObj> getReactionCount(TcUser user, String contentId) {
        return reactionRepo.findCountByContentId(contentId)
                .collectList()
                .map((List<ReactionTypeCount> reactionList) -> {
                    // Use a List type that handles deletions easily
                    reactionList = new LinkedList<>(reactionList);
                    // Prepare for the data to return
                    ReactionStats stats = new ReactionStats();
                    HashMap<String, Long> map = new HashMap<>();
                    while(!reactionList.isEmpty()){
                        ReactionTypeCount curCount = reactionList.getFirst();
                        reactionList.removeFirst();
                        map.put(curCount.getType(), curCount.getCount());
                    }
                    stats.setReactions(map);
                    return stats;
                })
                .flatMap((ReactionStats stats) -> {
                    return reactionRepo.findByContentAndUserId(contentId, user.getId())
                            .map((ReactionEntity entity) -> {
                               stats.setYourReaction(entity.getReactionId().getType());
                               return stats;
                            });
                })
                .map((ReactionStats stats)-> {
                    ResponseObj ret = ResponseObj.getInstanceOK("Success");
                    ret.setReactStats(stats);
                    return ret;
                })
                .defaultIfEmpty(ResponseObj.getInstanceNOTFOUND("Content Not Found!"));
    }

    public Mono<ResponseObj> removeReaction(TcUser user, String contentId) {
        return reactionRepo.findByContentAndUserId(contentId, user.getId())
                .doOnNext((ReactionEntity entity) -> {
                    reactionRepo.delete(entity);
                })
                .defaultIfEmpty(new ReactionEntity())
                .flatMap((ReactionEntity entity) -> {
                    return this.getReactionCount(user, contentId)
                            .doOnNext((ResponseObj obj) -> {
                                if(entity.getReactionId() == null) {
                                    obj.setStatus(HttpStatus.NOT_MODIFIED.value());
                                    obj.setMessage("You Have no reaction to this content!");
                                }
                            });
                });
    }

    public Mono<List<ContentReactionEntry>> getContentReactionListByContentId(
            TcUser user,
            TcBrands brands,
            String contentId,
            String type,
            int page,
            int size
    ) {
        return contentService.getPosting(user, brands, contentId)
                .flatMap((ResponseObj obj) -> {
                    if(obj.getStatus() != 200)
                    {
                        // if status is not 200, then we don't have a posting and we need to error
                        throw new ObjectResponseException(HttpStatus.NOT_FOUND, "Content Not Found!");
                    }
                    Pageable pageable = PageRequest.of(page, size);

                    Flux<ReactionEntity> reactions = type == null ?
                            reactionRepo.findByContentId(contentId, pageable) :
                            reactionRepo.findByContentIdAndType(contentId, type, pageable);
                    return reactions.collectList();
                })
                .map((List<ReactionEntity> entities) -> {
                    return entities.stream()
                            .map((ReactionEntity entity) -> {
                                ContentReactionEntry reactionEntry = new ContentReactionEntry();
                                reactionEntry.setMade(entity.getMade());
                                reactionEntry.setType(entity.getReactionId().getType());


                                reactionEntry.setProfileId
                                        (ProfileFunctionality.getProfileId(
                                                entity.getReactionId().getUserId(),
                                                entity.getBrandId()));
                                return reactionEntry;

                            }).toList();
                });

    }

    public Mono<List<ProfileReactionEntry>> getSelfReactions(TcUser user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reactionRepo.findByUserId(user.getId(), pageable)
                .map((ReactionEntity entity) -> {

                    ProfileReactionEntry ret = new ProfileReactionEntry();

                    ret.setType(entity.getReactionId().getType());
                    ret.setPrivate(entity.isPrivate());
                    ret.setContentId(entity.getReactionId().getContentId());
                    ret.setBrandId(entity.getBrandId());
                    ret.setMade(entity.getMade());
                    ret.setStale(entity.isStale());
                    ret.setVersion(entity.getVersion());
                    return ret;

                })
                .collectList();

    }


}
