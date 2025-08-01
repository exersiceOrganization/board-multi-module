package com.example.common.utils;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public class DateUtilsTest {

    @Test
    void testGetCurrent(){
        Long currentTime = DateUtils.currentTimeMillis();
        log.debug("currentTimeMillis : {}", currentTime);
        assertNotNull(currentTime);

        String curDate = DateUtils.getCurrentDate();
        log.debug("getCurrentDate : {}", curDate);
        assertNotNull(curDate);

        String curYear = DateUtils.getCurrentYear();
        log.debug("getCurrentYear : {}", curYear);
        assertNotNull(curYear);

        String curMonth = DateUtils.getCurrentMonth();
        log.debug("getCurrentMonth : {}", curMonth);
        assertNotNull(curMonth);

        String curDay = DateUtils.getCurrentDay();
        log.debug("getCurrentDay : {}", curDay);
        assertNotNull(curDay);

        String curDatetime = DateUtils.getCurrentDatetime();
        log.debug("getCurrentDatetime : {}" , curDatetime);
        assertNotNull(curDatetime);

        String curDatetimemicro = DateUtils.getCurrentDatetimemicro();
        log.debug("getCurrentDatetimemicro : {}", curDatetimemicro);
        assertNotNull(curDatetimemicro);

        String curDatetimemilli = DateUtils.getCurrentDatetimemilli();
        log.debug("getCurrentDatetimemilli : {}", curDatetimemilli);
        assertNotNull(curDatetimemilli);

        String curDatetimemicrok = DateUtils.getCurrentDatetimemicrok();
        log.debug("getCurrentDatetimemicrok : {}", curDatetimemicrok);
        assertNotNull(curDatetimemicrok);

        String curTimeMilli = DateUtils.getCurrentTimeMilli();
        log.debug("getCurrentTimeMilli : {}", curTimeMilli);
        assertNotNull(curDatetimemilli);

        String curTimemicro = DateUtils.getCurrentTimemicro();
        log.debug("getCurrentTimemicro : {}", curTimemicro);
        assertNotNull(curTimemicro);

        Long curSecmilli = DateUtils.getCurrentSecmilli();
        log.debug("getCurrentSecmilli : {}", curSecmilli);
        assertNotNull(curSecmilli);

        Long curSecmicro = DateUtils.getCurrentSecmicro();
        log.debug("getCurrentSecmicro : {}", curSecmicro);
        assertNotNull(curSecmicro);
    }

    @Test
    void testIsValidYear(){
        log.debug("isValidYear : {}", DateUtils.isValidYear("2025"));
        log.debug("isValidDate : {}", DateUtils.isValidDate("20250725"));
        log.debug("isValidTime : {}", DateUtils.isValidTime("151800"));
    }

    @Test
    void testCompare(){
        // 첫번째 일자가 작으면:-1 / 같으면:0 / 크면:1
        log.debug("compareDate : {}", DateUtils.compareDate("20250724", "202050725"));
        log.debug("getDaysInMonth : {}", DateUtils.getDaysInMonth("2025", "7"));
        log.debug("getMonthsBetween : {}", DateUtils.getMonthsBetween("20240724", "20250724"));
        log.debug("getDaysBetween : {}", DateUtils.getDaysBetween("20240724", "20250724"));
    }

    @Test
    void testcalcDate(){
        log.debug("getQuarterYear : {}", DateUtils.getQuarterYear("7"));
        log.debug("getQuarterStartEndDate : {}", DateUtils.getQuarterStartEndDate("2025", "7", 2));
        log.debug("getWeekday : {}", DateUtils.getWeekday("2025", "7", "24"));
        log.debug("getWeekday : {}", DateUtils.getWeekday("20250724"));
        log.debug("addYear : {}", DateUtils.addYear("20250724", 3));
        log.debug("addMonth : {}", DateUtils.addMonth("20250724", 6));
        log.debug("addDay : {}", DateUtils.addDay("20250724", 10));
        log.debug("addHour : {}", DateUtils.addHour("170200", 7));
        log.debug("addMinute : {}", DateUtils.addMinute("170200", 58));
        log.debug("addSecond : {}", DateUtils.addSecond("170511", 50));
//        log.debug("addTime : {}", DateUtils.addTime("2025-07-24T17:05:00.001", 6, ChronoUnit.HOURS, true));
        log.debug("addTime : {}", DateUtils.addTime("20250724170500.001", 6, ChronoUnit.HOURS, true));
        log.debug("diffTime : {}", DateUtils.diffTime("2025-07-24T19:00:00.000", "2025-07-24T17:00:00.000"));
        log.debug("getLastDay : {}", DateUtils.getLastDay("20250724"));
        log.debug("getTimeByFormat : {}", DateUtils.getTimeByFormat("2025-07-24T17:49:00.000", "yyyy-MM-dd HH:mm:ss.SSS (E)"));
        log.debug("getEpochMilliFromFormatTime : {}", DateUtils.getEpochMilliFromFormatTime("2025-07-24 오후 06:10:12", "yyyy-MM-dd a hh:mm:ss"));
        log.debug("getTimeByEpochMilli : {}", DateUtils.getTimeByEpochMilli(1753348212000L));
        log.debug("getDate : {}", DateUtils.getDate("2025-07-24 06:18:22", "yyyy-MM-dd hh:mm:ss"));
        log.debug("getDate : {}", DateUtils.getDate(LocalDate.now(), "yyyy년 MM월 dd일 E요일"));
        log.debug("isAfter : {}", DateUtils.isAfter("20250724", "20250725"));
        log.debug("isBefore : {}", DateUtils.isBefore("20250724", "20250723"));
        log.debug("isBetween : {}", DateUtils.isBetween("20250724", "20240824", "20260724"));
        log.debug("getAge : {}", DateUtils.getAge(2000, 7, 24));
        log.debug("getKoreanAge : {}", DateUtils.getKoreanAge(2000));
    }

    @Test
    void testGetCurrentTimeFormat(){
        String curTimeFormat = DateUtils.getCurrentTimeByFormat(" yyyy년 MM월 dd일 E요일");
        log.debug("getCurrentTimeByFormat : {}", curTimeFormat);
    }
}