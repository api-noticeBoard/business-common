package com.portfolio.common.business.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ValidationUtilsTest {
    @Test
    @DisplayName("기본 유효성 검사")
    void basicValidationsTest() {
        // given

        // when, then
        log.info("isNull1 : {}", ValidationUtils.isNull(null)); // true
        log.info("isNull2 : {}", ValidationUtils.isNull(""));   // false
        log.info("isNotNull1 : {}", ValidationUtils.isNotNull(null));   // false
        log.info("isNotNull2 : {}", ValidationUtils.isNotNull("")); // true
        log.info("isEmpty : {}", ValidationUtils.isEmpty("")); // true
        log.info("isNotEmpty : {}", ValidationUtils.isNotEmpty("")); // false
        log.info("isBlank : {}", ValidationUtils.isBlank("")); // true
        log.info("isNotBlank : {}", ValidationUtils.isNotBlank("")); // false
        log.info("equals : {}", ValidationUtils.equals("", " ")); // false
        log.info("equalsIgnoreCase : {}", ValidationUtils.equalsIgnoreCase("a", "A")); // true
    }

    @Test
    @DisplayName("문자열 유효성 검사")
    void stringValidations() {
        // given
        String str = "pineapple123";
        // when, then
        log.info("isLength : {}", ValidationUtils.isLength(str, 3)); // true
        log.info("isMinLength : {}", ValidationUtils.isMinLength(str, 3)); // true
        log.info("isMaxLength : {}", ValidationUtils.isMaxLength(str, 10)); // true
        log.info("isLengthBetween : {}", ValidationUtils.isLengthBetween(str, 5, 9)); // true
        log.info("isNumeric : {}", ValidationUtils.isNumeric(str)); // true
        log.info("isAlpha : {}", ValidationUtils.isAlpha("abcd")); // true
        log.info("isAlphaNumeric : {}", ValidationUtils.isAlphaNumeric("1234abcd")); // true
        log.info("isEmail : {}", ValidationUtils.isEmail("abc@gmail.com")); // true
        log.info("isKorean : {}", ValidationUtils.isKorean("한글")); // true
        log.info("isKoreanWithConsonantAndVowel : {}", ValidationUtils.isKoreanWithConsonantAndVowel("ㅎㅏㄴㄱㅡㄹ")); // true
        log.info("isUrl : {}", ValidationUtils.isUrl("http://www.naver.com")); // true
        log.info("isIPv4 : {}", ValidationUtils.isIPv4("127.0.0.1")); // true
        log.info("startsWith : {}", ValidationUtils.startsWith(str, "pine")); // true
        log.info("endsWith : {}", ValidationUtils.endsWith(str, "apple")); // true
        log.info("contains : {}", ValidationUtils.contains(str, "pine")); // true
        log.info("isUpperCase : {}", ValidationUtils.isUpperCase(str)); // true
        log.info("isLowerCase : {}", ValidationUtils.isLowerCase(str)); // true
    }

    @Test
    @DisplayName("숫자 유효성 검사")
    void numericValidations() {
        // given
        int num = 14;
        String dateTimeStr = "2025-09-24 13:22:12";
        LocalDate now = LocalDate.now();
        LocalDateTime dateTimeNow = LocalDateTime.now();
        String phoneNum = "010-1234-1234";
        String regNo = "6078612034";
        String coRegNo = "123456789012";
        String ssn = "2509253123456";
        String[] strArr = {"A", "AB", "abc", "abcd"};
        List<String> strList = new ArrayList<>();
        strList.add("Apple");
        strList.add("Banana");
        strList.add("Cherry");
        strList.add(null);
        Map<Integer, String> strMap = Map.of(1, "Apple", 2, "Banana", 3, "Cherry");
        // when

        // then
        log.info("isPositive : {}", ValidationUtils.isPositive(num));
        log.info("isNegative : {}", ValidationUtils.isNegative(num));
        log.info("isZero : {}", ValidationUtils.isZero(num));
        log.info("isBetween : {}", ValidationUtils.isBetween(num, 1, 10));
        log.info("isEven : {}", ValidationUtils.isEven(num));
        log.info("isOdd : {}", ValidationUtils.isOdd(num));
        log.info("isPrime : {}", ValidationUtils.isPrime(num));
        log.info("isDate : {}", ValidationUtils.isDate(dateTimeStr, "yyyy-MM-dd"));
        log.info("isDateTime : {}", ValidationUtils.isDateTime(dateTimeStr, "yyyy-MM-dd HH:mm:ss"));
        log.info("isFuture_date : {}", ValidationUtils.isFuture(now));
        log.info("isFuture_dateTime : {}", ValidationUtils.isFuture(dateTimeNow));
        log.info("isPast_date : {}", ValidationUtils.isPast(now));
        log.info("isPast_dateTime : {}", ValidationUtils.isPast(dateTimeNow));
        log.info("isToday_date : {}", ValidationUtils.isToday(now));
        log.info("isLeapYear : {}", ValidationUtils.isLeapYear(2025));
        log.info("isMobilePhoneNumber : {}", ValidationUtils.isMobilePhoneNumber(phoneNum));
        log.info("isGeneralPhoneNumber : {}", ValidationUtils.isGeneralPhoneNumber(phoneNum));
        log.info("isBusinessRegistrationNumber : {}", ValidationUtils.isBusinessRegistrationNumber(regNo));
        log.info("isMobilePhoneNumber : {}", ValidationUtils.isMobilePhoneNumber(phoneNum));
        log.info("isCorporateRegistrationNumber : {}", ValidationUtils.isCorporateRegistrationNumber(coRegNo));
        log.info("isSsn : {}", ValidationUtils.isSsn(ssn));
        log.info("isCreditCardNumber : {}", ValidationUtils.isCreditCardNumber("1234567890123"));
        log.info("isStockCode : {}", ValidationUtils.isStockCode("035420"));
        log.info("isSecurePassword : {}", ValidationUtils.isSecurePassword("Openlabs609!@"));
        log.info("isNotEmpty_Arr : {}", ValidationUtils.isNotEmpty(strArr));
        log.info("isNotEmpty_Coll : {}", ValidationUtils.isNotEmpty(strList));
        log.info("isNotEmpty_Map : {}", ValidationUtils.isNotEmpty(strMap));
        log.info("containsNull : {}", ValidationUtils.containsNull(strList));
    }
    @Test
    @DisplayName("기타 유효성 검사")
    void miscellaneousValidations(){
        // given
        String[] allowed = {"jpg", "png", "gif"};
        // when, then
        log.info("isTrue : {}", ValidationUtils.isTrue(null));
        log.info("isFalse : {}", ValidationUtils.isFalse(Boolean.FALSE));
        log.info("isValidFileExtension : {}", ValidationUtils.isValidFileExtension("image.PNG", allowed));
    }
}
