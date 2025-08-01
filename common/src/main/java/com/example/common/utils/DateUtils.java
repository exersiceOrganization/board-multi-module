package com.example.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import com.example.common.constant.Constants;
import com.example.common.exception.CommonException;
import lombok.experimental.UtilityClass;

/**
 * date utility class
 */
@UtilityClass
public class DateUtils {
    private static final String SIMPLE_DATE_FORMAT = "yyyyMMdd";
    private static final String SIMPLE_TIME_FORMAT = "HHmmss";

    /**
     * 현재 시각을 1/1000 초(밀리세컨드) 단위로 반환한다.
     * <pre>
     * example> 1689932849722
     * </pre>
     *
     * @return 밀리세컨드 단위 현재시각
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 현재 날짜를 YYYYMMDD(8) 형태의 문자열로 변환한다.
     * <pre>
     * example> 20231130
     * </pre>
     *
     * @return 현재날짜
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        Date timeInDate = new Date();
        return sdf.format(timeInDate);
    }

    /**
     * 현재 날짜를 패턴 형태의 문자열로 변환한다.
     *
     * <p>
     * <h4>예시</h4>
     * <pre>
     * String date = DateUtils.getCurrentDate("yyyy-MM-dd");
     * System.out.println(date);
     *
     * Output :
     * 2023-11-01
     *
     * String date2 = DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss");
     * System.out.println(date);
     *
     * Output :
     * 2023-11-01 13:00:01
     *
     * String date3 = DateUtils.getCurrentDate("yyyy년 MM월 dd일 EEE요일");
     * System.out.println(date);
     *
     * Output :
     * 2023년 11월 01일 수요일
     * </pre>
     * </p>
     *
     * @see <a href="https://docs.oracle.com/javase/tutorial/i18n/format/simpleDateFormat.html">Customizing Formats</a>
     * @param pattern 날짜 패턴. <a href="https://docs.oracle.com/javase/tutorial/i18n/format/simpleDateFormat.html">참고 Docs</a>
     * @return 현재 날짜를 {@code pattern}에 맞게 변환한 문자열
     */
    public static String getCurrentDate(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date timeInDate = new Date();
        return sdf.format(timeInDate);
    }

    /**
     * 현재 년도를 문자열로 변환한다.
     * <pre>
     * example> 2023
     * </pre>
     *
     * @return 현재년도
     */
    public static String getCurrentYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date timeInDate = new Date();
        return sdf.format(timeInDate);
    }

    /**
     * 현재 달을 문자열로 변환한다.
     * <pre>
     * example> 11
     * </pre>
     *
     * @return 현재달
     */
    public static String getCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date timeInDate = new Date();
        return sdf.format(timeInDate);
    }

    /**
     * 현재 일을 문자열로 변환한다.
     * <pre>
     * example> 30
     * </pre>
     *
     * @return 현재일자
     */
    public static String getCurrentDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date timeInDate = new Date();
        return sdf.format(timeInDate);
    }

    /**
     * 현재 날짜/시간을 YYYYMMDDhh24miss(14) 형태의 문자열로 변환한다.
     * <pre>
     * example> 20231130160841
     * </pre>
     *
     * @return 현재일시 YYYYMMDDhh24miss(14)
     */
    public static String getCurrentDatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date timeInDate = new Date();
        return sdf.format(timeInDate);
    }

    /**
     * 현재 일시를 YYYYMMDDhh24miss(14)+microsecond(6) 형태의 문자열로 변환한다.
     * <pre>
     * example> 20231130160939366230
     * </pre>
     *
     * @return 현재 일시 YYYYMMDDhh24miss(14)+microsecond(6)
     */
    public static String getCurrentDatetimemicro() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date timeInDate = new Date();
        String timeInFormat = sdf.format(timeInDate);

        // nano second = 1,000,000,000분의 1초
        double nanoTime = (double) System.nanoTime() / 1000000000; // 1초 단위로 변환
        long milliTime = (long) ((nanoTime - (long) (nanoTime)) * 1000000);

        timeInFormat = timeInFormat + String.format("%06d", milliTime);

        return timeInFormat;
    }

    /**
     * 현재 일시를 YYYYMMDDhh24miss(14)+millisecond(3) 형태의 문자열로 변환한다
     * <pre>
     * example> 20231130161044644
     * </pre>
     *
     * @return 현재 일시 YYYYMMDDhh24miss(14)+millisecond(3)
     */
    public static String getCurrentDatetimemilli() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date timeInDate = new Date();
        return sdf.format(timeInDate);
    }

    /**
     * 현재 일시를 YYYY-MM-DD hh24:mi:ss,microsecond (23) 형태의 문자열로 변환한다.
     * <pre>
     * example> 2023-11-30 16:11:27,001536
     * </pre>
     *
     * @return 현재 일시 YYYY-MM-DD hh24:mi:ss,microsecond (23)
     */
    public static String getCurrentDatetimemicrok() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,");
        Date timeInDate = new Date();
        String timeInFormat = sdf.format(timeInDate);

        // nano second = 1,000,000,000분의 1초
        double nanoTime = (double) System.nanoTime() / 1000000000; // 1초 단위로 변환
        long milliTime = (long) ((nanoTime - (long) (nanoTime)) * 1000000);

        timeInFormat = timeInFormat + String.format("%06d", milliTime);

        return timeInFormat;
    }

    /**
     * 현재 시각을 HHMMSS(6) 형태의 문자열로 변환한다
     * <pre>
     * example> 161222
     * </pre>
     *
     * @return 현재 시각 HHMMSS(6)
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_TIME_FORMAT);
        Date timeInDate = new Date();
        return sdf.format(timeInDate);
    }

    /**
     * 현재 시각을 HHmmssSSS(9) 형태의 문자열로 변환한다
     * {@link java.time.format.DateTimeFormatter}
     * <pre>
     * example> 161322204
     * </pre>
     *
     * @return 현재 시각 HHmmssSSS(9)
     */
    public static String getCurrentTimeMilli() {
        try {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmssSSS"));
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 현재 시각을 지정한 format 형태의 문자열로 변환한다
     * {@link java.time.format.DateTimeFormatter}
     *
     * <p>
     * <h4>example</h4>
     * <pre>
     * String date = DateUtils.getCurrentTimeByFormat("yyyy-MM-dd");
     *
     * Output :
     * 2023-11-01
     *
     * String date2 = DateUtils.getCurrentTimeByFormat("yyyy-MM-dd HH:mm:ss");
     * System.out.println(date);
     *
     * Output :
     * 2023-11-01 13:00:01
     *
     * String date3 = DateUtils.getCurrentTimeByFormat("yyyy년 MM월 dd일 EEE요일");
     * System.out.println(date);
     *
     * Output :
     * 2023년 11월 01일 수요일
     * </pre>
     * </p>
     *
     * @param format    변환할 format
     * @return 지정한 format 형태의 현재 시각
     */
    public static String getCurrentTimeByFormat(String format) {
        try {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 현재 시각을 HHMMSS(6)+microsecond(6) 형태의 문자열로 변환한다.
     * <pre>
     * example> 163304372925
     * </pre>
     *
     * @return 현재 시각 HHMMSS(6)+microsecond(6)
     */
    public static String getCurrentTimemicro() {
        SimpleDateFormat sdf = new SimpleDateFormat(SIMPLE_TIME_FORMAT);
        Date timeInDate = new Date();
        String timeInFormat = sdf.format(timeInDate);

        // nano second = 1,000,000,000분의 1초
        double nanoTime = (double) System.nanoTime() / 1000000000; // 1초 단위로 변환
        long milliTime = (long) ((nanoTime - (long) (nanoTime)) * 1000000);

        timeInFormat = timeInFormat + String.format("%06d", milliTime);

        return timeInFormat;
    }

    /**
     * 현재 시각의 millisecond를 반환한다.
     * <pre>
     * example> 739
     * </pre>
     *
     * @return 현재 시각 millisecond
     */
    public static Long getCurrentSecmilli() {
        SimpleDateFormat sdf = new SimpleDateFormat("SSS");
        Date timeInDate = new Date();
        String timeInFormat = sdf.format(timeInDate);

        return Long.parseLong(timeInFormat);
    }

    /**
     * 현재 시각의 microsecond를 반환한다.
     * <pre>
     * example> 853518
     * </pre>
     *
     * @return 현재 시각 microsecond
     */
    public static Long getCurrentSecmicro() {
        String timeInFormat = "";

        // nano second = 1,000,000,000분의 1초
        double nanoTime = (double) System.nanoTime() / 1000000000; // 1초 단위로 변환
        long milliTime = (long) ((nanoTime - (long) (nanoTime)) * 1000000);

        timeInFormat = timeInFormat + String.format("%06d", milliTime);

        return Long.parseLong(timeInFormat);
    }

    /**
     * 입력 년도가 1900년 이후의 4자리수의 적절한 연도인지 검사한다.
     * <pre>
     * example> DateUtils.isValidYear("2023")
     * </pre>
     *
     * @param year  체크할 년도
     * @return 입력값이 유효한 연도인지 여부
     */
    public static boolean isValidYear(String year) {
        boolean result = true;

        if (null == year) {
            return false;
        }

        if (4 != year.length()) {
            return false;
        }

        for (int i = 0; i < year.length(); i++) {
            // 숫자 check
            if (!Character.isDigit(year.charAt(i))) {
                return false;
            }
        }

        Integer iyear = Integer.parseInt(year);

        if (1900 > iyear || iyear > 9999) {
            return false;
        }

        return result;
    }

    /**
     * 날짜(YYYYMMDD)의 정합성을 검사한다. 년도는 1900년 이후여야 한다.
     * <pre>
     * example> DateUtils.isValidDate("20231130")
     * </pre>
     *
     * @param date  체크할 날짜
     * @return 입력값이 유효한 날짜인지 여부
     */
    public static boolean isValidDate(String date) {
        boolean result = true;

        if (null == date) {
            return false;
        }

        if (8 != date.length()) {
        return false;
        }

        for (int i = 0; i < date.length(); i++) {
            if (!Character.isDigit(date.charAt(i))) {
                return false;
            }
        }

        try{
            LocalDate.parse(date, DateTimeFormatter.ofPattern(SIMPLE_DATE_FORMAT));
        }catch (DateTimeParseException e){
            return false;
        }

//        Integer iyear = Integer.parseInt(date.substring(0, 4));
//        Integer imon = Integer.parseInt(date.substring(4, 6));
//        Integer iday = Integer.parseInt(date.substring(6, 8));
//
//        // 윤년 처리로직
//        if (iyear < 1900 || imon < 1 || imon > 12 || iday < 1) {
//            return false;
//        }
//        if ((iyear % 400 == 0 || ((iyear % 100 != 0) && iyear % 4 == 0)) && imon == 2 && iday == 29) {
//            return result;
//        }
//
//        int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
//
//        if (iday > daysOfMonth[imon - 1]) {
//            return false;
//        }
//

        return result;
    }

    /**
     * 시간(HHMMSS)의 정합성을 검사한다.
     * <pre>
     * example> DateUtils.isValidTime("163059")
     * </pre>
     *
     * @param time  체크할 시간
     * @return 입력값이 유효한 시간인지 여부
     */
    public static boolean isValidTime(String time) {
        boolean result = true;

        if (null == time) {
            return false;
        }

        if (6 != time.length()) {
            return false;
        }

        for (int i = 0; i < time.length(); i++) {
            if (!Character.isDigit(time.charAt(i))) {
                return false;
            }
        }

        try{
            LocalTime.parse(time, DateTimeFormatter.ofPattern(SIMPLE_TIME_FORMAT));
        }catch(DateTimeParseException e){
           return false;
        }
//        Integer ihour = Integer.parseInt(time.substring(0, 2));
//        Integer imin = Integer.parseInt(time.substring(2, 4));
//        Integer isec = Integer.parseInt(time.substring(4, 6));
//
//        if (imin < 0 || imin > 59 || isec < 0 || isec > 59 || ihour < 0 || ihour > 23) {
//            return false;
//        }

        return result;
    }

    /**
     * 두 일자의 선후 비교하여 반환하는 함수
     * <pre>
     * example> DateUtils.compareDate("19810904", "20231130")
     * </pre>
     *
     * @param d1    비교할 일자1
     * @param d2    비교할 일자2
     * @return ( 첫번째 일자가 작으면:-1 / 같으면:0 / 크면:1 )
     */
    public static int compareDate(String d1, String d2) {
        int result = 0;

        try {
            if (!(DateUtils.isValidDate(d1) || DateUtils.isValidDate(d2))) {
                throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
            }

            SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
            Date firstDate = format.parse(d1);
            Date secondDate = format.parse(d2);

            long calDate = firstDate.getTime() - secondDate.getTime();

            if (0 == calDate) {
                result = 0;
            } else if (0 < calDate) {
                result = 1;
            } else if (0 > calDate) {
                result = -1;
            }
        } catch (ParseException | RuntimeException ex) {
            // 예외처리
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력한 년도와 월에 해당하는 월의 일수(28~31)를 반환한다.
     * <pre>
     * example> DateUtils.getDaysInMonth("2023", "11")
     * </pre>
     *
     * @param year  년도
     * @param month 월
     * @return 월의 일수
     */
    public static int getDaysInMonth(String year, String month) {
        int result = 0;

        // 입력값 체크
        try {
            // 연도 유효체크
            if (!DateUtils.isValidYear(year)) {
                throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
            }

            // 월 유효체크
            if (0 >= Integer.parseInt(month) && Integer.parseInt(month) > 12) {
                throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
            }
        } catch (RuntimeException ex) {
            // 예외처리
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        Calendar cld = Calendar.getInstance();
        cld.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);

        result = cld.getActualMaximum(Calendar.DAY_OF_MONTH);

        return result;
    }

    /**
     * 두 날짜 사이의 개월 수를 계산한다. (종료일자, 시작일자)
     * <pre>
     * example>
     *      DateUtils.getMonthsBetween("20231130", "19810904")
     *      result : 506.83870967741933
     * </pre>
     *
     * @param endDate 종료일자
     * @param startDate 시작일자
     * @return 두일자 사이 개월수
     */
    public static double getMonthsBetween(String endDate, String startDate) {
        // 파라미터 유효체크
        if (null == endDate || null == startDate || !(DateUtils.isValidDate(endDate) || DateUtils.isValidDate(startDate))) {
            throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
        }

        // 변수 설정
        int iyear1 = Integer.parseInt(endDate.substring(0, 4));
        int imonth1 = Integer.parseInt(endDate.substring(4, 6));
        int iday1 = Integer.parseInt(endDate.substring(6, 8));

        int iyear2 = Integer.parseInt(startDate.substring(0, 4));
        int imonth2 = Integer.parseInt(startDate.substring(4, 6));
        int iday2 = Integer.parseInt(startDate.substring(6, 8));

        int idate1 = Integer.parseInt(endDate);
        int idate2 = Integer.parseInt(startDate);

        int date1Days = DateUtils.getDaysInMonth(endDate.substring(0, 4), endDate.substring(4, 6));
        int date2Days = DateUtils.getDaysInMonth(startDate.substring(0, 4), startDate.substring(4, 6));

        if ((iday1 == date1Days && iday2 == date2Days) || (iday1 == iday2)) {
            return ((iyear1 - iyear2) * 12 + (imonth1 - imonth2));
        }

        double ddays;
        String tmpDate = "";

        if (idate1 >= idate2) {
            ddays = iday1;

            tmpDate = String.format("%04d%02d%02d", iyear2, imonth2, iday1);

            if (DateUtils.isValidDate(tmpDate)) {
                ddays = date2Days;
            }

            ddays -= iday2;
        } else {
            ddays = iday2;

            tmpDate = String.format("%04d%02d%02d", iyear1, imonth1, iday2);

            if (DateUtils.isValidDate(tmpDate)) {
                ddays = date1Days;
            }
            ddays = iday1 - ddays;
        }
        return (ddays / 31.0) + (iyear1 - iyear2) * 12 + (imonth1 - imonth2);
    }

    /**
     * 두 날짜 사이의 일 수를 계산한다.
     * <pre>
     * example>
     *      DateUtils.getDaysBetween("20231130", "19810904")
     *      result : 15427
     * </pre>
     *
     * @param endDay 종료일자
     * @param startDay 시작일자
     * @return 두 일자 사이의 일수
     */
    public static int getDaysBetween(String endDay, String startDay) {
        long calDateDays = 0;
        long calDate = 0;
        int result = 0;

        try {
            // 날짜 유효체크
            if (!DateUtils.isValidDate(endDay) || !DateUtils.isValidDate(startDay)) {
                throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
            }

            SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
            Date firstDate = format.parse(endDay);
            Date secondDate = format.parse(startDay);

            // milliseconds 단위로 변환하여 계산
            calDate = firstDate.getTime() - secondDate.getTime();
            calDateDays = calDate / (24 * 60 * 60 * 1000);

            result = (int) calDateDays;
        } catch (ParseException | RuntimeException ex) {
            // 예외처리
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력한 월이 몇 분기(1~4) 인지를 반환한다.
     * <pre>
     * example>
     *      DateUtils.getQuarterYear("11")
     *      result : 4
     * </pre>
     *
     * @param month 입력월
     * @return 두 일자 사이의 분기수
     */
    public static int getQuarterYear(String month) {
        int imonth = Integer.parseInt(month);
        int result = 0;

        // 월 유효체크
        if (0 >= Integer.parseInt(month) && Integer.parseInt(month) > 12) {
            throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
        }

        // 1 : 1~3
        if (0 < imonth && imonth <= 3) {
            result = 1;
        }
        // 2 : 4~6
        else if (3 < imonth && imonth <= 6) {
            result = 2;
        }
        // 3 : 7~9
        else if (6 < imonth && imonth <= 9) {
            result = 3;
        }
        // 4 : 10~12
        else if (9 < imonth && imonth <= 12) {
            result = 4;
        }

        return result;
    }

    /**
     * 입력한 월이 속한 분기(1~4)의 첫 번째 날 또는 마지막 날을 반환한다.
     * <pre>
     * example>
     *      DateUtils.getQuarterStartEndDate("2023", "12", 1)
     *      result : 20231001
     *
     *      DateUtils.getQuarterStartEndDate("2023", "12", 2)
     *      result : 20231231
     * </pre>
     *
     * @param year  년
     * @param month 월
     * @param kind  (1:시작날짜 2:끝일자)
     * @return 입력한 월이 속한 분기(1~4)의 첫 번째 날 또는 마지막 날
     */
    public static String getQuarterStartEndDate(String year, String month, int kind) {
        String result = null;
        String mon = null;
        // 분기 check
        int iquater = getQuarterYear(month);

        try {
            if (1 == kind) {
                // 시작 날짜
                iquater = (iquater - 1) * 3 + 1; // 분기시작월

                mon = String.format("%02d", iquater);

                result = year + mon + "01";
            } else if (2 == kind) {
                // 끝 날짜
                iquater = iquater * 3; // 분기종료월

                mon = String.format("%02d", iquater);

                result = year + mon + String.valueOf(DateUtils.getDaysInMonth(year, mon));
            }
        } catch (RuntimeException ex) {
            // 예외처리
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력한 일자가 몇번째 요일인지를 반환한다
     * <pre>
     * example>
     *      DateUtils.getWeekday("2023", "12", "1")
     *      result : 5
     * </pre>
     *
     * @param year  년
     * @param month 월
     * @param day   일
     * @return (0: 일요일 … 6: 토요일)
     */
    public static int getWeekday(String year, String month, String day) {
        // 1자리 숫자에 0이 없으면 붙힘
        if (month.length() == 1)    month = "0" + month;
        if (day.length() == 1)  day = "0" + day;

        String date = year + month + day;
        int dayNum = 0;

        try {
            // 연도 유효체크
            if (!DateUtils.isValidYear(year)) {
                throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
            }

            // 월 유효체크
            if (0 >= Integer.parseInt(month) || Integer.parseInt(month) > 12) {
                throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
            }

            // 일자 유효체크
            if (0 >= Integer.parseInt(day) || Integer.parseInt(day) > DateUtils.getDaysInMonth(year, month)) {
                throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
            Date nDate = dateFormat.parse(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(nDate);

            dayNum = cal.get(Calendar.DAY_OF_WEEK) - 1;
        } catch (ParseException | RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return dayNum;
    }

    /**
     * 입력한 일자가 몇번째 요일인지를 반환한다
     * <pre>
     * example>
     *      DateUtils.getWeekday("20231201")
     *      result : 5
     * </pre>
     *
     * @param date  일자
     * @return (0: 일요일 … 6: 토요일)
     */
    public static int getWeekday(String date) {
        int dayNum = 0;

        try {
            // 날짜 유효체크
            if (!DateUtils.isValidDate(date)) {
                throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
            Date nDate = dateFormat.parse(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(nDate);

            dayNum = cal.get(Calendar.DAY_OF_WEEK) - 1;
        } catch (ParseException | RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return dayNum;
    }

    /**
     * 입력한 일자(date)보다 몇 년(year) 후의 일자(dest)를 계산한다
     * <pre>
     * example>
     *      DateUtils.addYear("20231130", 3)
     *      result : 20261130
     * </pre>
     *
     * @param date  일자
     * @param year  계산할 년수
     * @return 계산된 일자
     */
    public static String addYear(String date, int year) {
        String result = null;

        try {
            SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);

            Calendar cal = Calendar.getInstance();
            Date ddate = format.parse(date);
            cal.setTime(ddate);

            cal.add(Calendar.YEAR, year);

            result = format.format(cal.getTime());
        } catch (ParseException | RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력한 일자(date)보다 몇 개월(month) 후의 일자(dest)를 계산한다.
     * <pre>
     * example>
     *      DateUtils.addMonth("20231130", 3)
     *      result : 20240229
     * </pre>
     *
     * @param date  일자
     * @param month 계산할 월
     * @return 계산된 일자
     */
    public static String addMonth(String date, int month) {
        String result = null;

        try {
            SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);

            Calendar cal = Calendar.getInstance();
            Date ddate = format.parse(date);
            cal.setTime(ddate);

            cal.add(Calendar.MONTH, month);

            result = format.format(cal.getTime());
        } catch (ParseException | RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력한 일자(date)보다 몇 일(day) 후의 일자(dest)를 계산한다.</br>
     * 입력한 일자는 'yyyyMMdd' format 이여하 하고,
     * 결과값도 'yyyyMMdd' format 으로 리턴된다.
     * <pre>
     * example>
     *      DateUtils.addDay("20231130", 89)
     *      result : 20240227
     * </pre>
     *
     * @param date  일자
     * @param day   계산 일수
     * @return 계산된 일자
     */
    public static String addDay(String date, int day) {
        String result = null;

        try {
            SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);

            Calendar cal = Calendar.getInstance();
            Date ddate = format.parse(date);
            cal.setTime(ddate);

            cal.add(Calendar.DATE, day);

            result = format.format(cal.getTime());
        } catch (ParseException | RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력한 시간(time)보다 몇 시간(hour) 후의 시간(dest)을 계산한다.</br>
     * 입력한 일자는 'HHmmss' format 이여하 하고,
     * 결과값도 'HHmmss' format 으로 리턴된다.
     * <pre>
     * example>
     *      DateUtils.addHour("172956", 30)
     *      result : 232956
     * </pre>
     *
     * @param time  시간
     * @param hour  계산할 시간
     * @return 계산된 시간
     */
    public static String addHour(String time, int hour) {
        String result = null;

        try {
            SimpleDateFormat format = new SimpleDateFormat(SIMPLE_TIME_FORMAT);

            Calendar cal = Calendar.getInstance();
            Date ddate = format.parse(time);
            cal.setTime(ddate);

            cal.add(Calendar.HOUR, hour);

            result = format.format(cal.getTime());
        } catch (ParseException | RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력한 시간(time)보다 몇 분(minute) 후의 시간(dest)을 계산한다.</br>
     * 입력한 일자는 'HHmmss' format 이여하 하고,
     * 결과값도 'HHmmss' format 으로 리턴된다.
     * <pre>
     * example>
     *      DateUtils.addMinute("172956", 30)
     *      result : 175956
     * </pre>
     *
     * @param time  시간
     * @param min   계산할 분
     * @return 계산된 시각
     */
    public static String addMinute(String time, int min) {
        String result = null;

        try {
            SimpleDateFormat format = new SimpleDateFormat(SIMPLE_TIME_FORMAT);

            Calendar cal = Calendar.getInstance();
            Date ddate = format.parse(time);
            cal.setTime(ddate);

            cal.add(Calendar.MINUTE, min);

            result = format.format(cal.getTime());
        } catch (ParseException | RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력한 시간(time)보다 몇 초(minute) 후의 시간(dest)을 계산한다.</br>
     * 입력한 일자는 'HHmmss' format 이여하 하고,
     * 결과값도 'HHmmss' format 으로 리턴된다.
     * <pre>
     * example>
     *      DateUtils.addSecond("172956", 30)
     *      result : 173026
     * </pre>
     *
     * @param time  시간
     * @param sec   계산할 초
     * @return 계산된 시각
     */
    public static String addSecond(String time, int sec) {
        String result = null;

        try {
            SimpleDateFormat format = new SimpleDateFormat(SIMPLE_TIME_FORMAT);

            Calendar cal = Calendar.getInstance();
            Date ddate = format.parse(time);
            cal.setTime(ddate);

            cal.add(Calendar.SECOND, sec);

            result = format.format(cal.getTime());
        } catch (ParseException | RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력받은 time 에 원하는 시간을 가감하여 산출 {@link java.time.temporal.ChronoUnit}.
     * </br>시간 문자열 파싱을 위해, 입력 time 값은 정해진 형식(ex> yyyy-MM-ddTHH:mm:ss)을 만족해야 함.
     * {@link java.time.LocalDateTime}.
     * <pre>
     * example>
     *      DateUtils.addTime("2020-11-17T15:06:09", 30, ChronoUnit.MINUTES, false)
     *      result : 2020-11-17T14:36:09
     * </pre>
     *
     * @param time        시간 {@link java.time.LocalDateTime}
     * @param amountToAdd 계산할 단위의 시간
     * @param unit        계산할 단위 지정 {@link java.time.temporal.ChronoUnit}
     * @param plus        true 시 plus 계산. false 시 minus 계산.
     * @return 계산된 시간
     */
    public static String addTime(String time, long amountToAdd, ChronoUnit unit, boolean plus) {
        String result = null;

        try {
            // 포멧 변경 시
            LocalDateTime localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSS"));
//            LocalDateTime localDateTime = LocalDateTime.parse(time);

            if (plus) {
                result = localDateTime.plus(amountToAdd, unit).toString();
            } else {
                result = localDateTime.minus(amountToAdd, unit).toString();
            }

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력받은 두 시각의 차를 산출. 시간 문자열 파싱을 위해,
     * </br>시간 문자열 파싱을 위해, 입력 time 값은 정해진 형식(ex> yyyy-MM-ddTHH:mm:ss.SSS)을 만족해야 함.
     * {@link java.time.LocalDateTime}.
     * <pre>
     * example>
     *      DateUtils.diffTime("2020-11-17T15:06:32.456", "2020-11-17T15:06:09.123")
     *      result : 23333
     * </pre>
     *
     * @param time1 계산할 시간 1 {@link java.time.LocalDateTime}
     * @param time2 계산할 시간 2 {@link java.time.LocalDateTime}
     * @return 계산된 시간. milliseconds 단위.
     */
    public static long diffTime(String time1, String time2) {
        long result = 0;

        try {
            LocalDateTime localDateTime1 = LocalDateTime.parse(time1);
            LocalDateTime localDateTime2 = LocalDateTime.parse(time2);

            ZonedDateTime zdt1 = ZonedDateTime.of(localDateTime1, ZoneId.systemDefault());
            ZonedDateTime zdt2 = ZonedDateTime.of(localDateTime2, ZoneId.systemDefault());
            // Instant 객체(타임스탬프) 이용
            result = zdt1.toInstant().toEpochMilli() -
                    zdt2.toInstant().toEpochMilli();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력일자(yyyyMM)의 마지막 날을 반환한다.
     * <pre>
     * example>
     *      DateUtils.getLastDay("202311")
     *      result : 20231130
     * </pre>
     *
     * @param date 일자
     * @return 마지막 날짜 리턴
     */
    public static String getLastDay(String date) {
        String result = null;
        String days = null;
        int day = 0;

        try {
            day = DateUtils.getDaysInMonth(date.substring(0, 4), date.substring(4, 6));
            days = String.format("%02d", day);
            result = date.substring(0, 6) + days;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return result;
    }

    /**
     * 입력받은 시각을 지정한 format 형태의 문자열로 변환한다
     * {@link java.time.format.DateTimeFormatter}.
     * </br>시간 문자열 파싱을 위해, 입력 time 값은 정해진 형식(ex> yyyy-MM-ddTHH:mm:ss)을 만족해야 함
     * {@link java.time.LocalDateTime}.
     * <pre>
     * example>
     *      DateUtils.getTimeByFormat("2020-11-17T15:06:09", "yyyy년 MM월 dd일 EEE요일")
     *      result : 2020년 11월 17일 화요일
     * </pre>
     *
     * @param time   변환할 시각 {@link java.time.LocalDateTime}
     * @param format 변환할 format {@link java.time.format.DateTimeFormatter}
     * @return 지정한 format 형태의 입력받은 시각
     */
    public static String getTimeByFormat(String time, String format) {
        String timeInFormat = null;

        try {
            LocalDateTime localDateTime = LocalDateTime.parse(time);
            timeInFormat = localDateTime.format(DateTimeFormatter.ofPattern(format));
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return timeInFormat;
    }

    /**
     * 지정한 format 형태의 시각을 1970-01-01T00:00:00Z 기점의 Unix-epoch milliseconds 로 변환한다.
     * {@link java.time.format.DateTimeFormatter}.
     * <pre>
     * example>
     *      DateUtils.getEpochMilliFromFormatTime("2020-11-17 오후 03:06:59", "yyyy-MM-dd a hh:mm:ss")
     *      result : 1605593219000
     * </pre>
     *
     * @param time   변환할 시각
     * @param format 변환할 format {@link java.time.format.DateTimeFormatter}
     * @return 변환된 시각. milliseconds.
     */
    public static long getEpochMilliFromFormatTime(String time, String format) {
        long milliTime = 0;

        try {
            LocalDateTime localDateTime = LocalDateTime.parse(time,
                    DateTimeFormatter.ofPattern(format));
            ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
            // Instant 객체(타임스탬프) 이용
            milliTime = zdt.toInstant().toEpochMilli();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return milliTime;
    }

    /**
     * 1970-01-01T00:00:00Z 기점의 Unix-epoch milliseconds 를 system time zone 의 시간 문자열로
     * 변환한다.
     * <pre>
     * example>
     *      DateUtils.getTimeByEpochMilli(1605593219000L)
     *      result : 2020-11-17 15:06:59.000
     * </pre>
     *
     * @param epochMilli 변환할 시각(milliseconds)
     * @return 변환된 시각. (yyyy-MM-dd HH:mm:ss.SSS)
     */
    public static String getTimeByEpochMilli(long epochMilli) {
        String resStr = null;
        String timeFormat = "yyyy-MM-dd HH:mm:ss.SSS";

        try {
            Instant instant = Instant.ofEpochMilli(epochMilli);
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            resStr = zdt.format(DateTimeFormatter.ofPattern(timeFormat));
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return resStr;
    }

    /**
     * 지정된 Format의 날짜를 입력받아 LocalDate 객체로 반환한다.
     * <pre>
     * example>
     *      DateUtils.getDate("2020-11-17 오후 03:06:59", "yyyy-MM-dd a hh:mm:ss")
     * </pre>
     *
     * @param date 변환할 날짜
     * @param format 변환할 날짜 Format {@link java.time.format.DateTimeFormatter}.
     * @return LocalDate
     */
    public static LocalDate getDate(String date, String format) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * LocalDate를 지정된 포멧의 문자열로 변환한다.
     * <pre>
     * example>
     *      DateUtils.getDate(localDate, "yyyy년 MM월 dd일 EEE요일")
     *      result : 2020년 11월 17일 화요일
     * </pre>
     *
     * @param date 변환할 날짜
     * @param format 변환할 format {@link java.time.format.DateTimeFormatter}.
     * @return 변환된 format의 날짜
     */
    public static String getDate(LocalDate date, String format) {
        try {
            return date.format(DateTimeFormatter.ofPattern(format));
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 기준일보다 이후 날짜인지 확인한다.(format:yyyyMMdd)
     * <pre>
     * example>
     *      DateUtils.isAfter("20231119", "19810904")
     *      result : false
     * </pre>
     *
     * @param stdDate   기준일
     * @param otherDate 검사 대상 날짜
     * @return 결과값. 이후 날짜 true. 그 외 false.
     */
    public static boolean isAfter(String stdDate, String otherDate) {
        return isAfter(stdDate, otherDate, SIMPLE_DATE_FORMAT);
    }

    /**
     *  지정된 Format의 기준일보다 이후 날짜인지 확인한다.
     *  {@link java.time.format.DateTimeFormatter}
     * <pre>
     * example>
     *      DateUtils.isAfter("2020년 11월 17일 화요일", "2020년 11월 19일 목요일", "yyyy년 MM월 dd일 EEE요일")
     *      result : true
     * </pre>
     *
     * @param stdDate   기준일
     * @param otherDate 검사 대상 날짜
     * @param format    입력 날짜 format {@link java.time.format.DateTimeFormatter}.
     * @return  결과값. 이후 날짜 true. 그 외 false.
     */
    public static boolean isAfter(String stdDate, String otherDate, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate std = LocalDate.parse(stdDate, dateTimeFormatter);
        LocalDate other = LocalDate.parse(otherDate, dateTimeFormatter);
        return other.isAfter(std);
    }

    /**
     * 기준일보다 이전 날짜인지 확인한다.(format:yyyyMMdd)
     * <pre>
     * example>
     *      DateUtils.isBefore("20231119", "19810904")
     *      result : true
     * </pre>
     *
     * @param stdDate   기준일
     * @param otherDate 검사 대상 날짜
     * @return  결과값. 이전 날짜 true. 그 외 false.
     */
    public static boolean isBefore(String stdDate, String otherDate) {
        return isBefore(stdDate, otherDate, SIMPLE_DATE_FORMAT);
    }

    /**
     *  지정된 Format의 기준일보다 이전 날짜인지 확인한다.
     *  {@link java.time.format.DateTimeFormatter}
     * <pre>
     * example>
     *      DateUtils.isBefore("2020년 11월 17일 화요일", "2020년 11월 19일 목요일", "yyyy년 MM월 dd일 EEE요일")
     *      result : false
     * </pre>
     *
     * @param stdDate   기준일
     * @param otherDate 검사 대상 날짜
     * @param format    입력 날짜 format {@link java.time.format.DateTimeFormatter}.
     * @return  결과값. 이전 날짜 true. 그 외 false.
     */
    public static boolean isBefore(String stdDate, String otherDate, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate std = LocalDate.parse(stdDate, dateTimeFormatter);
        LocalDate other = LocalDate.parse(otherDate, dateTimeFormatter);
        return other.isBefore(std);
    }

    /**
     * 날짜가 시작일자 종료일자 사이 날짜인지 확인한다.(format:yyyyMMdd)
     * <pre>
     * example>
     *      DateUtils.isBetween("20231119", "19810904", "20231201")
     *      result : true
     * </pre>
     *
     * @param date      검사 대상 날짜
     * @param startDate 시작일자
     * @param endDate   종료일자
     * @return  결과값. 사이의 날짜 true. 그 외 false.
     */
    public static boolean isBetween(String date, String startDate, String endDate) {
        return isBetween(date, startDate, endDate, SIMPLE_DATE_FORMAT);
    }

    /**
     * 지정된 Format의 날짜가 시작일자 종료일자 사이 날짜인지 확인한다.
     * {@link java.time.format.DateTimeFormatter}
     * <pre>
     * example>
     *      DateUtils.isBetween("2023년 12월 01일 금요일"
     *              , "2020년 11월 17일 화요일", "2020년 11월 19일 목요일", "yyyy년 MM월 dd일 EEE요일")
     *      result : false
     * </pre>
     *
     * @param date      검사 대상 날짜
     * @param startDate 시작일자
     * @param endDate   종료일자
     * @param format    입력 날짜 format {@link java.time.format.DateTimeFormatter}.
     * @return  결과값. 사이의 날짜 true. 그 외 false.
     */
    public static boolean isBetween(String date, String startDate, String endDate, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate stdDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDate start = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate end = LocalDate.parse(endDate, dateTimeFormatter);

        return stdDate.isAfter(start) && stdDate.isBefore(end);
    }

    /**
     * 만 나이 계산.
     * <pre>
     * example>
     *      DateUtils.getAge(1981, 9, 4)
     *      result : 42
     * </pre>
     *
     * @param birthYear     생년
     * @param birthMonth    생월
     * @param birthDay      생일
     * @return 계산된 만 나이
     */
    public static int getAge(int birthYear, int birthMonth, int birthDay) {
        // 현재 시간 조회
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        // 만 나이 계산 (ex> 2023 - 1981 = 42)
        int age = currentYear - birthYear;
        /*
         * 만약 생일이 지나지 않았으면 -1
         * ex> 9월 4일 생은 904. 현재날짜 5월 25일은 525.
         *     두 수를 비교 시 생일이 더 클 경우 생일이 지나지 않았음을 판별 가능.
         */
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay) {
            age--;
        }
        return age;
    }

    /**
     * 한국 나이 계산.
     * <pre>
     * example>
     *      DateUtils.getKoreanAge(1981)
     *      result : 43
     * </pre>
     *
     * @param birthYear     생년
     * @return 계산된 한국 나이
     */
    public static int getKoreanAge(int birthYear) {
        // 현재 시간 조회
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);

        return currentYear - birthYear + 1;
    }
}

