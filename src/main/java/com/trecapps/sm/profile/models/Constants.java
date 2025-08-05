package com.trecapps.sm.profile.models;

import java.util.List;

public class Constants {

    public static boolean stringNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static final List<String> VALID_PRONOUNS = List.of(
            "he/him",
            "she/her",
            "they/them"
    );
}
