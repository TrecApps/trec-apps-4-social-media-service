package com.trecapps.sm.common.models;

import jdk.jshell.Snippet;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class ResponseObj {

    int status;

    String message;
    String id;

    ReactionStats reactStats;

    public ResponseEntity<ResponseObj> toEntity(){
        return new ResponseEntity<>(this, HttpStatus.valueOf(status));
    }

    public static ResponseObj getInstance(HttpStatus status, String message, String id) {
        ResponseObj ret = new ResponseObj();
        ret.id = id;
        ret.message = message;
        ret.status = status.value();
        return ret;
    }

    public static ResponseObj getInstance(HttpStatus status, String message) {
        return getInstance(status, message, null);
    }

    public static ResponseObj getInstanceOK(String message, String id) {
        return getInstance(HttpStatus.OK, message, id);
    }
    public static ResponseObj getInstanceOK(String message) {
        return getInstance(HttpStatus.OK, message);
    }

    public static ResponseObj getInstanceCREATED(String message, String id) {
        return getInstance(HttpStatus.CREATED, message, id);
    }
    public static ResponseObj getInstanceCREATED(String message) {
        return getInstance(HttpStatus.CREATED, message);
    }

    public static ResponseObj getInstanceNOTFOUND(String message) {
        return getInstance(HttpStatus.NOT_FOUND, message);
    }

    public static ResponseObj getInstanceBADREQUEST(String message) {
        return getInstance(HttpStatus.BAD_REQUEST, message);
    }
}
