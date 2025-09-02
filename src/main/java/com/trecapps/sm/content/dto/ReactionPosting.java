package com.trecapps.sm.content.dto;

import lombok.Data;

@Data
public class ReactionPosting {

    String reactType;
    boolean makePrivate = false;
    String brandId;

}
