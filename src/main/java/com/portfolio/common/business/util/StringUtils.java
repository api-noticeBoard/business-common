package com.portfolio.common.business.util;

import code.CaseType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class StringUtils {

    private static final Pattern SNAKE_CASE_PATTERN = Pattern.compile("_([a-z])");
    private static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("([a-z])([A-Z])");
    //----- 기본 검사 (Null, Empty, Blank) -----

    /**
     * 문자열(str)에 공백 문자(‘ ‘, ‘\t’, ‘\n’, ‘\r’)가 포함되어 있는지를 검사.
     * <pre>
     * example>
     *      StringUtils.isSpace("abc 12345")
     *      result : true
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @return 공백 문자(‘ ‘, ‘\t’, ‘\n’, ‘\r’)가 있으면 true
     */
    public static boolean isSpace(String checkStr) {
        boolean chk = false;

        if (checkStr == null) return false;
        if (checkStr.contains(" ") || checkStr.contains("\t")
                || checkStr.contains("\n") || checkStr.contains("\r")) {
            chk = true;
        }
        return chk;
    }

    /**
     * 특정 길이만큼의 문자열에 공백 문자(‘ ‘, ‘\t’, ‘\n’, ‘\r’)가 포함하는지 검사.
     * <pre>
     * example>
     *      StringUtils.isSpace("abc 12345", 3)
     *      result : false
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @param length   검사할 길이
     * @return 검사결과 리턴
     */
    public static boolean isSpace(String checkStr, int length) {
        boolean chk = false;

        if (checkStr == null || checkStr.length() < 1) return true;
        if (checkStr.substring(0, length).contains(" ") || checkStr.substring(0, length).contains("\t")
                || checkStr.substring(0, length).contains("\n") || checkStr.substring(0, length).contains("\r")) {
            chk = true;
        }
        return chk;
    }

    /**
     * 문자열이 null이거나 길이가 0인지 확인.
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("text")    = false
     * </pre>
     *
     * @param cs 확인할 CharSequence
     * @return null이거나 빈 문자열이면 true
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 문자열이 유의미한 내용을 가지고 있는지 확인 (isEmpty의 반대).
     *
     * @param cs 확인할 CharSequence
     * @return null이 아니고 빈 문자열이 아니면 true
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * 문자열이 null이거나, 비어있거나, 공백(whitespace)으로만 있는지 확인.
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("  \t\n  ") = true
     * StringUtils.isBlank("text")    = false
     * </pre>
     *
     * @param cs 확인할 CharSequence
     * @return null, 빈 문자열, 또는 공백으로만 이루어져 있으면 true
     */
    public static boolean isBlank(final CharSequence cs) {
        if (isEmpty(cs)) return true;

        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isWhitespace(cs.charAt(i))) return false;
        }
        return true;
    }

    /**
     * 문자열이 공백이 아닌 문자를 포함하고 있는지 확인 (isBlank의 반대).
     *
     * @param cs 확인할 CharSequence
     * @return 유의미한 문자를 포함하면 true
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 전체 문자열(str)이 숫자인지를 검사.
     * <pre>
     * example>
     *      StringUtils.isDigit("123a456")
     *      result : false
     * </pre>
     *
     * @param cs 검사할 CharSequence
     * @return 숫자 포함하면 true
     */
    public static boolean isDigit(final CharSequence cs) {
        boolean chk = false;
        if (cs == null) return chk;
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isDigit(cs.charAt(i))) return chk;
        }
        return true;
    }

    /**
     * 특정 길이(length)만큼의 문자열(str)이 숫자인지를 검사.
     * <pre>
     * example>
     *      StringUtils.isDigit("123a456", 3)
     *      result : true
     * </pre>
     *
     * @param cs     검사할 문장
     * @param length 검사할 길이
     * @return 숫자 포함하면 true
     */
    public static boolean isDigit(final CharSequence cs, int length) {
        boolean chk = false;
        if (cs == null) return chk;
        CharSequence ss = cs.subSequence(0, length);
        for (int i = 0; i < ss.length(); i++) {
            if (!Character.isDigit(ss.charAt(i))) return chk;
        }
        return true;
    }

    //----- 기본값 처리 -----

    /**
     * 문자열이 null이면 빈 문자열("")을 반환하고, 아니면 원래 문자열을 반환.
     * NPE 방지 사용.
     * <pre>
     * StringUtils.nullToEmpty(null)     = ""
     * StringUtils.nullToEmpty("text")   = "text"
     * </pre>
     *
     * @param str 확인할 문자열
     * @return null이 아닌 문자열
     */
    public static String nullToEmpty(final String str) {
        return str == null ? "" : str;
    }

    /**
     * 문자열이 비어있으면(Empty) 기본 문자열을, 아니면 원래 문자열을 반환.
     *
     * @param str        확인할 문자열
     * @param defaultStr 기본값으로 사용할 문자열
     * @return 원래 문자열 또는 기본 문자열
     */
    public static String defaultIfEmpty(final String str, final String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    /**
     * 문자열이 공백이면(Blank) 기본 문자열을, 아니면 원래 문자열 반환.
     *
     * @param str        확인할 문자열
     * @param defaultStr 기본값으로 사용할 문자열
     * @return 원래 문자열 또는 기본 문자열
     */
    public static String defaultIfBlank(final String str, final String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }
    //----- 공백 제거 (Trimming) -----

    /**
     * 문자열의 앞뒤 공백을 제거. `String.trim()`의 Null-Safe 버전.
     *
     * @param str 공백을 제거할 문자열
     * @return 공백이 제거된 문자열 또는 null
     */
    public static String trim(final String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 문자열의 앞뒤 공백을 제거, 결과가 빈 문자열이 되면 null 반환.
     * DB에 저장 시 "" 대신 NULL을 넣고 싶을 때 유용.
     *
     * @param str 공백을 제거할 문자열
     * @return 공백이 제거된 문자열 또는 null
     */
    public static String trimToNull(final String str) {
        final String trimmed = trim(str);
        return isEmpty(trimmed) ? null : trimmed;
    }

    /**
     * 문자열의 앞뒤 공백을 제거, 결과가 null이 되면 빈 문자열("")을 반환.
     * Trim 결과로 절대 null을 받고 싶지 않을 때 사용.
     *
     * @param str 공백을 제거할 문자열
     * @return 공백이 제거된 문자열. (null이 아님을 보장)
     */
    public static String trimtoEmpty(final String str) {
        return str == null ? "" : str.trim();
    }

    //----- 비교 (Comparison) -----

    /**
     * 두 문자열이 대소문자 구분 없이 동일한지 비교.(Null-Safe)
     *
     * @param cs1 첫 번째 CharSequence
     * @param cs2 두 번째 CharSequence
     * @return 대소문자 구분 없이 두 문자열이 같으면 true
     */
    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) return true;
        if (cs1 == null || cs2 == null) return false;
        return cs1.toString().equals(cs2.toString());
    }

    public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) return true;
        if (cs1 == null || cs2 == null) return false;
        return cs1.toString().equalsIgnoreCase(cs2.toString());
    }

    //----- 부분 문자열 추출 (Substring) -----

    /**
     * 문자열에서 특정 구분자(separator)가 처음 나타나는 위치 이전의 부분 문자열을 반환.
     * <pre>
     * StringUtils.substringBefore("data.txt", ".")    = "data"
     * StringUtils.substringBefore("data.txt", "/")    = "data.txt" // 구분자 없을 때
     * </pre>
     *
     * @param str       전체 문자열
     * @param separator 구분자
     * @return 구분자 이전의 부분 문자열
     */
    public static String substringBefore(final String str, final String separator) {
        if (isEmpty(str) || separator == null) return str;
        if (!separator.isEmpty()) {
            final int pos = str.indexOf(separator);
            if (pos == -1) return str;
            return str.substring(0, pos);
        } else {
            return "";
        }
    }

    /**
     * 문자열에 특정 구분자(separator)가 처음 나타나는 위치 이후의 문자열을 반환합.
     * <pre>
     * StringUtils.substringAfter("data.txt", ".")     = "txt"
     * StringUtils.substringAfter("path/to/file", "/") = "to/file"
     * </pre>
     *
     * @param str       전체 문자열
     * @param separator 구분자
     * @return 구분자 이후의 부분 문자열
     */
    public static String substringAfter(final String str, final String separator) {
        if (isEmpty(str)) return str;
        if (separator == null) return "";
        final int pos = str.indexOf(separator);
        if (pos == -1) return "";
        return str.substring(0, pos + separator.length() - 1);
    }

    //----- 문자열 변형 (Manipulation) -----
    /**
     * 문자열의 순서를 뒤집음.
     *
     * @param str 뒤집을 문자열
     * @return 뒤집힌 문자열
     */
    public static String reverse(final String str) {
        if (str == null) return null;
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 문자열을 주어진 최대 길이로 자르고, 길이가 길 경우 생략 부호(...)를 붙임.
     * 생략 부호는 maxWidth 길이에 포함.
     *
     * @param str      축약할 문자열
     * @param maxWidth 최대 길이 (4 이상이어야 의미가 있음)
     * @return 축약된 문자열
     */
    public static String abbreviate(final String str, final int maxWidth) {
        if (str == null) return null;
        if (maxWidth < 4) {
            throw new IllegalArgumentException("Maximum width be at least 4");
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        return str.substring(0, maxWidth - 3) + "...";
    }

    /**
     * 문자열에서 숫자(0-9)만 추출하여 반환.
     *
     * @param str 숫자만 추출할 문자열
     * @return 숫자만 포함된 문자열. 입력이 null이면 빈 문자열("")을 반환.
     */
    public static String getDigitOnly(final String str) {
        if (str == null) return null;
        final StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            final char c = str.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    //----- 케이스 변환 (Case Conversion) -----

    /**
     * 문자열의 첫 글자를 대문자로 변환.
     * <pre>
     * StringUtils.capitalize(null)     = null
     * StringUtils.capitalize("hello")  = "Hello"
     * </pre>
     *
     * @param str 변환할 문자열
     * @return 첫 글자가 대문자로 변환된 문자열
     */
    public static String capitalize(final String str) {
        if (isEmpty(str)) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * SnakeCase문자열을 CamelCase로 변환.
     *
     * @param str 변환할 문자열
     * @return CalmelCase로 변환된 문자열
     */
    public static String snakeCaseToCamelCase(final String str) {
        if (isBlank(str)) return str;
        String lowerCaseStr = str.toLowerCase();
        Matcher matcher = SNAKE_CASE_PATTERN.matcher(lowerCaseStr);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * CamelCase문자열을 SnakeCase로 변환.
     *
     * @param str 변환할 문자열
     * @return SnakeCase로 변환된 문자열
     */
    public static String camelCaseToSnakeCase(final String str) {
        if (isBlank(str)) return str;
        Matcher matcher = CAMEL_CASE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            // ([a-z])([A-Z])의 ()가 matcher.group의 순서로 들어감
            matcher.appendReplacement(sb, matcher.group(1) + "_" + matcher.group(2).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString().toLowerCase();
    }

    /**
     * 문자열에 space를 기준으로 case type을 지정해 변환.
     *
     * @param str      변환할 문자열
     * @param caseType 변환할 caseType
     * @return 지정한 caseType으로 변환된 문자열
     */
    public static String convertCaseFromSpace(final String str, final CaseType caseType) {
        if (isBlank(str)) {
            return str;
        }
        switch (caseType) {
            case CAMEL_CASE:
                String[] words = str.trim().split("\\s+");
                return Arrays.stream(words)
                        .skip(1)    // 첫 단어 skip
                        .map(word -> {
                            if (word.isEmpty()) return "";
                            return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                        })
                        .collect(Collectors.joining("", words[0], ""));
            case SNAKE_CASE:
                return str.trim().toLowerCase().replaceAll("\\s+", "_");
            default:
                throw new IllegalArgumentException("지원하지 않는 타입입니다. : " + caseType);
        }
    }

    //----- 채우기 및 반복 (Padding & Repeating) -----

    /**
     * 문자열을 주어진 횟수만큼 반복.
     *
     * @param str    반복할 문자열
     * @param repeat 반복 횟수
     * @return 반복된 문자열
     */
    public static String repeat(final String str, final int repeat) {
        if (str == null) return null;
        if (repeat <= 0) return "";
        final StringBuilder sb = new StringBuilder(str.length() * repeat);
        for (int i = 0; i < repeat; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 문자열을 지정 길이만큼 왼쪽부터 특정 문자로 채움(padding).
     *
     * @param str     패딩을 적용할 문자열
     * @param size    목표 길이
     * @param padChar 채울 문자
     * @return 패딩이 적용된 문자열
     */
    public static String leftPad(final String str, final int size, final char padChar) {
        if (str == null) return null;
        final int pads = size - str.length();
        if (pads <= 0) return str;
        return repeat(String.valueOf(padChar), pads).concat(str);
    }

    /**
     * 문자열을 지정 길이만큼 오른쪽부터 특정 문자로 채움(padding).
     *
     * @param str     패딩을 적용할 문자열
     * @param size    목표 길이
     * @param padChar 채울 문자
     * @return 패딩이 적용된 문자열
     */
    public static String rightPad(final String str, final int size, final char padChar) {
        if (str == null) return null;
        final int pads = size - str.length();
        if (pads <= 0) return str;
        return str.concat(repeat(String.valueOf(padChar), pads));
    }
    // TODO: 양옆 padding

    //----- 문자열 마스킹 (Masking) -----

    /**
     * 문자열의 특정 구간을 마스킹 문자로 대체하는 범용 마스킹 메서드.
     * 시작 인덱스는 포함(inclusive), 끝 인덱스는 미포함(exclusive).
     * 인덱스가 범위를 벗어나도 Exception 없이 안전하게 동작.
     *
     * @param str      마스킹할 문자열
     * @param start    마스킹 시작 인덱스 (음수일 경우 0으로 처리)
     * @param end      마스킹 끝 인덱스 (문자열 길이를 초과할 경우 끝까지)
     * @param maskChar 마스킹에 사용할 문자
     * @return 마스킹된 문자열
     */
    public static String mask(final String str, int start, int end, final char maskChar) {
        if (isEmpty(str)) return str;
        final int len = str.length();
        if (start < 0) start = 0;
        if (end > len) end = len;
        if (start >= end) return str;
        final int maskLen = end - start;
        final String mask = repeat(String.valueOf(maskChar), maskLen);
        return str.substring(0, start) + mask + str.substring(end);
    }

    /**
     * 이름의 가운데 글자를 마스킹. (e.g., 홍길동 -> 홍*동)
     *
     * @param name 마스킹할 이름
     * @return 마스킹된 이름
     */
    public static String maskName(final String name) {
        if (isEmpty(name)) return name;
        final int len = name.length();
        if (len <= 1) return name;
        if (len == 2) return mask(name, 1, 2, '*');
        return mask(name, 1, len - 1, '*');
    }

    // TODO: 주민번호, 전화번호, 메일주소 등등 masking

    //----- 보안 (Security) -----

    /**
     * HTML 특수 문자를 이스케이프 처리하여 XSS(Cross-Site Scripting) 공격 방지.
     * 사용자 입력값을 웹 페이지에 표시하기 전에 반드시 사용.
     * <pre>
     * StringUtils.escapeHtml("<script>alert('XSS')</script>")
     *   = "&lt;script&gt;alert('XSS')&lt;/script&gt;"
     * </pre>
     *
     * @param str 이스케이프할 문자열
     */
    public static String escapeHtml(final String str) {
        if (str == null) return null;
        return str.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt'")
                .replace("\"", "&qout;")
                .replace("'", "&#39;");
    }
    //----- 고급 알고리즘 -----

    /**
     * 두 문자열 간의 레벤슈타인 거리(Levenshtein distance)를 계산.
     * 한 문자열을 다른 문자열로 변경하는 데 필요한 최소 편집(삽입, 삭제, 대체) 횟수.
     * 오타 교정, 유사 검색어 추천 등 문자열 유사도를 측정할 때 사용.
     * Null 입력을 허용하고, 이 경우 예외 대신 최대값에 가까운 거리를 반환하도록 개선.
     *
     * @param str1 첫 번째 CharSequence
     * @param str2 두 번째 CharSequence
     * @return 두 문자열 간의 편집 거리
     */
    public static int levenshteinDistance(CharSequence str1, CharSequence str2) {
        // null이 아닌 length 반환.
        if (str1 == null || str2 == null)
            return (str1 == null ? 0 : str1.length()) + (str2 == null ? 0 : str2.length());

        int str1Len = str1.length();
        int str2Len = str2.length();

        // 한쪽이 빈 문자열이면, 다른 쪽의 길이가 곧 거리
        if (str1Len == 0) return str2Len;
        if (str2Len == 0) return str1Len;

        // --- 알고리즘 구현을 위한 배열 초기화 (공간 최적화) ---
        // 2차원 행렬을 시뮬레이션하기 위해 두 개의 1차원 배열만 사용
        int[] previousRow = new int[str2Len + 1];
        int[] currentRow = new int[str2Len + 1];

        // --- 첫 번째 행(previousRow) 초기화: 첫 번째 문자열이 비어있을 때의 비용 ---
        for (int i = 0; i <= str2Len; i++) {
            previousRow[i] = i;
        }

        // --- 핵심 로직: 동적 프로그래밍을 이용한 비용 계산 ---
        // 첫 번째 문자열의 각 글자를 순회
        for (int i = 1; i <= str1Len; i++) {
            char str1_char = str1.charAt(i - 1);    // 현재 s1의 글자
            currentRow[0] = i; // 현재 행의 첫 번째 칸은 'i' (삽입 횟수)

            // 두 번째 문자열의 각 글자를 순회
            for (int j = 1; j <= str2Len; j++) {
                char str2_char = str2.charAt(j - 1);
                // 두 글자가 같으면 비용은 0, 다르면 1
                int cost = (str1_char == str2_char) ? 0 : 1;

                // --- 최소 비용 계산: 세 가지 경우 중 가장 작은 값을 선택 ---
                // 1. 왼쪽 값: 삽입 (currentRow[j - 1] + 1)
                // 2. 위쪽 값: 삭제 (previousRow[j] + 1)
                // 3. 대각선 값: 치환/유지 (previousRow[j - 1] + cost)
                currentRow[j] = Math.min(Math.min(currentRow[j - 1] + 1, previousRow[j] + 1), previousRow[j - 1] + cost);
            }
            // --- 행 업데이트: 현재 행의 값을 다음 반복을 위해 이전 행으로 복사 ---
            System.arraycopy(currentRow, 0, previousRow, 0, str2Len + 1);
        }
        return previousRow[str2Len];
    }

    //----- 검색 및 확인 (Searching & Checking) -----
    /**
     * 대상 문자열이 검색 문자열을 대소문자 구분 없이 포함하는지 확인(Null-Safe)
     * <pre>
     * StringUtils.containsIgnoreCase(null, *)     = false
     * StringUtils.containsIgnoreCase(*, null)     = false
     * StringUtils.containsIgnoreCase("Hello", "lo") = true
     * StringUtils.containsIgnoreCase("Hello", "LO") = true
     * StringUtils.containsIgnoreCase("Hello", "ol") = false
     * </pre>
     *
     * @param str       대상 문자열
     * @param searchStr 검색할 문자열
     * @return 포함하고 있으면 true
     */
    public static boolean containsIgnoreCase(final String str, final String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.toLowerCase().contains(searchStr.toLowerCase());
    }

    /**
     * 문자열이 알파벳으로만 구성되어 있는지 확인합니다.
     * <pre>
     * StringUtils.isAlpha(null)    = false
     * StringUtils.isAlpha("")      = false
     * StringUtils.isAlpha("abc")   = true
     * StringUtils.isAlpha("ab c")  = false // 공백
     * StringUtils.isAlpha("ab2c")  = false // 숫자
     * </pre>
     *
     * @param cs 확인할 CharSequence
     * @return 알파벳으로만 이루어져 있으면 true
     */
    public static boolean isAlpha(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isLetter(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 문자열이 알파벳과 숫자로만 구성되어 있는지 확인합니다.
     * <pre>
     * StringUtils.isAlphanumeric(null)    = false
     * StringUtils.isAlphanumeric("")      = false
     * StringUtils.isAlphanumeric("abc")   = true
     * StringUtils.isAlphanumeric("ab c")  = false // 공백
     * StringUtils.isAlphanumeric("ab2c")  = true
     * </pre>
     *
     * @param cs 확인할 CharSequence
     * @return 알파벳 또는 숫자로만 이루어져 있으면 true
     */
    public static boolean isAlphaNumberic(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isLetterOrDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //----- 접두사/접미사 처리 (Prefix & Suffix) -----

    /**
     * 문자열의 시작 부분이 특정 접두사로 시작하면 해당 접두사 제거.(대소문자 구분).
     * <pre>
     * StringUtils.removeStart(null, *)      = null
     * StringUtils.removeStart("abc", "a")   = "bc"
     * StringUtils.removeStart("abc", "b")   = "abc"
     * StringUtils.removeStart("abc", "A")   = "abc"
     * </pre>
     *
     * @param str    원본 문자열
     * @param remove 제거할 접두사
     * @return 접두사가 제거된 문자열
     */
    public static String removePrefix(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.startsWith(remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    /**
     * 문자열의 끝 부분이 특정 접미사로 끝나면 해당 접미사 제거.(대소문자 구분)
     * <pre>
     * StringUtils.removeEnd(null, *)      = null
     * StringUtils.removeEnd("abc", "c")   = "ab"
     * StringUtils.removeEnd("abc.txt", ".txt") = "abc"
     * StringUtils.removeEnd("abc", "b")   = "abc"
     * </pre>
     *
     * @param str    원본 문자열
     * @param remove 제거할 접미사
     * @return 접미사가 제거된 문자열
     */
    public static String removeSuffix(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    /**
     * [신규] 문자열을 다른 문자열로 감쌉니다.
     * <pre>
     * StringUtils.wrap(null, *)     = null
     * StringUtils.wrap("text", "'")   = "'text'"
     * StringUtils.wrap("text", "\"")  = "\"text\""
     * StringUtils.wrap("text", "<>")  = "<>text<>" // 감싸는 문자열이 1자 이상이어도 가능
     * </pre>
     *
     * @param str      감쌀 문자열
     * @param wrapWith 감싸는 데 사용할 문자열
     * @return 감싸진 문자열
     */
    public static String wrap(final String str, final String wrapWith) {
        if (isEmpty(str) || isEmpty(wrapWith)) {
            return str;
        }
        return wrapWith + str + wrapWith;
    }

    //----- 메시지 포매팅 (Message Formatting) -----

    /**
     * SLF4J 로깅 프레임워크와 유사하게 메시지 템플릿의 '{}'를 인자로 치환.
     * '문자열' + '연결' 보다 가독성이 높고 효율적.
     * <pre>
     * StringUtils.format("Hello, {}", "World")                 = "Hello, World"
     * StringUtils.format("계좌번호: {}, 고객명: {}", "123-45", "홍길동") = "계좌번호: 123-45, 고객명: 홍길동"
     * StringUtils.format("인자 부족: {}", "test")              = "인자 부족: test"
     * StringUtils.format("플레이스홀더 부족: {} {}", "a")        = "플레이스홀더 부족: a {}"
     * </pre>
     *
     * @param template 메시지 템플릿. '{}'를 플레이스홀더로 사용
     * @param args     치환할 인자(들)
     * @return 인자가 치환된 최종 문자열
     */
    public static String format(final String template, final Object... args) {
        if (template == null || args == null | args.length == 0) {
            return template;
        }
        StringBuilder sb = new StringBuilder(template.length() + 50);
        int i = 0;  // 템플릿 순회를 위한 인덱스
        int argIndex = 0;   // 인자 배열 순회를 위한 argIndex. ({}안에 들어갈 index)
        while (i < template.length()) {
            char c = template.charAt(i);
            // while문 현재 문자가 '{', '}'안에 있고, template.length 문자위치보다 클때,
            if (c == '{' && i + 1 < template.length() && template.charAt(i + 1) == '}' && argIndex < args.length) {
                // 인자값 sb에 추가후 다음 인자 준비
                sb.append(args[argIndex++]);
                // `{}` 두 문자를 건너뛰기 위해 인덱스를 2 증가
                i += 2;
            } else {
                // placeHolder가 아닐 경우 sb에 추가후 다음 문자로 이동
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }

    //----- 분리 및 분할 (Splitting & Slicing) -----

    /**
     * 문자열을 특정 구분자로 분리하여 문자열 배열로 반환.(trim 없이)
     * String.split()과 달리 정규표현식이 아닌 일반 문자로 분리하므로 더 빠르고 직관적.
     * 마지막에 빈 문자열이 있어도 유지. (e.g., "a,b,," -> ["a", "b", "", ""])
     * <pre>
     * StringUtils.split("a,b,c", ',')   = ["a", "b", "c"]
     * StringUtils.split("a, b, c", ',') = ["a", " b", " c"] // 공백은 제거되지 않음
     * StringUtils.split("a,,b", ',')    = ["a", "", "b"]
     * </pre>
     *
     * @param str           분리할 문자열
     * @param separatorChar 구분자 문자
     * @return 분리된 문자열 배열
     */
    public static String[] split(final String str, final char separatorChar) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return new String[0];
        }
        List<String> list = new ArrayList<>();
        int i = 0;
        int start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                list.add(str.substring(start, i));
                match = true;
                start = ++i;
                continue;
            }
            i++;
        }
        if (match || start < len) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[0]);
    }

    /**
     * 문자열의 왼쪽부터 지정된 길이만큼의 문자열을 반환.
     * <pre>
     * StringUtils.left("abc", 2)  = "ab"
     * StringUtils.left("abc", 5)  = "abc"
     * </pre>
     * @param str 원본 문자열
     * @param len 가져올 길이
     * @return 왼쪽 부분 문자열
     */
    public static String subStringLeft(final String str, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    /**
     * 문자열의 오른쪽부터 지정된 길이만큼의 문자열을 반환.
     * <pre>
     * StringUtils.right("abc", 2) = "bc"
     * StringUtils.right("abc", 5) = "abc"
     * </pre>
     * @param str 원본 문자열
     * @param len 가져올 길이
     * @return 오른쪽 부분 문자열
     */
    public static String subStringRight(final String str, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return "";
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(str.length() - len);
    }

    /**
     * 문자열 특정 위치부터 지정한 길이만큼 문자열 반환.
     * <pre>
     * StringUtils.mid("abcde", 2, 3) = "cde"
     * StringUtils.mid("abcde", 2, 8) = "cde"
     * </pre>
     * @param str 원본 문자열
     * @param pos 시작 위치 (0부터)
     * @param len 가져올 길이
     * @return 중간 부분 문자열
     */
    public static String subStringMiddle(final String str, int pos, final int len) {
        if (str == null) {
            return null;
        }
        if (len < 0 || pos > str.length()) {
            return "";
        }
        if (pos < 0) {
            pos = 0;
        }
        if (str.length() <= pos + len) {
            return str.substring(pos);
        }
        return str.substring(pos, pos + len);
    }

    //----- 고급 검색 및 비교 (Advanced Searching & Comparison) -----
    /**
     * 문자열이 주어진 접두사 중 하나로 시작하는지 대소문자 구분 없이 확인.
     * @param str 확인할 문자열
     * @param prefixes 접두사 목록
     * @return 하나라도 일치하면 true
     */
    public static boolean startsWithAnyIgnoreCase(final String str, final String... prefixes) {
        if (isEmpty(str) || prefixes == null || prefixes.length == 0) return false;
        for (final String prefix : prefixes) {
            if (str.toLowerCase().startsWith(prefix.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 두 문자열의 맨 앞에서부터 공통되는 부분을 추출.
     * <pre>
     * StringUtils.getCommonPrefix("abcde", "abxyz") = "ab"
     * StringUtils.getCommonPrefix("apple", "apply") = "appl"
     * </pre>
     * @param str1 첫 번째 문자열
     * @param str2 두 번째 문자열
     * @return 공통 접두사
     */
    public static String getCommonPrefix(final String str1, final String str2) {
        if (str1 == null || str2 == null) {
            return "";
        }
        int minLen = Math.min(str1.length(), str2.length());
        int i = 0;
        while (i < minLen && str1.charAt(i) == str2.charAt(i)) {
            i++;
        }
        return str1.substring(0, i);
    }

    //----- 문자 속성 판별 (Character Property Checks) -----

    /**
     * 문자열이 모두 대문자로 구성되었는지 확인.
     * @param cs 확인할 CharSequence
     * @return 모두 대문자이면 true
     */
    public static boolean isAllUpperCase(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isUpperCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 문자열이 모두 소문자로 구성되었는지 확인.
     * @param cs 확인할 CharSequence
     * @return 모두 소문자이면 true
     */
    public static boolean isAllLowerCase(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isLowerCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //----- 구조적 변형 (Structural Transformation) -----
    /**
     * 문자열을 주어진 폭(width)의 중앙에 위치시킴. 빈 공간은 공백으로 채움.
     * <pre>
     * StringUtils.centerOfSentence("a", 3)  = " a "
     * StringUtils.centerOfSentence("ab", 4) = " ab "
     * StringUtils.centerOfSentence("abc", 2)= "abc"
     * </pre>
     * @param str 중앙에 위치시킬 문자열
     * @param size 전체 폭
     * @return 중앙 정렬된 문자열
     */
    public static String centerOfSentence(String str, final int size) {
        return centerOfSentence(str, size, ' ');
    }

    /**
     * 문자열을 주어진 폭(width)의 중앙에 위치시킴. 빈 공간은 공백으로 채움.
     * <pre>
     * StringUtils.centerOfSentence("a", 3 , ' ')  = " a "
     * StringUtils.centerOfSentence("ab", 4, ' ') = " ab "
     * StringUtils.centerOfSentence("abc", 2, ' ')= "abc"
     * </pre>
     * @param str 중앙에 위치시킬 문자열
     * @param size 전체 폭
     * @param padChar 채울 문자
     * @return 중앙 정렬된 문자열
     */
    public static String centerOfSentence(String str, final int size, char padChar) {
        if (str == null || size <= 0) {
            return str;
        }
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        str = leftPad(str, strLen + pads / 2, padChar);
        str = rightPad(str, size, padChar);
        return str;
    }

    /**
     * [신규] 문자열의 끝에 있는 개행 문자(\n, \r, 또는 \r\n)를 **하나만** 제거.
     * 파일이나 네트워크 스트림에서 한 줄씩 읽을 때 유용.
     * <pre>
     * StringUtils.chomp("abc\n")  = "abc"
     * StringUtils.chomp("abc\r\n")= "abc"
     * StringUtils.chomp("abc")    = "abc"
     * </pre>
     * @param str 처리할 문자열
     * @return 개행 문자가 제거된 문자열
     */
    public static String chomp(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        // 문자열 길이가 1인 경우
        if (str.length() == 1) {
            char ch = str.charAt(0);
            if (ch == '\r' || ch == '\n') {
                return "";
            }
            return str;
        }
        // 문자열이 2이상인 경우
        int lastIdx = str.length() - 1;
        char lastCh = str.charAt(lastIdx);
        if (lastCh == '\n') {
            // 끝 \n뒤에 \r이 있으면 삭제 반환
            if (str.charAt(lastIdx - 1) == '\r') {
                return str.substring(0, lastIdx - 1);
            }
            return str.substring(0, lastIdx);
        } else if (lastCh == '\r') {
            return str.substring(0, lastIdx);
        }
        return str;
    }

    /**
     * 문자열의 가운데 부분을 축약하고 생략 부호로 대체.
     * <pre>
     * StringUtils.abbreviateMiddle("12345-67890-ABCDE", "-", 15)
     * // 결과: "12345-...-ABCDE"
     * </pre>
     * @param str 축약할 문자열
     * @param middle 생략 부호로 사용할 문자열
     * @param length 목표 길이
     * @return 가운데가 축약된 문자열
     */
    public static String abbreviateMiddle(final String str, final String middle, final int length) {
        if (isEmpty(str) || isEmpty(middle) || length >= str.length() || length < middle.length() + 2) {
            return str;
        }
        int targetStr = length - middle.length();
        int startOffset = targetStr / 2 + targetStr % 2;
        int endOffset = str.length() - targetStr /2;
        return str.substring(0, startOffset) + middle + str.substring(endOffset);
    }
    //----- 19. 데이터 변환 및 포매팅 (Data Conversion & Formatting) -----
}
