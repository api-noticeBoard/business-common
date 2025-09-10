package com.portfolio.common.business.audit;

import com.portfolio.common.business.user.UserInfoHolder;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Spring Data JPA의 Auditing 기능을 사용하여 엔티티의 생성자나 수정자를 자동으로 기록하는 클래스입니다.
 * 이 클래스는 현재 애플리케이션의 '감사자(auditor)', 즉 변경을 수행한 사용자의 ID를 제공하는 역할을 합니다.
 */

@Component
public class AuditorAwareImpl implements AuditorAware<Long> { // AuditorAware<T> 인터페이스를 구현합니다.
    // <Long>은 감사자의 ID가 Long 타입임을 의미합니다.

    /**
     * 현재 요청을 수행한 사용자의 ID를 반환합니다.
     * Spring Data JPA는 엔티티 저장/수정 시 이 메서드를 호출하여
     * @CreatedBy 또는 @LastModifiedBy 필드에 값을 자동 주입합니다.
     *
     * @return 현재 감사자의 ID를 담고 있는 Optional 객체
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        // 1. 현재 사용자의 ID를 가져오는 커스텀 유틸리티 메서드 호출
        // UserInfoHolder는 일반적으로 ThreadLocal 등을 이용해 현재 요청의 컨텍스트(사용자 정보)를 저장하는 클래스입니다.
        // 이 메서드는 로그인된 사용자가 있으면 해당 사용자의 ID를, 없으면 시스템 ID(예: 0L)를 반환합니다.
        Long userId = UserInfoHolder.getUserIdOrSystem();

        // 2. userId를 Optional로 감싸서 반환
        // Optional.of()는 인자가 null이 아님을 확신할 때 사용합니다.
        // UserInfoHolder.getUserIdOrSystem() 메서드가 null을 반환하지 않기 때문에 안전합니다.
        // Spring Data Auditing은 Optional.empty()가 반환되면 해당 필드를 null로 설정합니다.
        return Optional.of(userId);
    }
}
