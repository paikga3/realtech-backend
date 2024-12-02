package realtech.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import realtech.security.AuthenticatedUser;

public class SecurityUtil {
    // 현재 인증 객체 가져오기
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // 현재 사용자 가져오기
    public static AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser userDetails = (AuthenticatedUser) authentication.getPrincipal();
            return userDetails;
        }
        return null;
    }

    
    // 현재 사용자가 특정 권한을 가지고 있는지 확인
    public static boolean hasRole(String role) {
        Authentication authentication = getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }

    // 현재 사용자가 어드민인지 확인
    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }
}
