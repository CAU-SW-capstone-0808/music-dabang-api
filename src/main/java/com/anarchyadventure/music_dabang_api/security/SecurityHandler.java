package com.anarchyadventure.music_dabang_api.security;

import com.anarchyadventure.music_dabang_api.entity.user.User;
import com.anarchyadventure.music_dabang_api.entity.user.UserRole;
import com.anarchyadventure.music_dabang_api.exceptions.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityHandler {
    /**
     * SecurityContext에 존재하는 Authentication 객체를 통해 유저 객체를 반환
     * 반환된 유저 객체는 영속성 컨텍스트를 벗어난 상태이므로, 이에 주의
     */
    public static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal == null) {
            return null;
        }

        if (principal instanceof PrincipalDetails pd) {
            return pd.getUser();
        }

        return null;
    }

    public static User getUserAuth(UserRole... userRoles) {
        User user = getUser();
        if (user == null) throw new UnauthorizedException();
        if (userRoles.length == 0) {
            return user;
        }
        for (UserRole role: userRoles) {
            if (user.getRole().hasAuth(role)) {
                return user;
            }
        }
        throw new UnauthorizedException();
    }
}
