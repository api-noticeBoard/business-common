package com.portfolio.common.business.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

public class DateUtils {

    private static final long SECONDS_PER_MINUTE = 60;
    private static final long SECONDS_PER_HOUR = SECONDS_PER_MINUTE * 60;
    private static final long SECONDS_PER_DAY = SECONDS_PER_HOUR * 24;

    // 자주 사용되는 날짜/시간 포멧 상수 정의
    private static final DateTimeFormatter FLEXIBLE_MS_FORMAT = new DateTimeFormatterBuilder()
            // 1. 기본 날짜/시간 패턴을 추가합니다.
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            // 2. 선택적인 섹션을 시작합니다. 이 섹션은 없어도 파싱에 성공합니다.
            .optionalStart()
            // 3. 소수점 이하 초(나노초) 부분을 추가합니다.
            //    - ChronoField.NANO_OF_SECOND: 파싱할 필드는 나노초 단위입니다.
            //    - 1: 최소 1자리 숫자
            //    - 9: 최대 9자리 숫자
            //    - false: 소수점(.)은 이미 appendLiteral로 처리했으므로 여기서는 처리하지 않습니다.
            .appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true)
            // 4. 선택적인 섹션을 종료합니다.
            .optionalEnd()
            // 5. 지금까지 구성한 규칙으로 포맷터를 생성합니다.
            .toFormatter();
    private static final DateTimeFormatter MS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]");
    private static final DateTimeFormatter SEC_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter MIN_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter HOUR_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter YEAR_FORMAT = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    // 유틸리티 클래스는 인스턴스화를 방지하기 위해 private 생성자를 명시적으로 선언
    private DateUtils() {
        throw new UnsupportedOperationException("이 클래스는 인스턴스화 할 수 없습니다.");
    }
    /** ============================== 포멧 변환 시작 ============================== */
    /**
     * 밀리세컨드 표시
     * yyyy-MM-dd HH:mm:ss.SSS 형식으로 변환
     *
     * @param localDateTime
     * @return
     */
    public static String formatToDateTimeMill(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }

        return localDateTime.format(MS_FORMAT);
    }
    /**
     * 초까지 표시
     * yyyy-MM-dd HH:mm:ss 형식으로 변환
     *
     * @param localDateTime
     * @return
     */
    public static String formatToDateTimeSec(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }

        return localDateTime.format(SEC_FORMAT);
    }
    /**
     *  분까지 표시
     *  yyyy-MM-dd HH:mm 형식으로 변환
     *
     * @param localDateTime
     * @return
     */
    public static String formatToDateTimeMin(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }

        return localDateTime.format(MIN_FORMAT);
    }
    /**
     *  시간까지 표시
     *  yyyy-MM-dd HH 형식으로 변환
     *
     * @param localDateTime
     * @return
     */
    public static String formatToDateTimeHour(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }

        return localDateTime.format(HOUR_FORMAT);
    }
    /**
     * 날짜 표시
     * yyyy-MM-dd 형식으로 변환
     *
     * @param localDateTime
     * @return
     */
    public static String formatToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }

        return localDateTime.format(DATE_FORMAT);
    }
    /**
     * 달까지 표시
     * yyyy-MM 형식으로 변환
     *
     * @param localDateTime
     * @return
     */
    public static String formatToDateMonth(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }

        return localDateTime.format(MONTH_FORMAT);
    }
    /**
     * 년 표시
     * yyyy 형식으로 변환
     *
     * @param localDateTime
     * @return
     */
    public static String formatToDateYear(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }

        return localDateTime.format(YEAR_FORMAT);
    }
    /**
     * 시간 표시
     * HH:mm  형식으로 변환
     *
     * @param localDateTime
     * @return
     */
    public static String formatToTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }

        return localDateTime.format(TIME_FORMAT);
    }
    /** ============================== 포멧 변환 끝 ============================== */
    /**
     * UX(사용자 경험)를 향상을 위해 현재시간 기준 작성시간 비교
     * <pre>
     *     DateUtils.formatToRelativeTime(postDateTime);
     *     result : 30분 전
     * </pre>
     * @param postDateTime
     * @return
     */
    public static String formatToRelativeTime(LocalDateTime postDateTime) {
        // 파라미터값 없을 때
        if (postDateTime == null) {
            return "";
        }

        // 현재시간 기준 비교
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(postDateTime, now);
        long sec = duration.getSeconds();
        if (sec < SECONDS_PER_MINUTE) return "방금 전";  // 방금 전(1분 미만)
        if (sec < SECONDS_PER_HOUR ) return sec/SECONDS_PER_MINUTE + "분 전"; // X분 전
        if (sec >= SECONDS_PER_HOUR && sec < SECONDS_PER_DAY) return sec/(60 * 60) + "시간 전"; // X시간 전

        // 현재날짜 기준 비교
        LocalDate postDate = postDateTime.toLocalDate();
        LocalDate today = now.toLocalDate();
        Period period = Period.between(postDate, today);
        if (postDate.isEqual(today.minusDays(1))) return "어제";
        if (period.getDays() > 0) return period.getDays() + "일 전";
        if (period.getMonths() > 0) return period.getMonths() + "달 전";
        if (period.getYears() > 0) return period.getYears() + "년 전";

        return postDateTime.format(SEC_FORMAT);
    }

    /**
     * 문자열을 LocalDateTime 객체로 변환하는 메서드 (Parsing)
     * yyy-MM-dd HH:mm:ss.SSS 형식으로 String를 LocalDateTime 변환
     *
     * @param dateTimeString
     * @return
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        try {
            dateTimeString = dateTimeString.trim();
            LocalDateTime parsedDateTime = LocalDateTime.parse(dateTimeString, FLEXIBLE_MS_FORMAT);
            return parsedDateTime.truncatedTo(ChronoUnit.MILLIS);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * 문자열을 LocalDate 객체로 변환하는 메서드 (Parsing)
     * yyy-MM-dd 형식으로 String를 LocalDate 변환
     * @param dateTimeString
     * @return
     */
    public static LocalDate parseDate(String dateTimeString) {
        try {
            dateTimeString = dateTimeString.trim();
            LocalDateTime dateTime = parseDateTime(dateTimeString);
            if (dateTime != null) return dateTime.toLocalDate();
            return LocalDate.parse(dateTimeString, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * 해당 날짜의 자정을 LocalDateTime 리턴.
     *
     * @param date
     * @return
     */
    public static LocalDateTime getStartOfDay(LocalDate date) {
        if(date == null) return null;
        return date.atStartOfDay();
    }

    /**
     * 해당 날짜의 마지막을 시간을 리턴. (2025-09-12 23:59:59.999)
     * BETWEEN 구문의 끝 조건으로 자주 사용
     * @param date
     * @return
     */
    public static LocalDateTime getEndOfDay(LocalDate date) {
        if (date == null) return null;
        return date.atTime(LocalTime.MAX);
    }

    /**
     * 두 날짜 사이의 기간을 확인(윤년 등 정확한 일 수 계산)
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDayBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate ==  null) return 0;
        if (startDate.isAfter(endDate)) {
            // 시작 시간이 종료 시간보다 나중이면 위치를 바꿔서 계산
            return getDayBetween(endDate, startDate);
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 두 날짜 사이의 기간을 확인(윤년 등 정확한 일 수 계산)
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getTimeBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate ==  null) return 0;
        if (startDate.isAfter(endDate)) {
            // 시작 시간이 종료 시간보다 나중이면 위치를 바꿔서 계산
            return getTimeBetween(endDate, startDate);
        }
        return ChronoUnit.SECONDS.between(startDate, endDate);
    }

    /**
     * 비교 시간을 년, 개월, 일, 시간, 분, 초로 변환.
     *
     * @param totalSeconds
     * @return
     */
    public static String formatSecondsToDuration(long totalSeconds) {
        if (totalSeconds < 0) return "유효하지 않은 값";
        if (totalSeconds == 0) return "0초";

        // 1. 계산의 기준점이 될 시작 시간을 정의합니다. (Unix Epoch Time, UTC 기준)
        LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);

        // 2. 시작 시간에 주어진 초를 더해 종료 시간을 계산합니다.
        LocalDateTime endDateTime = startDateTime.plusSeconds(totalSeconds);

        // --- 이제 명확한 시작점과 끝점이 생겼으므로, 정확한 계산이 가능합니다. ---

        // 3. Period를 사용하여 '년, 월, 일'의 차이를 계산합니다.
        //    (LocalDate로 변환하여 날짜 부분만 비교)
        Period period = Period.between(startDateTime.toLocalDate(), endDateTime.toLocalDate());

        // 4. Duration을 사용하여 '시, 분, 초'의 차이를 계산합니다.
        //    (LocalTime으로 변환하여 시간 부분만 비교)
        Duration duration = Duration.between(startDateTime.toLocalTime(), endDateTime.toLocalTime());

        // 5. [엣지 케이스 처리] 시간 계산 결과가 음수일 경우 (예: 10:00 -> 09:00)
        //    날짜에서 하루를 빌려와서(빼서) 시간을 양수로 보정합니다.
        if (duration.isNegative()) {
            period = period.minusDays(1);
            duration = duration.plusDays(1);
        }

        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        int hours = duration.toHoursPart();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();

        // 6. 결과를 문자열로 조합합니다.
        StringBuilder result = new StringBuilder();
        if (years > 0) result.append(years).append("년 ");
        if (months > 0) result.append(months).append("개월 ");
        if (days > 0) result.append(days).append("일 ");
        if (hours > 0) result.append(hours).append("시간 ");
        if (minutes > 0) result.append(minutes).append("분 ");
        if (seconds > 0) result.append(seconds).append("초");

        String finalResult = result.toString().trim();
        return finalResult.isEmpty() ? "0초" : finalResult;
    }

    /** 영업일 계산 대체공휴일, 공휴일 계산 */
    /**
     * 해당 날짜가 주말인지 확인.
     * @param date
     * @return
     */
    public static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * 해당 날짜가 영업일(주말, 공휴일)인지 확인.
     * @param date
     * @param holidays
     * @return
     */
    public static boolean isBusinessDay(LocalDate date, Set<LocalDate> holidays) {
        Set<LocalDate> hoildaySet = (holidays != null) ? holidays : Set.of();

        if (isWeekend(date) || hoildaySet.contains(date)) {
            return false;
        }

        return true;
    }

    /**
     * 해당 날짜 이후의 가장 가까운 다음 영업일
     *
     * @param date
     * @param holidays
     * @return
     */
    public static LocalDate getNextBusinessDay(LocalDate date, Set<LocalDate> holidays) {
        LocalDate nextDay = date;
        while (!isBusinessDay(nextDay, holidays)) {
            nextDay = nextDay.plusDays(1);
        }

        return nextDay;
    }

    /** 주/월/분기 계산 */
    /**
     * 해당 날짜가 포함된 주의 시작일(월요일)반환
     *
     * @param date
     * @return
     */
    public static LocalDate getStartOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * 해당 날짜가 포함된 달의 마지막일 반환.
     *
     * @param date
     * @return
     */
    public static LocalDate getEndOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 해당 날짜가 포함된 분기의 시작일 반환(1,4,7,10월의 1일)
     *
     * @param date
     * @return
     */
    public static LocalDate getStartOfQuarter(LocalDate date) {
        int currentMonth = date.getMonthValue();                    // 현재 월 반환
        int firstMonthOfQuarter = ((currentMonth -1) / 3) *3 +1;    // 분기의 앞달 설정

        return LocalDate.of(date.getYear(), firstMonthOfQuarter, 1);
    }

    /**
     * 시간대 처리 LocalDateTime을 외국 시간대로 변환
     *
     * @param localDateTime
     * @param fromZone
     * @param toZone
     * @return
     */
    public static LocalDateTime convertTimeZone(LocalDateTime localDateTime, ZoneId fromZone, ZoneId toZone) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(fromZone);
        ZonedDateTime converted = zonedDateTime.withZoneSameInstant(toZone);

        return converted.toLocalDateTime();
    }

}
