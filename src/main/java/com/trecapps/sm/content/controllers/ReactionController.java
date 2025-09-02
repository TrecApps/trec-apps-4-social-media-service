package com.trecapps.sm.content.controllers;

import com.trecapps.auth.common.models.TrecAuthentication;
import com.trecapps.sm.common.models.ResponseObj;
import com.trecapps.sm.content.dto.ContentReactionEntry;
import com.trecapps.sm.content.dto.ProfileReactionEntry;
import com.trecapps.sm.content.dto.ReactionPosting;
import com.trecapps.sm.content.models.ReactionTypeCount;
import com.trecapps.sm.content.services.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/Reactions")
public class ReactionController {

    @Autowired
    ReactionService reactionService;


    @PostMapping("/{contentId}")
    Mono<ResponseEntity<ResponseObj>> postReaction(
            Authentication authentication,
            @PathVariable String contentId,
            @RequestBody ReactionPosting reactionPosting
            ) {
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return reactionService.postReaction(
                trecAuthentication.getUser(),
                trecAuthentication.getBrand(),
                contentId,
                reactionPosting
        ).map(ResponseObj::toEntity);
    }

    @GetMapping("/count/{contentId}")
    Mono<ResponseEntity<ResponseObj>> getCount(
            Authentication authentication,
            @PathVariable String contentId
    ){
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return reactionService.getReactionCount(trecAuthentication.getUser(), contentId).map(ResponseObj::toEntity);

    }

    @DeleteMapping("/{contentId}")
    Mono<ResponseEntity<ResponseObj>> deleteReaction(
            Authentication authentication,
            @PathVariable String contentId
    ){
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return reactionService.removeReaction(trecAuthentication.getUser(), contentId).map(ResponseObj::toEntity);
    }

    @GetMapping("/list/{contentId}")
    Mono<List<ContentReactionEntry>> listReactionsByContent(
            Authentication authentication,
            @PathVariable String contentId,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return reactionService.getContentReactionListByContentId(
                trecAuthentication.getUser(),
                trecAuthentication.getBrand(),
                contentId,
                type,
                page, size
        );
    }

    @GetMapping("/mine")
    Mono<List<ProfileReactionEntry>> listMyReactions(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return reactionService.getSelfReactions(trecAuthentication.getUser(), page, size);
    }

}
