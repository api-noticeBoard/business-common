package com.portfolio.common.business.utils;

import com.portfolio.common.business.api.HolidayApiClient;
import com.portfolio.common.business.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Slf4j
@SpringBootTest // exclude 속성을 제거합니다.
@ActiveProfiles("test") // 'test' 프로필을 활성화하여 application-test.yml을 읽게 합니다.
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

    @Autowired
    private HolidayApiClient holidayApiClient;

    @Test
    @DisplayName("@SpringbootTest 환경 API 동작 및 영업일 확인 테스트")
    void isBusinessDayTest(){

        int year = 2025;
        LocalDate holidayDate = LocalDate.of(year, 8, 15); // 8월 15일 (광복절)
        // when: 실제 테스트할 메서드를 호출합니다.
        Set<LocalDate> holidays = holidayApiClient.getHoliday(year);
        boolean isBusinessDay = DateUtils.isBusinessDay(holidayDate, holidays);

        // then: 결과를 검증합니다.
        log.info("테스트 날짜: {}", holidayDate);
        log.info("API로부터 받은 공휴일: {}", holidays);
        log.info("영업일 여부: {}", isBusinessDay);
    }

    @Test
    @DisplayName("@SpringbootTest 환경 API 동작 및 다음 영업일 확인 테스트")
    void getNextBusinessDayTest(){
        int year = 2025;

        LocalDate holidayDate = LocalDate.of(year, 8, 15);
        Set<LocalDate> holidays = holidayApiClient.getHoliday(2025);
        LocalDate nextDay = DateUtils.getNextBusinessDay(holidayDate, holidays);

        log.info("holidayDate : {}", holidayDate);
        log.info("holidays : {}", holidays);
        log.info("nextDay : {}", nextDay);
    }

    @Test
    void getStartOfWeekTest() {
        int year = 2025;
        LocalDate date = LocalDate.of(year, 8, 15);
        LocalDate resultDay = DateUtils.getStartOfWeek(date);

        log.info("resultDay : {}",resultDay);
    }

    @Test
    void getEndOfMonthTest() {
        int year = 2025;
        LocalDate date = LocalDate.of(year, 8,15);
        LocalDate resultDay = DateUtils.getEndOfMonth(date);

        log.info("resultDay : {}",resultDay);
    }
    @Test
    void getStartOfQuarterTest() {
        int year = 2025;
        LocalDate date = LocalDate.of(year, 8,15);
        LocalDate resultDay = DateUtils.getStartOfQuarter(date);

        log.info("resultDay : {}",resultDay);
    }

    @Test
    void convertTimeZoneTest() {
//        LocalDateTime dateTime = LocalDateTime.of(2025, 8,15, 16, 55,14);
        LocalDateTime dateTime = LocalDateTime.now();
        ZoneId fromZone = ZoneId.of("Asia/Seoul");
        ZoneId toZone = ZoneId.of("America/New_York");
        LocalDateTime resultDay = DateUtils.convertTimeZone(dateTime, fromZone, toZone);

        log.info("resultDay : {}",resultDay);
    }
}
