package com.example.utils.core;

import ch.qos.logback.core.util.StringUtil;
import com.example.constant.Constants;
import com.example.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public class StringUtilsTest {

    @Test
    void testCheck() {
        boolean result = StringUtils.isSpace("abc 12345");
        log.debug("isSpace : {}", result);
        assertEquals(true, result);
        result = StringUtils.isSpace("abc 12345", 3);
        log.debug("isSpace : {}", result);
        assertEquals(false, result);

        result = StringUtils.isAllSpace("   12345", 3);
        log.debug("isAllSpace : {}", result);
        assertEquals(true, result);

        result = StringUtils.isEmpty(null);
        log.debug("isEmpty : {}", result);
        assertEquals(true, result);

        result = StringUtils.isBlank("    ");
        log.debug("isBlank : {}", result);
        assertEquals(true, result);

        result = StringUtils.isDigit("123a456");
        log.debug("isDigit : {}", result);
        assertEquals(false, result);
        result = StringUtils.isDigit("123a456", 3);
        log.debug("isDigit : {}", result);
        assertEquals(true, result);

        result = StringUtils.hasDigit("abc1def");
        log.debug("hasDigit : {}", result);
        assertEquals(true, result);
        result = StringUtils.hasDigit("abc1def", 3);
        log.debug("hasDigit : {}", result);
        assertEquals(false, result);

        result = StringUtils.isAlpha("abc1def");
        log.debug("isAlpha : {}", result);
        assertEquals(false, result);
        result = StringUtils.isAlpha("abc1def", 3);
        log.debug("isAlpha : {}", result);
        assertEquals(true, result);

        result = StringUtils.isDigitAlpha("abc1#def");
        log.debug("isDigitAlpha : {}", result);
        assertEquals(false, result);
        result = StringUtils.isDigitAlpha("abc1#def", 3);
        log.debug("isDigitAlpha : {}", result);
        assertEquals(true, result);

        result = StringUtils.isLowerCase("abcDef");
        log.debug("isLowerCase : {}", result);
        assertEquals(false, result);
        result = StringUtils.isLowerCase("abcDef", 3);
        log.debug("isLowerCase : {}", result);
        assertEquals(true, result);

        result = StringUtils.isUpperCase("ABCdEF");
        log.debug("isUpperCase : {}", result);
        assertEquals(false, result);
        result = StringUtils.isUpperCase("ABCdEF", 3);
        log.debug("isUpperCase : {}", result);
        assertEquals(true, result);

        result = StringUtils.isAllZero("0000 0000", 5);
        log.debug("isAllZero : {}", result);
        assertEquals(false, result);

        result = StringUtils.isHexNumStr("16afeg567", 7);
        log.debug("isHexNumStr : {}", result);
        assertEquals(false, result);
    }

    @Test
    void testConvert() {
        String result = StringUtils.getLowerCase("16AFEG567");
        log.debug("getLowerCase : {}", result);
        assertEquals("16afeg567", result);
        result = StringUtils.getLowerCase("16AFEG567", 5);
        log.debug("getLowerCase : {}", result);
        assertEquals("16afe", result);

        result = StringUtils.getUpperCase("16afeg567");
        log.debug("getUpperCase : {}", result);
        assertEquals("16AFEG567", result);
        result = StringUtils.getUpperCase("16afeg567", 5);
        log.debug("getUpperCase : {}", result);
        assertEquals("16AFE", result);

        result = StringUtils.getTrim("   16afeg567   ");
        log.debug("getTrim : {}", result);
        assertEquals("16afeg567", result);

        result = StringUtils.getLeftTrim("   16afeg567   ");
        log.debug("getLeftTrim : {}", result);
        assertEquals("16afeg567   ", result);
        result = StringUtils.getRightTrim("   16afeg567   ");
        log.debug("getRightTrim : {}", result);
        assertEquals("   16afeg567", result);

        result = StringUtils.getFormatString(123.456789, "%015f_");
        log.debug("getFormatString : {}", result);
        assertEquals("00000123.456789_", result);
    }

    @Test
    void testModification() {
        int result = StringUtils.compareString("abc", "acb");
        log.debug("compareString : {}", result);
        assertEquals(-1, result);
        result = StringUtils.compareString("abcdef", "abcdfe", 3);
        log.debug("compareString : {}", result);
        assertEquals(0, result);
        result = StringUtils.compareString("abcdef", "abCdfe", 3, true);
        log.debug("compareString : {}", result);
        assertEquals(0, result);

        String resultString = StringUtils.concateString("abc", "acb");
        log.debug("concateString : {}", resultString);
        assertEquals("abcacb", resultString);

        resultString = StringUtils.changeDoubleToString(123.456);
        log.debug("changeDoubleToString : {}", resultString);
        assertEquals("123.456", resultString);
        resultString = StringUtils.changeIntToString(123456);
        assertEquals("123456", resultString);
        resultString = StringUtils.changeLongToString(123456789000L);
        assertEquals("123456789000", resultString);

        log.debug("stringLen : {}", StringUtils.stringLen("abcd"));
        log.debug("stringIndex : {}", StringUtils.stringIndex("abcdefg", "ef"));
        log.debug("stringConcat : {}", StringUtils.stringConcat("abc", "defghij", 2));
        log.debug("nvl : {}", StringUtils.nvl(null, "empty string"));
        log.debug("stringPadding : {}", StringUtils.stringPadding(10, "test", 0, '#'));
        log.debug("stringReplace : {}", StringUtils.stringReplace("123test321", '1', '#') );
        log.debug("stringRmAll : {}", StringUtils.stringRmAll("123test321", "test") );
        log.debug("stringToInt : {}", StringUtils.stringToInt("123test321", 3) );
        log.debug("stringToLong : {}", StringUtils.stringToLong("1234567890", 9) );
        log.debug("stringToDouble : {}", StringUtils.stringToDouble("123.4567890") );
    }

    @Test
    void testMasking() {
        String resultString = StringUtils.masking("8109041234567", "1");
        log.debug("masking : {}", resultString);
        assertEquals("8109041******", resultString);

        resultString = StringUtils.maskingView("8109041234567", "1");
        log.debug("maskingView : {}", resultString);
        assertEquals("810904-1******", resultString);

        resultString = StringUtils.automaskingView("8109041234567", "1", "1");
        log.debug("automaskingView : {}", resultString);
        assertEquals("810904-1******", resultString);
        log.debug("automaskingView : {}", StringUtils.automaskingView("8109041234567", "1", "1", false) );
        log.debug("automaskingView : {}", StringUtils.automaskingView("6458602159", "1", "1", false) );
    }

    @Test
    void testEncoding() {
        log.debug("chkHangul : {}", StringUtils.chkHangul("abc def 한글 ghi") );
        log.debug("ecdUtfToEucKr : {}", StringUtils.ecdUtfToEucKr("def 한글") );
        log.debug("ecdUtfToEucKr : {}", StringUtils.ecdEucKrToUtf(new byte[] {100, 101, 102, 32, -57, -47, -79, -37}) );
        log.debug("subStrByte : {}", StringUtils.subStrByte(new byte[] {100, 101, 102, 32, -57, -47, -79, -37}, 4, 2) );
        log.debug("subStrByte : {}", StringUtils.subStrByte(new byte[] {100, 101, 102, 32, -57, -47, -79, -37}, 4) );
        log.debug("unicodRvsn : {}", StringUtils.unicodRvsn(new byte[] {100, 101, 102, 32, -57, -47, -79, -37}, 5) );
    }

    @Test
    void testFormat() {
        log.debug("getHttpDate : {}", StringUtils.getHttpDate(Locale.KOREA) );
        log.debug("getStrLength : {}", StringUtils.getStrLength("def 한글") );
        log.debug("getStrNByteLength : {}", StringUtils.getStrNByteLength("def 한글", 8) );
        log.debug("chkStrLength : {}", StringUtils.chkStrLength("def 한글", 8) );

        assertThatExceptionOfType(CommonException.class)
                .isThrownBy(() -> { StringUtils.chkStrLength("def 한글", 8, "test"); })
                .withMessage("test 항목의 길이를 확인하십시오.");

        log.debug("getEucKrPadding : {}", StringUtils.getEucKrPadding(10, "test 한글", 0, '#') );
        log.debug("isInclude3Byte : {}", StringUtils.isInclude3Byte("test 한글") );
        log.debug("getEucKrCut : {}", Arrays.asList(StringUtils.getEucKrCut("test#한글#abc", new int[] {4, 5, 4})) );

        log.debug("setComma : {}", StringUtils.setComma("1234567890") );
        log.debug("longToComma : {}", StringUtils.longToComma(4919230000000000L, 15, StringUtils.CNV_PLUSSIGN) );
        log.debug("longToComma : {}", StringUtils.longToComma(1251000, 15, StringUtils.CNV_PLUSSIGN) );
        log.debug("longToComma : {}", StringUtils.longToComma(125, 15, StringUtils.CNV_PLUSSIGN) );
        log.debug("doubleToComma : {}", StringUtils.doubleToComma(123.456789, 15, 5, StringUtils.CNV_PLUSSIGN) );

        log.debug("setDateFormat : {}", StringUtils.setDateFormat("19810904", "#") );
        log.debug("setDateFormat : {}", StringUtils.setDateFormat("19810904") );

        log.debug("setSubStr : {}", StringUtils.setSubStr("abcde 12345 fghi", '#', 5) );
        log.debug("strLCpyS : {}", StringUtils.strLCpyS("abcde 12345 fghi", 5) );
        log.debug("strLCpy : {}", StringUtils.strLCpy("abcde 12345", 5) );

        log.debug("longToStrWithPadding : {}", StringUtils.longToStrWithPadding(123456789, 15, true) );
        log.debug("doubleToStrWithPadding : {}", StringUtils.doubleToStrWithPadding(123.456789, 15, 3, true) );

        Map<String, String> testMap = new HashMap<>();
        testMap.put("test1", "  1 test   ");
        testMap.put("test2", "2   test   ");
        StringUtils.mapTrim(testMap);
        log.debug("testMap : {}", testMap );
    }

    @Test
    void testGenerateSecretString() {
        String result = StringUtils.generateSecretString(10);
        log.debug("generateSecretString : {}", result);
        assertEquals(10, result.length());
    }

    @Test
    void testStringReplace() {
        String fileSeparator = File.separator;
        String origin = "abc def 한글 ghi";
        String expect = "\\abc_def_한글_ghi";
        String result = fileSeparator + StringUtils.stringReplace(origin, ' ', '_');
        log.debug("result : {}", result);
        assertEquals(expect, result);
    }

    @Test
    void testStringEncodingConvert() {
        try {
//            String origin = "한글 ghi 123";
//            String ksc5601Str = new String(origin.getBytes("utf-8"), "ksc5601");
//            log.debug("origin ({}), size({}) to ksc5601({}), size({})",
//                    origin, origin.getBytes("utf-8").length, ksc5601Str, ksc5601Str.getBytes().length);
//            String convertedResult = Hex.encodeHexString(ksc5601Str.getBytes());
//            log.debug("bytes : {}", convertedResult);
//            byte[] converted = org.apache.commons.codec.binary.StringUtils.getBytesUnchecked(origin, "ksc5601");
//            log.debug("converted({}) : {}", converted.length, Hex.encodeHexString(converted));
//
//            String reconverted = new String(ksc5601Str.getBytes("ksc5601"), "utf-8");
//            log.debug("reconverted({}) : {}", reconverted.getBytes().length, reconverted);

            // "def    한　Ａghi 123" : 20 bytes
            byte[] ksc5601bytes = {
                    (byte)0xc7, (byte)0x20,
                    0x64, 0x65, 0x66, 0x20, 0x20, 0x20, 0x20,
                    (byte) 0xc7, (byte) 0xd1, (byte) 0xa1, (byte) 0xa1, (byte) 0xa3, (byte) 0xc1,
                    0x67, 0x68, 0x69, 0x20, 0x31, 0x32, 0x33
            };
            log.info("ksc5601bytes : {}", new String(ksc5601bytes, Constants.CHARSET_KSC5601));
            String convertedEucKr = new String(ksc5601bytes, "euc-kr");
            log.info("euc-kr: {}", convertedEucKr);
            String convertedKsc5601 = new String(convertedEucKr.getBytes("euc-kr"), Constants.CHARSET_KSC5601);
            log.info("convertedKsc5601({}): {}", convertedKsc5601.getBytes(Constants.CHARSET_KSC5601).length, convertedKsc5601);
            String hex = Hex.encodeHexString(convertedKsc5601.getBytes(Constants.CHARSET_KSC5601)).toUpperCase();
            log.info("hex : {}", hex);
            byte[] convertBytes = StringUtils.unicodRvsn(ksc5601bytes, 1);

            // case : 잘못된 인코딩
            String convertedUtf8 = new String(ksc5601bytes, "utf-8");
            log.info("utf-8: {}", convertedUtf8);
            String convertedUtf8ToKsc5601 = new String(convertedEucKr.getBytes("utf-8"), Constants.CHARSET_KSC5601);
            log.info("convertedUtf8ToKsc5601: {}", convertedUtf8ToKsc5601);
        } catch (UnsupportedEncodingException e) {
            assert(true);
        }
    }
}
