package com.portolio.common.business.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 엔티티의 공통 필드를 정의하는 추상 클래스입니다.
 * @MappedSuperclass: 이 클래스를 상속하는 엔티티에 아래 필드들을 컬럼으로 추가해줍니다.
 * @EntityListeners(AuditingEntityListener.class): JPA Auditing 기능을 활성화하여,
 *     엔티티가 생성되거나 수정될 때 @CreatedDate, @CreatedBy 등을 자동으로 채워줍니다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt; // 생성 일시

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String createdBy; // 생성자 (사용자 ID)

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정 일시

    @LastModifiedBy
    @Column(nullable = false)
    private String modifiedBy; // 수정자 (사용자 ID)
}
