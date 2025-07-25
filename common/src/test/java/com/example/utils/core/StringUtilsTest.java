package com.example.utils.core;

import ch.qos.logback.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public class StringUtilsTest {

    @Test
    void testCheckStr(){
        String testStr = "abc 12345";
        boolean result = StringUtils.isSpace(testStr);
        log.debug("isSpace : {}", result);
        assertEquals(true, result);

        result = StringUtils.isSpace(testStr, 3);
        log.debug("isSpace(checkStr, length) : {}", result);
        assertEquals(false, result);

        testStr = "     12345";
        result = StringUtils.isAllSpace(testStr, 5);
        log.debug("isAllSpace : {}", result);
        assertEquals(true, result);

        result = StringUtils.isEmpty("     ");
        log.debug("isEmpty : {}", result);
        assertEquals(false, result);

        result = StringUtils.isBlank("               ");
        log.debug("isBlank : {}", result);
        assertEquals(true, result);

        testStr = "123a45";
        result = StringUtils.isDigit(testStr);
        log.debug("isDigit : {}", result);
        assertEquals(false, result);

        result = StringUtils.isDigit(testStr, 3);
        log.debug("isDigit(checkStr, length) : {}", result);
        assertEquals(true, result);

        testStr = "abc1def#";
        result = StringUtils.hasDigit(testStr);
        log.debug("isDigit(checkStr, length) : {}", result);
        assertEquals(true, result);

        result = StringUtils.hasDigit(testStr, 3);
        log.debug("isDigit(checkStr, length) : {}", result);
        assertEquals(false, result);

        result = StringUtils.isAlpha(testStr);
        log.debug("isAlpha : {}", result);
        assertEquals(false, result);

        result = StringUtils.isAlpha(testStr, 3);
        log.debug("isAlpha(checkStr, length) : {}", result);
        assertEquals(true, result);

        result = StringUtils.isDigitAlpha(testStr);
        log.debug("isDigitAlpha : {}", result);
        assertEquals(false, result);


        result = StringUtils.isDigitAlpha(testStr, 3);
        log.debug("isDigitAlpha(checkStr, length) : {}", result);
        assertEquals(true, result);

        testStr = "abcDef";
        result = StringUtils.isLowerCase(testStr);
        assertEquals(false, result);
    }

    @Test
    void testConvert(){

    }

    @Test
    void testModification(){

    }

    @Test
    void testMasking(){

    }

    @Test
    void testEncoding(){

    }

    @Test
    void testFormat(){
        log.debug("getFormatString : {}", StringUtils.getFormatString(123.456789, "%015f_"));
    }

    @Test
    void testGenerateSecretString(){
        String result = StringUtils.generateSecretString(10);
        log.debug("generateSecretString : {}", result);
    }
}
