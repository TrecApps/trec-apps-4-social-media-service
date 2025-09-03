package com.trecapps.sm.profile.service;

import com.trecapps.auth.common.models.TcBrands;
import com.trecapps.auth.common.models.TcUser;
import com.trecapps.sm.common.functionality.ObjectResponseException;
import com.trecapps.sm.common.functionality.ProfileFunctionality;
import com.trecapps.sm.common.models.ResponseObj;
import com.trecapps.sm.profile.models.ConnectionEntry;
import com.trecapps.sm.profile.models.ConnectionLink;
import com.trecapps.sm.profile.models.Profile;
import com.trecapps.sm.profile.repos.ConnectionRepo;
import com.trecapps.sm.profile.repos.ProfileRepoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionsService {

    @Autowired
    ConnectionRepo connectionRepo;

    @Autowired
    ProfileRepoMongo profileRepo;

    ConnectionLink getLink(String follower, String followee){
        ConnectionLink link = new ConnectionLink();
        link.setFollower(follower);
        link.setFollowee(followee);
        return link;
    }

    Mono<Optional<ConnectionEntry>> getTwoWayConnection(String profile1, String profile2) {
        ConnectionLink link1 = getLink(profile1, profile2);
        ConnectionLink link2 = getLink(profile2, profile1);

        Mono<Optional<ConnectionEntry>> mono1 = connectionRepo.findById(link1)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty());
        Mono<Optional<ConnectionEntry>> mono2 = connectionRepo.findById(link2)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty());

        return mono1.zipWith(mono2)
                .map((Tuple2<Optional<ConnectionEntry>, Optional<ConnectionEntry>> tup) -> {
                    Optional<ConnectionEntry> ent1 = tup.getT1();
                    Optional<ConnectionEntry> ent2 = tup.getT1();

                    return ent1.isPresent() ? ent1 : ent2;
                } );
    }



    public Mono<ResponseObj> attemptFollow(
            TcUser user,
            TcBrands brand,
            String profile
    ) {
        return profileRepo.findById(profile)
                .defaultIfEmpty(new Profile())
                .doOnNext((Profile profile1) -> {
                    if(profile1.getId() == null)
                        throw new ObjectResponseException(HttpStatus.NOT_FOUND, "Profile not found");
                })
                .thenReturn(ProfileFunctionality.getProfileId(user, brand))

                .flatMap((String profileId) -> {

                    Mono<Optional<ConnectionEntry>> existingEntry = null;

                    if(profile.startsWith("User-")){
                        // ToDo - check Block

                        if(profileId.startsWith("User-")){
                            existingEntry = this.getTwoWayConnection(profileId, profile)  ;
                        }
                    }
                    if(existingEntry == null){
                        // We are dealing with a one way connection
                        existingEntry = connectionRepo.findById(getLink(profileId, profile))
                                .map(Optional::of)
                                .defaultIfEmpty(Optional.empty());
                    }
                    return existingEntry;
                })
                .flatMap((Optional<ConnectionEntry> oEntry) -> {
                    if(oEntry.isPresent())
                        throw new ObjectResponseException(HttpStatus.ALREADY_REPORTED, "Connection Already Exists!");
                    ConnectionEntry entry = new ConnectionEntry();
                    entry.setMade(OffsetDateTime.now());
                    entry.setOneWay(brand != null || profile.startsWith("Brand-"));
                    entry.setId(getLink(
                            ProfileFunctionality.getProfileId(user, brand), profile
                    ));

                    return connectionRepo.save(entry)
                            .doOnNext((ConnectionEntry e) -> {
                                // ToDo - notification support
                            })
                            .thenReturn(ResponseObj.getInstanceOK("Made"));
                })
                .onErrorResume(ObjectResponseException.class, (ObjectResponseException e) -> Mono.just(e.toResponseObj()));
    }


    public Mono<ResponseObj> approveRequest(TcUser user, String profile) {
        if(profile.startsWith("Brand-"))
            return Mono.just(ResponseObj.getInstance(
                    HttpStatus.BAD_REQUEST, "This endpoint only applies to User Profiles"));

        return connectionRepo.findById(getLink(profile, String.format("User-%s", user.getId())))
                .defaultIfEmpty(new ConnectionEntry())
                .flatMap((ConnectionEntry entry) -> {
                    if(entry.getId() == null)
                        throw new ObjectResponseException(HttpStatus.NOT_FOUND, "Connection Entry not found!");

                    if(entry.getAccepted() != null)
                        throw new ObjectResponseException(HttpStatus.ALREADY_REPORTED, "Connection already Accepted!");

                    entry.setAccepted(OffsetDateTime.now());
                    return connectionRepo.save(entry)
                            .doOnNext((ConnectionEntry e) -> {
                                // ToDo - notify requester of request
                            })
                            .thenReturn(ResponseObj.getInstanceOK("Made"));
                })
                .onErrorResume(ObjectResponseException.class, (ObjectResponseException e) -> Mono.just(e.toResponseObj()));
    }

    public Mono<ResponseObj> unfollow(TcUser user, TcBrands brands, String profile){
        return this.getTwoWayConnection(
                ProfileFunctionality.getProfileId(user,brands),
                profile
        ).flatMap((Optional<ConnectionEntry> oEntry) -> {
            if(oEntry.isEmpty())
                throw new ObjectResponseException(HttpStatus.NOT_FOUND, "Connection not found!");

            ConnectionEntry entry = oEntry.get();

            if(entry.isOneWay() && entry.getId().getFollower().equals(profile)){
                throw new ObjectResponseException(HttpStatus.CONFLICT, "This endpoint does not stop others from following you!");
            }

            return connectionRepo.delete(entry)
                    .thenReturn(ResponseObj.getInstanceOK("Removed"));
        })
        .onErrorResume(ObjectResponseException.class, (ObjectResponseException e) -> Mono.just(e.toResponseObj()));
    }

    public Mono<List<ConnectionEntry>> findMyConnections(TcUser user, TcBrands brands, int page, int size, boolean getFollowers) {

        return Mono.just(ProfileFunctionality.getProfileId(user, brands))
                .flatMap((String profileId) -> {
                    Pageable pageable = PageRequest.of(page,size);
                    Flux<ConnectionEntry> entries = getFollowers ?
                            connectionRepo.findByFollowee(profileId, pageable) :
                            connectionRepo.findByFollower(profileId, pageable);
                    return entries.collectList();
                });



    }


}
