package com.portfolio.common.business.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 애플리케이션 전반에서 사용될 수 있는 유효성 검증 유틸리티 클래스입니다.
 * 모든 메서드는 정적(static) 메서드로 제공되어 인스턴스화 없이 사용할 수 있습니다.
 * 객체의 null 여부, 빈 값 여부, 문자열, 숫자, 날짜 및 다양한 비즈니스 규칙에 대한 검증 기능을 포함합니다.
 */
@UtilityClass
public final class ValidationUtils {
    // 정규 표현식 패턴을 상수로 미리 컴파일하여 성능을 최적화합니다.
    // Pattern 객체는 스레드에 안전(thread-safe)합니다.
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^[0-9]+$");
    private static final Pattern ALPHA_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern ALPHA_NUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    private static final Pattern KOREAN_PATTERN = Pattern.compile("^[가-힣]+$");
    private static final Pattern KOREAN_CONSONANT_VOWEL_PATTERN = Pattern.compile("^[ㄱ-ㅎㅏ-ㅣ가-힣]+$");
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?|ftp)://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?$");
    private static final Pattern IPV4_PATTERN = Pattern.compile("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    private static final Pattern MOBILE_PHONE_PATTERN = Pattern.compile("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$");
    private static final Pattern GENERAL_PHONE_PATTERN = Pattern.compile("^\\d{2,3}-\\d{3,4}-\\d{4}$");
    private static final Pattern BUSINESS_REG_NO_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern CORPORATE_REG_NO_PATTERN = Pattern.compile("^\\d{13}$");
    private static final Pattern STOCK_CODE_PATTERN = Pattern.compile("^[0-9]{6}$"); // 예: 삼성전자 005930
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$"); // 8~16자, 대/소문자, 숫자, 특수문자 포함

    // 1. 기본 유효성 검사 (Basic Validations)
    /**
     * 객체가 null인지 확인합니다.
     * @param obj 확인할 객체
     * @return 객체가 null이면 true, 아니면 false
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * 객체가 null이 아닌지 확인합니다.
     * @param obj 확인할 객체
     * @return 객체가 null이 아니면 true, null이면 false
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * 객체가 비어있는지(null 또는 empty) 확인합니다.
     * 지원하는 타입: CharSequence, Collection, Map, Array
     * 그 외 타입은 null 여부만 확인합니다.
     * @param obj 확인할 객체
     * @return 객체가 비어있으면 true, 아니면 false
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        if (obj instanceof CharSequence) return ((CharSequence) obj).length() == 0;
        if (obj instanceof Collection) return ((Collection<?>) obj).isEmpty();
        if (obj instanceof Map) return ((Map<?, ?>) obj).isEmpty();
        if (obj.getClass().isArray()) return Array.getLength(obj) == 0;
        // 위의 타입에 해당하지 않는 경우, 객체의 문자열 표현이 비어있는지 확인
        return obj.toString().isEmpty();
    }

    /**
     * 객체가 비어있지 않은지(not null and not empty) 확인합니다.
     * @param obj 확인할 객체
     * @return 객체가 비어있지 않으면 true, 비어있으면 false
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 문자열이 공백(whitespace)만으로 이루어져 있거나 비어있는지 확인합니다.
     * @param str 확인할 문자열
     * @return 문자열이 null이거나, 비어있거나, 공백만으로 이루어져 있으면 true
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 문자열이 공백만으로 이루어져 있지 않고, 비어있지도 않은지 확인합니다.
     * @param str 확인할 문자열
     * @return 문자열이 null이 아니고, 비어있지 않으며, 공백 이외의 문자를 포함하면 true
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 두 객체가 동일한지 확인합니다 (null-safe).
     * @param a 비교할 첫 번째 객체
     * @param b 비교할 두 번째 객체
     * @return 두 객체가 동일하면 true
     */
    public static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    /**
     * 두 문자열이 대소문자 구분 없이 동일한지 확인합니다.
     * @param str1 비교할 첫 번째 문자열
     * @param str2 비교할 두 번째 문자열
     * @return 두 문자열이 대소문자 구분 없이 동일하면 true
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
    }

    // 2. 문자열 유효성 검사 (String Validations)
    /**
     * 문자열의 길이가 정확히 일치하는지 확인합니다.
     * @param str 확인할 문자열
     * @param length 비교할 길이
     * @return 문자열의 길이가 주어진 길이와 같으면 true
     */
    public static boolean isLength(String str, int length) {
        return str != null && str.length() == length;
    }

    /**
     * 문자열의 길이가 최소 길이 이상인지 확인합니다.
     * @param str 확인할 문자열
     * @param minLength 최소 길이
     * @return 문자열의 길이가 최소 길이보다 크거나 같으면 true
     */
    public static boolean isMinLength(String str, int minLength) {
        return str != null && str.length() >= minLength;
    }

    /**
     * 문자열의 길이가 최대 길이를 초과하지 않는지 확인합니다.
     * @param str 확인할 문자열
     * @param maxLength 최대 길이
     * @return 문자열의 길이가 최대 길이보다 작거나 같으면 true
     */
    public static boolean isMaxLength(String str, int maxLength) {
        return str != null && str.length() <= maxLength;
    }

    /**
     * 문자열의 길이가 주어진 범위 내에 있는지 확인합니다.
     * @param str 확인할 문자열
     * @param minLength 최소 길이
     * @param maxLength 최대 길이
     * @return 문자열의 길이가 최소 길이와 최대 길이 사이에 있으면 true
     */
    public static boolean isLengthBetween(String str, int minLength, int maxLength) {
        return str != null && str.length() >= minLength && str.length() <= maxLength;
    }

    /**
     * 문자열이 숫자로만 구성되어 있는지 확인합니다.
     * @param str 확인할 문자열
     * @return 문자열이 숫자로만 구성되어 있으면 true
     */
    public static boolean isNumeric(String str) {
        return isNotBlank(str) && NUMERIC_PATTERN.matcher(str).matches();
    }

    /**
     * 문자열이 알파벳으로만 구성되어 있는지 확인합니다.
     * @param str 확인할 문자열
     * @return 문자열이 알파벳으로만 구성되어 있으면 true
     */
    public static boolean isAlpha(String str) {
        return isNotBlank(str) && ALPHA_PATTERN.matcher(str).matches();
    }

    /**
     * 문자열이 알파벳 또는 숫자로만 구성되어 있는지 확인합니다.
     * @param str 확인할 문자열
     * @return 문자열이 알파벳 또는 숫자로만 구성되어 있으면 true
     */
    public static boolean isAlphaNumeric(String str) {
        return isNotBlank(str) && ALPHA_NUMERIC_PATTERN.matcher(str).matches();
    }

    /**
     * 문자열이 유효한 이메일 형식인지 확인합니다.
     * @param email 확인할 이메일 문자열
     * @return 유효한 이메일 형식이면 true
     */
    public static boolean isEmail(String email) {
        return isNotBlank(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 문자열이 한글로만 구성되어 있는지 확인합니다. (자음, 모음 단독 사용 제외)
     * @param str 확인할 문자열
     * @return 한글로만 구성되어 있으면 true
     */
    public static boolean isKorean(String str) {
        return isNotBlank(str) && KOREAN_PATTERN.matcher(str).matches();
    }

    /**
     * 문자열이 한글(자음, 모음 포함)로만 구성되어 있는지 확인합니다.
     * @param str 확인할 문자열
     * @return 한글(자음, 모음 포함)로만 구성되어 있으면 true
     */
    public static boolean isKoreanWithConsonantAndVowel(String str) {
        return isNotBlank(str) && KOREAN_CONSONANT_VOWEL_PATTERN.matcher(str).matches();
    }

    /**
     * 문자열이 유효한 URL 형식인지 확인합니다.
     * @param url 확인할 URL 문자열
     * @return 유효한 URL 형식이면 true
     */
    public static boolean isUrl(String url) {
        return isNotBlank(url) && URL_PATTERN.matcher(url).matches();
    }

    /**
     * 문자열이 유효한 IPv4 주소 형식인지 확인합니다.
     * @param ipAddress 확인할 IPv4 주소 문자열
     * @return 유효한 IPv4 주소 형식이면 true
     */
    public static boolean isIPv4(String ipAddress) {
        return isNotBlank(ipAddress) && IPV4_PATTERN.matcher(ipAddress).matches();
    }

    /**
     * 문자열이 특정 접두사로 시작하는지 확인합니다.
     * @param str 확인할 문자열
     * @param prefix 접두사
     * @return 접두사로 시작하면 true
     */
    public static boolean startsWith(String str, String prefix) {
        return str != null && prefix != null && str.startsWith(prefix);
    }

    /**
     * 문자열이 특정 접미사로 끝나는지 확인합니다.
     * @param str 확인할 문자열
     * @param suffix 접미사
     * @return 접미사로 끝나면 true
     */
    public static boolean endsWith(String str, String suffix) {
        return str != null && suffix != null && str.endsWith(suffix);
    }

    /**
     * 문자열이 특정 문자열을 포함하는지 확인합니다.
     * @param str 확인할 문자열
     * @param searchStr 포함 여부를 확인할 문자열
     * @return 특정 문자열을 포함하면 true
     */
    public static boolean contains(String str, String searchStr) {
        return str != null && searchStr != null && str.contains(searchStr);
    }

    /**
     * 문자열이 대문자로만 구성되어 있는지 확인합니다.
     * @param str 확인할 문자열
     * @return 대문자로만 구성되어 있으면 true
     */
    public static boolean isUpperCase(String str) {
        if (isBlank(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c) && !Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 문자열이 소문자로만 구성되어 있는지 확인합니다.
     * @param str 확인할 문자열
     * @return 소문자로만 구성되어 있으면 true
     */
    public static boolean isLowerCase(String str) {
        if (isBlank(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (Character.isLetter(c) && !Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

    // 3. 숫자 유효성 검사 (Numeric Validations)
    /**
     * 숫자가 양수인지 확인합니다. (0 포함 안 함)
     * @param number 확인할 숫자
     * @return 숫자가 양수이면 true
     */
    public static boolean isPositive(Number number) {
        if (number == null) return false;
        if (number instanceof Integer) return number.intValue() > 0;
        if (number instanceof Long) return number.longValue() > 0;
        if (number instanceof Double) return number.doubleValue() > 0;
        if (number instanceof Float) return number.floatValue() > 0;
        return number.doubleValue() > 0; // 기타 숫자 타입
    }

    /**
     * 숫자가 음수인지 확인합니다. (0 포함 안 함)
     * @param number 확인할 숫자
     * @return 숫자가 음수이면 true
     */
    public static boolean isNegative(Number number) {
        if (number == null) return false;
        if (number instanceof Integer) return number.intValue() < 0;
        if (number instanceof Long) return number.longValue() < 0;
        if (number instanceof Double) return number.doubleValue() < 0;
        if (number instanceof Float) return number.floatValue() < 0;
        return number.doubleValue() < 0;
    }

    /**
     * 숫자가 0인지 확인합니다.
     * @param number 확인할 숫자
     * @return 숫자가 0이면 true
     */
    public static boolean isZero(Number number) {
        if (number == null) return false;
        return number.doubleValue() == 0.0;
    }

    /**
     * 숫자가 주어진 범위 내에 있는지 확인합니다. (min, max 포함)
     * @param number 확인할 숫자
     * @param min 최소값
     * @param max 최대값
     * @return 숫자가 범위 내에 있으면 true
     */
    public static boolean isBetween(Number number, Number min, Number max) {
        if (number == null || min == null || max == null) return false;
        double value = number.doubleValue();
        return value >= min.doubleValue() && value <= max.doubleValue();
    }

    /**
     * 숫자가 짝수인지 확인합니다.
     * @param number 확인할 정수형 숫자 (Integer, Long, Short, Byte)
     * @return 숫자가 짝수이면 true
     */
    public static boolean isEven(Number number) {
        if (number == null) return false;
        return number.longValue() % 2 == 0;
    }

    /**
     * 숫자가 홀수인지 확인합니다.
     * @param number 확인할 정수형 숫자 (Integer, Long, Short, Byte)
     * @return 숫자가 홀수이면 true
     */
    public static boolean isOdd(Number number) {
        if (number == null) return false;
        return number.longValue() % 2 != 0;
    }

    /**
     * 숫자가 소수(prime number)인지 확인합니다.
     * @param n 확인할 정수
     * @return 숫자가 소수이면 true
     */
    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // 4. 날짜 및 시간 유효성 검사 (Date & Time Validations)
    /**
     * 문자열이 유효한 날짜 형식인지 확인합니다.
     * @param dateStr 확인할 날짜 문자열
     * @param format 날짜 형식 (예: "yyyy-MM-dd")
     * @return 유효한 날짜 형식이면 true
     */
    public static boolean isDate(String dateStr, String format) {
        if (isBlank(dateStr) || isBlank(format)) return false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 문자열이 유효한 날짜/시간 형식인지 확인합니다.
     * @param dateTimeStr 확인할 날짜/시간 문자열
     * @param format 날짜/시간 형식 (예: "yyyy-MM-dd HH:mm:ss")
     * @return 유효한 날짜/시간 형식이면 true
     */
    public static boolean isDateTime(String dateTimeStr, String format) {
        if (isBlank(dateTimeStr) || isBlank(format)) return false;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDateTime.parse(dateTimeStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 주어진 날짜가 현재 날짜보다 미래인지 확인합니다.
     * @param date 확인할 날짜
     * @return 미래 날짜이면 true
     */
    public static boolean isFuture(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    /**
     * 주어진 날짜/시간이 현재 날짜/시간보다 미래인지 확인합니다.
     * @param dateTime 확인할 날짜/시간
     * @return 미래 날짜/시간이면 true
     */
    public static boolean isFuture(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * 주어진 날짜가 현재 날짜보다 과거인지 확인합니다.
     * @param date 확인할 날짜
     * @return 과거 날짜이면 true
     */
    public static boolean isPast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    /**
     * 주어진 날짜/시간이 현재 날짜/시간보다 과거인지 확인합니다.
     * @param dateTime 확인할 날짜/시간
     * @return 과거 날짜/시간이면 true
     */
    public static boolean isPast(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * 주어진 날짜가 오늘인지 확인합니다.
     * @param date 확인할 날짜
     * @return 오늘 날짜이면 true
     */
    public static boolean isToday(LocalDate date) {
        return date != null && date.isEqual(LocalDate.now());
    }

    /**
     * 주어진 해가 윤년인지 확인합니다.
     * @param year 확인할 연도
     * @return 윤년이면 true
     */
    public static boolean isLeapYear(int year) {
        return java.time.Year.isLeap(year);
    }

    // 5. 비즈니스/금융 관련 유효성 검사 (Business/Financial Validations)
    /**
     * 문자열이 유효한 대한민국 휴대폰 번호 형식인지 확인합니다. (하이픈 제외)
     * @param phoneNum 확인할 휴대폰 번호 문자열
     * @return 유효한 휴대폰 번호 형식이면 true
     */
    public static boolean isMobilePhoneNumber(String phoneNum) {
        return isNotBlank(phoneNum) && MOBILE_PHONE_PATTERN.matcher(phoneNum).matches();
    }

    /**
     * 문자열이 유효한 일반 전화번호 형식인지 확인합니다. (하이픈 포함)
     * @param phoneNum 확인할 전화번호 문자열
     * @return 유효한 전화번호 형식이면 true
     */
    public static boolean isGeneralPhoneNumber(String phoneNum) {
        return isNotBlank(phoneNum) && GENERAL_PHONE_PATTERN.matcher(phoneNum).matches();
    }

    /**
     * 대한민국 사업자등록번호(10자리)의 유효성을 검증합니다.
     * @param regNo 검증할 사업자등록번호 10자리 문자열
     * @return 유효한 사업자등록번호이면 true
     */
    public static boolean isBusinessRegistrationNumber(String regNo) {
        if (!isLength(regNo, 10) || !isNumeric(regNo)) {
            return false;
        }

        int[] checkWeights = {1, 3, 7, 1, 3, 7, 1, 3, 5};
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (regNo.charAt(i) - '0') * checkWeights[i];
        }
        sum += ((regNo.charAt(8) - '0') * 5) / 10;
        int checkDigit = (10 - (sum % 10)) % 10;

        return (regNo.charAt(9) - '0') == checkDigit;
    }

    /**
     * 대한민국 법인등록번호(13자리)의 유효성을 검증합니다.
     * @param regNo 검증할 법인등록번호 13자리 문자열
     * @return 유효한 법인등록번호이면 true
     */
    public static boolean isCorporateRegistrationNumber(String regNo) {
        if (!isLength(regNo, 13) || !isNumeric(regNo)) {
            return false;
        }

        int[] weights = {1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2};
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (regNo.charAt(i) - '0') * weights[i];
        }
        int checkDigit = (10 - (sum % 10)) % 10;

        return (regNo.charAt(12) - '0') == checkDigit;
    }

    /**
     * 주민등록번호 유효성을 검증합니다. (외국인 등록번호 포함)
     * !!주의!!: 주민등록번호는 민감한 개인정보이므로 수집, 처리, 보관 시 법적 요구사항을 반드시 준수해야 합니다.
     * @param ssn 검증할 주민등록번호 13자리 문자열 (하이픈 제외)
     * @return 유효한 주민등록번호이면 true
     */
    public static boolean isSsn(String ssn) {
        if (!isLength(ssn, 13) || !isNumeric(ssn)) {
            return false;
        }

        int sum = 0;
        int[] weights = {2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5};
        for (int i = 0; i < 12; i++) {
            sum += (ssn.charAt(i) - '0') * weights[i];
        }

        int checkDigit = (11 - (sum % 11)) % 10;

        return (ssn.charAt(12) - '0') == checkDigit;
    }

    /**
     * 신용카드 번호의 유효성을 Luhn 알고리즘(Mod-10)으로 검증합니다.
     * @param cardNumber 검증할 카드번호 문자열 (숫자만)
     * @return 유효한 카드번호이면 true
     */
    public static boolean isCreditCardNumber(String cardNumber) {
        if (isBlank(cardNumber)) {
            return false;
        }
        String sanitized = cardNumber.replaceAll("\\s+", "");
        if (!isNumeric(sanitized) || sanitized.length() < 13 || sanitized.length() > 19) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;
        for (int i = sanitized.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(sanitized.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    /**
     * 주식 종목 코드 형식(6자리 숫자)이 유효한지 확인합니다.
     * @param stockCode 확인할 주식 종목 코드
     * @return 유효한 형식이면 true
     */
    public static boolean isStockCode(String stockCode) {
        return isNotBlank(stockCode) && STOCK_CODE_PATTERN.matcher(stockCode).matches();
    }

    /**
     * 안전한 비밀번호 형식인지 확인합니다.
     * (예: 8~16자, 최소 하나의 대문자, 소문자, 숫자, 특수문자 포함)
     * @param password 확인할 비밀번호
     * @return 안전한 형식이면 true
     */
    public static boolean isSecurePassword(String password) {
        return isNotBlank(password) && PASSWORD_PATTERN.matcher(password).matches();
    }

    // 6. 배열 및 컬렉션 유효성 검사 (Array & Collection Validations)
    /**
     * 배열이 null 또는 비어있는지 확인합니다.
     * @param array 확인할 배열
     * @return 배열이 null이거나 길이가 0이면 true
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 배열이 비어있지 않은지 확인합니다.
     * @param array 확인할 배열
     * @return 배열이 null이 아니고 길이가 0보다 크면 true
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 컬렉션이 null 또는 비어있는지 확인합니다.
     * @param collection 확인할 컬렉션
     * @return 컬렉션이 null이거나 비어있으면 true
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 컬렉션이 비어있지 않은지 확인합니다.
     * @param collection 확인할 컬렉션
     * @return 컬렉션이 null이 아니고 비어있지 않으면 true
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 맵이 null 또는 비어있는지 확인합니다.
     * @param map 확인할 맵
     * @return 맵이 null이거나 비어있으면 true
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 맵이 비어있지 않은지 확인합니다.
     * @param map 확인할 맵
     * @return 맵이 null이 아니고 비어있지 않으면 true
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 배열에 null 요소가 포함되어 있는지 확인합니다.
     * @param array 확인할 배열
     * @return null 요소를 포함하면 true
     */
    public static boolean containsNull(Object[] array) {
        if (isEmpty(array)) {
            return false;
        }
        for (Object element : array) {
            if (element == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 컬렉션에 null 요소가 포함되어 있는지 확인합니다.
     * @param collection 확인할 컬렉션
     * @return null 요소를 포함하면 true
     */
    public static boolean containsNull(Collection<?> collection) {
        if (isEmpty(collection)) {
            return false;
        }
        for (Object element : collection) {
            if (element == null) {
                return true;
            }
        }
        return false;
    }

    // 7. 기타 유효성 검사 (Miscellaneous Validations)
    /**
     * Boolean 객체가 true인지 확인합니다 (null-safe).
     * @param bool 확인할 Boolean 객체
     * @return Boolean 객체가 null이 아니고 true이면 true
     */
    public static boolean isTrue(Boolean bool) {
        return Boolean.TRUE.equals(bool);
    }

    /**
     * Boolean 객체가 false인지 확인합니다 (null-safe).
     * @param bool 확인할 Boolean 객체
     * @return Boolean 객체가 null이 아니고 false이면 true
     */
    public static boolean isFalse(Boolean bool) {
        return Boolean.FALSE.equals(bool);
    }

    /**
     * 파일 확장자가 허용된 목록에 포함되는지 확인합니다.
     * @param filename 파일 이름
     * @param allowedExtensions 허용된 확장자 목록 (예: "jpg", "png", "gif")
     * @return 확장자가 허용 목록에 있으면 true
     */
    public static boolean isValidFileExtension(String filename, String... allowedExtensions) {
        if (isBlank(filename) || isEmpty(allowedExtensions)) {
            return false;
        }
        String extension = getFileExtension(filename);
        if (isBlank(extension)) {
            return false;
        }
        for (String allowed : allowedExtensions) {
            if (extension.equalsIgnoreCase(allowed)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 파일 이름에서 확장자를 추출합니다.
     * @param filename 파일 이름
     * @return 확장자 (점 제외), 없으면 빈 문자열
     */
    private static String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(dotIndex + 1);
    }
}

