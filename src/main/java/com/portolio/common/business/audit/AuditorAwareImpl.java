package com.portolio.common.business.audit;

import com.portolio.common.business.user.UserInfoHolder;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Long userId = UserInfoHolder.getUserIdOrSystem();
        return Optional.of(userId);
    }
}
