package com.portfolio.common.business.util;

import com.portfolio.common.system.exception.BusinessException;
import com.portfolio.common.system.exception.ErrorCode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

@Slf4j
@UtilityClass
public class NumberUtils {

    private static final int DEFAULT_SCALE = 2;                                     // 기본 소수점 자리수
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP; // 기본 반올림 모드

    // --- 한글 금액 표기 상수 ---
    private static final String[] KOREAN_DIGITS = {"", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
    private static final String[] KOREAN_UNITS = {"", "십", "백", "천"};
    private static final String[] KOREAN_BIG_UNITS = {"", "만", "억", "조", "경"};

    /**
     * 숫자를 세자리마다 ','가 포함된 문자열로 변환
     * <pre>
     *     NumberUtils.formatWithComma(123456);
     *     result : "123,456"
     * </pre>
     * @param number
     * @return
     */
    public static String formatWithComma(BigDecimal number){
        if (number == null) { return null; }

        // NumberFormat.getInstance()는 현재 시스템의 기본 Locale
        // 특정 국가 형식을 원하면 new Locale("ko", "KR")등을 인자로 줄 수 있음
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    /**
     * 숫자를 원화(KRW)형식 문자열로 변환
     * <pre>
     *     NumberUtils.formatAsKoreanWon(1234567);
     *     result : "₩1,234,567"
     * </pre>
     * @param amount
     * @return
     */
    public static String formatAsKoreanWon(BigDecimal amount) {
        if (amount == null) { return null; }


        // getCurrencyInstance(Locale)를 사용하여 특정 국가의 통화 형식을 가져옴
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.KOREA);
        return formatter.format(amount);
    }

    /**
     * 숫자를 Locale별 숫자 및 통화 형식 문자열로 변환
     * <pre>
     *     NumberUtils.formatAsKoreanWon(123456789, Locale.US);
     *     result : "$123,456,789.00"
     * </pre>
     * @param amount
     * @param locale
     * @return
     */
    public static String formatAsKoreanWon(BigDecimal amount, Locale locale) {
        if (amount == null) { return null; }

        // getCurrencyInstance(Locale)를 사용하여 특정 국가의 통화 형식을 가져옴
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return formatter.format(amount);
    }

    /**
     * 소수점을 포함한 숫자를 지정된 패턴으로 포맷팅
     * <pre>
     *     NumberUtils.formatWithPattern(1234.567, "#,##0.00");
     *     result : "1,234.57"
     * </pre>
     * @param number
     * @param pattern
     * @return
     */
    public static String formatWithPattern(BigDecimal number, String pattern) {
        if (number == null || pattern == null) { return null; }

        DecimalFormat formatter = new DecimalFormat(pattern);
        formatter.setRoundingMode(DEFAULT_ROUNDING_MODE);
        return formatter.format(number);
    }

    /**
     * 콤마나 통화 기호가 포함된 문자열을 BigDecimal 객체로 안전하게 변환
     * <pre>
     *     NumberUtils.parseStringToBigDecimal("1,234,567.89");
     *     result : BigDecimal(1234567.89)
     * </pre>
     * @param formattedNumber
     * @return
     */
    public static BigDecimal parseStringToBigDecimal(String formattedNumber) {
        if (formattedNumber == null || formattedNumber.trim().isEmpty()) { return null; }

        try {
            // 전처리 단계: 모든 통화 기호와 공백을 제거합니다.
            // \\p{Sc}는 유니코드의 모든 통화 기호(Currency Symbol)를 나타내는 정규식입니다.
            // \\s는 모든 공백 문자(스페이스, 탭 등)를 의미합니다.
            String cleanString = formattedNumber.trim().replaceAll("[\\p{Sc}\\s]", "");
            // 모든 Locale에 대한 숫자 포멧을 파싱하기 위해 getInstance() 사용
            NumberFormat format = NumberFormat.getInstance();
            // 통화 기호 등 숫자가 아닌 문자를 파싱하기 위해 getCurrencyInstance()를 사용 가능.
            // 일반적인 숫자 문자열("1,234.5") 파싱을 위해 getInstance()가 더 범용적.
            // 정수 부분만 파싱하려면 setParseIntegerOnly(true)를 설정 가능.
            Number number = format.parse(cleanString.trim());

            return new BigDecimal(number.toString());
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 두 BigDecimal 숫자를 합함.
     * null 값은 0으로 처리하여 NullPointerException을 방지
     *
     * @param num1 비교할 첫 번째 숫자
     * @param num2 비교할 두 번째 숫자
     * @return
     */
    public static BigDecimal safeAdd(BigDecimal num1, BigDecimal num2) {
        BigDecimal valA = (num1 == null) ? BigDecimal.ZERO : num1;
        BigDecimal valB = (num2 == null) ? BigDecimal.ZERO : num2;

        return valA.add(valB);
    }

    /**
     * 두 BigDecimal 숫자를 뺌.
     * null 값은 0으로 처리하여 NullPointerException을 방지
     * @param minuend
     * @param subtrahend
     * @return
     */
    public static BigDecimal safeSubtract(BigDecimal minuend, BigDecimal subtrahend) {
        BigDecimal valA = (minuend == null) ? BigDecimal.ZERO : minuend;
        BigDecimal valB = (subtrahend == null) ? BigDecimal.ZERO : subtrahend;

        return valA.subtract(valB);
    }

    public static BigDecimal safeMultiply(BigDecimal num1, BigDecimal num2) {
        BigDecimal valA = (num1 == null) ? BigDecimal.ZERO : num1;
        BigDecimal valB = (num2 == null) ? BigDecimal.ZERO : num2;

        return valA.multiply(valB);
    }

    /**
     * 첫 번째 BigDecimal 숫자를 두 번째 숫자로 나눔
     * 0으로 나누는 경우 ArithmeticException이 발생하므로 주의.
     * 기본 소수점 자리수(2)와 기본 반올림 모드(HALF_UP)를 사용.
     * @param dividend
     * @param divisor
     * @return
     */
    public static BigDecimal safeDivide(BigDecimal dividend, BigDecimal divisor) {
        return safeDivide(dividend, divisor, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 첫 번째 BigDecimal 숫자를 두 번째 숫자로 나누되, 소수점 자리수와 반올림 모드를 직접 지정
     *
     * @param dividend
     * @param divisor
     * @param scale
     * @param roundingMode
     * @return
     */
    public static BigDecimal safeDivide(BigDecimal dividend, BigDecimal divisor, int scale, RoundingMode roundingMode) {
        if (dividend == null || divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, ": Divisor cannot be null or zero");
        }
        return dividend.divide(divisor, scale, roundingMode);
    }

    /**
     * 숫자를 지정된 길이에 맞춰 왼쪽을 0으로 채운(zero-padding) 문자열로 변환
     * <pre>
     *     NumberUtils.formatWithZeroPadding(123, 5);
     *     result : "00123"
     * </pre>
     * @param number
     * @param length
     * @return
     */
    public static String formatWithZeroPadding(BigDecimal number, int length) {
        if (number == null) { return null; }

        return String.format("%0" + length + "d", number.longValue());
    }

    /**
     * 숫자를 한글 금액으로 변환표기
     * <pre>
     *     NumberUtils.formatAsKoreaCurrencyText(123456789);
     *     result : 일억이천삼백사십오만육천칠백팔십구
     * </pre>
     * @param amount
     * @return
     */
    public static String formatAsKoreaCurrencyText(BigDecimal amount) {
        if (amount == null) { return null; }

        long number = amount.longValue();
        if (number == 0) { return "영"; }

        StringBuilder result = new StringBuilder();
        int unitIndex = 0;

        while (number > 0) {
            // 4자리 처리(만, 억, 조 단위)
            long part = number % 10000;
            if (part > 0) {
                String partStr = readFourDigits(part);
                result.insert(0, partStr + KOREAN_BIG_UNITS[unitIndex]);
            }
            number /= 10000;
            unitIndex++;
        }

        // "일십" -> "십", "일백" -> "백"과 같은 불필요한 "일"제거
//        result.toString().replaceAll("일([십백천])", "$1").trim();

        return result.toString();
    }
    // formatAsKoreaCurrencyText의 보조 메서드 (digit 표기 ex: 육천칠백팔십구)
    private static String readFourDigits(long part) {
        if (part == 0) {return "";}
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            long digit = part % 10;
            if (digit > 0) { sb.insert(0, KOREAN_DIGITS[(int) digit] + KOREAN_UNITS[i]); }
            part /= 10;
        }

        return sb.toString();
    }

    /**
     * 주어진 문자열이 유효한 숫자인지 확인(정수 및 소수점, 음수 기호(-)를 허용)
     * @param str
     * @return
     */
    public static boolean isNumberic(String str) {
        if (str == null || str.trim().isEmpty()) { return false; }
        // Java 정규표현식을 사용하여 숫자 형식 검사.
        // ^-? : 맨 앞에 -가 있거나 없음.
        // \\d+ : 숫자가 1개 이상.
        // (\\.\\d+)? : 소수점과 그 뒤 숫자가 있거나 없음.
        return str.matches("^-?\\d+(\\.\\d+)?$");
    }

    /**
     * 두 Bigdecimal 객체를 null-safe하게 비교.(null == 0)
     * @param num1 비교할 첫 번째 숫자
     * @param num2 비교할 두 번째 숫자
     * @return  a > b : 1, a == b : 0, a < b : -1
     */
    public static int safeCompare(BigDecimal num1, BigDecimal num2) {
        BigDecimal valA = (num1 == null) ? BigDecimal.ZERO : num1;
        BigDecimal valB = (num2 == null) ? BigDecimal.ZERO : num2;

        return valA.compareTo(valB);
    }

    /**
     * 첫 번째 숫자가 두 번째 숫자보다 큰지 확인 (a > b)
     *
     * @param a 비교할 첫 번째 숫자
     * @param b 비교할 두 번째 숫자
     * @return
     */
    public static boolean isGreaterThan(BigDecimal a, BigDecimal b) {
        return safeCompare(a, b) > 0;
    }

    /**
     * 첫 번째 숫자가 두 번째 숫자보다 크거나 같은지 확인 (a >= b)
     *
     * @param a 비교할 첫 번째 숫자
     * @param b 비교할 두 번째 숫자
     * @return
     */
    public static boolean isGreaterThanOrEqual(BigDecimal a, BigDecimal b) {
        return safeCompare(a, b) >= 0;
    }

    /**
     * 첫 번째 숫자가 두 번째 숫자보다 작은지 확인 (a < b)
     *
     * @param a 비교할 첫 번째 숫자
     * @param b 비교할 두 번째 숫자
     * @return
     */
    public static boolean isLessThan(BigDecimal a, BigDecimal b) {
        return safeCompare(a, b) < 0;
    }

    /**
     * 첫 번째 숫자가 두 번째 숫자보다 작거나 같은지 확인 (a < b)
     *      *
     * @param a 비교할 첫 번째 숫자
     * @param b 비교할 두 번째 숫자
     * @return
     */
    public static boolean isLessThanOrEqual(BigDecimal a, BigDecimal b) {
        return safeCompare(a, b) <= 0;
    }

    /**
     * 두 숫자가 같은지 확인. (a == b)
     *
     * @param a 비교할 첫 번째 숫자
     * @param b 비교할 두 번째 숫자
     * @return
     */
    public static boolean isEqual(BigDecimal a, BigDecimal b) {
        return safeCompare(a, b) == 0;
    }

    /**
     * 해당 숫자가 0인지 확인.
     *
     * @param number
     * @return
     */
    public static boolean isZero(BigDecimal number) {
        return number != null && BigDecimal.ZERO.compareTo(number) == 0;
    }

    /**
     * 주어진 숫자를 특정 단위(unit)에 맞춰 반올림합니다.
     * 예: (12345, 100) -> 12300, (12355, 100) -> 12400
     *
     * @param number 반올림할 원본 숫자
     * @param unit   반올림 기준 단위 (예: 10, 100, 1000). 0보다 커야 함.
     * @return 단위에 맞춰 반올림된 BigDecimal
     */
    public static BigDecimal roundToUnit(BigDecimal number, int unit) {
        if (number == null || unit <= 0) return number;
        BigDecimal unitDecimal = new BigDecimal(unit);
        return number.divide(unitDecimal, 0, RoundingMode.HALF_UP).multiply(unitDecimal);
    }
    /**
     * 주어진 숫자를 특정 단위(unit)에 맞춰 버림(절사)
     * <pre>
     *     NumberUtils.floorToUnit(12399, 100);
     *     result : 12300
     * </pre>
     * @param number 버림할 원본 숫자
     * @param unit   버림 기준 단위 (예: 10, 100, 1000). 0보다 커야 함.
     * @return       단위에 맞춰 버림된 BigDecimal
     */
    public static BigDecimal floorToUnit(BigDecimal number, int unit) {
        if (number == null || unit <= 0) { return null; }

        BigDecimal unitDecimal = new BigDecimal(unit);
        // (숫자 / 단위)를 소수점 0자리에서 버림한 후, 다시 단위를 곱함
        return number.divide(unitDecimal, 0, RoundingMode.DOWN).multiply(unitDecimal);
    }

    /**
     *기준값 대비 변경값의 변화율(수익률 등)을 백분율(%)로 계산합니다.
     * 공식: ((after - before) / before) * 100
     *
     * @param before 기준값 (예: 매수 가격)
     * @param after  변경값 (예: 현재 가격)
     * @param scale  소수점 자리수
     * @return 변화율(%). 기준값이 0이거나 null이면 0을 반환.
     * @return
     */
    public static BigDecimal calculatePercentageChange(BigDecimal before, BigDecimal after, int scale) {
        if (before == null || after == null || isZero(before)) { return BigDecimal.ZERO; }

        BigDecimal chage = after.subtract(before);
        // 백분율 계산 시 정확도를 위해 나눗셈에서 소수점 2자리를 추가로 확보한 뒤 최종 결과 반올림.
        return chage.divide(before, scale + 2, DEFAULT_ROUNDING_MODE)
                .multiply(new BigDecimal("100"))
                .setScale(scale, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 전체(total)에서 부분(part)이 차지하는 비중을 백분율(%)로 계산.
     * 공식: (part / total) * 100
     *
     * @param part  부분 값
     * @param total 전체 값
     * @param scale 소수점 자리수
     * @return 비중(%). 전체값이 0이거나 null이면 0을 반환.
     */
    public static BigDecimal calculatePortionPercentage(BigDecimal part, BigDecimal total, int scale) {
        if (part == null || total == null || isZero(total)) { return BigDecimal.ZERO; }

        return part.divide(total, scale + 2, DEFAULT_ROUNDING_MODE)
                .multiply(new BigDecimal("100"))
                .setScale(scale, DEFAULT_ROUNDING_MODE);
    }

    /**
     *주어진 값에 특정 백분율(%)을 적용한 값을 계산합니다.
     * 예: (1000, 5) -> 50 (1000의 5%는 50)
     *
     * @param amount     원본 값
     * @param percentage 적용할 백분율 (예: 5%인 경우 5를 전달)
     * @return 백분율이 적용된 값
     */
    public static BigDecimal applyPercentage(BigDecimal amount, BigDecimal percentage) {
        if (amount == null || percentage == null) { return BigDecimal.ZERO; }

        // 퍼센트 소수로 변환 (5 -> 0.05)
        BigDecimal percentAsDecimal = percentage.divide(new BigDecimal("100"));
        return amount.multiply(percentAsDecimal);
    }

    /**
     * 숫자가 시작(start)과 끝(end) 범위 내에 있는지 확인 (경계값 포함).
     *
     * @param value 확인할 숫자
     * @param start 시작 범위
     * @param end   끝 범위
     * @return value >= start && value <= end 이면 true
     */
    public static boolean isBetweenInclusive(BigDecimal value, BigDecimal start, BigDecimal end) {
        if (value == null || start == null || end == null) { return false; }

        // value.compareTo(start) >= 0  : value가 start보다 크거나 같다
        // value.compareTo(end) <= 0    : value가 end보다 작거나 같다
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    /**
     * 숫자가 시작(start)과 끝(end) 범위 내에 있는지 확인 (경계값 미포함).
     *
     * @param value 확인할 숫자
     * @param start 시작 범위
     * @param end   끝 범위
     * @return value > start && value < end 이면 true
     */
    public static boolean isBetweenExclusive(BigDecimal value, BigDecimal start, BigDecimal end){
        if (value == null || start == null || end == null) { return false; }

        // value.compareTo(start) > 0  : value가 start보다 큼
        // value.compareTo(end) < 0    : value가 end보다 작음
        return value.compareTo(start) > 0 && value.compareTo(end) < 0;
    }

    /**
     * BigDecimal에서 정수 부분만 추출합니다.
     *
     * @param number 원본 숫자
     * @return 정수 부분. number가 null이면 null 반환.
     */
    public static BigDecimal getIntegerPart(BigDecimal number) {
        if (number == null) { return null; }
        // setScale(0, RoundingMode.DOWN)은 소수점 이하를 버림
        return number.setScale(0, RoundingMode.DOWN);
    }

    /**
     * BigDecimal에서 소수 부분만 추출합니다.
     * 예: -123.45 -> 0.45
     *
     * @param number 원본 숫자
     * @return 소수 부분 (항상 0 이상). number가 null이면 null 반환.
     */
    public static BigDecimal getFractionalPart(BigDecimal number) {
        if (number == null) { return null; }
        // 원본 숫자에서 정수만 빼 소수 부분만 남김.
        // abs()를 사용하여 음수라도 소수 부분은 양수로 반환.<절댓값(absolute value)>
        return number.subtract(getIntegerPart(number)).abs();
    }

    /**
     * 값이 null이면 기본값(BigDecimal.ZERO)을 반환, 아니면 원래 값을 반환.
     *
     * @param number 확인할 숫자
     * @return null이 아닌 숫자
     */
    public static BigDecimal getOrDefault(BigDecimal number) {
        return number == null ? BigDecimal.ZERO : number;
    }

    /**
     * 값이 null이면 기본값(BigDecimal.ZERO)을 반환, 아니면 원래 값을 반환.
     *
     * @param number 확인할 숫자
     * @param defaultValue null일 경우 반환될 기본값
     * @return null이 아닌 숫자
     */
    public static BigDecimal getOrDefault(BigDecimal number, BigDecimal defaultValue) {
        return number == null ? defaultValue : number;
    }

    /**
     * BigDecimal 컬렉션 합계 계산.(null == 0)
     *
     * @param numbers
     * @return
     */
    public static BigDecimal sum(Collection<BigDecimal> numbers) {
        if (numbers == null || numbers.isEmpty()) { return BigDecimal.ZERO; }

        return numbers.stream()
                .filter(Objects::nonNull)                   // 스트림의 null요소 filtering
                .reduce(BigDecimal.ZERO, BigDecimal::add);  // 초기값 0부터 시작 모든 요소를 더함.
    }

    /**
     * BigDecimal 컬렉션의 평균 계산.(null 제외)
     *
     * @param numbers 숫자 컬렉션
     * @param scale   결과의 소수점 자리수
     * @return 평균값. 컬렉션이 비어있으면 0을 반환.
     */
    public static BigDecimal average(Collection<BigDecimal> numbers, int scale) {
        if (numbers == null || numbers.isEmpty()) { return BigDecimal.ZERO; }
        // null이 아닌 요소들만 필터링하여 새로운 리스트 생성.
        List<BigDecimal> nonNullNumbers = numbers.stream()
                .filter(Objects::nonNull)                   // 스트림의 null요소 filtering
                .toList();                                  // List로 만듬
        if (nonNullNumbers.isEmpty()) { return BigDecimal.ZERO; }
        BigDecimal sum = sum(nonNullNumbers);
        // nonNull 리스트의 평균 계산
        return sum.divide(new BigDecimal(nonNullNumbers.size()), scale, DEFAULT_ROUNDING_MODE);
    }

    /**
     * BigDecimal 컬렉션에서 최대값 찾기
     *
     * @param numbers 숫자 컬렉션
     * @return 최대값. 컬렉션이 비어있거나 모든 요소가 null이면 null 반환.
     */
    public static BigDecimal max(Collection<BigDecimal> numbers) {
        if (numbers == null || numbers.isEmpty()) { return BigDecimal.ZERO; }

        return numbers.stream()
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())     // 자연 순서(오른차순)로 최대값 찾기
                .orElse(null);                // numbers가 비었을 경우 null 반환.
    }

    /**
     * BigDecimal 컬렉션에서 최소값 찾기
     *
     * @param numbers 숫자 컬렉션
     * @return 최소값. 컬렉션이 비어있거나 모든 요소가 null이면 null 반환.
     */
    public static BigDecimal min(Collection<BigDecimal> numbers) {
        if (numbers == null || numbers.isEmpty()) { return BigDecimal.ZERO; }

        return numbers.stream()
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())     // 자연 순서(오른차순)로 최소값 찾기
                .orElse(null);                // numbers가 비었을 경우 null 반환.
    }
}
