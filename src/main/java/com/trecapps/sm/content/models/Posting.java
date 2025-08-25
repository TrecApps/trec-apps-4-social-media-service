package com.trecapps.sm.content.models;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document("posting")
@Slf4j
public class Posting {

    @MongoId
    String id;          // ID of the posting

    List<String> parents = new ArrayList<>(); // Parents (Posting is a comment if non-empty)

    public boolean isPost() {
        return parents.isEmpty();
    }

    public void setParents(Posting parentPosting){
        if(new HashSet<>(parentPosting.getParents()).size() != parentPosting.getParents().size()){
            log.error("Circular Parent Detection on Posting {} detected!", parentPosting.getId());
            throw new IllegalStateException(String.format("Posting with Circular parenting detected. Posting id %s", parentPosting.getId()));
        }

        parents.clear();
        parents.addAll(parentPosting.getParents());
        parents.add(parentPosting.getId());
    }

    String userId;          // User Id of who posted it
    String profilePoster;   // Profile ID of who posted it (identical to userId if no brand used)

    String profileOwner;    // Populated only if poster directs it to another profile

    String moduleId;        // If part of a group, which group this belongs to

    OffsetDateTime made;

    List<PostingContent> contents;

}
