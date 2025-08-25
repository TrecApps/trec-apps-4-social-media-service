package com.trecapps.sm.common.functionality;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProfileFunctionality {

    private static final String USER_PREFIX = "User-";
    private static final String BRAND_PREFIX = "Brand-";

    public static String getProfileId(String userId, String brandId){
        String id = brandId == null ? userId : brandId;
        String prefix = brandId == null ? USER_PREFIX : BRAND_PREFIX;

        try{
            return String.format("%s%s", prefix, UUID.fromString(id));
        } catch(IllegalArgumentException e) {
            throw new ObjectResponseException(HttpStatus.BAD_REQUEST, "Invalid User/Brand Id detected");
        }
    }
}
