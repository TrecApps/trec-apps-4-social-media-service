package com.trecapps.sm.common;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

//@ReadingConverter
//@Component
public class InstantToOffsetDateTimeConverter implements Converter<Instant, OffsetDateTime> {

    ZoneOffset offset = OffsetDateTime.now().getOffset();

    @Override
    public OffsetDateTime convert(Instant source) {
        return source.atOffset(offset);
    }

}
