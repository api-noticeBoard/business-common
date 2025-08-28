package com.portfolio.common.business.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 애플리케이션의 어느 곳에서든 현재 인증된 사용자 정보에 '안전하고 편리하게' 접근할 수 있도록 도와주는 유틸리티 클래스입니다.
 * 모든 메서드가 static으로 선언되어 있어, 클래스 인스턴스를 생성할 필요 없이 `UserInfoHolder.getUserId()`와 같이 바로 사용할 수 있습니다.
 *
 * 이 클래스의 핵심 목적은 Spring Security의 복잡한 API 호출을 캡슐화하고,
 * 비즈니스 로직 코드의 가독성과 테스트 용이성을 높이는 것입니다.
 */
public class UserInfoHolder {

    /**
     * 시스템 자체에서 수행되는 작업(예: 스케줄링, 배치)이나 비로그인 사용자를 나타내기 위한 가상 사용자 ID입니다.
     * JPA Auditing에서 @CreatedBy, @LastModifiedBy 필드를 채울 때,
     * 로그인한 사용자가 없는 경우 이 ID가 대신 사용됩니다.
     */
    private static final Long SYSTEM_USER_ID = 0L;

    /**
     * private 생성자.
     * 이 클래스는 static 메서드로만 구성된 유틸리티 클래스이므로,
     * 외부에서 `new UserInfoHolder()`를 통해 객체를 생성하는 것을 원천적으로 차단합니다.
     */
    private UserInfoHolder() {}

    /**
     * 현재 인증된 사용자의 고유 ID(Long 타입)를 반환합니다.
     * @return 현재 로그인한 사용자의 ID.
     * @throws IllegalStateException 사용자가 인증되지 않은 상태(로그인하지 않은 상태)에서 호출될 경우 예외를 발생시킵니다.
     */
    public static Long getUserId() {
        return getAuthUser().userId();
    }

    /**
     * 현재 사용자의 이름(username)을 반환합니다.
     * @return 사용자 이름 (String 타입)
     * @throws IllegalStateException 사용자가 인증되지 않은 경우
     */
    public static String getUsername() {
        return getAuthUser().username(); // AuthUser record의 username 필드를 반환
    }

    /**
     * 현재 사용자의 이름(username)을 반환하되, 인증 정보가 없으면 기본 이름을 반환합니다.
     * @param defaultName 인증 정보가 없을 때 반환할 기본 이름 (예: "System")
     * @return 사용자 이름 또는 기본 이름
     */
    public static String getUsernameOrDefault(String defaultName) {
        try {
            return getUsername();
        } catch (IllegalStateException e) {
            return defaultName;
        }
    }

    /**
     * 현재 인증된 사용자의 ID를 반환하되, 인증 정보가 없는 경우에는 시스템 ID(0L)를 안전하게 반환합니다.
     * 주로 JPA Auditing의 AuditorAwareImpl에서 사용되어, 로그인 사용자가 없는 시스템 작업에서도
     * createdBy, modifiedBy 필드가 null이 되지 않도록 보장합니다.
     * @return 현재 사용자 ID 또는 시스템 ID(0L).
     */
    public static Long getUserIdOrSystem() {
        try {
            return getAuthUser().userId();
        } catch (IllegalStateException e) {
            return SYSTEM_USER_ID;
        }
    }

    /**
     * 이 클래스의 핵심 메서드로, Spring Security 컨텍스트에서 완전한 사용자 정보 객체(AuthUser)를 가져옵니다.
     * @return 현재 인증된 사용자의 정보를 담고 있는 AuthUser record 객체.
     * @throws IllegalStateException 사용자가 인증되지 않았거나, 저장된 Principal 객체가 예상한 AuthUser 타입이 아닐 경우.
     */
    public static AuthUser getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new IllegalStateException("인증된 사용자 정보를 찾을 수 없습니다.");
        }
        // Java 21의 패턴 매칭 사용
        if (authentication.getPrincipal() instanceof AuthUser authUser) {
            return authUser;
        }
        throw new IllegalStateException("Principal 객체가 예상한 AuthUser 타입이 아닙니다.");
    }
}
