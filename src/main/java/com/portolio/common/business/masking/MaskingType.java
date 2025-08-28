package com.portolio.common.business.masking;

import java.util.function.Function;

/**
 * 다양한 종류의 데이터 마스킹 규칙을 정의하고 관리하는 Enum(열거형 타입) 클래스입니다.
 * 이 Enum을 사용하면 마스킹 로직을 중앙에서 관리할 수 있어 일관성이 있고,
 * 새로운 마스킹 규칙이 필요할 때 이 파일에 새로운 상수만 추가하면 되므로 확장성이 매우 좋습니다.
 */
public enum MaskingType {

    /**
     * 이름 마스킹 규칙.
     * 문자열의 첫 글자와 마지막 글자를 제외한 가운데 글자 하나를 '*'로 치환합니다.
     * 예: "홍길동" -> "홍*동", "선우" -> "선*", "제갈공명" -> "제*공명" (첫 번째 가운데 글자만)
     * 정규표현식 `(?<=.{1}).(?=.$)`:
     *   - `(?<=.{1})`: 앞에 어떤 문자든 한 글자가 있는지 확인 (전방 탐색).
     *   - `.`        : 어떤 문자든 한 글자를 선택.
     *   - `(?=.$)`   : 뒤에 어떤 문자 한 글자와 문자열의 끝이 있는지 확인 (후방 탐색).
     */
    NAME(s -> s.replaceAll("(?<=.{1}).(?=.$)", "*")),

    /**
     * 전화번호 마스킹 규칙.
     * 일반적인 휴대전화 번호 형식(010-1234-5678)에서 가운데 네 자리를 '****'로 치환합니다.
     * 예: "010-1234-5678" -> "010-****-5678"
     * 정규표현식 `(?<=\\d{3}-)\\d{4}(?=-\\d{4})`:
     *   - `(?<=\\d{3}-)`: 앞에 숫자 3개와 하이픈이 있는지 확인 (전방 탐색).
     *   - `\\d{4}`       : 숫자 4개를 선택.
     *   - `(?=-\\d{4})`  : 뒤에 하이픈과 숫자 4개가 있는지 확인 (후방 탐색).
     */
    PHONE(s -> s.replaceAll("(?<=\\d{3}-)\\d{4}(?=-\\d{4})", "****")),

    /**
     * 이메일 주소 마스킹 규칙.
     * 이메일의 로컬 파트(ID 부분) 앞에서 두 글자를 제외한 나머지를 '*'로 치환합니다.
     * 예: "testuser@email.com" -> "te******@email.com"
     *     "abc@email.com" -> "ab*@email.com"
     *     "a@email.com" -> "a@" (마스킹하지 않음)
     */
    EMAIL(s -> {
        int atIndex = s.indexOf('@');
        if (atIndex <= 1) return s;
        String localPart = s.substring(0, atIndex);
        String domainPart = s.substring(atIndex);
        if (localPart.length() <= 2) return localPart.charAt(0) + "*" + domainPart;
        String prefix = localPart.substring(0, 2);
        String mask = "*".repeat(localPart.length() - 2);
        return prefix + mask + domainPart;
    });

    /**
     * 각 Enum 상수가 자신의 마스킹 로직을 함수(Function) 형태로 저장하는 private final 필드입니다.
     * `Function<String, String>`은 'String 타입의 입력을 받아 String 타입의 결과를 반환하는 함수'를 의미합니다.
     */
    private final Function<String, String> maskingFunction;

    /**
     * Enum 상수가 생성될 때, 각자의 마스킹 로직(람다 표현식)을 이 생성자를 통해 `maskingFunction` 필드에 주입합니다.
     * 이 생성자는 private으로, 외부에서 호출할 수 없으며 Enum 상수가 정의될 때 내부적으로만 사용됩니다.
     * @param maskingFunction 각 Enum 상수에 정의된 마스킹 로직 람다 표현식.
     */
    MaskingType(Function<String, String> maskingFunction){
        this.maskingFunction = maskingFunction;
    }

    /**
     * 실제 마스킹 로직을 실행하는 public 메서드입니다.
     * 외부(예: MaskingSerializer)에서는 이 메서드를 호출하여 마스킹을 수행합니다.
     *
     * 예를 들어 `MaskingType.NAME.mask("홍길동")` 과 같이 호출되면,
     * NAME 상수가 가지고 있는 `s -> s.replaceAll(...)` 람다 함수가 실행됩니다.
     *
     * @param original 마스킹을 적용할 원본 문자열 (e.g., "홍길동").
     * @return 마스킹 처리된 결과 문자열 (e.g., "홍*동").
     *         만약 입력된 원본 문자열이 null이거나 비어있다면, 예외를 발생시키지 않고 안전하게 빈 문자열("")을 반환합니다.
     */
    public String mask(String original) {
        if (original == null || original.isBlank()) return "";

        // 'this'는 이 메서드를 호출한 Enum 상수 자신을 가리킵니다 (e.g., NAME, PHONE, EMAIL).
        // 해당 상수의 maskingFunction 필드에 저장된 람다 함수를 실행(apply)하고,
        // 그 실행 결과를 최종적으로 반환합니다.
        return this.maskingFunction.apply(original);
    }
}
