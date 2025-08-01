package com.example.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public class NumberUtilsTest {

    @Test
    void testTransform(){
        String testStr = "1234.5678ABCDE";
        BigDecimal converted = NumberUtils.getDecimalFromString(testStr, 9);
        log.debug("converted : {}", converted);
        assertEquals(new BigDecimal("1234.5678"), converted);

        String korStr = NumberUtils.longToKor(456789000);
        log.debug("korStr : {}", korStr);
        assertEquals("사억오천육백칠십팔만구천", korStr);
    }

    @Test
    void testSetScalePrc(){
        int testVal = 3456;
        int resultVal = NumberUtils.setScalePrc(BigDecimal.valueOf(testVal), -3, RoundingMode.UP).intValue();
        log.debug("testVal : {} -> RoundUP : {}", testVal, resultVal);
        assertEquals(4000, resultVal);

        testVal = 56;
        resultVal = NumberUtils.setScalePrc(BigDecimal.valueOf(testVal), -3, RoundingMode.UP).intValue();
        log.debug("testVal : {} -> Roundup : {}", testVal, resultVal);
        assertEquals(1000, resultVal);

        // 올림
        long resultLong = NumberUtils.longRoundUp(121, -1);
        log.debug("resultLong_up : {}", resultLong);
        assertEquals(130, resultLong);
        // 내림
        resultLong = NumberUtils.longRoundDown(129, -1);
        log.debug("resultLong_down : {}", resultLong);
        assertEquals(120, resultLong);
        // 반올림
        resultLong = NumberUtils.longRoundOff(124, -1);
        log.debug("resultLong_off : {}", resultLong);
        assertEquals(120, resultLong);
        resultLong = NumberUtils.longRoundOff(125, -1);
        log.debug("resultLong_off : {}", resultLong);
        assertEquals(130, resultLong);
    }

    @Test
    void testDouble(){
        // 올림
        double resultDbl = NumberUtils.doubleRoundUp(125.2, 0);
        log.debug("up1 : {}", resultDbl);
        assertEquals(126, resultDbl);
        resultDbl = NumberUtils.doubleRoundUp(125.21, 1);
        log.debug("up2 : {}", resultDbl);
        assertEquals(125.3, resultDbl);
        // 내림
        resultDbl = NumberUtils.doubleRoundDown(123.8, 0);
        log.debug("down1 : {}", resultDbl);
        assertEquals(123, resultDbl);
        resultDbl = NumberUtils.doubleRoundDown(123.89, 1);
        log.debug("down2 : {}", resultDbl);
        assertEquals(123.8, resultDbl);
        // 반올림
        resultDbl = NumberUtils.doubleRoundOff(123.89, 1);
        log.debug("off1 : {}", resultDbl);
        assertEquals(123.9, resultDbl);
        resultDbl = NumberUtils.doubleRoundOff(123.83, 1);
        log.debug("off2 : {}", resultDbl);
        assertEquals(123.8, resultDbl);
    }

    @Test
    void testTrunc(){
        // 값을 받아 버림 처리
        long resultLong = NumberUtils.truncLong(129, -1);
        log.debug("truncLong : {}", resultLong);
        assertEquals(120, resultLong);

        BigDecimal converted = NumberUtils.truncDecimal(BigDecimal.valueOf(123.456), 1);
        log.debug("truncDecimal : {}", converted);
        assertEquals(BigDecimal.valueOf(123.4), converted);
    }

    @Test
    void testCompare(){
        String[] strArr = { "123", "456", "abcd", "efgh" };
        int resultIdx = NumberUtils.inStringArray("123", strArr);
        log.debug("isStringArray : {}", resultIdx);
        assertEquals(0, resultIdx);

        long[] longArr = { 123, 456, 789, 123000};
    }

    @Test
    void testGenerateSecretLongNumber(){
        // length = 20 을 넣으면 long 데이터보더 커서 예외발생
        int length = 15;
        for (int i = 0; i < 1; i++) {
            long secNum = NumberUtils.generateSecretLongNumber(length);
            log.debug("secNum : {}", secNum);
            assertEquals(length, Long.toString(secNum).length());
        }
    }
}
