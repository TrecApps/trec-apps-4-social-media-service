package com.trecapps.sm.content.controllers;

import com.trecapps.auth.common.models.TrecAuthentication;
import com.trecapps.sm.common.models.ResponseObj;
import com.trecapps.sm.content.dto.ContentPost;
import com.trecapps.sm.content.dto.ContentPut;
import com.trecapps.sm.content.models.Posting;
import com.trecapps.sm.content.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/Content")
public class ContentController {

    @Autowired
    ContentService contentService;

    @PostMapping
    Mono<ResponseEntity<ResponseObj>> postContent(
            Authentication authentication,
            @RequestBody ContentPost post
            ){
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return contentService.postContent(trecAuthentication.getUser(), trecAuthentication.getBrand(), post)
                .map(ResponseObj::toEntity);
    }

    @PutMapping
    Mono<ResponseEntity<ResponseObj>> putContent(
            Authentication authentication,
            @RequestBody ContentPut put
    ){
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return contentService.editContent(trecAuthentication.getUser(), trecAuthentication.getBrand(), put)
                .map(ResponseObj::toEntity);
    }

    @DeleteMapping
    Mono<ResponseEntity<ResponseObj>> deleteContent(
            Authentication authentication,
            @RequestParam String contentId
    ){
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return contentService.deleteContent(trecAuthentication.getUser(), trecAuthentication.getBrand(), contentId)
                .map(ResponseObj::toEntity);
    }

    @GetMapping("/id/{id}")
    Mono<ResponseEntity<Posting>> getPosting(
            Authentication authentication,
            @PathVariable String id){
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return contentService.getPosting(trecAuthentication.getUser(), trecAuthentication.getBrand(), id)
                .map((ResponseObj obj) -> {
                    if(obj.getStatus() == 200)
                        return ResponseEntity.ok((Posting)obj.getData());
                    return new ResponseEntity<>(HttpStatus.valueOf(obj.getStatus()));
                });
    }

    @GetMapping("/byProfile/{profileId}")
    Mono<List<String>> getPostingsByProfile(
            Authentication authentication,
            @PathVariable String profileId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return contentService.getPostingList(
                trecAuthentication.getUser(),
                trecAuthentication.getBrand(),
                profileId,
                null,
                page,
                size );
    }

    @GetMapping("/byModule/{moduleId}")
    Mono<List<String>> getPostingsByModule(
            Authentication authentication,
            @PathVariable String moduleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return contentService.getPostingList(
                trecAuthentication.getUser(),
                trecAuthentication.getBrand(),
                null,
                moduleId,
                page,
                size );
    }

    @GetMapping("/byParent/{parentId}")
    Mono<List<Posting>> getPostingsByParent(
            Authentication authentication,
            @PathVariable String parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        TrecAuthentication trecAuthentication = (TrecAuthentication) authentication;
        return contentService.getReplyList(
                trecAuthentication.getUser(),
                trecAuthentication.getBrand(),
                parentId,
                page,
                size );
    }
}
