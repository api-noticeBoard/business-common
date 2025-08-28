package com.portfolio.common.business.user;

import java.io.Serializable;
import java.util.Set;

/**
 * Java 21의 'record'를 사용하여 현재 인증된 사용자의 핵심 정보를 담는 불변(Immutable) 데이터 객체를 정의합니다.
 * 이 객체는 Spring Security의 SecurityContext에 저장되어 애플리케이션 전반에서 사용자의 신원을 나타냅니다.
 *
 * 'record'를 사용하면, 컴파일러가 자동으로 다음 요소들을 생성해 줍니다:
 * - 모든 필드를 위한 private final 필드
 * - 모든 필드를 인자로 받는 정규 생성자(Canonical Constructor)
 * - 각 필드에 대한 접근자(Accessor) 메서드 (e.g., userId(), username(), roles())
 * - equals(), hashCode(), toString() 메서드의 자동 구현
 *
 * @param userId    사용자의 고유 식별자(ID). 데이터베이스의 Primary Key에 해당합니다.
 * @param username  사용자의 이름 또는 로그인 ID.
 * @param roles     사용자가 가진 권한 목록 (e.g., "ROLE_USER", "ROLE_ADMIN"). Set을 사용하여 중복 없는 권한을 보장합니다.
 */
public record AuthUser(Long userId,
                       String username,
                       Set<String> roles) implements Serializable {

    /**
     * 직렬화(Serialization) 버전 UID(Unique ID).
     *
     * 이 객체가 파일에 저장되거나 네트워크를 통해 전송되는 '직렬화' 과정을 거칠 때 사용됩니다.
     * 나중에 직렬화된 데이터를 다시 객체로 '역직렬화'할 때, JVM은 저장된 데이터의 serialVersionUID와
     * 현재 클래스의 serialVersionUID를 비교합니다.
     * 두 ID가 일치해야만 안전하게 역직렬화할 수 있으며, 이는 클래스 버전 간의 호환성 문제를 방지해 줍니다.
     * '1L'과 같이 상수로 명시하는 것이 표준적인 관례입니다.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 'record'의 정규 생성자(Canonical Constructor)를 명시적으로 구현한 부분입니다.
     * record는 모든 필드를 인자로 받는 생성자를 자동으로 만들어주지만,
     * 필드 값에 대한 유효성 검사나 '방어적 복사'와 같은 추가 로직이 필요할 때 이렇게 직접 구현할 수 있습니다.
     *
     * 생성자 이름이 없고, 파라미터 목록도 없는 간결한 형태의 생성자(Compact Constructor)입니다.
     */
    public AuthUser {
        // 이 생성자의 핵심 목적은 '깊은 불변성(Deep Immutability)'을 보장하는 것입니다.
        // record 자체는 필드 참조를 바꿀 수 없게 하지만(얕은 불변성),
        // 참조하는 객체(여기서는 Set) 내부의 내용이 변경되는 것은 막지 못합니다.

        // '방어적 복사(Defensive Copy)' 로직:
        // 외부에서 전달받은 roles Set(수정 가능할 수 있는)의 복사본을 만듭니다.
        // Set.copyOf()는 수정이 불가능한(Unmodifiable) Set을 반환합니다.
        // 이 수정 불가능한 Set을 최종적으로 AuthUser의 roles 필드에 할당합니다.
        //
        // 이로써, AuthUser 객체가 생성된 이후에는 외부에서 원본 Set을 변경하더라도
        // AuthUser 내부의 roles 상태는 절대로 변하지 않음을 보장할 수 있습니다.
        // 이는 특히 '권한'과 같은 민감한 데이터를 다룰 때 매우 중요한 보안 조치입니다.
        roles = Set.copyOf(roles);
    }
}
