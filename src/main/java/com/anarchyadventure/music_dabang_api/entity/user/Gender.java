package com.anarchyadventure.music_dabang_api.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    M, F;

    @JsonCreator
    public static Gender parse(String value) {
        for (Gender g : values()) {
            if (g.name().equalsIgnoreCase(value)) {
                return g;
            }
        }
        return null;
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
