package com.anarchyadventure.music_dabang_api.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserAge {
    AGE30("30대 이하"), AGE40("40대"), AGE50("50대"),
    AGE60("60대"), AGE70("70대"), AGE80("80대 이상");

    private final String description;

    @JsonCreator
    public static UserAge from(String value) {
        for (UserAge userAge : UserAge.values()) {
            if (userAge.name().equalsIgnoreCase(value)) {
                return userAge;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }
}
