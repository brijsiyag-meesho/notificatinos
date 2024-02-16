package com.meesho.notification.convertors.elasticsearch;

import lombok.NonNull;
import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeConvertor implements PropertyValueConverter {

    @Override
    @NonNull
    public Object write(@NonNull Object value) {
        if(value instanceof LocalDateTime localDateTime){
            return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return value;
    }

    @Override
    @NonNull
    public Object read(@NonNull Object value) {
        if(value instanceof Long epochTime){
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochTime), ZoneId.systemDefault());
        }
        return value;
    }
}
