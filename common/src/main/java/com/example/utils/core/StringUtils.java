package com.example.utils.core;

import com.example.constant.Constants;
import com.example.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StringUtils {

    // 콤마문자 변경 시 추가할 prefix flag.
    public static final int CNV_NOFLAG = 0;
    public static final int CNV_PLUSSIGN = 2;   // '+'
    public static final int CNV_ASTERISK = 4;   // '*'
    // 입력된 자리수를 넘을 때 채워질 문자
    static final char OVERFLOW_CHAR = '#';
    // 콤마문자 변경 시 추가할 prefix 문자
    static final char PLUS_CHAR = '+';
    static final char ASTERISK_CHAR = '*';

    // 생성을 방지하기 위해 예외 발생 (유틸리티 클래스임을 명시)
    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 문자열(str)에 공백 문자(' ', '\t', '\n', '\r')가 포함되어 있는지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isSpace("abc 12345")
     *      result : true
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @return 공백여부(true / false)
     */
    public static boolean isSpace(String checkStr) {

        boolean chkInt = false;

        if (checkStr == null) return false;

        try {
            if (checkStr.contains(" ") || checkStr.contains("\t")
                    || checkStr.contains("\n") || checkStr.contains("\r")) {
                chkInt = true;
            }
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 특정 길이(length)만큼의 문자열(str)에 공백 문자(' ', '\t', '\n', '\r')가 포함되어 있는지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isSpace("abc 12345", 3)
     *      result : false
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @param length   검사할 길이
     * @return 검사결과 리턴 (boolean)
     */
    public static boolean isSpace(String checkStr, int length) {

        boolean chkInt = false;

        if (checkStr == null || length < 1) return false;

        try {
            if (checkStr.substring(0, length).contains(" ") || checkStr.substring(0, length).contains("\t")
                    || checkStr.substring(0, length).contains("\n") || checkStr.substring(0, length).contains("\r")) {
                chkInt = true;
            }
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 특정 길이(length)만큼의 문자열(str)이 공백문자(' ')로만 구성되어 있는지 확인
     * <pre>
     * example>
     *      StringUtils.isAllSpace("     12345", 3)
     *      result : true
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @param length   검사할 길이
     * @return 공백여부(true / false)
     */
    public static boolean isAllSpace(String checkStr, int length) {

        try {
//            String pattern = "^\\s*$";
            String pattern = "^[ ]*$";
            String subStr = checkStr.substring(0, length);

            if (subStr.matches(pattern)) return true;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return false;
    }

    /**
     * 전체 문자열(str)이 없거나 null인지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isEmpty(null)
     *      result : true
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @return 공백여부 리텀
     */
    public static boolean isEmpty(String checkStr) {

        boolean chkInt = false;

        if (checkStr == null) return true;

        try {
            if (checkStr.isEmpty()) chkInt = true;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 전체 문자열(str)이 비었거나 Space 값만 포함되어 있는지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isBlank("       ")
     *      result : true
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @return 공백여부 리턴
     */
    public static boolean isBlank(String checkStr) {

        boolean chkInt = false;

        if (checkStr == null) return true;

        try {
            if (checkStr.isEmpty()) {
                chkInt = true;
            } else {
                int strLen = checkStr.length();
                for (int i = 0; i < strLen; i++) {
                    if (!Character.isWhitespace(checkStr.charAt(i))) return false;
                }

                chkInt = true;
            }
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 전체 문자열(str)이 숫자인지를 검사한다.
     * <pre>
     * example>
     *      StringUtils.isDigit("123a456")
     *      result : false
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @return 숫자여부 리턴
     */
    public static boolean isDigit(String checkStr) {

        boolean chkInt = false;

        if (checkStr == null) return false;

        try {
            char[] strToChar = checkStr.toCharArray();

            for (int i = 0; i < strToChar.length; i++) {
                if (!Character.isDigit(strToChar[i])) return chkInt;
            }

            chkInt = true;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 특정 길이(length)만큼의 문자열(str)이 숫자인지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isDigit("123a456", 3)
     *      result : true
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @param length   검사할 길이
     * @return 숫자여부 리턴
     */
    public static boolean isDigit(String checkStr, int length) {

        boolean chkInt = false;

        if (checkStr == null || length < 1) return false;

        try {
            char[] strToChar = checkStr.substring(0, length).toCharArray();

            for (int i = 0; i < strToChar.length; i++) {
                if (!Character.isDigit(strToChar[i])) return chkInt;
            }

            chkInt = true;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 전체 문자열(str)에 숫자가 포함되어 있는지 검사한다.
     * <pre>
     * example>
     *      StringUtils.hasDigit("abc1def")
     *      result : true
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @return 숫자포함 여부 리턴
     */
    public static boolean hasDigit(String checkStr) {

        boolean chkInt = false;

        try {
            int chkCnt = 0;
            char[] strToChar = checkStr.toCharArray();

            for (int i = 0; i < strToChar.length; i++) {
                if (Character.isDigit(strToChar[i])) chkCnt++;
            }

            if (chkCnt > 0) chkInt = true;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 특정 길이(length)만큼의 문자열(str)에 숫자가 포함되어 있는지 검사한다.
     * <pre>
     * example>
     *      StringUtils.hasDigit("abc1def, 3)
     *      result : false
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @param length   검사할 길이
     * @return 검사결과 리턴
     */
    public static boolean hasDigit(String checkStr, int length) {

        boolean chkInt = false;

        if (checkStr == null || length > 1) return false;

        try {
            int chkCnt = 0;
            char[] strToChar = checkStr.substring(0, length).toCharArray();

            for (int i = 0; i < strToChar.length; i++) {
                if (Character.isDigit(strToChar[i])) chkCnt++;
            }

            if (chkCnt > 0) chkInt = true;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 전체 문자열(str)이 알파벳인지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isAlpha("abc1def")
     *      result : false
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @return 알파멧여부 리턴
     */
    public static boolean isAlpha(String checkStr) {

        boolean chkInt = false;

        if (checkStr == null) return false;

        try {
            String pattern = "^[a-zA-Z]*$";

            if (checkStr.matches(pattern)) chkInt = true;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 특정 길이(length)만큼의 문자열(str)이 알파벳인지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isAlpha("abc1def", 3)
     *      result : true
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @param length   검사할 길이
     * @return 알파벳 여부 리턴
     */
    public static boolean isAlpha(String checkStr, int length) {

        boolean chkInt = false;

        if (checkStr == null || length < 1) return false;

        try {
            String pattern = "^[a-zA-Z]*$";

            checkStr = checkStr.substring(0, length);

            if (checkStr.matches(pattern)) chkInt = true;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 전체 문자열(str)이 알파벳 또는 숫자인지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isDigitAlpha("abc1#def")
     *      result : false
     * </pre>
     *
     * @return 알파멧 또는 숫자 여부 리턴
     * @parma checkStr 검사할 문장
     */
    public static boolean isDigitAlpha(String checkStr) {

        boolean chkInt = false;

        if (checkStr == null) return false;

        try {
            String pattern = "^[a-zA-Z0-9]*$";

            if (checkStr.matches(pattern)) chkInt = true;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 특정 길이(length)만큼의 문자열(str)이 알파멧 또는 숫자인지를 검사한다.
     * <pre>
     * example>
     *      StringUtils.isDigitAlpha("abc1#def", 3)
     *      result : true
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @param length   검사할 길이
     * @return 알파벳 또는 숫자여부 리턴
     */
    public static boolean isDigitAlpha(String checkStr, int length) {

        boolean chkInt = false;

        if (checkStr == null || length < 1) return false;

        try {
            String pattern = "^[a-zA-Z0-9]*$";
            checkStr = checkStr.substring(0, length);

            if (checkStr.matches(pattern)) chkInt = true;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 전체 문자열(str)이 소문자인지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isLowerCase("abcDef")
     *      result : false
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @return 소문자 여부 리턴
     */
    public static boolean isLowerCase(String checkStr) {

        boolean chkInt = true;

        if (checkStr == null) return false;

        try {
            int chkCnt = 0;
            char[] strToChar = checkStr.toCharArray();

            for (int i = 0; i < strToChar.length; i++) {
                if (Character.isUpperCase(strToChar[i])) chkCnt++;
            }

            if (chkCnt > 0) chkInt = false;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 특정 길이(length)만큼의 문자열(str)이 소문자인지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isLowerCase("abcDef", 3)
     *      result : true
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @param length   검사할 길이
     * @return 소문자 여부 리턴
     */
    public static boolean isLowerCase(String checkStr, int length) {

        boolean chkInt = true;

        if (checkStr == null || length < 1) return false;

        try {
            int chkCnt = 0;
            char[] strToChar = checkStr.substring(0, length).toCharArray();

            for (int i = 0; i < strToChar.length; i++) {
                if (Character.isUpperCase(strToChar[i])) chkCnt++;
            }

            if (chkCnt > 0) chkInt = false;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 전체 문자열(str)이 대문자인지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isUpperCase("ABCdEF")
     *      result : false
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @return 대문자 포함 여부 리턴
     */
    public static boolean isUpperCase(String checkStr) {

        boolean chkInt = true;

        if (checkStr == null) return false;

        try {
            int chkCnt = 0;
            char[] strToChar = checkStr.toCharArray();

            for (int i = 0; i < strToChar.length; i++) {
                if (Character.isLowerCase(strToChar[i])) chkCnt++;
            }

            if (chkCnt > 0) chkInt = false;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 특정 길이(length)만큼의 문자열(str)이 대문자인지 검사한다.
     * <pre>
     * example>
     *      StringUtils.isUpperCase("ABCdEF", 3)
     *      result : true
     * </pre>
     *
     * @param checkStr 검사할 문장
     * @param length   검사할 길이
     * @return 대문자 포함 여부 리턴
     */
    public static boolean isUpperCase(String checkStr, int length) {

        boolean chkInt = true;

        if (checkStr == null) return false;

        try {
            int chkCnt = 0;
            char[] strToChar = checkStr.substring(0, length).toCharArray();

            for (int i = 0; i < strToChar.length; i++) {
                if (!Character.isLowerCase(strToChar[i])) chkCnt++;
            }

            if (chkCnt > 0) chkInt = false;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 입력 문자열이 '0'으로만 구성되었는지 확인
     * <pre>
     * example>
     *      StringUtils.isAllZero("0000 0000, 5)
     *      result : false
     * </pre>
     *
     * @param checkStr 검사할 문자열
     * @param length   검사할 길이
     * @return 입력받은 길이만큼 '0'으로 구성되면 true
     */
    public static boolean isAllZero(String checkStr, int length) {

        try {
            String pattern = "^[0]*$";
            String subStr = checkStr.substring(0, length);

            if (subStr.matches(pattern)) return true;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return false;
    }

    /**
     * 입력 문자열이 16진수문자로만 구성되어있는지 확인
     * <pre>
     * example>
     *      StringUtils.isHexNumStr("16afeg567", 7)
     *      result : false
     * </pre>
     *
     * @param checkStr 검사할 문자열
     * @param length   검사할 길이
     * @return 입력받은 길이만큼 16진수에 사용하는 문자(0-9, A-F, a-f)일 때 true
     */
    public static boolean isHexNumStr(String checkStr, int length) {

        try {
            String pattern = "^[0-9a-fA-F]*$'";
            String subStr = checkStr.substring(0, length);

            if (subStr.matches(pattern)) return true;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return false;
    }

    /**
     * 변환 대상 문자열(inputStr)의 대문자를 소문자로 변환
     * <pre>
     * example>
     *      StringUtils.getLowerCase("16AFEG567")
     *      result : 16afeg567
     * </pre>
     *
     * @param inputStr 변환할 문자열
     * @return 소문자 변환 리턴
     */
    public static String getLowerCase(String inputStr) {

        // destination : 목적지
        String dest = "";

        try {
            dest = inputStr.toLowerCase();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return dest;
    }

    /**
     * 변환 대상 문자열(inputStr)의 특정 길이(length)만큼 대문자를 소문자로 변환
     * <pre>
     * example>
     *      StringUtils.getLowerCase("16AFEG567", 5)
     *      result : 16afe
     * </pre>
     *
     * @param inputStr 변환할 문자열
     * @param length   변환할 길이
     * @return 소문자 변환 리턴
     */
    public static String getLowerCase(String inputStr, int length) {

        // destination : 목적지
        String dest = "";

        try {
            dest = inputStr.substring(0, length).toLowerCase();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return dest;
    }

    /**
     * 변환 대상 문자열(inputStr)의 소문자를 대문자로 변환
     * <pre>
     * example>
     *      StringUtils.getLowerCase("16afeg567")
     *      result : 16AFEG567
     * </pre>
     *
     * @param inputStr 변환할 문자열
     * @return 대문자 변환 리턴
     */
    public static String getUpperCase(String inputStr) {

        // destination : 목적지
        String dest = "";

        try {
            dest = inputStr.toUpperCase();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return dest;
    }

    /**
     * 변환 대상 문자열(inputStr)의 특정길이(length)만큼 소문자를 대문자로 변환
     * <pre>
     * example>
     *      StringUtils.getLowerCase("16afeg567", 5)
     *      result : 16AFE
     * </pre>
     *
     * @param inputStr 변환할 문자열
     * @param length   변환할 길이
     * @return 대문자 변환 리턴
     */
    public static String getUpperCase(String inputStr, int length) {

        // destination : 목적지
        String dest = "";

        try {
            dest = inputStr.substring(0, length).toUpperCase();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return dest;
    }

    /**
     * 문자열(inputStr)의 앞뒤 공백을 제거
     * <per>
     * example>
     * StringUtils.getTrim("   16afeg567   ")
     * result : "16afeg567"
     * </per>
     *
     * @param inputStr 변환할 문자열
     * @return 공백 제거 후 리턴
     */
    public static String getTrim(String inputStr) {

        String dest = "";

        try {
            dest = inputStr.trim();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return dest;
    }

    /**
     * 문자열(inputStr)의 앞 공백을 제거
     * <per>
     * example>
     * StringUtils.getLeftTrim("   16afeg567   ")
     * result : "16afeg567   "
     * </per>
     *
     * @param inputStr 변환할 문자열
     * @return 공백 제거 후 리턴
     */
    public static String getLeftTrim(String inputStr) {

        String dest = "";

        try {
            int len = inputStr.length();
            int i = 0;

            while (i < len && Character.isWhitespace(inputStr.charAt(i))) i++;

            dest = inputStr.substring(i);

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return dest;
    }

    /**
     * 문자열(inputStr)의 뒤 공백을 제거
     * <per>
     * example>
     * StringUtils.getRightTrim("   16afeg567   ")
     * result : "   16afeg567"
     * </per>
     *
     * @param inputStr 변환할 문자열
     * @return 공백 제거 후 리턴
     */
    public static String getRightTrim(String inputStr) {

        String dest = "";

        try {
            int len = inputStr.length();

            while (inputStr.charAt(len - 1) == ' ') {
//                dest = inputStr.substring(0, len - 1);
                len--;
            }
            //
//            while (len > 0 && Character.isWhitespace(inputStr.charAt(len - 1))){
//                len--;
//            }
            dest = inputStr.substring(0, len);

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return dest;
    }

    /**
     * 숫자를 주어진 포멧에 맞게 문자열로 변환한다.
     * <pre>
     * example>
     *      StringUtils.getFormatString(123.456789, "%015f_")
     *      result : 00000123.456789_
     * </pre>
     *
     * @param num    숫자
     * @param format 변경할 포멧
     * @return 포멧 변경후 리턴
     */
    public static String getFormatString(double num, String format) {

        String output = "";

        try {
            Double dblToStr = num;

            String input = dblToStr.toString();
            // double to string으로 올 때 double의 원본 이진수 오차를 버리고
            // 10진수 문자열로 BigDecimal 객체를 생성하여 정확성을 확보
            output = String.format(format, new java.math.BigDecimal(input));
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return output;
    }

    /**
     * 문자열 비교
     * <pre>
     *  example>
     *      StringUtils.compareString("abc", "acb")
     *      result : -1
     *  </pre>
     *
     * @param s1 값1
     * @param s2 값2
     * @return 앞에 값이 작으면 -1 같으면 0 크면 1
     */
    public static int compareString(String s1, String s2) {

        int chkInt = 0;

        try {
            if (isEmpty(s1)) {
                if (isEmpty(s2)) {
                    chkInt = 0;
                } else {
                    chkInt = -1;
                }
            } else if (isEmpty(s2)) {
                if (isEmpty(s1)) {
                    chkInt = 0;
                } else {
                    chkInt = 1;
                }
            } else {
                // .compareTo()는 값이 없는 경우 NullPointException 발생
                // 그래서 위에 isEmpty()로 체크 후 compareTo 실행
                chkInt = s1.compareTo(s2);
                if (chkInt == 0) chkInt = 0;  // s1 == s2
                else if (chkInt > 0) chkInt = 1;  // s1 > s2
                else if (chkInt < 0) chkInt = -1; // s1 < s2
            }
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return chkInt;
    }

    /**
     * 문자열 비교
     * <pre>
     * example>
     *      StringUtils.compareString("abcdef", "abcdfe", 3)
     *      result : 0
     * </pre>
     *
     * @param s1     값1
     * @param s2     값2
     * @param length 체크할 자릿수
     * @return 앞에 값이 작으면 -1 같으면 0 크면 1
     */
    public static int compareString(String s1, String s2, int length) {

        try {
            // rtVal : return value
            int rtVal = s1.substring(0, length).compareTo(s2.substring(0, length));

            if (rtVal < 0)
                return -1;
            else if (rtVal == 0)
                return 0;
            else
                return 1;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 문자열 비교
     * <pre>
     * example>
     *      StringUtils.compareString("abcdef", "abcdfe", 3)
     *      result : 0
     * </pre>
     *
     * @param s1              값1
     * @param s2              값2
     * @param length          체크할 자릿수
     * @param caseInsensitive 대소문자 구분여부
     *                        true : 대소문자 비구분 / false : 대소문자 구분
     * @return 앞에 값이 작으면 -1 같으면 0 크면 1
     */
    public static int compareString(String s1, String s2, int length, boolean caseInsensitive) {

        try {
            // null check
            if (s1 == null && s2 == null) return 0;

            if (s1 == null) return -1;

            if (s2 == null) return 1;

            // string compare
            int rtVal = 0;
            String s1SubStr = s1.substring(0, length);
            String s2SubStr = s2.substring(0, length);
            // case Insensitive
            if (caseInsensitive) rtVal = s1SubStr.compareToIgnoreCase(s2SubStr);
            else rtVal = s1SubStr.compareTo(s2SubStr);

            if (rtVal < 0)
                return -1;
            else if (rtVal == 0)
                return 0;
            else
                return 1;

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 문자열 붙이기
     * <pre>
     * example>
     *      StringUtils.concateString("abc", "acb")
     *      result : "abcacb"
     * </pre>
     *
     * @param s1 값1
     * @param s2 값2
     * @return 두 문장을 붙여 리턴
     */
    public static String concateString(String s1, String s2) {

        try {
            return s1.concat(s2);
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * double을 string 변환
     * <pre>
     * example>
     *      StringUtils.changeDoubleToString(123.456)
     *      result : 123.456
     * </pre>
     *
     * @param num 숫자
     * @return 문자로 변환 후 리턴
     */
    public static String changeDoubleToString(double num) {

        String rtVal = "";
        // BigDecimal 사용 이유는 정확한 소수점 계산을 위함
        BigDecimal val = BigDecimal.valueOf(num);

        try {
            rtVal = val.toString();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return rtVal;
    }

    /**
     * int를 stirng 변환
     * <pre>
     * example>
     *      StringUtils.changeIntToString(123456)
     *      result : 123456
     * </pre>
     *
     * @param num 숫자
     * @return 문자로 변환 후 리턴
     */
    public static String changeIntToString(int num) {

        String rtVal = "";
        Integer val = num;

        try {
            rtVal = val.toString();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return rtVal;
    }

    /**
     * long을 string으로 변환
     * <pre>
     * example>
     *      StringUtils.changeLongToString(1234567890L)
     *      result : 1234567890
     * </pre>
     *
     * @param num 숫자
     * @return 문자로 변환 리턴
     */
    public static String changeLongToString(long num) {

        String rtVal = "";
        Long val = num;

        try {
            rtVal = val.toString();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return rtVal;
    }

    /**
     * 문자열의 길이를 리턴해주는 함수
     * <pre>
     *     Stringutils.stringLen("abcd")
     *     result : 4
     * </pre>
     *
     * @param str 문자열
     * @return 길이를 리턴
     */
    public static int stringLen(String str) {

        int rtVal = 0;

        try {
            rtVal = str.length();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return rtVal;
    }

    /**
     * 문자열 비교하여 위치 확인
     * <pre>
     *     StringUtils.stringIndex("abcdefg", "ef")
     *     result : 4
     * </pre>
     *
     * @param s1 값1
     * @param s2 값2
     * @return 갑1에 값2가 문장이 처음 존재하는 위치를 리턴
     */
    public static int stringIndex(String s1, String s2) {

        try {
            return s1.indexOf(s2);
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 정해진 위치까지 문자열 붙이기
     * <pre>
     *     StringUtils.stringConcat("abc", "defghij", 2)
     *     result : abcde
     * </pre>
     *
     * @param s1  값1
     * @param s2  값2
     * @param num 붙일 길이
     * @return s2의 num 길이 만큼 s1에 붙여서 리턴
     */
    public static String stringConcat(String s1, String s2, int num) {

        try {
            return s1.concat(s2.substring(0, num));
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * string이 empty이면 특정 문자열을 반환
     * <pre>
     *     StringUtils.nvl(null, "empty string")
     *     result : empty string
     * </pre>
     *
     * @param str    공백체크 문장
     * @param defVal 공백이면 변경할 문장
     * @return 공백여부 체크해 공백이면 대체 값으로 변경 리턴
     */
    public static String nvl(String str, String defVal) {
        String rtVal = str;
        if (rtVal == null) rtVal = defVal;
        return rtVal;
    }

    /**
     * 특정 문자를 붙여서 원하는 길이로 맞추는 함수
     * <pre>
     *     StringUtils.stringPadding(10, "test", 0, '#')
     *     result : ######test
     * </pre>
     *
     * @param num    변경 후 문장 길이
     * @param srcStr 변경 전 문장
     * @param padDir 0 : PADDING_LEFT / 1 : PADDING_RIGHT
     * @param word   길이만큼 채울 문자
     * @return 패딩 변경된 문장
     */
    public static String stringPadding(int num, String srcStr, int padDir, char word) {
        final int PADDING_LEFT = 0;
        final int PADDING_RIGHT = 1;

        try {
            String rtVal = "";
            if (padDir != 0 && padDir != 1)
                throw CommonException.builder().message("패딩구분코드를 확인하십시오").build();

            StringBuilder builder = new StringBuilder(srcStr);
            if (padDir == 0) builder.reverse();

            while (builder.toString().getBytes().length < num) {
                builder.append(word);
            }

            if (padDir == 0) rtVal = builder.reverse().toString();
            else rtVal = builder.toString();

//            // 필요한 padding 문자 수 계산
//            // 단 word가 1바이트라고 가정
//            int srcStrByteLen = srcStr.getBytes(StandardCharsets.UTF_8).length;
//            int paddingCnt = num - srcStrByteLen;
//            // srcStr이 더 클경우 srcStr 반환
//            if (paddingCnt <= 0) return srcStr;
//
//            if (padDir == PADDING_LEFT){
//                for (int i = 0; i < paddingCnt; i++) {
//                    builder.append(word);
//                }
//                builder.append(srcStr);
//            }else {
//                builder.append(srcStr);
//                for (int i = 0; i < paddingCnt; i++) {
//                    builder.append(word);
//                }
//            }
//            rtVal = builder.toString();

            return rtVal;
        } catch (CommonException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * string에서 특정문자열1을 특정문자열2로 변환
     * <pre>
     *     StringUtils.stringReplace("123test321", '1', '#)
     *     result : #23test32#
     * </pre>
     *
     * @param srcStr 변환전 문장
     * @param word1  찾을 문장
     * @param word2  변경할 문장
     * @return 변경 후 리턴
     */
    public static String stringReplace(String srcStr, char word1, char word2) {

        try {
            String rtVal = "";
            rtVal = srcStr.replace(word1, word2);
            return rtVal;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 문자열에서 특정 패턴을 찾아 모두 제거
     * <pre>
     *     StringUtils.stringRmAll("123test321", "test")
     *     result : 123321
     * </pre>
     *
     * @param srcStr 변경전 문장
     * @param rmStr  삭제할 문장
     * @return 제거 후 리턴
     */
    public static String stringRmAll(String srcStr, String rmStr) {

        try {
            String rtVal = "";
            rtVal = srcStr.replaceAll(rmStr, "");
            return rtVal;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * String의 입력받은 자리수까지 integer로 변환해서 리턴
     * <pre>
     *     StringUtils.stringToInt("123test321", 3)
     *     result : 123
     * </pre>
     *
     * @param str    변환할 문장
     * @param length 변환할 길이
     * @return 정수로 변환해 리턴
     */
    public static int stringToInt(String str, int length) {

        try {
            int rtVal = 0;
            rtVal = Integer.parseInt(str.substring(0, length));
            return rtVal;


        } catch (NumberFormatException ex) {
            // string이 들어갈 경우 exception
            throw ex;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * String을 입력받은 size만큼 long으로 변환해서 리턴
     * <pre>
     *     StringUtils.stringToLong("1234567890", 9)
     *     result : 1234567890
     * </pre>
     *
     * @param str    변환할 문장
     * @param length 변환할 길이
     * @return       long타입으로 변환 후 리턴
     */
    public static long stringToLong(String str, int length){

        try{
            long rtVal = 0;
            rtVal = Long.parseLong(str.substring(0, length));
            return rtVal;
        }catch (NumberFormatException ex){
            throw ex;
        }catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * String을 long으로 변환해서 리턴
     * <pre>
     *     StringUtils.stringToLong("1234567890")
     *     result : 1234567890
     * </pre>
     *
     * @param str    변환할 문장
     * @return       long타입으로 변환 후 리턴
     */
    public static long stringToLong(String str){

        try{
            long rtVal = 0;
            rtVal = Long.parseLong(str);
            return rtVal;
        }catch (NumberFormatException ex){
            throw ex;
        }catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * String을 double으로 변환해서 리턴
     * <pre>
     *     StringUtils.stringToDouble("123.4567890")
     *     result : 123.4567890
     * </pre>
     *
     * @param str    변환할 문장
     * @return       double타입으로 변환 후 리턴
     */
    public static double stringToDouble(String str){

        try{
            double rtVal = 0;
            rtVal = Double.parseDouble(str);
            return rtVal;
        }catch (NumberFormatException ex){
            throw ex;
        }catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 입력받은 String을 마스킹하는 함수
     * <pre>
     *     StringUtils.masking("8109041234567", "1")
     *     result : 810904******
     * </pre>
     *
     * @param str   마스킹할 내용
     * @param types 1: 주민번호 2: 전화번호
     * @return
     */
    public static String masking(String str, String types){
        final String RRN = "1";  //Resident Registration Number: 주민번호
        final String PHONE_NUMBER = "2";

        try{
            String msk = str;

            if (str == null || str.isEmpty()) return msk;

            int len = str.length();

            if (RRN.equals(types)){
                // 주민번호
                if (len == 13) msk = str.substring(0, 7) + "******";
            }else if(PHONE_NUMBER.equals(types)) {
                // 전화번호
               if (len >= 11) {
                    //010-1111-2222
                    msk = str.substring(0, 3) + "****" + str.substring(7);
                } else if (len == 10) {
                    if ("02".equals(str.substring(0, 2))) {
                        //02-1111-2222
                        msk = str.substring(0, 2) + "****" + str.substring(6);
                    } else {
                        //031-111-2222
                        msk = str.substring(0, 3) + "***" + str.substring(6);
                    }
                } else if (len == 9) {
                    //02-111-2222
                    msk = str.substring(0, 2) + "***" + str.substring(5);
                } else if (len == 8) {
                    //1111-2222
                    msk = "****" + str.substring(4);
                } else if (len == 7) {
                    //111-2222
                    msk = "***" + str.substring(3);
                } else {
                    //확인필요
                }
            } else {
                throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
            }

            return msk;
        } catch (CommonException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 입력받은 String 을 마스킹 하는 함수.
     * 중간에 "-" 까지 붙임.
     * <pre>
     * example>
     *      StringUtils.maskingView("8109041234567", "1")
     *      result : 810904-1******
     * </pre>
     *
     * @param str   마스킹할 내용
     * @param types 마스킹 타입 (1:주민번호 2:전화번호)
     * @return 마스킹 처리 후 리턴
     */
    public static String maskingView(String str, String types) {
        try {
            String msk = str;

            if (str == null || str.isEmpty()) {
                return msk;
            }

            int len = str.length();
            if ("1".equals(types)) {
                //주민번호
                if (len == 13) {
                    msk = str.substring(0, 6) + "-" + str.substring(6, 7) + "******";
                } else if (len == 10) {
                    msk = str.substring(0, 3) + "-" + str.substring(3, 5) + "-" + str.substring(5);
                }
            } else if ("2".equals(types)) {
                //전화번호
                if (len >= 11) {
                    //010-1111-2222
                    msk = str.substring(0, 3) + "-****-" + str.substring(7);
                } else if (len == 10) {
                    if ("02".equals(str.substring(0, 2))) {
                        //02-1111-2222
                        msk = str.substring(0, 2) + "-****-" + str.substring(6);
                    } else {
                        //031-111-2222
                        msk = str.substring(0, 3) + "-***-" + str.substring(6);
                    }
                } else if (len == 9) {
                    //02-111-2222
                    msk = str.substring(0, 2) + "-***-" + str.substring(5);
                } else if (len == 8) {
                    //1111-2222
                    msk = "****-" + str.substring(4);
                } else if (len == 7) {
                    //111-2222
                    msk = "***-" + str.substring(3);
                } else {
                    //확인필요
                }
            } else {
                throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
            }

            return msk;
        } catch (CommonException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 입력받은 String 을 마스킹 하는 함수.
     * 중간에 - 추가 여부 선택 가능.
     * <pre>
     * example>
     *      StringUtils.automaskingView("8109041234567", "1", "1")
     *      result : 810904-1******
     * </pre>
     *
     * @param str    마스킹할 문장
     * @param types  ( 1:주민번호 2:전화번호 3:계좌번호 4:고객명 5:이메일 6:상세주소 7:카드번호 8:타기관계좌번호 9:펀드계좌번호)
     * @param viewTp ( 1:중간에 - 추가 ELSE:중간에 - 추가안함 )
     * @return 마스킹 처리 후 리턴
     */
    public static String automaskingView(String str, String types, String viewTp) {
        try {
            //마스킹 해제 권한 여부 설정.
            boolean bMaskDel = false;
            return automaskingView(str, types, viewTp, bMaskDel);
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 입력받은 String 을 마스킹 하는 함수.
     * 중간에 - 추가 여부 선택 가능.
     * <pre>
     * example>
     *      StringUtils.automaskingView("8109041234567", "1", "1", true)
     *      result : 810904-1234567
     * </pre>
     *
     * @param str      마스킹할 문장
     * @param types    ( 1:주민번호 2:전화번호 3:계좌번호 4:고객명 5:이메일 6:상세주소 7:카드번호 8:타기관계좌번호 9:펀드계좌번호)
     * @param viewTp   ( 1:중간에 - 추가 ELSE:중간에 - 추가안함 )
     * @param bMaskDel 마스킹 해제 권한 여부
     * @return 마스킹 처리 후 리턴
     */
    public static String automaskingView(String str, String types, String viewTp, boolean bMaskDel) {
        try {

            String msk = str;
            if (str == null || str.trim().isEmpty()) {
                return "";
            }
            str = StringUtils.getTrim(str);
            msk = str;

            String mskStr = "**************************************************";
            int msklen = str.length() / 2;

            int len = str.length();
            if ("1".equals(types)) {
                //주민번호
                if ("1".equals(viewTp)) {
                    //중간 바 추가
                    if (len == 13) {
                        if (bMaskDel) {
                            msk = str.substring(0, 6) + "-" + str.substring(6);
                        } else {
                            msk = str.substring(0, 6) + "-" + str.substring(6, 7) + "******";
                        }
                    } else if (len == 10) {
                        // 주민번호 위치에 사업자등록번호 등록 시 masking 없이 '-' 만 추가
                        msk = str.substring(0, 3) + "-" + str.substring(3, 5) + "-" + str.substring(5);
                    }
                } else {
                    //중간 바 없음.
                    if (len == 13) {
                        if (bMaskDel) {
                            msk = str.substring(0, 6) + str.substring(6);
                        } else {
                            msk = str.substring(0, 6) + str.substring(6, 7) + "******";
                        }
                    }
                }
            } else if ("2".equals(types)) {
                //전화번호
                if ("1".equals(viewTp)) {
                    //중간 바 추가
                    if (len >= 11) {
                        //010-1111-2222
                        if (bMaskDel) {
                            msk = str.substring(0, 3) + "-" + str.substring(3, 7) + "-" + str.substring(7);
                        } else {
                            msk = str.substring(0, 3) + "-****-" + str.substring(7);
                        }
                    } else if (len == 10) {
                        if ("02".equals(str.substring(0, 2))) {
                            //02-1111-2222
                            if (bMaskDel) {
                                msk = str.substring(0, 2) + "-" + str.substring(2, 6) + "-" + str.substring(6);
                            } else {
                                msk = str.substring(0, 2) + "-****-" + str.substring(6);
                            }
                        } else {
                            //031-111-2222
                            if (bMaskDel) {
                                msk = str.substring(0, 3) + "-" + str.substring(3, 6) + "-" + str.substring(6);
                            } else {
                                msk = str.substring(0, 3) + "-***-" + str.substring(6);
                            }
                        }
                    } else if (len == 9) {
                        //02-111-2222
                        if (bMaskDel) {
                            msk = str.substring(0, 2) + "-" + str.substring(2, 5) + "-" + str.substring(5);
                        } else {
                            msk = str.substring(0, 2) + "-***-" + str.substring(5);
                        }
                    } else if (len == 8) {
                        //1111-2222
                        if (bMaskDel) {
                            msk = str.substring(0, 4) + "-" + str.substring(4);
                        } else {
                            msk = "****-" + str.substring(4);
                        }
                    } else if (len == 7) {
                        //111-2222
                        if (bMaskDel) {
                            msk = str.substring(0, 3) + "-" + str.substring(3);
                        } else {
                            msk = "***-" + str.substring(3);
                        }
                    }
                } else {
                    //중간 바 없음
                    if (len >= 11) {
                        //010-1111-2222
                        if (!bMaskDel) {
                            msk = str.substring(0, 3) + "****" + str.substring(7);
                        }
                    } else if (len == 10) {
                        if ("02".equals(str.substring(0, 2))) {
                            //02-1111-2222
                            if (!bMaskDel) {
                                msk = str.substring(0, 2) + "****" + str.substring(6);
                            }
                        } else {
                            //031-111-2222
                            if (!bMaskDel) {
                                msk = str.substring(0, 3) + "***" + str.substring(6);
                            }
                        }
                    } else if (len == 9) {
                        //02-111-2222
                        if (!bMaskDel) {
                            msk = str.substring(0, 2) + "***" + str.substring(5);
                        }
                    } else if (len == 8) {
                        //1111-2222
                        if (!bMaskDel) {
                            msk = "****" + str.substring(4);
                        }
                    } else if (len == 7) {
                        //111-2222
                        if (!bMaskDel) {
                            msk = "***" + str.substring(3);
                        }
                    }
                }
            } else if ("3".equals(types)) {
                //3:계좌번호
                if (str.length() != 11) {
                    throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
                }

                if ("1".equals(viewTp)) {
                    if (bMaskDel) {
                        msk = str.substring(0, 3) + "-" + str.substring(3, 5) + "-" + str.substring(5);
                    } else {
                        msk = str.substring(0, 3) + "-" + str.substring(3, 4) + "*-***" + str.substring(8);
                    }
                } else {
                    if (bMaskDel) {
                        msk = str;
                    } else {
                        msk = str.substring(0, 3) + str.substring(3, 4) + "****" + str.substring(8);
                    }
                }
            } else if ("9".equals(types)) {
                //9:펀드계좌번호는 13자리  계좌번호 11 + 펀드 2 = 13
                //or 14자리  계좌번호 11 + 펀드 3 = 14
                if (str.length() != 13 && str.length() != 14) {
                    throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
                }

                if ("1".equals(viewTp)) {
                    if (bMaskDel) {
                        msk = str.substring(0, 3) + "-" + str.substring(3, 5) + "-" + str.substring(5, 11) + "-" + str.substring(11);
                    } else {
                        msk = str.substring(0, 3) + "-" + str.substring(3, 4) + "*-***" + str.substring(8, 11) + "-" + str.substring(11);
                    }
                } else {
                    if (bMaskDel) {
                        msk = str;
                    } else {
                        msk = str.substring(0, 3) + str.substring(3, 4) + "****" + str.substring(8);
                    }
                }
            } else if ("4".equals(types)) {
                //4:고객명
                if (!bMaskDel) {
                    if (chkHangul(str)) {
                        if (len <= 2) {
                            msk = str.substring(0, 1) + "*";
                        } else if (len >= 3) {
                            msk = str.substring(0, 1) + mskStr.substring(0, len - 2) + str.substring(len - 1);
                        }
                    } else {

                        if (msklen == 0) {
                            msk = mskStr.substring(0, len);
                        } else {
                            if (msklen > 4)
                                msklen = 4;

                            msk = str.substring(0, msklen) + mskStr.substring(0, len - msklen);
                        }
                    }
                }

            } else if ("5".equals(types)) {
                //5:이메일
                if (!bMaskDel) {
                    int idx = str.indexOf("@");
                    int fondView = 0;

                    if (idx < 0) {
                        idx = len;
                    } else {
                        msklen = idx / 2;
                    }
                    if (idx % 2 > 0) msklen++;
                    fondView = msklen / 2;
                    if (fondView <= 0)
                        fondView = 1;

                    msk = str.substring(0, fondView) + mskStr.substring(0, msklen) + str.substring(fondView + msklen);
                }
            } else if ("6".equals(types)) {
                //6:상세주소
                if (!bMaskDel) {

                    String sTmp = str;
                    for (int i = 0; i < 10; i++) {
                        sTmp = sTmp.replace(String.valueOf(i), "*");
                    }
                    msk = sTmp;
                }
            } else if ("7".equals(types)) {
                //7:카드번호
                if (!bMaskDel) {
                    //7~12 자리 마스킹
                    if (str.length() > 12) {
                        msk = str.substring(0, 6) + "******" + str.substring(12);
                    } else {
                        msk = str.substring(0, str.length() - msklen) + mskStr.substring(0, msklen);
                    }
                }
            } else if ("8".equals(types)) {
                //8:타기관계좌번호
                if (!bMaskDel) {
                    int mlen1 = str.length() / 3;
                    int mlen2 = mlen1;
                    if (str.length() % 3 > 0)
                        mlen2++;

                    int vieLen = str.length() - mlen2 - mlen1;
                    msk = str.substring(0, vieLen) + mskStr.substring(0, mlen2);

                    if (mlen1 > 0) {
                        msk += str.substring(str.length() - mlen1);
                    }
                }
            } else {
                throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
            }

            return msk;
        } catch (CommonException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 한글여부 리턴
     * <pre>
     *     StringUtils.chkHangul("abc def 한글 ghi")
     *     result : true
     * </pre>
     *
     * @param word 체크할 문장
     * @return 한글포함여부 리턴
     */
    public static boolean chkHangul(String word) {
        return word.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    }


    /**
     * Utf8 문자열을 euc-kr로 변환 리턴.
     * utf8이 기본으로 되어 있어 string 으로 리턴 불가 byte 배열로 리턴.
     * <pre>
     * example>
     *      StringUtils.ecdUtfToEucKr("def 한글")
     *      result : [100, 101, 102, 32, -57, -47, -79, -37]
     * </pre>
     *
     * @param putf 변환할 문장
     * @return 변환 후 리턴
     */
    public static byte[] ecdUtfToEucKr(String putf) {
        byte[] rv;

        try {
            rv = putf.getBytes("euc-kr");
        } catch (UnsupportedEncodingException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
        return rv;
    }

    /**
     * euc-kr 문자열을 Utf8로 변환 리턴.
     * utf8이 기본으로 되어 있어 string 으로 받을수 없고 byte로만 받을수 있음.
     * <pre>
     * example>
     *      StringUtils.ecdEucKrToUtf(new byte[] {100, 101, 102, 32, -57, -47, -79, -37})
     *      result : def 한글
     * </pre>
     *
     * @param pStr 변환할 문장
     * @return 변환후 리턴
     */
    public static String ecdEucKrToUtf(byte[] pStr) {
        String rv = "";

        try {
            rv = new String(pStr, "euc-kr");
        } catch (UnsupportedEncodingException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
        return rv;
    }

    /**
     * Byte 단위 Substring 처리용 함수
     * <pre>
     * example>
     *      StringUtils.subStrByte(new byte[] {100, 101, 102, 32, -57, -47, -79, -37}, 4, 2)
     *      result : [-57, -47]
     * </pre>
     *
     * @param pStr  : 작업할 문자열
     * @param pSidx : 자를 시작위치
     * @param plen  : 길이
     * @return 원하는 길이만큼 잘라 리턴
     */
    public static byte[] subStrByte(byte[] pStr, int pSidx, int plen) {

        if (pStr == null || pStr.length < 1) {
            return new byte[0];
        }

        byte[] arry = new byte[plen];

        if (pStr.length < pSidx + plen) {
            throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
        }

        for (int i = 0; i < plen; i++) {
            arry[i] = pStr[pSidx + i];
        }

        return arry;
    }

    /**
     * Byte 단위 Substring 처리용 함수
     * <pre>
     * example>
     *      StringUtils.subStrByte(new byte[] {100, 101, 102, 32, -57, -47, -79, -37}, 4)
     *      result : [-57, -47, -79, -37]
     * </pre>
     *
     * @param pStr  : 작업할 문자열
     * @param pSidx : 자를 시작 위치
     * @return 정해진 길이 이후 잘라서 리턴
     */
    public static byte[] subStrByte(byte[] pStr, int pSidx) {

        if (pStr == null || pStr.length < 1) {
            return new byte[0];
        }

        int len = pStr.length - pSidx;

        if (len <= 0 || pSidx < 0 || pStr.length <= pSidx) {
            throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
        }

        byte[] arry = new byte[len];

        for (int i = 0; i < len; i++) {
            arry[i] = pStr[pSidx + i];
        }

        return arry;
    }

    /**
     * Byte 단위 Substring 처리용 함수.
     * 한글 등 유니 코드가 중간에서 잘릴 경우 문자가 깨지는 것을 방지하기 위해 space 문자로 대체.
     * <pre>
     * example>
     *      StringUtils.unicodRvsn(new byte[] {100, 101, 102, 32, -57, -47, -79, -37}, 5)
     *      result : [100, 101, 102, 32, 32]
     * </pre>
     *
     * @param zSrc : 작업할 문자열
     * @param nLen : 길이. 0보다 작거나 같고/원래 문자열 길이보다 클 경우 원래 문자열 길이를 사용
     * @return 원하는 길이만큼 잘라 리턴
     */
    public static byte[] unicodRvsn(byte[] zSrc, int nLen) {

        int maxLen = zSrc.length;
        if (nLen <= 0 || maxLen < nLen) {
            nLen = maxLen;
        }

        try {
            int i = 0;
            boolean unicodeFlag = false;
            byte[] arry = new byte[nLen];
            for (i = 0; i < nLen; i++) {
                arry[i] = zSrc[i];
                byte chk = (byte) (zSrc[i] & 0x80);
                if ((!unicodeFlag) && (chk != 0x00)) {
                    unicodeFlag = true;
                } else {
                    unicodeFlag = false;
                }
            }

            if (unicodeFlag) {
                arry[i - 1] = ' ';
            }

            return arry;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * GMT 기준 현재 시각을 http date형식으로 리턴한다.
     * {@code ex> Thu, 17 Nov 2005 18:49:58 GMT }
     * <pre>
     * example>
     *      StringUtils.getHttpDate(Locale.KOREA)
     *      result : 수, 13 12월 2023 06:29:14 GMT
     * </pre>
     *
     * @param locale
     * @return
     */
    public static String getHttpDate(Locale locale) {
        // Thu, 17 Nov 2005 18:49:58 GMT
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", locale);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    /**
     * String 실제 길이 리턴.
     * 한글인 경우 length 으로 계산할 경우 1로 계산이 되기 때문에
     * byte로 계산한 실제 길이를 리턴.
     * <pre>
     * example>
     *      StringUtils.getStrLength("def 한글")
     *      result : 10
     * </pre>
     *
     * @param pStr 문자열
     * @return 문자열의 길이를 리턴
     */
    public static int getStrLength(String pStr) {
        int len = 0;

        try {
            if (pStr != null && !pStr.isEmpty()) {
                len = pStr.getBytes(StandardCharsets.UTF_8).length;
            }
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }


        return len;
    }

    /**
     * String 실제 길이 리턴
     * <pre>
     * 한글인 경우 length 으로 계산할 경우 1로 계산이 되기 때문에
     * byte로 계산한 실제 길이를 리턴.
     * 최대 길이 지정하여, 해당 길이 넘을때 최대 길이만큼만 리턴.
     * </pre>
     * <pre>
     * example>
     *      StringUtils.getStrNByteLength("def 한글", 8)
     *      result : 8
     * </pre>
     *
     * @param pStr    문자열
     * @param nMaxLen 리턴할 문자열 최대 길이
     * @return 문자열의 길이를 리턴
     */
    public static int getStrNByteLength(String pStr, int nMaxLen) {
        int len = getStrLength(pStr);
        if (len > nMaxLen) {
            len = nMaxLen;
        }

        return len;
    }

    /**
     * 입력값의 길이를 체크하는 함수.
     * 길이를 초과한 경우 false 리턴.
     * <pre>
     * example>
     *      StringUtils.chkStrLength("def 한글", 8)
     *      result : false
     * </pre>
     *
     * @param pStr   문자열
     * @param chkLen 체크할 길이
     * @return 초과여부 리턴
     */
    public static boolean chkStrLength(String pStr, int chkLen) {
        int len = 0;
        boolean lenChk = true;
        try {
            if (pStr != null && !pStr.isEmpty()) {
                len = pStr.getBytes(StandardCharsets.UTF_8).length;
            }

            if (len > chkLen) {
                lenChk = false;
            }
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return lenChk;
    }

    /**
     * 입력값의 길이를 체크하는 함수.
     * 입력값의 길이를 체크해 초과하는 경우 Exception 오류처리.
     * <pre>
     * example>
     *      StringUtils.chkStrLength("def 한글", 8, "test")
     *      result : throw exception message "test 항목의 길이를 확인하십시오."
     * </pre>
     *
     * @param pStr   문자열
     * @param chkLen 체크할 길이
     * @param colNm  오류시 표시할 항목명
     */
    public static void chkStrLength(String pStr, int chkLen, String colNm) {
        int len = 0;

        try {
            if (pStr != null && !pStr.isEmpty()) {
                len = pStr.getBytes(StandardCharsets.UTF_8).length;
            }

            if (len > chkLen) {
                throw CommonException.builder().message(colNm + " 항목의 길이를 확인하십시오.").build();
            }
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * Euc-kr로 변환했다고 생각하고 pading 처리.
     * <pre>
     * utf-8 문자는 한글이 3바이트 euc-kr은 2byte.
     * padding 처리시 문제가 있어 euc-kr로 변경 되었다고 판단하고(2byte 계산),
     * padding 처리하는 함수.
     * </pre>
     * <pre>
     * example>
     *      StringUtils.getEucKrPadding(10, "test 한글", 0, '#')
     *      result : #test 한글
     * </pre>
     *
     * @param iMaxLen 변환 후 문자 길이
     * @param srcStr  변환할 문자
     * @param gbn     0:PADDING_LEFT/1:PADDING_RIGHT
     * @param wrd     추가할 문자
     * @return 변환 후 리턴
     */
    public static String getEucKrPadding(int iMaxLen, String srcStr, int gbn, char wrd) {
        String rv = "";
        String sTmp = "";
        int byteLen = 0;

        if (srcStr == null || srcStr.isEmpty()) {
            rv = StringUtils.stringPadding(iMaxLen, "", gbn, wrd);
        } else {
            for (int i = 0; i < srcStr.length(); i++) {
                sTmp = srcStr.substring(i, i + 1);

                if (isInclude3Byte(sTmp)) {
                    byteLen += 2;
                } else {
                    byteLen++;
                }
            }

            int incre = iMaxLen - byteLen;
            rv = StringUtils.stringPadding(srcStr.getBytes().length + incre, srcStr, gbn, wrd);
        }

        return rv;
    }

    /**
     * utf-8 에서 3byte 문자열이 있는지 확인
     * <pre>
     * example>
     *      StringUtils.isInclude3Byte("test 한글")
     *      result : true
     * </pre>
     *
     * @param input 확인할 문자
     * @return 3byte 문자 여부 리턴
     */
    public static boolean isInclude3Byte(String input) {
        for (int k = 0; k < input.length(); k++) {
            if (input.substring(k, k + 1).getBytes().length > 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * UTF-8 String 을 EUC-KR 형태의 글자 길이(2byte)로 계산된 길이의 배열로 만들어 리턴
     * <pre>
     * example>
     *      StringUtils.getEucKrCut("test#한글#abc", new int[] {4, 5, 4})
     *      result : [test, #한글, #abc]
     * </pre>
     *
     * @param srcStr  변환할 문자열
     * @param iMaxLen 배열로 만들 문자의 길이들
     * @return 변환 후 리턴
     */
    public static String[] getEucKrCut(String srcStr, int[] iMaxLen) {
        if (iMaxLen.length < 1) {
            throw CommonException.builder().message("입력된 배열의 길이를 확인하십시오.").build();
        }

        if (srcStr == null || srcStr.isEmpty()) {
            return new String[0];
        }

        String sCngData = srcStr;
        String sTmp = "";
        int byteLen = 0;
        String[] rv = new String[iMaxLen.length];

        for (int i = 0; i < iMaxLen.length; i++) {

            byteLen = 0;
            StringBuilder sTmpRv = new StringBuilder();
            for (int j = 0; j < sCngData.length(); j++) {
                sTmpRv.append(sCngData.substring(j, j + 1));
                sTmp = sCngData.substring(j, j + 1);

                if (sTmp.getBytes().length > 1) {
                    byteLen += 2;
                } else {
                    byteLen++;
                }

                if (iMaxLen[i] > byteLen) {
                    continue;
                } else if (iMaxLen[i] == byteLen) {
                    rv[i] = sTmpRv.toString();
                    sCngData = sCngData.substring(j + 1);
                    break;
                } else {
                    throw CommonException.builder().message("입력된 배열의 값을 확인하십시오.").build();
                }
            }
        }

        return rv;
    }

    /**
     * 정규표현식을 이용해서 천단위 콤마 찍기
     * <pre>
     * example>
     *      StringUtils.setComma("1234567890")
     *      result : 1,234,567,890
     * </pre>
     *
     * @param num 숫자형태의 문자열
     * @return 변환 후 리턴.
     */
    public static String setComma(String num) {

        //Null 체크
        if (num == null || num.trim().isEmpty()) return "0";

        String num2 = num.trim();

        //숫자형태가 아닌 문자열일경우 디폴트 0으로 반환
        String numberExpr = "^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][-+]?[0-9]+)?$";
        boolean isNumber = num2.matches(numberExpr);
        if (!isNumber) {
            throw CommonException.builder().message("숫자 여부를 확인하십시오.").build();
        }

        String strResult = num2; //출력할 결과를 저장할 변수
        Pattern p = Pattern.compile("(^[+-]?\\d+)(\\d{3})"); //정규표현식
        Matcher regexMatcher = p.matcher(num2);

        while (regexMatcher.find()) {
            strResult = regexMatcher.replaceAll("$1,$2"); //치환 : 그룹1 + "," + 그룹2
            regexMatcher.reset(strResult);
        }
        return strResult;
    }

    /**
     * long 입력값을  정규표현식을 이용해서 천단위 콤마 찍기.
     * 출력 폭에 맞춰 오른쪽 정렬해 리턴.
     * 지정한 출력 폭보다 입력값이 길다면, OVERFLOW_CHAR('#')으로 출력 폭만큼 채워서 반환함.
     *
     * <p>
     * <h4>example</h4>
     * </p>
     * <pre>
     * String num1 = StringUtils.longToComma(4919230000000000L, 15, StringUtils.CNV_PLUSSIGN);
     * String num2 = StringUtils.longToComma(1251000, 15, StringUtils.CNV_PLUSSIGN);
     * String num3 = StringUtils.longToComma(-500100, 15, StringUtils.CNV_PLUSSIGN);
     * System.out.println(num1);
     * System.out.println(num2);
     * System.out.println(num3);
     *
     * Output :
     * ###############
     *      +1,251,000
     *        -500,100
     *
     * </pre>
     *
     * @param lSrc  입력 숫자
     * @param nLen  출력 폭
     * @param nFlag 출력값 앞에 '+'(양수일 때, CNV_PLUSSIGN) 또는 '*'(CNV_ASTERISK) 문자 추가
     *              둘 다 추가도 가능(CNV_PLUSSIGN | CNV_ASTERISK)
     * @return 변환 후 리턴.
     */
    public static String longToComma(long lSrc, int nLen, int nFlag) {
        String numStr = Long.toString(lSrc);

        String resStr = numStr;
        try {
            // 할당한 자리에 표현할 수 없을 경우 '#'으로 채움
            int orgLen = numStr.length();
            if (orgLen > nLen) {
                char[] ch = new char[nLen];
                Arrays.fill(ch, OVERFLOW_CHAR);
                StringBuilder sb = new StringBuilder(nLen);
                sb.append(ch);
                return sb.toString();
            }

            // 더이상 문자 추가될 여지가 없을 경우, 그대로 리턴.
            if (orgLen == nLen) {
                return numStr;
            }

            //정규표현식. 적은 자리수부터 3자리씩 끊어서 grouping
            Pattern p = Pattern.compile("(^[+-]?\\d+)(\\d{3})");
            Matcher regexMatcher = p.matcher(numStr);

            while (regexMatcher.find()) {
                // overflow check
                if (orgLen >= nLen) {
                    break;
                }
                // 치환 : 그룹1 + "," + 그룹2
                resStr = regexMatcher.replaceAll("$1,$2");
                //치환된 문자열로 다시 matcher객체 얻기
                regexMatcher.reset(resStr);
                orgLen++;
            }

            if ((nFlag & CNV_PLUSSIGN) != 0 && (lSrc > 0) && (nLen > orgLen)) {
                resStr = PLUS_CHAR + resStr;
                orgLen++;
            }

            if ((nFlag & CNV_ASTERISK) != 0 && (nLen > orgLen)) {
                resStr = ASTERISK_CHAR + resStr;
                orgLen++;
            }
            // 출력 폭에 맞춰 오른쪽 정렬
            resStr = String.format("%" + nLen + "s", resStr);
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return resStr;
    }

    /**
     * double 입력값을  정규표현식을 이용해서 천단위 콤마 찍기.
     * 출력 폭에 맞춰 오른쪽 정렬해 리턴.
     * 지정한 출력 폭보다 입력값이 길다면, OVERFLOW_CHAR('#')으로 출력 폭만큼 채워서 반환함.
     * <pre>
     * example>
     *      StringUtils.doubleToComma(123.456789, 15, 5, StringUtils.CNV_PLUSSIGN)
     *      result :      +123.45679
     * </pre>
     *
     * @param dSrc   입력 숫자
     * @param nLen   출력 폭
     * @param nFract 소숫점 이하 정밀도 지정
     * @param nFlag  출력값 앞에 '+'(양수일 때, CNV_PLUSSIGN) 또는 '*'(CNV_ASTERISK) 문자 추가
     *               둘 다 추가도 가능(CNV_PLUSSIGN | CNV_ASTERISK)
     * @return 변환 후 리턴.
     */
    public static String doubleToComma(double dSrc, int nLen, int nFract, int nFlag) {
        String numStr = String.format("%." + nFract + "f", dSrc);

        try {
            // 할당한 자리에 표현할 수 없을 경우 '#'으로 채움
            StringBuilder sb = new StringBuilder(nLen);
            int orgLen = numStr.length();
            if (orgLen > nLen) {
                char[] ch = new char[nLen];
                Arrays.fill(ch, OVERFLOW_CHAR);
                sb.append(ch);
                return sb.toString();
            }

            // 정수부 소수부 분리
            String[] realStr = numStr.split("\\.");
            int decimalLen = 0;
            if (realStr.length > 1) {
                decimalLen = realStr[1].length() + 1;
            }

            // 정수부분 long 값으로 변환 후, longToComma() 이용
            long lSrc = Long.parseLong(realStr[0]);
            String resStr = longToComma(lSrc, nLen - decimalLen, nFlag);
            // 결과값 정수부와 실수부 결합
            sb.append(resStr);
            if (decimalLen > 0) {
                sb.append(".").append(realStr[1]);
            }

            return sb.toString();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 출력용 날짜 포멧 변경
     * <pre>
     * 입력 받은 delimiter로 년 월 일을 구분함
     * 'YYYYMMDD' 를 'YYYY-MM-DD' , YYYY/MM/DD' YYYY.MM.DD' 식으로 로 리턴
     * </pre>
     * <pre>
     * example>
     *      StringUtils.setDateFormat("19810904", "#")
     *      result : 1981#9#4
     * </pre>
     *
     * @param pdate     날짜입력(YYYYMMDD)
     * @param delimiter 구분자("-" , "/" , "." 등)
     * @return 출력용 날짜 리턴
     */
    public static String setDateFormat(String pdate, String delimiter) {

        if (pdate == null || pdate.trim().isEmpty() || pdate.trim().length() != 8) {
            throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();

        }

        if (!isDigit(pdate)) {
            throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
        }

        String sTmp = pdate.trim();

        String sYYYY = sTmp.substring(0, 4);
        String sMM = sTmp.substring(4, 6);
        String sDD = sTmp.substring(6, 8);

        return sYYYY + delimiter + Integer.parseInt(sMM) + delimiter + Integer.parseInt(sDD);
    }

    /**
     * 출력용 날짜 포멧 변경
     * <pre>
     * 'YYYYMMDD' 를 'YYYY년 MM년 DD일' 로 리턴
     * '01월' 은 '1월' 로 리턴
     * </pre>
     * <pre>
     * example>
     *      StringUtils.setDateFormat("19810904")
     *      result : 1981년 9월 4일
     * </pre>
     *
     * @param pdate 날짜입력(YYYYMMDD)
     * @return 출력용 날짜 리턴
     */
    public static String setDateFormat(String pdate) {

        if (pdate == null || pdate.trim().isEmpty() || pdate.trim().length() != 8) {
            throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
        }

        if (!isDigit(pdate)) {
            throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
        }

        String sTmp = pdate.trim();

        String sYYYY = sTmp.substring(0, 4);
        String sMM = sTmp.substring(4, 6);
        String sDD = sTmp.substring(6, 8);

        return sYYYY + "년 " + Integer.parseInt(sMM) + "월 " + Integer.parseInt(sDD) + "일";
    }

    /**
     * 문자열을 특정 값으로 설정하는 함수.
     * 문자열 처음부터 설정 src 문자로 길이만큼 채운 후 리턴.
     * <pre>
     * example>
     *      StringUtils.setSubStr("abcde 12345 fghi", '#', 5)
     *      result : ##### 12345 fghi
     * </pre>
     *
     * @param psDst 설정 대상 문자열
     * @param cSrc  설정 src 문자
     * @param nLen  설정할 길이
     * @return 설정된 문자열 리턴
     */
    public static String setSubStr(String psDst, char cSrc, int nLen) {
        try {
            // nLen 길이만큼 cSrc로 초기화
            char[] ch = new char[nLen];
            Arrays.fill(ch, cSrc);

            int dstLen = psDst.length();
            StringBuilder sb = new StringBuilder(dstLen);
            sb.append(ch).append(psDst.substring(nLen, dstLen));
            return sb.toString();
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 문자열 복사 함수.
     * 최대 Target의 Size만큼 복사후 남은공간 SPACE처리.
     * <pre>
     * example>
     *      StringUtils.strLCpyS("abcde 12345", 5)
     *      result : abcde
     * </pre>
     *
     * @param pzSrc  복사 src 문자열
     * @param nDstSz 복사 대상 크기
     * @return 복사된 문자열 리턴
     */
    public static String strLCpyS(String pzSrc, int nDstSz) {
        if (nDstSz < 0) {
            throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
        }

        try {
            // Target 문자열을 공백으로 초기화
            char[] ch = new char[nDstSz];
            Arrays.fill(ch, ' ');
            StringBuilder sb = new StringBuilder(nDstSz);
            sb.append(ch);

            // src 가 null 이라면 공백 문자열 리턴
            if (pzSrc == null) {
                return sb.toString();
            }

            // overflow없이 무조건 대상크기만큼 채워준다
            int cpyLen = pzSrc.length();
            if (cpyLen >= nDstSz) {
                return pzSrc.substring(0, nDstSz);
            }
            return sb.replace(0, cpyLen, pzSrc).toString();

        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 문자열 복사 함수
     * <pre>
     * 최대 Target의 Size만큼 복사.
     * 복사 src 문자열이 null 이면 빈 문자열 리턴.
     * <pre>
     * example>
     *      StringUtils.strLCpy("abcde 12345", 5)
     *      result : abcde
     * </pre>
     *
     * @param pzSrc  복사 src 문자열
     * @param nDstSz 복사 대상 크기
     * @return 복사된 문자열 리턴
     */
    public static String strLCpy(String pzSrc, int nDstSz) {
        if (pzSrc == null) {
            return "";
        }
        if (nDstSz < 0 || nDstSz > pzSrc.length()) {
            throw CommonException.builder().message(Constants.INPUT_PARAM_ERROR_MESSAGE).build();
        }
        try {
            return pzSrc.substring(0, nDstSz);
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 출력 폭을 지정하여  long 을 문자열로 변환.
     * 출력 폭에 맞춰 오른쪽 정렬해 리턴.
     * 지정한 출력 폭보다 결과값이 길다면, OVERFLOW_CHAR('#')으로 출력 폭만큼 채워서 반환함.
     * <pre>
     * example>
     *      StringUtils.longToStrWithPadding(123456789, 15, true)
     *      result : 000000123456789
     * </pre>
     *
     * @param lSrc        변환할 수
     * @param nLen        출력 폭
     * @param zeroPadding left padding 문자를 '0' 또는 ' ' 선택
     * @return 문자열로 변환해 리턴
     */
    public static String longToStrWithPadding(long lSrc, int nLen, boolean zeroPadding) {
        try {
            String resStr = null;
            if (zeroPadding) {
                resStr = String.format("%0" + nLen + "d", lSrc);
            } else {
                resStr = String.format("%" + nLen + "d", lSrc);
            }

            char[] ch = new char[nLen];
            if (resStr.length() > nLen) {
                Arrays.fill(ch, OVERFLOW_CHAR);
                StringBuilder sb = new StringBuilder(nLen);
                sb.append(ch);
                return sb.toString();
            }

            return resStr;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 출력 폭을 지정하여 double 을 문자열로 변환.
     * 출력 폭에 맞춰 오른쪽 정렬해 리턴.
     * 지정한 출력 폭보다 결과값이 길다면, OVERFLOW_CHAR('#')으로 출력 폭만큼 채워서 반환함.
     * <pre>
     * example>
     *      StringUtils.doubleToStrWithPadding(123.456789, 15, 3, true)
     *      result : 00000000123.457
     * </pre>
     *
     * @param dValue      변환할 수
     * @param nLen        출력 폭
     * @param nFract      소숫점 이하 정밀도 지정
     * @param zeroPadding left padding 문자를 '0' 또는 ' ' 선택
     * @return 문자열로 변환해 리턴
     */
    public static String doubleToStrWithPadding(double dValue, int nLen, int nFract, boolean zeroPadding) {
        try {
            String resStr = null;
            if (zeroPadding) {
                resStr = String.format("%0" + nLen + "." + nFract + "f", dValue);
            } else {
                resStr = String.format("%" + nLen + "." + nFract + "f", dValue);
            }

            char[] ch = new char[nLen];
            if (resStr.length() > nLen) {
                Arrays.fill(ch, OVERFLOW_CHAR);
                StringBuilder sb = new StringBuilder(nLen);
                sb.append(ch);
                return sb.toString();
            }

            return resStr;
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * Map의 항목들 Trim처리
     * <pre>{@code
     * example>
     *      Map<String, String> testMap = new HashMap<>();
     *      testMap.put("test1", "  1 test   ");
     *      testMap.put("test2", "2   test   ");
     *      StringUtils.mapTrim(testMap);
     *
     *      result : {test2=2   test, test1=1 test}
     * }</pre>
     *
     * @param inputMap trim 처리될 값들이 저장되어있는 Map
     */
    public static void mapTrim(Map<String, String> inputMap) {
        for (Map.Entry<String, String> entry : inputMap.entrySet()) {
            try {
                String keyMapVal = entry.getValue();
                entry.setValue(keyMapVal.trim());
            } catch (RuntimeException e) {
                log.info("key: {}, value: {}", entry.getKey(), entry.getValue());
            }
        }
    }


    /**
     * SecureRandom을 이용하여, 원하는 자릿수의 랜덤 문자열 생성
     * <pre>
     * example>
     *      StringUtils.generateSecretString(10)
     *      result : e1f06ac638
     * </pre>
     *
     * @param length 생성될 문자열 자릿수
     * @return 생성된 랜덤 문자열
     */
    public static String generateSecretString(int length) {
        SecureRandom secRandom = new SecureRandom();

        byte[] result = new byte[length];
        secRandom.nextBytes(result);

        return Hex.encodeHexString(result).substring(0, length);
    }
}
