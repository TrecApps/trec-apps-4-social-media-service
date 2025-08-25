package com.trecapps.sm.profile.controllers;

import com.trecapps.sm.profile.models.Constants;
import com.trecapps.sm.profile.models.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/Profile")
public class ProfileController {




//    @GetMapping("/pronouns")
//    Mono<ResponseEntity<List<String>>> getPronounList()
//    {
//        return Mono.just(new ResponseEntity<>(Constants.VALID_PRONOUNS, HttpStatus.OK));
//    }
//
//    @GetMapping("/by/{id}")
//    ResponseEntity<Profile> getProfileByUser(@PathVariable("id")String id) {
//
//    }
//
//    @GetMapping("/search")
//    ResponseEntity<List<ProfileSelections>> searchProfiles(
//            @RequestParam("query")String query,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "20") int size
//    )
//    {
//
//    }
}
