package com.portfolio.common.business.util;

import code.CaseType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@Slf4j
public class StringUtilsTest {
    @Test
    @DisplayName("기본검사")
    void basicInspection(){
        // given
        String testStr1 = "Hello World!!";
        String digitTest = "123qwe456";
        // when
        boolean isSpace1 = StringUtils.isSpace(testStr1);
        boolean isSpace2 = StringUtils.isSpace(testStr1, 5);
        boolean isEmpty = StringUtils.isEmpty(testStr1);
        boolean isNotEmpty = StringUtils.isNotEmpty(testStr1);
        boolean isBlank = StringUtils.isBlank(testStr1);
        boolean isNotBlank = StringUtils.isNotBlank(testStr1);
        boolean isDigit1 = StringUtils.isDigit(digitTest);
        boolean isDigit2 = StringUtils.isDigit(digitTest, 3);
        // then
        log.info("isSpace1 : {}", isSpace1);  // true
        log.info("isSpace2 : {}", isSpace2);  // false
        log.info("isEmpty : {}", isEmpty);  // false
        log.info("isNotEmpty : {}", isNotEmpty);  // true
        log.info("isBlank : {}", isBlank);  // false
        log.info("isNotBlank : {}", isNotBlank);  // true
        log.info("isDigit1 : {}", isDigit1);  // true
        log.info("isDigit2 : {}", isDigit2);  // true
    }

    @Test
    @DisplayName("기본값 처리")
    void defualtProcessing(){
        // given
        String testStr = "ABCDE";
        String defaultStr = "Test text";
        // when
        String nullToEmpty = StringUtils.nullToEmpty(null);
        String defaultIfEmpty1 = StringUtils.defaultIfEmpty(testStr, defaultStr);
        String defaultIfEmpty2 = StringUtils.defaultIfEmpty(null, defaultStr);
        String defaultIfBlank1 = StringUtils.defaultIfBlank(testStr, defaultStr);
        String defaultIfBlank2 = StringUtils.defaultIfBlank(null, defaultStr);
        
        // then
        log.info("nullToEmpty : {}", nullToEmpty);  // ""
        log.info("defaultIfEmpty1 : {}", defaultIfEmpty1);  // ABCDE
        log.info("defaultIfEmpty2 : {}", defaultIfEmpty2);  // Test text
        log.info("defaultIfBlank1 : {}", defaultIfBlank1);  // ABCDE
        log.info("defaultIfBlank2 : {}", defaultIfBlank2);  // Test text
        
    }
    
    @Test
    @DisplayName("공백 제거")
    void Trimming(){
        // given
        String str = "    trimTest    ";
        // when
        String trim = StringUtils.trim(str);
        String trimToNull = StringUtils.trimToNull("");
        String trimtoEmpty = StringUtils.trimtoEmpty(null);
        // then
        log.info("trim : {}", trim);    // trimTest
        log.info("trimToNull : {}", trimToNull);    // null
        log.info("trimtoEmpty : {}", trimtoEmpty);    // ""
    }

    @Test
    @DisplayName("비교")
    void Comparison(){
        // given
        String str = "onetwoya";
        String strEqual = "onetwoya";
        String strIgnoreCase = "OneTwoYa";
        String strNotEqual = "hanadulyap";

        // when
        boolean equals = StringUtils.equals(str, strEqual);
        boolean equalsIgnoreCase = StringUtils.equalsIgnoreCase(str, strIgnoreCase);
        boolean notEquals = StringUtils.equals(str, strNotEqual);
        // then
        log.info("equals : {}", equals);    // true
        log.info("equalsIgnoreCase : {}", equalsIgnoreCase);    // true
        log.info("notEquals : {}", notEquals);  // false
    }

    @Test
    @DisplayName("부분 문자열 추출")
    void Substring(){
        // given
        String testStr = "test.txt";
        // when, then
        log.info("substringBefore1 : {}", StringUtils.substringBefore(testStr, "."));    // test
        log.info("substringBefore2 : {}", StringUtils.substringBefore(testStr, "/"));    // test.txt
        log.info("substringAfter : {}", StringUtils.substringAfter(testStr, "."));    // test.
    }
    @Test
    @DisplayName("문자열 변형")
    void Manipulation(){
        // given
        String str = "abcdefghijklmnop12345";
        // when, then
        log.info("reverse : {}", StringUtils.reverse(str)); // ponmlkjihgfedcba
        log.info("abbreviate : {}", StringUtils.abbreviate(str, 9)); // abcdefg...
        log.info("getDigitOnly : {}", StringUtils.getDigitOnly(str)); // 12345

    }
    @Test
    @DisplayName("케이스 변환")
    void CaseConversion(){
        // given
        String snakeStr = "SNAKE_CASE_TEST";
        String camelStr = "camelCaseTest";
        String str = "convert case from space";
        // when, then
        log.info("capitalize : {}", StringUtils.capitalize(snakeStr));  // SNAKE_CASE_TEST
        log.info("toCamelCase : {}", StringUtils.snakeCaseToCamelCase(snakeStr));  // snakeCaseTest
        log.info("toSnakeCase : {}", StringUtils.camelCaseToSnakeCase(camelStr));  // camel_case_test
        log.info("convertCaseFromSpace1 : {}", StringUtils.convertCaseFromSpace(str, CaseType.CAMEL_CASE));  // convertCaseFromSpace
        log.info("convertCaseFromSpace2 : {}", StringUtils.convertCaseFromSpace(str, CaseType.SNAKE_CASE));  // convert_case_from_space

    }
    @Test
    @DisplayName("채우기 반복")
    void repeatAndPaddingTest(){
        // given
        String str = "test";
        int size = 10;
        char charPad = '_';
        // when
        String leftPad = StringUtils.leftPad(str, size, charPad);
        String rightPad = StringUtils.rightPad(str, size, charPad);

        // then
        log.info("leftPad : {}", leftPad);  // ______test
        log.info("rightPad : {}", rightPad);    // test______

    }
    @Test
    @DisplayName("마스킹")
    void maskTest(){
        // given
        String phone = "01012345678";
        String name = "곽철용";
        char maskChar = '#';
        // when
        String mask = StringUtils.mask(phone, 3, 7, maskChar);
        String maskName = StringUtils.maskName(name);

        // then
        log.info("mask : {}", mask);    // 010####5678
        log.info("maskName : {}", maskName);    // 곽*용
    }
    @Test
    @DisplayName("XSS 공격 방지")
    void securityTest(){
        // given
        String attack = "<script>alert('XXS')</script>";
        // when
        String escapeHtml = StringUtils.escapeHtml(attack);

        // then
        log.info("escapeHtml : {}", escapeHtml);    // &lt;script&gt&#39;alert(&#39;XXS&#39;)&lt;/script&gt&#39;

    }
    @Test
    @DisplayName("리벤슈타인거리(동일하게 되는 최소 편집 횟수)")
    void levenshteinDistanceTest(){
        // given
        String str1 = "sodapop";
        String str2 = "java";
        // when
        int levenshteinDist = StringUtils.levenshteinDistance(str1, str2);
        // then
        log.info("levenshteinDist : {}", levenshteinDist);  // 6
    }
    @Test
    @DisplayName("검색 및 확인")
    void SearchingAndChecking(){
        // given
        String str1 = "아도겐";
        String str2 = "켄의 어류겐";
        String search = "어류겐";
        String hasAlpha = "IwillFindYou";
        String discord = "난널찾을것이다";
        String hasAlphaNum = "oneTwoThree123";
        // when
        boolean containsIgnoreCase1 = StringUtils.containsIgnoreCase(str1, search);
        boolean containsIgnoreCase2 = StringUtils.containsIgnoreCase(str2, search);
        boolean isAlpha = StringUtils.isAlpha(hasAlpha);
        boolean isAlphaNumberic = StringUtils.isAlphaNumberic(hasAlphaNum);
        boolean isNot = StringUtils.isAlpha(discord);

        // then
        log.info("containsIgnoreCase1 : {}", containsIgnoreCase1);  // false
        log.info("containsIgnoreCase2 : {}", containsIgnoreCase2);  // true
        log.info("isAlpha : {}", isAlpha);  // true
        log.info("isAlphaNumberic : {}", isAlphaNumberic);  // true
        log.info("isNot : {}", isNot);  // false

    }
    @Test
    @DisplayName("접두/접미사 처리")
    void PrefixAndSuffix(){
        // given
        String nothing = null;
        String prefix = "여기까지접두사";
        String suffix = "접미사여기까지";
        String del = "여기까지";
        String wrapStr = "<>";
        // when, then
        log.info("removePrefix : {}", StringUtils.removePrefix(prefix, del));    // 접두사
        log.info("removeSuffix : {}", StringUtils.removeSuffix(suffix, del));    // 접미사
        log.info("Empty : {}", StringUtils.removeSuffix(nothing, del));  // null
        log.info("wrap : {}", StringUtils.wrap(prefix, wrapStr));  // <>여기까지접두사<>

    }
    @Test
    @DisplayName("메세지 포매팅")
    void msgFormatting(){
        // given, when
        String formating1 = StringUtils.format("이것은 {}입니다. {}", "포매팅 테스트", "테스트2");
        String formating2 = StringUtils.format("{}", "포매팅 테스트");

        // then
        log.info("formating1 : {}", formating1);    // 이것은 포매팅 테스트입니다. 테스트2
        log.info("formating2 : {}", formating2);    // 이것은 포매팅 테스트입니다. 테스트2

    }
    @Test
    @DisplayName("분리 및 분할")
    void SplittingAndSlicing(){
        // given
        String str = "a,b    ,,";
        String subStr = "abcdefghi";
        // when
        String[] split = StringUtils.split(str, ',');
        // then
        log.info("split : {}", Arrays.toString(split)); //[a, b    , , ]
        log.info("subStringLeft : {}", StringUtils.subStringLeft(subStr, 5)); // abcde
        log.info("subStringRight : {}", StringUtils.subStringRight(subStr, 5)); // efghi
        log.info("subStringMiddle : {}", StringUtils.subStringMiddle(subStr, 3, 3)); // def
    }
    @Test
    @DisplayName("고급 검색 및 비교")
    void advancedSearchingAndComparison(){
        // given
        String str1 = "first class";
        String str2 = "first class student";
        String[] pfs = {"first", "second", "third", "fourth"};
        // when, then
        log.info("startsWithAnyIgnoreCase : {}", StringUtils.startsWithAnyIgnoreCase(str1, pfs));   // true
        log.info("getCommonPrefix : {}", StringUtils.getCommonPrefix(str1, str2));  // first class
    }
    @Test
    @DisplayName("문자 속성 판별")
    void characterPropertyChecks(){
        // given
        String str = "character";
        // when, then
        log.info("isAllUpperCase : {}", StringUtils.isAllUpperCase(str));   // false
        log.info("isAllLowerCase : {}", StringUtils.isAllLowerCase(str));   // true
    }
    @Test
    @DisplayName("구조적 변형")
    void structuralTransformation(){
        // given
        String sentence = "abc";
        String chompStr = "abc\ndef";
        String contractStr = "12345-67890-ABCDE";
        // when, then
        log.info("centerOfSentence1 : {}", StringUtils.centerOfSentence(sentence, 10, '_'));    // ___abc____
        log.info("centerOfSentence2 : {}", StringUtils.centerOfSentence(sentence, 4));  // abc // def 개행됨
        log.info("chomp : {}", StringUtils.abbreviateMiddle(contractStr, "-", 15));    // abc
        log.info("chomp : {}", StringUtils.abbreviateMiddle(chompStr, "#", 4));    // abc


    }
}

