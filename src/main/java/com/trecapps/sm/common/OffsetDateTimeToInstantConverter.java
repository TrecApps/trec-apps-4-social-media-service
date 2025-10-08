package com.trecapps.sm.common;

import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetDateTimeToInstantConverter implements Converter<OffsetDateTime, Instant> {

    ZoneOffset offset = OffsetDateTime.now().getOffset();

    @Override
    public Instant convert(OffsetDateTime source) {
        return source.toInstant();
//        Instant use = Instant.
//        return source.atOffset(offset);
    }

}
