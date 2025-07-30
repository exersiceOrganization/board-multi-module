package com.example.utils.core;

import com.example.exception.CommonException;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@UtilityClass
public class NumberUtils {

    /**
     * 입력 string의 처음부터 일정부분을 BigDecimal 값으로 변환.
     * <pre>
     *     BigDecimal converted =
     *          NumberUtils.getDecimalFromString("1234.5678ABCDE", 9);
     * </pre>
     *
     * @param num       변환 대상 문자열
     * @param length    변환할 길이
     * @return          변환 결과값
     */
    public static BigDecimal getDecimalFromString(String num, int length){

        BigDecimal rtVal = null;
        try {
            num = num.substring(0, length);
            rtVal = new BigDecimal(num);
        } catch (NumberFormatException ex) {
            throw CommonException.builder().message("숫자 포멧을 확인하십시오.").build();
        } catch (RuntimeException ex) {
            // 20338 처리중 오류가 발생했습니다.
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return rtVal;
    }

    /**
     * 입력받은 숫자로 한글금액 변경
     * <p>
     * <h4>example</h4>
     * <pre>
     * String numStr = NumberUtils.longToKor(100000000);
     * System.out.println(numStr);
     *
     * Output :
     * 일억
     *
     * String numStr2 = NumberUtils.longToKor(48_653_000);
     * System.out.println(numStr2);
     *
     * Output :
     * 사천팔백육십오만삼천
     *
     * String numStr3 = NumberUtils.longToKor(512232400000000L);
     * System.out.println(numStr3);
     *
     * Output :
     * 오백일십이조이천삼백이십사억
     * </pre>
     * </p>
     *
     *
     * @param lVal 한글변환 숫자
     * @return 한글금액 변경 리턴
     */
    public static String longToKor(long lVal){
        String zeroStr = "영";
        String minusStr = "-";
        String cmpZero = "0000";    // compareZero
        String[] zDigitStr = {"", "일", "이", "삼", "사", "오", "육", "칠", "팔", "구"};
        String[] zBaseStr0 = {"", "십", "백", "천"};
        String[] zBaseStr1 = {"", "만", "억", "조"};

        StringBuilder sb = new StringBuilder();

        // '영' 처리
        if (lVal == 0) {
            sb.append(zeroStr);
            return sb.toString();
        }

        long absVal = lVal;
        // 음수 값은 '-' prefix를 붙임
        if (lVal < 0){
            sb.append(minusStr);
            absVal = lVal * -1;
        }

        String numStr = String.valueOf(absVal);
        int numLen = numStr.length();
        if (numLen > 16) {
            throw CommonException.builder().message("입력값을 확인하십시오").build();
        }

        try {
            for (int i = numLen - 1; i >= 0; i--) {
                // 앞자리부터 한자리씩 처리.
                String idxStr = numStr.substring(numLen - i - 1, numLen - i);
                int nDigitIdx = Integer.parseInt(idxStr);

                // 각 단위 숫자(일 ~ 구) 처리
                sb.append(zDigitStr[nDigitIdx]);
                // 천 단위(십 / 백 / 천)
                if (nDigitIdx > 0) {
                    sb.append(zBaseStr0[i % 4]);
                }
                // 만 단위 이상(만 / 억 / 조)
                // 천 단위가 전부 '0'이면, 해당 단위 skip.
                int moduler = i % 4;
                if (moduler == 0 && !cmpZero.equals(numStr.substring(Math.max(numLen - i - 4, 0), numLen - 1))) {
                    sb.append(zBaseStr1[i / 4]);
                }
            }
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return sb.toString();
    }

    /**
     * BigDecimal 값을 받아서 원하는 자리수로 반올림, 올림, 버림 처리.
     * <pre>
     *     BigDecimal converted =
     *          Numberutils.setScalePrc(BigDecimal.valuesOf(3456), -3, RoundingMode.Up);
     *          result : 4000
     * </pre>
     *
     * @param num       입력 값
     * @param scale     변환 자리수.
     * @param roundTp   적용할 반올림모드 {@link java.math.BigDecimal}
     * @return          처리 완료된 리턴 값
     */
    public static BigDecimal setScalePrc(BigDecimal num, int scale, int roundTp){
        return num.setScale(scale, RoundingMode.valueOf(roundTp));
    }

    /**
     * BigDecimal 값을 받아서 원하는 자리수로 반올림, 올림, 버림 처리.
     *<pre>
     * example>
     *      BigDecimal converted =
     *              NumberUtils.setScalePrc(BigDecimal.valueOf(3456), -3, RoundingMode.UP)
     *      result : 4000
     * </pre>
     *
     * @param num     입력 값
     * @param scale   변환 자리수.
     * @param roundTp 적용할 반올림 모드. {@link java.math.RoundingMode}
     * @return        처리 완료된 리턴 값
     */
    public static BigDecimal setScalePrc(BigDecimal num, int scale, RoundingMode roundTp){
        return num.setScale(scale, roundTp);
    }

    /**
     * long 값을 받아서 원하는 자리수로 올림 처리.
     * 변환 자리수는 음수값으로 나타냄
     * <pre>
     *     NumberUtils.longRoundUp(123, -1);
     *     result : 130
     * </pre>
     *
     * @param lSrc  입력 값.
     * @param nPos  변환 자리수. 음수값.
     * @return      처리 완료된 리턴 값
     */
    public static long longRoundUp(long lSrc, int nPos){
        return setScalePrc(BigDecimal.valueOf(lSrc), nPos, RoundingMode.UP).longValue();
    }

    /**
     * long 값을받아서 원하는 자리수로 버림 처리.
     * 변환 자리수는 음수값으로 나타냄.
     * <pre>
     * example>
     *      NumberUtils.longRoundDown(125, -1)
     *      result : 120
     * </pre>
     *
     * @param lSrc 입력 값
     * @param nPos 변환 자리수. 음수값.
     * @return 처리 완료된 리턴 값
     */
    public static long longRoundDown(long lSrc, int nPos) {
        return setScalePrc( BigDecimal.valueOf(lSrc), nPos, RoundingMode.DOWN).longValue();
    }

    /**
     * long 값을받아서 원하는 자리수로 반올림 처리. 변환 자리수는 음수값으로 나타냄.
     * <pre>
     * example>
     *      NumberUtils.longRoundOff(124, -1)
     *      result : 120
     *      NumberUtils.longRoundOff(125, -1)
     *      result : 130
     * </pre>
     *
     * @param lSrc 입력 값
     * @param nPos 변환 자리수. 음수값.
     * @return 처리 완료된 리턴 값
     */
    public static long longRoundOff(long lSrc, int nPos) {
        return setScalePrc( BigDecimal.valueOf(lSrc), nPos, RoundingMode.HALF_UP).longValue();
    }


    /**
     * double 값을받아서 기준소수점자리위치 미만자리를 올림 처리.
     * 변환 자리수는 양수값으로 나타냄.
     * <pre>
     * example>
     *      NumberUtils.doubleRoundUp(125.1, 0)
     *      result : 126.0
     *      NumberUtils.doubleRoundUp(125.14, 1)
     *      result : 125.2
     * </pre>
     *
     * @param dSrc 입력 값
     * @param nPos 변환 자리수. 양수값.
     * @return 처리 완료된 리턴 값
     */
    public static double doubleRoundUp(double dSrc, int nPos) {
        return setScalePrc( BigDecimal.valueOf(dSrc), nPos, RoundingMode.UP).doubleValue();
    }

    /**
     * double 값을받아서 기준소수점자리위치 미만자리를 버림 처리.
     * 변환 자리수는 양수값으로 나타냄.
     * <pre>
     * example>
     *      NumberUtils.doubleRoundDown(125.6, 0)
     *      result : 125.0
     *      NumberUtils.doubleRoundDown(125.16, 1)
     *      result : 125.1
     * </pre>
     *
     * @param dSrc 입력 값
     * @param nPos 변환 자리수. 양수값.
     * @return 처리 완료된 리턴 값
     */
    public static double doubleRoundDown(double dSrc, int nPos) {
        return setScalePrc( BigDecimal.valueOf(dSrc), nPos, RoundingMode.DOWN).doubleValue();
    }

    /**
     * double 값을받아서 기준소수점자리위치 미만자리를 반올림 처리.
     * 변환 자리수는 양수값으로 나타냄.
     * <pre>
     * example>
     *      NumberUtils.doubleRoundOff(125.6, 0)
     *      result : 126.0
     *      NumberUtils.doubleRoundOff(125.16, 1)
     *      result : 125.2
     * </pre>
     *
     * @param dSrc 입력 값
     * @param nPos 변환 자리수. 양수값.
     * @return 처리 완료된 리턴 값
     */
    public static double doubleRoundOff(double dSrc, int nPos) {
        return setScalePrc( BigDecimal.valueOf(dSrc), nPos, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * long 값을받아서 원하는 자리수로 버림 처리.
     * <pre>
     * example>
     *      NumberUtils.trunclong(123, -1)
     *      result : 120
     * </pre>
     *
     * @param src   입력 값
     * @param nPos  변환 자리수. 음수값.
     * @return  처리 완료된 리턴 값
     */
    public static long truncLong(long src, int nPos) {
        return setScalePrc( new BigDecimal(src), nPos, RoundingMode.DOWN).longValue();
    }

    /**
     * BigDecimal 값을 받아서 원하는 자리수로 버림 처리.
     * <pre>
     * example>
     *      NumberUtils.truncDecimal(BigDecimal.valueOf(123.456), 1)
     *      result : 123.4
     * </pre>
     *
     * @param src   입력 값
     * @param nPos  변환 자리수.
     * @return  처리 완료된 리턴 값
     */
    public static BigDecimal truncDecimal(BigDecimal src, int nPos) {
        return setScalePrc( src, nPos, RoundingMode.DOWN);
    }

    /**
     * 입력받은 문자열과 문자열 배열을 비교하여, 일치하는 배열의 index 를 리턴.
     * 일치하는 String 이 없다면 -1 리턴.
     * <pre>
     * example>
     *      String[] targetArray = { "123", "456", "abcd", "efgh"};
     *      int resultIndex = NumberUtils.inStringArray("456", targetArray);
     *      result : 1
     * </pre>
     *
     * @param pzSrc 체크 대상 문자열
     * @param zData 체크 대상과 비교할 문자열들
     * @return 체크 대상 문자열과 일치하는 문자열 배열의 index
     */
    public static int inStringArray(String pzSrc, String[] zData) {
        try {
            // 배열을 List 로 변환
            List<String> strArr = Arrays.asList(zData);

            return strArr.indexOf(pzSrc);
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * 입력받은 수와 숫자 배열을 비교하여, 일치하는 배열의 index 를 리턴.
     * 일치하는 수가 없다면 -1 리턴.
     * <pre>
     * example>
     *      long[] longArray = { 123, 456, 789, 123000};
     *      int resultIndex = NumberUtils.inLongArray(123000, longArray);
     *      result : 3
     * </pre>
     *
     * @param lSrc 체크 대상 수
     * @param lData 체크 대상과 비교할 숫자 배열
     * @return 체크 대상 수와 일치하는 숫자 배열의 index
     */
    public static int inLongArray(long lSrc, long[] lData) {
        try {
            // primitive type 배열을 object type List로 변환
            List<Long> longList = Arrays.stream(lData).boxed().collect(Collectors.toList());

            return longList.indexOf(lSrc);
        } catch (RuntimeException ex) {
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }
    }

    /**
     * SecureRandom을 이용하여, 원하는 자릿수의 랜덤 숫자 생성
     * <pre>
     * example>
     *      NumberUtils.generateSecretLongNumber(10)
     *      result : 6140634638
     * </pre>
     *
     * @param length    생성될 숫자 자릿수
     * @return  생성된 랜덤 숫자
     */
    public static long generateSecretLongNumber(int length) {
        long lowerLimit = (long) Math.pow(10, length - (double) 1);
        long upperLimit = (long) Math.pow(10, length);

        SecureRandom secRandom = new SecureRandom();
        return secRandom.nextLong(lowerLimit, upperLimit);
    }

    /**
     * java17의 RandomGenerator.nextLong(origin, bound) 의 대안 함수
     *
     * @param origin    the least value that can be returned
     * @param bound     the upper bound (exclusive) for the returned value
     * @return  a pseudorandomly chosen long value between the origin (inclusive) and the bound (exclusive)
     */
    public static long nextLong(long origin, long bound) {
        SecureRandom secRandom = new SecureRandom();

        long r = secRandom.nextLong();
        long n = bound - origin;
        long m = n - 1;
        if ((n & m) == 0L) // power of two
            r = (r & m) + origin;
        else if (n > 0L) { // reject over-represented candidates
            for (long u = r >>> 1; // ensure nonnegative
                 u + m - (r = u % n) < 0L; // rejection check
                 u = secRandom.nextLong() >>> 1) // retry
                ;
            r += origin;
        } else { // range not representable as long
            while (r < origin || r >= bound)
                r = secRandom.nextLong();
        }
        return r;
    }
}
