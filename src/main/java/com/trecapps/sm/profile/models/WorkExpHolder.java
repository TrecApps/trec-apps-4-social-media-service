package com.trecapps.sm.profile.models;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Set;

@Data
public class WorkExpHolder {
    @Indexed
    Set<String> perspective;

    WorkExp workExperience;
}
