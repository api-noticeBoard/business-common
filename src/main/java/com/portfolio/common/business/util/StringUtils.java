package com.portfolio.common.business.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class StringUtils {

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

        if (checkStr == null) { return false; }
        if (checkStr.contains(" ") || checkStr.contains("\t")
        || checkStr.contains("\n") || checkStr.contains("\r")){
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

        if (checkStr == null || checkStr.length() < 1) { return true; }
        if (checkStr.substring(0, length).contains(" ") || checkStr.substring(0, length).contains("\t")
                || checkStr.substring(0, length).contains("\n") || checkStr.substring(0, length).contains("\r")){
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
        return cs == null | cs.length() == 0;
    }

    /**
     * 문자열이 유의미한 내용을 가지고 있는지 확인 (isEmpty의 반대).
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
        if (isEmpty(cs)) { return true; }

        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isWhitespace(cs.charAt(i))){ return false; }
        }
        return true;
    }

    /**
     * 문자열이 공백이 아닌 문자를 포함하고 있는지 확인 (isBlank의 반대).
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
        if (cs == null) { return chk; }
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isDigit(cs.charAt(i))){ return chk; }
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
     * @param cs 검사할 문장
     * @param length 검사할 길이
     * @return 숫자 포함하면 true
     */
    public static boolean isDigit(final CharSequence cs, int length) {
        boolean chk = false;
        if (cs == null) { return chk; }
        CharSequence ss = cs.subSequence(0, length);
        for (int i = 0; i < ss.length(); i++) {
            if (!Character.isDigit(ss.charAt(i))){ return chk; }
        }
        return true;
    }

    // TODO: isAlpha(String checkStr)

    // TODO: isAlpha(String checkStr, int length)

    // TODO: isLowerCase(String checkStr)

    // TODO: isLowerCase(String checkStr, int length)

    // TODO: isUpperCase(String checkStr)

    // TODO: isUpperCase(String checkStr, int length)

    // TODO: isAllZero(String checkStr, int length)

    // TODO: isHexNumStr(String checkStr, int nLen)

    // TODO: etLowerCase(String inputStr, int length)

    // TODO: getUpperCase(String inputStr)

    // TODO: getUpperCase(String inputStr, int length)

    // TODO: getFormatString(double num, String format)

    // TODO: concateString(String s1, String s2)

    // TODO:

    // TODO:

    //----- 기본값 처리 -----
    /**
     * 문자열이 null이면 빈 문자열("")을 반환하고, 아니면 원래 문자열을 반환.
     * NPE 방지 사용.
     * <pre>
     * StringUtils.nullToEmpty(null)     = ""
     * StringUtils.nullToEmpty("text")   = "text"
     * </pre>
     * @param str 확인할 문자열
     * @return null이 아닌 문자열
     */
    public static String nullToEmpty(final String str) {
        return str == null ? "" : str;
    }

    /**
     * 문자열이 비어있으면(Empty) 기본 문자열을, 아니면 원래 문자열을 반환.
     * @param str 확인할 문자열
     * @param defaultStr 기본값으로 사용할 문자열
     * @return 원래 문자열 또는 기본 문자열
     */
    public static String defaultIfEmpty(final String str, final String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    /**
     * 문자열이 공백이면(Blank) 기본 문자열을, 아니면 원래 문자열 반환.
     * @param str 확인할 문자열
     * @param defaultStr 기본값으로 사용할 문자열
     * @return 원래 문자열 또는 기본 문자열
     */
    public static String defaultIfBlank(final String str, final String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }
    //----- 공백 제거 (Trimming) -----
    /**
     * 문자열의 앞뒤 공백을 제거. `String.trim()`의 Null-Safe 버전.
     * @param str 공백을 제거할 문자열
     * @return 공백이 제거된 문자열 또는 null
     */
    public static String trim(final String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 문자열의 앞뒤 공백을 제거, 결과가 빈 문자열이 되면 null 반환.
     * DB에 저장 시 "" 대신 NULL을 넣고 싶을 때 유용.
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
     * @param str 공백을 제거할 문자열
     * @return 공백이 제거된 문자열. (null이 아님을 보장)
     */
    public static String trimtoEmpty(final String str) {
        return str == null ? "" : str.trim();
    }

    //----- 비교 (Comparison) -----
    /**
     * 두 문자열이 대소문자 구분 없이 동일한지 비교.(Null-Safe)
     * @param cs1 첫 번째 CharSequence
     * @param cs2 두 번째 CharSequence
     * @return 대소문자 구분 없이 두 문자열이 같으면 true
     */
    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) { return true; }
        if (cs1 == null || cs2 == null) { return false; }
        return cs1.toString().equals(cs2.toString());
    }

    public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) { return true; }
        if (cs1 == null || cs2 == null) { return false; }
        return cs1.toString().equalsIgnoreCase(cs2.toString());
    }

    //----- 부분 문자열 추출 (Substring) -----
    /**
     * [신규] 문자열에서 특정 구분자(separator)가 처음 나타나는 위치 이전의 부분 문자열을 반환.
     * <pre>
     * StringUtils.substringBefore("data.txt", ".")    = "data"
     * StringUtils.substringBefore("data.txt", "/")    = "data.txt" // 구분자 없을 때
     * </pre>
     * @param str 전체 문자열
     * @param separator 구분자
     * @return 구분자 이전의 부분 문자열
     */
    public static String substringBefore(final String str, final String separator) {
        if (isEmpty(str) || separator == null) { return str; }
        if (separator.isEmpty()) { return ""; }
        final int pos = str.indexOf(separator);
        if (pos == -1) { return str; }
        return str.substring(0, pos);
    }

    /**
     * [신규] 문자열에 특정 구분자(separator)가 처음 나타나는 위치 이후의 문자열을 반환합.
     * <pre>
     * StringUtils.substringAfter("data.txt", ".")     = "txt"
     * StringUtils.substringAfter("path/to/file", "/") = "to/file"
     * </pre>
     * @param str 전체 문자열
     * @param separator 구분자
     * @return 구분자 이후의 부분 문자열
     */
    public static String substringAfter(final String str, final String separator) {
        if (isEmpty(str)) { return str; }
        if (separator == null) { return ""; }
        final int pos = str.indexOf(separator);
        if (pos == -1) { return ""; }
        return str.substring(0, pos + separator.length());
    }

    //----- 문자열 변형 (Manipulation) -----
    /**
     * 문자열의 순서를 뒤집음.
     * @param str 뒤집을 문자열
     * @return 뒤집힌 문자열
     */
    public static String reverse(final String str) {
        if (str == null) { return null; }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 문자열을 주어진 최대 길이로 자르고, 길이가 길 경우 생략 부호(...)를 붙임.
     * 생략 부호는 maxWidth 길이에 포함.
     * @param str    축약할 문자열
     * @param maxWidth 최대 길이 (4 이상이어야 의미가 있음)
     * @return 축약된 문자열
     */
    public static String abbreviate(final String str, final int maxWidth) {
        if (str == null) {return null; }
        if (maxWidth < 4) { throw new IllegalArgumentException("Maximum width be at least 4"); }
        if (str.length() <= maxWidth) { return str; }
        return str.substring(0, maxWidth - 3) + "...";
    }

    /**
     * 문자열에서 숫자(0-9)만 추출하여 반환.
     * @param str 숫자만 추출할 문자열
     * @return 숫자만 포함된 문자열. 입력이 null이면 빈 문자열("")을 반환.
     */
    public static String getDigitOnly(final String str) {
        if (str == null) {return null; }
        final StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            final char c = str.charAt(i);
            if (Character.isDigit(c)) { sb.append(c); }
        }
        return sb.toString();
    }
}
