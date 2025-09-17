package com.portfolio.common.business.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        boolean isNotEmpty = StringUtils.isNotBlank(testStr1);
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
        String testStr = "Test text";
        // when
        String nullToEmpty = StringUtils.nullToEmpty(null);
        
        // then
        log.info("nullToEmpty : {}", nullToEmpty);  // ""
        
    }
    
    @Test
    @DisplayName("공백 제거")
    void Trimming(){
        // given
    
        // when
    
        // then
//        log.info("result : {}", result);
    }

    @Test
    @DisplayName("비교")
    void Comparison(){
        // given

        // when

        // then
//        log.info("result : {}", result);
    }

    @Test
    @DisplayName("부분 문자열 추출")
    void Substring(){
        // given

        // when

        // then
//        log.info("result : {}", result);
    }
}
