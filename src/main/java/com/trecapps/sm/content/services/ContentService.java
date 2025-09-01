package com.trecapps.sm.content.services;

import com.trecapps.auth.common.models.TcBrands;
import com.trecapps.auth.common.models.TcUser;
import com.trecapps.sm.common.functionality.ObjectResponseException;
import com.trecapps.sm.common.functionality.ProfileFunctionality;
import com.trecapps.sm.common.models.ResponseObj;
import com.trecapps.sm.content.dto.ContentPost;
import com.trecapps.sm.content.dto.ContentPut;
import com.trecapps.sm.content.models.Posting;
import com.trecapps.sm.content.repos.ContentRepo;
import com.trecapps.sm.content.repos.ProfileRepoMongo;
import com.trecapps.sm.profile.models.Profile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@Slf4j
public class ContentService {

    @Autowired
    ContentRepo contentRepo;

    @Autowired
    ProfileRepoMongo profileRepo;

    @Value("${trecapps.sm.enable-cross-profile-posting:true}")
    boolean allowCrossProfilePosting;

    public Mono<ResponseObj> postContent(TcUser user, TcBrands brand, ContentPost post){

        return Mono.just(ProfileFunctionality.getProfileId(user, brand))
                .doOnNext((String profileId) -> {
                    // ToDo - check block table and make sure post isn't crossing any lines
                })
                .doOnNext((String profileId) -> {
                    // ToDo - handle modules (does module exist, is user allowed to post there
                })
                .flatMap((String profileId) -> {
                    String parent = post.getParentId();
                    Posting newPost = new Posting();
                    OffsetDateTime now = OffsetDateTime.now();

                    newPost.setId(UUID.randomUUID().toString());
                    newPost.setMade(now);
                    newPost.setUserId(user.getId());
                    newPost.setProfilePoster(profileId);

                    newPost.appendContent(post.getContent());

                    if(parent == null){
                        return Mono.just(newPost);
                    }
                    return contentRepo.findById(parent)
                            .defaultIfEmpty(new Posting())
                            .map((Posting parentPosting) -> {
                                if(parentPosting.getId() == null)
                                    throw new ObjectResponseException(HttpStatus.NOT_FOUND, "Parent content not found");
                                newPost.setParents(parentPosting);
                                return newPost;
                            });
                })
                .flatMap((Posting newPost) -> {
                    if(post.getProfileId() == null){
                        newPost.setProfileOwner(newPost.getProfilePoster());
                        return Mono.just(newPost);
                    }
                    return profileRepo.findById(post.getProfileId())
                            .defaultIfEmpty(new Profile())
                            .map((Profile profile) -> {
                                if(profile.getId() == null)
                                    throw new ObjectResponseException(HttpStatus.NOT_FOUND, "Parent content not found");
                                newPost.setProfileOwner(profile.getId());
                                return newPost;
                            });
                })
                .flatMap(contentRepo::save)
                .doOnNext((Posting newPost) -> {
                    // ToDo - add Broadcast mechanism
                })
                .map((Posting newPost) -> ResponseObj.getInstanceOK("Posted!", newPost.getId()))
                .onErrorResume(ObjectResponseException.class, (ObjectResponseException e) -> Mono.just(e.toResponseObj()))
                .onErrorResume((Throwable thrown)-> {
                    log.error("Error processing Content Posting", thrown);
                    return Mono.just(ResponseObj.getInstance(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error!"));
                });
    }

    public Mono<ResponseObj> editContent(TcUser user, TcBrands brand, ContentPut put){
        return Mono.just(ProfileFunctionality.getProfileId(user, brand))
                .flatMap((String profileId) -> {
                    return contentRepo.findById(put.getContentId())
                            .defaultIfEmpty(new Posting())
                            .doOnNext((Posting post) -> {
                                if(post.getId() == null)
                                    throw new ObjectResponseException(HttpStatus.NOT_FOUND, "Content not found");
                                if(!post.getProfilePoster().equals(profileId) || !user.getId().equals(post.getUserId()))
                                    throw new ObjectResponseException(HttpStatus.FORBIDDEN, "This is not your Content!");
                            });
                })
                .flatMap((Posting post) -> {
                    post.appendContent(put.getContent());
                    return contentRepo.save(post);
                })
                .doOnNext((Posting post) -> {
                    // ToDo - render stale every reaction that reacted to the previous version of this post
                })
                .thenReturn(ResponseObj.getInstanceOK("Success"))
                .onErrorResume(ObjectResponseException.class, (ObjectResponseException e) -> Mono.just(e.toResponseObj()))
                .onErrorResume((Throwable thrown)-> {
                    log.error("Error processing Content Posting", thrown);
                    return Mono.just(ResponseObj.getInstance(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error!"));
                });
    }


}
