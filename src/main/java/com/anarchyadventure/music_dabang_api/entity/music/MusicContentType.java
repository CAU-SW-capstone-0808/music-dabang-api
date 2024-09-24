package com.anarchyadventure.music_dabang_api.entity.music;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MusicContentType {
    MUSIC, LIVE;

    @JsonCreator
    public static MusicContentType from(String value) {
        for (MusicContentType type : MusicContentType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }
}
