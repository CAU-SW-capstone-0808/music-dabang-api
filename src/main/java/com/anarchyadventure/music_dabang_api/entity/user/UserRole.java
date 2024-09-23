package com.anarchyadventure.music_dabang_api.entity.user;

public enum UserRole {
    USER, ADMIN;

    public boolean hasAuth(UserRole role) {
        if (role == ADMIN) {
            return this == ADMIN;
        }
        return true;
    }
}
