package com.trecapps.sm.content.controllers;

import com.trecapps.auth.common.models.TrecAuthentication;
import com.trecapps.sm.common.models.ResponseObj;
import com.trecapps.sm.content.dto.ContentPost;
import com.trecapps.sm.content.dto.ContentPut;
import com.trecapps.sm.content.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
}
