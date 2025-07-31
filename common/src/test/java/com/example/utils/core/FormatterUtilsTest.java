package com.example.utils.core;

import com.example.constant.Constants;
import com.example.data.FixedTestData;
import com.example.exception.CommonException;
import com.example.model.*;
import com.example.model.core.FixedFieldData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.example.internal.annotations.FixedData.PAD_TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class FormatterUtilsTest {

    /**
     * FixedData Annotation이 세팅된 Object(FixedDataTestInfo)를 고정길이 문자열로 변환하고
     * 문자열이 올바르게 변환됬는지 확인한다.
     */
    @Test
    void convertFixedDataTestInfoToStringTest() {
        FixedDataTestInfo fixedDataTestInfo = FixedTestData.getFixedDataTestInfo();
        //convert FixedData to String
        String fixedString = FormatterUtils.getFixedData(fixedDataTestInfo);

        assertEquals(fixedString, FixedTestData.getFixedDataTestInfoString());
    }

    /**
     * 고정길이 문자열을 FixedDataTestInfo 타입으로 변환하고
     * 데이터가 올바르게 변환됬는지 확인한다.
     */
    @Test
    void convertStringToFixedDataTestInfoTest() {
        String fixedString = FixedTestData.getFixedDataTestInfoString();

        //convert String to FixedData
        FixedDataTestInfo converted = FormatterUtils.getFixedData(fixedString, FixedDataTestInfo.class);

        FixedTestData.checkFixedDataTestInfo(converted);
    }

    /**
     * 잘못 작성된 고정길이 문자열을 변환하려 했을 때 CommonException을 발생시키는 지 확인한다.
     */
    @Test
    void invaildStringToFixedDataTestInfoTest() {
        String fixedString = FixedTestData.getInvalidLengthFixedDataTestInfoString();
        assertThrows(CommonException.class, ()->{
            FormatterUtils.getFixedData(fixedString, FixedDataTestInfo.class);
        });

        String fixedString2 = FixedTestData.getInvalidTypeFixedDataTestInfoString();
        assertThrows(CommonException.class, ()->{
            FormatterUtils.getFixedData(fixedString2, FixedDataTestInfo.class);
        });
    }

    /**
     * 잘못 작성된 고정길이 문자열을 변환하려 했을 때,
     * ErrorData 가 정상적으로 반환되는지 확인한다.
     */
    @Test
    void invaildStringToFixedDataTestInfoErrorDataTest() {
        FormatterUtils.setDataParsingThrowException(false);

        // 1. max length 초과 시
        String fixedString = FixedTestData.getInvalidLengthFixedDataTestInfoString();
        FixedDataTestInfo result = FormatterUtils.getFixedData(fixedString, FixedDataTestInfo.class);
        Map<String, Object> errorData = FormatterUtils.getErrorDataMap();
        int fixedStringLength = fixedString.getBytes().length;
        log.debug("fixedStringLength : {}, result : {}", fixedStringLength, result);
        // {FORMATTER_LENGTH_ERROR=OVERSIZE:120/110}
        log.debug("InvalidLengthFixedDataTest : {}", errorData);
        assertEquals("OVERSIZE:120/110", errorData.get(FormatterUtils.FORMATTER_LENGTH_ERROR));

        // 2. 잘못된 fixed string 입력 시 : data type error 발생
//        fixedString = "000000abcd00000a4323000012d3.000000012.20000000003000000001200004334.0000123.2340000001234a           ZZZABC  ";
//        result = FormatterUtils.getFixedData(fixedString, FixedDataTestInfo.class);
//        errorData = FormatterUtils.getErrorDataMap();
//        fixedStringLength = fixedString.getBytes().length;
//        log.debug("fixedStringLength : {}, result : {}", fixedStringLength, result);
//        log.debug("InvalidTypeFixedDataTest : {}", errorData);
//        assertEquals("abcd", errorData.get("a"));

        // 3. 변환 대상 Object 에 비해 짧은 fixed string 입력 시
        fixedString = "000000abcd00000a4323000012d3.000000012.20000000003000000001200004334.0000123.C";
        result = FormatterUtils.getFixedData(fixedString, FixedDataTestInfo.class);
        errorData = FormatterUtils.getErrorDataMap();
        fixedStringLength = fixedString.getBytes().length;
        log.debug("fixedStringLength : {}, result : {}", fixedStringLength, result);
        // {FORMATTER_LENGTH_ERROR=UNDERSIZE:78/110}
        log.debug("Invalid Type&Length FixedDataTest : {}", errorData);
        assertEquals("abcd", errorData.get("a"));
        assertEquals("UNDERSIZE:78/110", errorData.get(FormatterUtils.FORMATTER_LENGTH_ERROR));
    }

    /**
     * @FixedVo, @FixedList, @FixedData 가 존재하는 Object를 고정길이 문자열로 변환하고
     * 정상적으로 변환되었는지 확인한다.
     */
    @Test
    void convertFixedDataToStringTest() {
        FixedMessageBody msg = FixedTestData.getFixedMessageBody();
        //convert FixedData to String
        String fixedString = FormatterUtils.getFixedData(msg);

        assertEquals(fixedString, FixedTestData.getFixedMessageString());
    }

    /**
     * @FixedList 가 2개 존재하는 Object를 고정길이 문자열로 변환하고
     * 정상적으로 변환되었는지 확인한다.
     */
    @Test
    void convertFixedMultiListDataToStringTest() {
        FixedMultiListMessageBody msg = FixedTestData.getFixedMultiListMessageBody();
        //convert FixedData to String
        String fixedString = FormatterUtils.getFixedData(msg);
        log.debug("fixedString : {}", fixedString);
        assertEquals(fixedString, FixedTestData.getFixedMultiListMessageString());

        FixedMultiListMessageBody convertString = FormatterUtils.getFixedData(fixedString, FixedMultiListMessageBody.class);
        log.debug("convertString : {}", convertString);
        assertEquals(msg, convertString);
    }

    /**
     * @FixedList 가 2개 존재하는 고정길이 문자열을 Object로 변환하고
     * 정상적으로 변환되었는지 확인한다.
     */
    @Test
    void convertStringToFixedMultiListData() {
        String message = FixedTestData.getFixedMultiListMessageString();
        //convert String to FixedData
        FixedMultiListMessageBody converted = FormatterUtils.getFixedData(message, FixedMultiListMessageBody.class);
        log.debug("object : {}", converted);
        String convertedMessage = FormatterUtils.getFixedData(converted);
        assertEquals(message, convertedMessage);
    }

    /**
     * multi-depth인 data 를 고정길이 문자열로 변환 후 다시 Object 로 변환 테스트
     */
    @Test
    @SuppressWarnings("unchecked")
    void multipleFixedListDataTest() {
        GenericMessage<FixedMessageBody> multipleFixedListData = FixedTestData.getMultipleFixedListData();
        String converted = FormatterUtils.getFixedData(multipleFixedListData);
        log.debug("converted : {}", converted);
        assertEquals(FixedTestData.getMultipleFixedListDataString(), converted);

        GenericMessage<FixedMessageBody> convertedObj = FormatterUtils.getFixedData(converted, GenericMessage.class, FixedMessageBody.class);
        log.debug("convertedObj : {}", convertedObj);
        assertEquals(multipleFixedListData, convertedObj);
    }

    @Test
    void multipleFixedListDataSimpleTest() {
        String fixedString = "00001first#####second####0000100003";
        MULTILISTTEXTIn convertedObj = FormatterUtils.getFixedData(fixedString, MULTILISTTEXTIn.class);
        log.debug("convertedObj : {}", convertedObj);

        String converted = FormatterUtils.getFixedData(convertedObj);
        log.debug("converted : {}", converted);
        assertEquals(fixedString, converted);
    }

    /**
     * Object field 중 List 가 null 인 data 를 고정길이 문자열로 변환 시 test
     */
    @Test
    void fixedListNullTest() {
        // 마지막 list null 일때 test
        String expected = "000000item20000000000000000lf1-e1-field10000000lf1-e1-field20000000lf1-e1-field3000000000000000001230000000lf1-e2-field10000000lf1-e2-field20000000lf1-e2-field3000000000000000004560000000field3-field10000000field3-field20000000field3-field300000000000000000123300000000lf2-e1-field10000000lf2-e1-field20000000lf2-e1-field3000000000000000007890000000lf2-e2-field10000000lf2-e2-field20000000lf2-e2-field3000000000000000007890000000lf2-e3-field10000000lf2-e3-field20000000lf2-e3-field300000000000000000789";
        FixedMultiListMessageBody msg = FixedTestData.getFixedMultiListMessageBody();
        msg.setListField3(null);
        //convert FixedData to String
        String fixedString = FormatterUtils.getFixedData(msg);
        log.debug("fixedString : {}", fixedString);
        assertEquals(expected, fixedString);

        FixedMultiListMessageBody convertObject = FormatterUtils.getFixedData(fixedString, FixedMultiListMessageBody.class);
        log.debug("convertObject : {}", convertObject);
        assertEquals(0, convertObject.getListField3().size());

        // 중간 list 의 size 0 일때 변환 test
        String sizeZeroExpected = "000000item20000000000000000lf1-e1-field10000000lf1-e1-field20000000lf1-e1-field3000000000000000001230000000lf1-e2-field10000000lf1-e2-field20000000lf1-e2-field3000000000000000004560000000field3-field10000000field3-field20000000field3-field30000000000000000012300";
        convertObject.setListField2(null);;
        convertObject.setRefListSize(0);
        fixedString = FormatterUtils.getFixedData(convertObject);
        log.debug("size 0 fixedString : {}", fixedString);
        assertEquals(sizeZeroExpected, fixedString);

        // 0이 아닌 size 가 설정되었는데 list object 가 null 일 경우 exception 발생 test
        convertObject.setRefListSize(2);
        assertThrows(CommonException.class, ()->{
            FormatterUtils.getFixedData(convertObject);
        });

        // refSize field 가 null 일 경우 exception 발생 test
        convertObject.setRefListSize(null);
        assertThrows(CommonException.class, ()->{
            FormatterUtils.getFixedData(convertObject);
        });
    }

    /**
     * 고정길이 문자열을 FixedMessageBody 타입으로 변환하고
     * 데이터가 올바르게 변환됬는지 확인한다.
     */
    @Test
    void convertStringToFixedDataTest() {
        String message = FixedTestData.getFixedMessageString();

        //convert String to FixedData
        FixedMessageBody converted = FormatterUtils.getFixedData(message, FixedMessageBody.class);

        FixedTestData.checkFixedMessageBody(converted);

        int maxLength = FormatterUtils.getObjectMaxLength(InnerMessage.class, null);
        log.debug("InnerMessage maxLength : {}", maxLength);
        assertEquals(80, maxLength);
    }

    /**
     * 고정길이 문자열을 @FixedVo, @FixedList, @FixedData 가 존재하는 FixedMessageBody 타입으로 변환하고,
     * 에러 발생시데이터가 올바르게 변환됬는지 확인한다.
     */
    @Test
    @SuppressWarnings("unchecked")
    void convertStringToFixedErrorDataTest() {
        String message = FixedTestData.getFixedMessageInvalidString();

        //convert String to FixedData
        FormatterUtils.setDataParsingThrowException(false);
        FixedMessageBody converted = FormatterUtils.getFixedData(message, FixedMessageBody.class);
        log.debug("converted : {}", converted);
        Map<String, Object> errorDataMap = FormatterUtils.getErrorDataMap();
        log.debug("errorDataMap : {}", errorDataMap);
        List<Map<String, Object>> innerMessage = (List<Map<String, Object>>) errorDataMap.get("field4");
//        assertEquals(2, innerMessage.size() );

        int maxLength = FormatterUtils.getObjectMaxLength(InnerMessage.class, null);
        log.debug("InnerMessage maxLength : {}", maxLength);
        assertEquals(80, maxLength);
    }


    /**
     * Generic Type이 존재하는 객체를 고정길이 문자열로 변환하고
     * 정상적으로 변환됬는지 확인한다.
     */
    @Test
    void convertGenricMessageToStringTest() {
        GenericMessage<InnerMessage> msg = FixedTestData.getGenericMessage();

        //convert FixedData to String
        String fixedString = FormatterUtils.getFixedData(msg);
        assertEquals(fixedString, FixedTestData.getGenericMessageString());
    }

    /**
     * 고정길이 문자열을 GenericMessage<InnerMessage> 타입으로 변환하고
     * 데이터가 올바르게 변환됬는지 확인한다.
     */
    @Test
    void convertStringToGenricTest() {
        String message = FixedTestData.getGenericMessageString();

        //convert String to FixedData
        @SuppressWarnings("unchecked")
        GenericMessage<InnerMessage> converted = FormatterUtils.getFixedData(message, GenericMessage.class, InnerMessage.class);

        FixedTestData.checkGenericMessage(converted);
    }

    /**
     * FixedData에 세팅된 length보다 큰 값이 들어갔을 때 CommonException 발생여부를 확인한다.
     */
    @Test
    void invalidFieldTest() {
        FixedMessageBody msg = FixedTestData.getFixedMessageBody();
        msg.setField1("12345678901");    //field1 length = 10, parameter length = 11

        assertThrows(CommonException.class, ()->{
            FormatterUtils.getFixedData(msg);
        });
    }

    /**
     * FixedData에 세팅된 padding Type의 방향의 시작 문자가 padding char와 같았을 때 CommonException 발생여부를 확인한다.
     * 2024-03-22 padding char 가 들어오는 경우도 있으므로, 해당 로직 삭제 처리함
     */
    @Test
    @Disabled
    void invalidFieldDataTest() {
        FixedMessageBody msg = FixedTestData.getFixedMessageBody();
        msg.setField1("0123456789");    //padType : left, padChar : 0
        assertThrows(CommonException.class, ()->{
            FormatterUtils.getFixedData(msg);
        });

        msg.setField2(1234567890);    //padType : right, padChar : 0
        assertThrows(CommonException.class, ()->{
            FormatterUtils.getFixedData(msg);
        });
    }

    /**
     * Fixed~ Annotation이 설정되있지 않은 Object를 고정길이 문자열로 변환하려 했을 때 변환하지 않는지 확인한다.
     */
    @Test
    void notFixedDataConversionTest() {
        SampleModel sample = SampleModel.builder()
                .name("name")
                .phone_number("010-1111-2222")
                .build();

        assertEquals(FormatterUtils.getFixedData(sample).length(), 0);
    }

    /**
     * FixedList Annotation에 지정된 size보다 큰 data를 고정길이 문자열로 변환하려 했을 때 CommonException 발생여부를 확인한다.
     */
    @Test
    void invalidFixedListLengthTest(){
        FixedMessageBody msg = FixedTestData.getFixedMessageBody();
        msg.getField4().add(InnerMessage.builder()
                .field1("f4-field3-field1")
                .field2("f4-field3-field2")
                .field3("f4-field3-field3")
                .field4(123)
                .build());
        msg.getField4().add(InnerMessage.builder()
                .field1("f4-field4-field1")
                .field2("f4-field4-field2")
                .field3("f4-field4-field3")
                .field4(123)
                .build());
        assertThrows(CommonException.class, ()->{
            FormatterUtils.getFixedData(msg);
        });
    }

    /**
     * 1. field 중간에 위치한 FixedList에 size 설정하지 않고 exception 발생 여부 확인.
     * 2. dataParsingThrowException false 로 설정하고 exception 발생 여부 확인.
     */
    @Test
    void invalidFixedListDataTest() {
        InvalidFixedListMessage invalidFixedListMessage = FixedTestData.getInvalidFixedListMessage();
        assertThrows(CommonException.class, ()->{
            FormatterUtils.getFixedData(invalidFixedListMessage);
        });

        FormatterUtils.setDataParsingThrowException(false);
        String fixedString = FormatterUtils.getFixedData(invalidFixedListMessage);
        assertThrows(CommonException.class, ()->{
            FormatterUtils.getFixedData(fixedString, InvalidFixedListMessage.class);
        });
        FormatterUtils.setDataParsingThrowException(true);
    }


    /**
     * 한글이 포함된 데이터를 테스트한다.
     */
    @Test
    void convertKoreanFixedDataTestInfoTest() {
        FixedDataTestInfo data = FixedTestData.getKoreanEucKrFixedDataTestInfo();
        String message = FormatterUtils.getFixedData(data);
        assertEquals(message, FixedTestData.getKoreanEucKrFixedDataTestInfoString());
        log.info(message);
    }

    /**
     * 한글이 포함된 데이터를 테스트한다.
     */
    @Test
    void convertKoreanStringToFixedDataTestInfoTest() {
        String fixedString = FixedTestData.getKoreanEucKrFixedDataTestInfoString();

        //convert String to FixedData
        FixedDataTestInfo converted = FormatterUtils.getFixedData(fixedString, FixedDataTestInfo.class);

        FixedTestData.checkKoreanEucKrFixedDataTestInfo(converted);
    }

    /**
     * 한글이 포함된 데이터를 테스트한다.
     */
    @Test
    void convertKoreanFixedDataToStringTest() {
        FixedMessageBody msg = FixedTestData.getKoreanFixedMessageBody();
        //convert FixedData to String
        String fixedString = FormatterUtils.getFixedData(msg);

        assertEquals(fixedString, FixedTestData.getKoreanFixedMessageString());
    }

    /**
     * 한글이 포함된 데이터를 테스트한다.
     */
    @Test
    void convertKoreanStringToFixedDataTest() {
        FixedMessageBody data = FormatterUtils.getFixedData(FixedTestData.getKoreanFixedMessageString(), FixedMessageBody.class);
        assertNotNull(data);


        FixedTestData.checkKoreanFixedMessageBody(data);
    }

    /**
     * 한글이 포함된 데이터를 테스트한다.
     */
    @Test
    void convertKoreanGenricMessageToStringTest() {
        GenericMessage<InnerMessage> msg = FixedTestData.getKoreanGenericMessage();

        //convert FixedData to String
        String fixedString = FormatterUtils.getFixedData(msg);
        assertEquals(fixedString, FixedTestData.getKoreanGenericMessageEucKrString());
    }

    /**
     * 한글이 포함된 데이터를 테스트한다.
     */
    @Test
    void convertKoreanStringToGenricTest() {
        String message = FixedTestData.getKoreanGenericMessageEucKrString();
        @SuppressWarnings("unchecked")
        GenericMessage<InnerMessage> converted = FormatterUtils.getFixedData(message, GenericMessage.class, InnerMessage.class);

        FixedTestData.checkKoreanGenericMessage(converted);

    }

    /**
     * 고정길이 문자열로 변환되는지 테스트
     */
    @Test
    void getFixedDataStringTest() {
        FixedFieldData fieldData = FixedFieldData.builder()
                .dataType("java.lang.String")
                .length(10)
                .padChar('$')
                .padType(PAD_TYPE.RIGHT)
                .value("test")
                .build();

        String fixedDataString = FormatterUtils.getFixedDataString(fieldData);

        assertEquals("test$$$$$$", fixedDataString);
    }

    // (2023-11-07) value "0" 일 경우 정상처리 테스트
    @Test
    void getFixedDataStringForZeroTest() {
        FixedFieldData fieldData = FixedFieldData.builder()
                .dataType("java.lang.Integer")
                .length(10)
                .padChar('0')
                .padType(PAD_TYPE.RIGHT)
                .value("0")
                .build();

        String fixedDataString = FormatterUtils.getFixedDataString(fieldData);
        log.debug("fixedDataString : {}", fixedDataString);

        assertEquals("0000000000", fixedDataString);

        FixedDataTestInfo fixedDataTestInfo = FixedTestData.getFixedDataTestInfo();
        fixedDataTestInfo.setA(0);
        fixedDataTestInfo.setA1(0);
        fixedDataTestInfo.setB(0);
        fixedDataTestInfo.setB1(0L);

        //convert String to FixedData
        String converted = FormatterUtils.getFixedData(fixedDataTestInfo);
        log.debug("converted : {}", converted);
        assertEquals(true, converted.startsWith("00000000000000000000"));
    }

    /**
     * Object 의 value 가 전부 empty 일때, 정상변환 테스트
     */
    @Test
    void emptyFixedStringTest() {
        FixedDataTestInfo fieldData = FixedDataTestInfo.builder().build();
        String string = FormatterUtils.getFixedData(fieldData);
        log.debug("FixedDataTestInfo string : {}", string);
        assertNotNull(string);

        FixedDataTestInfo testData = FormatterUtils.getFixedData(string, FixedDataTestInfo.class);
        log.debug("FixedDataTestInfo object : {}", testData);
        assertNotNull(testData);

        GenericMessage<InnerMessage> genericData = GenericMessage.<InnerMessage>builder()
                .field3(InnerMessage.builder().build())
                .field4(new ArrayList<InnerMessage>())
                .build();
        string = FormatterUtils.getFixedData(genericData);
        log.debug("GenericMessage string : {}", string);
        @SuppressWarnings("unchecked")
        GenericMessage<InnerMessage> converted = FormatterUtils.getFixedData(string, GenericMessage.class, InnerMessage.class);
        log.debug("GenericMessage object : {}", converted);
        assertNotNull(converted);
    }

    /**
     * trimPaddingCharacters(false) 설정 시,
     * padding character 가 trim 되지 않고 Object 로 변환되는지 test
     */
    @Test
    void trimPaddingCharactersTest() {
        FixedDataTestInfo fieldData = FixedDataTestInfo.builder().build();
        fieldData.setH("  ab");
        String string = FormatterUtils.getFixedData(fieldData);
        log.debug("fixed string : {}", string);

        FormatterUtils.setTrimPaddingCharacters(false);
        FixedDataTestInfo testData = FormatterUtils.getFixedData(string, FixedDataTestInfo.class);
        FormatterUtils.setTrimPaddingCharacters(true);
        log.debug("testData : {}", testData);
        assertEquals(15, testData.getG().length());
        assertEquals("  ab ", testData.getH());
    }

    /**
     * padding character 가 전각 문자일 때 test
     */
    @Test
    void fullWidthPaddingCharacterTest() {
        // EUC-KR test
        String expected = "f1　　　　f2        ００００f30000000004";
        FullWidthPadChar fieldData = FullWidthPadChar.builder()
                .f1("f1")
                .f2("f2")
                .f3("f3")
                .f4(4).build();
        String convertString = FormatterUtils.getFixedData(fieldData, Constants.CHARSET_EUC_KR);
        log.debug("fixed string : {}", convertString);
        assertEquals(expected, convertString);

        FullWidthPadChar convertObject = FormatterUtils.getFixedData(
                convertString, FullWidthPadChar.class, Constants.CHARSET_EUC_KR, null);
        log.debug("convertObject : {}", convertObject);
        assertEquals(fieldData, convertObject);

        // UTF-8 test : byte 수 맞지 않아 space 로 보정 실행
        expected = "f1　　  f2        ００  f30000000004";
        convertString = FormatterUtils.getFixedData(fieldData, Constants.CHARSET_UTF_8);
        log.debug("fixed string : {}", convertString);
        log.debug("fixed string length : {}", convertString.length());
//        assertEquals(expected, convertString);
//
//        convertObject = FormatterUtils.getFixedData(
//                convertString, FullWidthPadChar.class, Constants.CHARSET_UTF_8, null);
//        log.debug("convertObject : {}", convertObject);
//        log.debug("conver len : {}", convertObject.toString().length());
//        assertEquals("f1　　  ", convertObject.getF1());
//        assertEquals("  f3", convertObject.getF3());
    }
}
