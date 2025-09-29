package com.trecapps.sm.common.functionality;

import com.trecapps.auth.common.models.TcBrands;
import com.trecapps.auth.common.models.TcUser;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProfileFunctionality {

    private static final String USER_PREFIX = "User-";
    private static final String BRAND_PREFIX = "Brand-";

    public static String getProfileId(String userId, String brandId){
        String id = brandId == null ? userId : brandId;
        String prefix = brandId == null ? USER_PREFIX : BRAND_PREFIX;

        if(id.length() == 32){
            // Possibly a UUID without '-'
            id =
                    id.substring(0, 8) + "-" +
                            id.substring(8, 12) + "-" +
                            id.substring(12, 16) + "-" +
                            id.substring(16, 20) + "-" +
                            id.substring(20);
        }

        try{
            return String.format("%s%s", prefix, UUID.fromString(id));
        } catch(IllegalArgumentException e) {
            throw new ObjectResponseException(HttpStatus.BAD_REQUEST, "Invalid User/Brand Id detected");
        }
    }

    public static String getProfileId(TcUser user, TcBrands brand){
        String id = brand == null ? user.getId() : brand.getId();
        String prefix = brand == null ? USER_PREFIX : BRAND_PREFIX;

        if(id.length() == 32){
            // Possibly a UUID without '-'
            id =
                    id.substring(0, 8) + "-" +
                    id.substring(8, 12) + "-" +
                    id.substring(12, 16) + "-" +
                    id.substring(16, 20) + "-" +
                    id.substring(20);
        }

        try{


            return String.format("%s%s", prefix, UUID.fromString(id));
        } catch(IllegalArgumentException e) {
            throw new ObjectResponseException(HttpStatus.BAD_REQUEST, "Invalid User/Brand Id detected");
        }
    }
}
