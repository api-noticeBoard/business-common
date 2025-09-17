package com.portfolio.common.business.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
public class NumberUtilsTest {

    @Test
    @DisplayName("숫자 포매팅")
        void formatWithCommaTest(){
        // given
        Locale locale = Locale.US;
        // when
        String formatWithComma = NumberUtils.formatWithComma(BigDecimal.valueOf(1234567890));
        String formatAsKoreanWon1 = NumberUtils.formatAsKoreanWon(BigDecimal.valueOf(123456789));
        String formatAsKoreanWon2 = NumberUtils.formatAsKoreanWon(BigDecimal.valueOf(123456789), locale);
        String formatWithPattern = NumberUtils.formatWithPattern(BigDecimal.valueOf(123456789), "#,##0.00");
        BigDecimal parseStringToBigDecimal = NumberUtils.parseStringToBigDecimal("₩1,234,567.89");
        String formatWithZeroPadding = NumberUtils.formatWithZeroPadding(BigDecimal.valueOf(123), 5);
        String formatAsKoreaCurrencyText = NumberUtils.formatAsKoreaCurrencyText(BigDecimal.valueOf(123456789));
        formatAsKoreaCurrencyText = "금 " + formatAsKoreaCurrencyText + " 원";
        // then
        log.info("formatWithComma : {}", formatWithComma);    // 1,234,567,890
        log.info("formatAsKoreanWon1 : {}", formatAsKoreanWon1);    // ₩123,456,789
        log.info("formatAsKoreanWon2 : {}", formatAsKoreanWon2);  // $123,456,789.00
        log.info("formatWithPattern : {}", formatWithPattern);    // 123,456,789.00
        log.info("parseStringToBigDecimal : {}", parseStringToBigDecimal);    // 1234567.89
        log.info("formatWithZeroPadding : {}", formatWithZeroPadding);    // 00123
        log.info("formatAsKoreaCurrencyText : {}", formatAsKoreaCurrencyText);    // 금 일억이천삼백사십오만육천칠백팔십구 원
    }
    @Test
    @DisplayName("안전한 연산")
    void safeArithmeticTest(){
        // given, when
        BigDecimal safeAdd = NumberUtils.safeAdd(BigDecimal.valueOf(123.123), BigDecimal.valueOf(456.456));
        BigDecimal safeSubtract = NumberUtils.safeSubtract(BigDecimal.valueOf(4321.11), BigDecimal.valueOf(1234.11));
        BigDecimal safeMultiply = NumberUtils.safeMultiply(BigDecimal.valueOf(123), BigDecimal.valueOf(456));
        BigDecimal safeDivide1 = NumberUtils.safeDivide(BigDecimal.valueOf(120), BigDecimal.valueOf(20.4));
        BigDecimal safeDivide2 = NumberUtils.safeDivide(BigDecimal.valueOf(120), BigDecimal.valueOf(20.4), 2, RoundingMode.UP);
        // then
        log.info("safeAdd : {}", safeAdd);    // 579.579
        log.info("safeSubtract : {}", safeSubtract);    // 579.579
        log.info("safeMultiply : {}", safeMultiply);    // 579.579
        log.info("safeDivide1 : {}", safeDivide1);    // 579.579
        log.info("safeDivide2 : {}", safeDivide2);    // 579.579
    }
    @Test
    @DisplayName("안전한 비교")
    void safeComparisonTest() {
        // given, when
        boolean isNumberic = NumberUtils.isNumberic("-123.45x");
        boolean isZero = NumberUtils.isZero(BigDecimal.valueOf(0.00));
        int safeCompare = NumberUtils.safeCompare(BigDecimal.valueOf(1), BigDecimal.valueOf(1.1234));
        boolean isGreaterThan = NumberUtils.isGreaterThan(BigDecimal.valueOf(10), BigDecimal.valueOf(10));
        boolean isGreaterThanOrEqual = NumberUtils.isGreaterThanOrEqual(BigDecimal.valueOf(10), BigDecimal.valueOf(10));
        boolean isLessThan = NumberUtils.isLessThan(BigDecimal.valueOf(10), BigDecimal.valueOf(10));
        boolean isLessThanOrEqual = NumberUtils.isLessThanOrEqual(BigDecimal.valueOf(10), BigDecimal.valueOf(10));
        boolean isEqual = NumberUtils.isEqual(BigDecimal.valueOf(10.00), BigDecimal.valueOf(10.000));
        // then
        log.info("isNumberic : {}", isNumberic);    // false
        log.info("isZero : {}", isZero);    // true
        log.info("safeCompare : {}", safeCompare);    // -1
        log.info("isGreaterThan : {}", isGreaterThan);    // false
        log.info("isGreaterThanOrEqual : {}", isGreaterThanOrEqual);    // true
        log.info("isLessThan : {}", isLessThan);    // false
        log.info("isLessThanOrEqual : {}", isLessThanOrEqual);    // true
        log.info("isEqual : {}", isEqual);    // true
    }
    @Test
    @DisplayName("반올림, 절삭")
    void roundingAndTruncationTest(){
        // given
        int unit = 10;
        // when
        BigDecimal roundToUnit = NumberUtils.roundToUnit(BigDecimal.valueOf(123456), unit);
        BigDecimal floorToUnit = NumberUtils.floorToUnit(BigDecimal.valueOf(123456), unit);
        // then
        log.info("roundToUnit : {}", roundToUnit);    // 123460
        log.info("floorToUnit : {}", floorToUnit);    // 123450
    }
    @Test
    @DisplayName("비율, 백분율")
    void ratioAndPercentageTest() {
        // given
        int scale = 2;
        // when
        BigDecimal calculatePercentageChange = NumberUtils.calculatePercentageChange(BigDecimal.valueOf(10), BigDecimal.valueOf(7), scale);
        BigDecimal calculatePortionPercentage = NumberUtils.calculatePortionPercentage(BigDecimal.valueOf(6009.31), BigDecimal.valueOf(24095), scale);
        BigDecimal applyPercentage = NumberUtils.applyPercentage(BigDecimal.valueOf(24095), BigDecimal.valueOf(24.94));

        // then
        log.info("calculatePercentageChange : {}", calculatePercentageChange);    // -30.00
        log.info("calculatePortionPercentage : {}", calculatePortionPercentage);    // 24.94
        log.info("applyPercentage : {}", applyPercentage);    // 6009.2930

    }
    @Test
    @DisplayName("범위 확인")
    void rangeTest() {
        // given, when
        boolean isBetweenInclusive = NumberUtils.isBetweenInclusive(BigDecimal.valueOf(20), BigDecimal.valueOf(0), BigDecimal.valueOf(20));
        boolean isBetweenExclusive = NumberUtils.isBetweenExclusive(BigDecimal.valueOf(20), BigDecimal.valueOf(0), BigDecimal.valueOf(20));
        // then
        log.info("isBetweenInclusive : {}", isBetweenInclusive);    // true
        log.info("isBetweenExclusive : {}", isBetweenExclusive);    // false
    }
    @Test
    @DisplayName("추출, 기본값 처리")
    void extractionAndDefaultProcessingTest() {
        // given, when
        BigDecimal getIntegerPart = NumberUtils.getIntegerPart(BigDecimal.valueOf(12.345));
        BigDecimal getFractionalPart = NumberUtils.getFractionalPart(BigDecimal.valueOf(12.345));
        BigDecimal getOrDefault1 = NumberUtils.getOrDefault(null);
        BigDecimal getOrDefault2 = NumberUtils.getOrDefault(null, BigDecimal.valueOf(12.345));
        // then
        log.info("getIntegerPart : {}", getIntegerPart);    // 12
        log.info("getFractionalPart : {}", getFractionalPart);    // 0.345
        log.info("getOrDefault1 : {}", getOrDefault1);    // 0
        log.info("getOrDefault2 : {}", getOrDefault2);    // 12.345

    }
    @Test
    @DisplayName("컬렉션 데이터 처리")
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
