package com.portfolio.common.business;

import com.portfolio.common.business.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

@Slf4j
public class DateUtilsTest {

    @Test
    void RelativeTimeTest(){
        // give
        String result = "";
        // 시간 계산
        LocalDateTime postDateTime = LocalDateTime.of(2025, 9,12, 14, 0);
        LocalDateTime now = LocalDateTime.now();

        Duration durationTime = Duration.between(postDateTime, now);
        long sec = durationTime.getSeconds();

        // when
        // 사용자 친화적 표시
//        if (sec < 60) result = "방금 전";  // 방금 전(1분 미만)
//        if (sec < (60 * 60)) result = sec/60 + "분 전"; // X분 전
//        if (sec >= (60 * 60) && sec < (60 * 60 * 24)) result = sec/(60 * 60) + "시간 전"; // X시간 전
//
//        // 날짜 계산
//        LocalDate postDate = postDateTime.toLocalDate();
//        LocalDate today = now.toLocalDate();
//
//        Period period = Period.between(postDate, today);
//
//        if (period.getDays() > 0) result = period.getDays() + "일 전";
//        if (period.getMonths() > 0) result = period.getMonths() + "달 전";
//        if (period.getYears() > 0) result = period.getYears() + "년 전";
        result = DateUtils.formatToRelativeTime(postDateTime);

        // then
        log.info("result : {}", result);
    }

    @Test
    void parsestringToDate(){

        String dateString = "2025-09-12 14:50:11";
        LocalDateTime parseString = DateUtils.parseDateTime(dateString);
        log.info("parseString : {}", parseString);

        LocalDateTime result = parseString.truncatedTo(ChronoUnit.MILLIS);
        log.info("result : {}", result);

        String formattedString = DateUtils.formatToDateTimeMill(parseString);
        log.info("formattedString : {}", formattedString);
    }

    @Test
    void betweenDate(){
        // give
        String startStr = "2025-09-10 12:11:11.1111";
        String endStr = "2025-09-12 14:50:11";

        LocalDate startDate = DateUtils.parseDate(startStr);
        LocalDate endDate = DateUtils.parseDate(endStr);

        LocalDateTime startTime = DateUtils.getStartOfDay(startDate);
        LocalDateTime endTime = DateUtils.getEndOfDay(endDate);

        log.info("startDate : {}", startDate);
        log.info("endDate : {}", endDate);
        log.info("startTime : {}", startTime);
        log.info("endTime : {}", endTime);

        // when
        long betweenDate = DateUtils.getDayBetween(startDate, endDate);
        Long betweenTime = DateUtils.getTimeBetween(startTime, endTime);

        long zero = 0L;
        // then
        log.info("betweenDate : {}", betweenDate);
        log.info("betweenTime : {}", betweenTime);

        log.info("formatSecondsToDuration : {}", DateUtils.formatSecondsToDuration(zero));

    }
}
