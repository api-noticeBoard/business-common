package com.portfolio.common.business.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
public class NumberUtilsTest {

    @Test
    @DisplayName("통화 표시")
    void formatAsKoreanWon() {
        // given
        Locale locale = Locale.US;
        // when
        String result = NumberUtils.formatAsKoreanWon(BigDecimal.valueOf(123456789));
        String result2 = NumberUtils.formatAsKoreanWon(BigDecimal.valueOf(123456789), locale);
        // then
        log.info("result : {}", result);    // ₩123,456,789
        log.info("result2 : {}", result2);  // $123,456,789.00

    }
    @Test
    @DisplayName("문자를 숫자로 변환")
    void parseStringToBigDecimalTest(){
        BigDecimal result = NumberUtils.parseStringToBigDecimal("₩1,234,567.89");

        log.info("result : {}", result);    // 1234567.89
    }
    @Test
    @DisplayName("지정된 길이 만큼 왼쪽으 0으로 채움")
    void formatWithZeroPaddingTest() {
        // given, when
        String result = NumberUtils.formatWithZeroPadding(BigDecimal.valueOf(123), 5);
        // than
        log.info("result : {}", result);    // 00123
    }
    @Test
    @DisplayName("숫자 -> 한글 변환")
    void formatAsKoreaCurrencyTest(){
        // given, when
        String result = NumberUtils.formatAsKoreaCurrencyText(BigDecimal.valueOf(123456789));
        result = "금 " + result + " 원";
        // then
        log.info("result : {}", result);    // 금 일억이천삼백사십오만육천칠백팔십구 원
    }
    @Test
    @DisplayName("주어진 문자열이 유효한 숫자인지 확인")
    void isNumbericTest() {
        // given, when
        boolean result = NumberUtils.isNumberic("-123.45x");
        // then
        log.info("result : {}", result);    // false
    }
    @Test
    @DisplayName("해당 숫자가 0인지 확인")
    void isZeroTest() {
        boolean result = NumberUtils.isZero(BigDecimal.valueOf(0.00));
        log.info("result : {}", result);    // true
    }
    @Test
    @DisplayName("Bigdecimal null-safe 비교")
    void safeCompareTest() {
        // given, when
        int result = NumberUtils.safeCompare(BigDecimal.valueOf(1), BigDecimal.valueOf(1.1234));
        // then
        log.info("result : {}", result);    // -1
    }
    @Test
    @DisplayName("기준값 대비 변경값의 변화율을 백분율(%)로 계산")
    void calculatePercentageChangeTest() {
        // given
        int scale = 2;
        // when
        BigDecimal result = NumberUtils.calculatePercentageChange(BigDecimal.valueOf(10), BigDecimal.valueOf(7), scale);
        // then
        log.info("result : {}", result);    // -30.00
    }
    @Test
    @DisplayName("전체(total)에서 부분(part)이 차지하는 비중을 백분율(%)로 계산")
    void calculatePortionPercentageTest() {
        // given
        int scale = 2;
        // when
        BigDecimal result = NumberUtils.calculatePortionPercentage(BigDecimal.valueOf(6009.31), BigDecimal.valueOf(24095), scale);
        // then
        log.info("result : {}", result);    // 24.94
    }
    @Test
    @DisplayName("주어진 값을 백분율(%) 적용값 계산")
    void applyPercentageTest() {
        // given, when
        BigDecimal result = NumberUtils.applyPercentage(BigDecimal.valueOf(24095), BigDecimal.valueOf(24.94));
        // then
        log.info("result : {}", result);    // 6009.2930
    }
    @Test
    @DisplayName("숫자가 시작(start)과 끝(end) 범위 내에 있는지 확인")
    void isBetweenInclusiveTest() {
        // given, when
        boolean result = NumberUtils.isBetweenInclusive(BigDecimal.valueOf(20), BigDecimal.valueOf(0), BigDecimal.valueOf(20));
        // then
        log.info("result : {}", result);    // true
    }
    @Test
    @DisplayName("정수 추출")
    void getIntegerPartTest() {
        // given, when
        BigDecimal result = NumberUtils.getIntegerPart(BigDecimal.valueOf(12.345));
        // then
        log.info("result : {}", result);    // 12
    }
    @Test
    @DisplayName("현재 값중 최소값 반환")
    void collectionDataProcessingTest() {
        // given
        List<BigDecimal> numbers = List.of(
                new BigDecimal(10),
                new BigDecimal(50),
                new BigDecimal(100),
                new BigDecimal(1),
                new BigDecimal(-12),
                new BigDecimal("-12.345"),
                new BigDecimal("23.412"),
                new BigDecimal("1.2"),
                new BigDecimal(10),
                BigDecimal.ZERO
        );
        List<String> numbersStr = List.of(
                "10",
                "50",
                "100",
                "1",
                "-12",
                "-12.345",
                "23.412",
                "1.2",
                "10",
                "0"
        );
        // String to BigDecimal
        List<BigDecimal> numAsBigDecimal = numbersStr.stream().map(BigDecimal::new).collect(Collectors.toList());
        // when
        BigDecimal sum = NumberUtils.sum(numbers);
        BigDecimal average = NumberUtils.average(numbers, 5);
        BigDecimal min = NumberUtils.min(numbers);
        BigDecimal max = NumberUtils.max(numbers);
        // then
        log.info("sum : {}", sum);          // 171.267
        log.info("average : {}", average);  // 17.12670
        log.info("min : {}", min);          // -12.345
        log.info("max : {}", max);          // 100

    }
}
