package com.portfolio.common.business;

import com.portfolio.common.business.config.BusinessCommonConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
// 2. @ComponentScan을 사용하여 스캔할 패키지와 제외할 대상을 명시적으로 지정합니다.
@ComponentScan(
        // (포함 조건) HolidayApiClient가 있는 패키지만 스캔하도록 범위를 좁힙니다.
        basePackages = "com.portfolio.common.business.api",
        // (제외 조건) @Configuration 어노테이션이 붙은 클래스는 스캔에서 제외합니다.
        // 이렇게 하면 BusinessCommonConfig가 스캔되지 않습니다.
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = org.springframework.context.annotation.Configuration.class)
)
public class TestApplication {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
