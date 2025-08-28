package com.portolio.common.business.masking;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Jackson JSON 직렬화(Serialization) 시 필드 값을 마스킹하기 위해 만든 커스텀 어노테이션입니다.
 * DTO의 특정 필드에 이 어노테이션을 붙이면, API 응답 JSON에서 해당 필드의 값이 자동으로 마스킹 처리됩니다.
 */
@Target(ElementType.FIELD)                          // 어노테이션의 사용 위치를 제한
@Retention(RetentionPolicy.RUNTIME)                 // 어노테이션 정보의 생존 기간을 설정
@JacksonAnnotationsInside                           // 메타 어노테이션(Meta-annotation)으로, 여러 개의 Jackson 어노테이션을 하나의 대표 어노테이션으로 묶어주는 역할
@JsonSerialize(using = MaskingSerializer.class)     // JSON으로 직렬화(변환)할 때는, 기본 변환 방식을 사용 말고 askingSerializer.class를 사용
public @interface Masking {
    MaskingType value();
}
