package com.portolio.common.business.config;

import com.portolio.common.business.audit.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * business-common 라이브러리의 자동 설정을 담당하는 클래스.
 * 이 라이브러리를 의존성에 추가하는 것만으로도 관련 빈들이 등록되고 기능이 활성화됩니다.
 *
 * @Configuration: 이 클래스가 Spring의 설정 파일임을 나타냅니다.
 * @EnableJpaAuditing: JPA Auditing 기능을 활성화합니다. auditorAwareRef 속성을 통해
 *                   어떤 AuditorAware 구현체를 사용할지 명시적으로 지정합니다.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class BusinessCommonConfig {

    /**
     * AuditorAware<Long> 타입의 빈을 Spring 컨테이너에 등록합니다.
     * @return AuditorAwareImpl 인스턴스
     */
    @Bean
    public AuditorAware<Long> auditorAware() {
        return new AuditorAwareImpl();
    }
}
