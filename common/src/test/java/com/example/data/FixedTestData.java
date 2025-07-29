package com.example.data;

import com.example.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FixedTestData {
    /**
     * FixedDataTestInfo 샘플
     * @return FixedDataTestInfo
     */
    public static FixedDataTestInfo getFixedDataTestInfo() {
        return FixedDataTestInfo.builder()
                .a(1654)
                .b(34323)
                .c(1233.0f)
                .d(12.2)
                .a1(3)
                .b1(12L)
                .c1(4334.0f)
                .d1(123.234)
                .e(new BigDecimal(1234))
                .g("ZZZ")
                .h("ABC")
                .build();
    }

    /**
     * getFixedDataTestInfo 함수의 결과 값이 정상적으로 변환된 고정길이 문자열
     * @return 고정길이 문자열
     */
    public static String getFixedDataTestInfoString() {
        /*
         * 0000001654       //a
         * 0000034323       //b
         * 00001233.0       //c
         * 00000012.2       //d
         * 0000000003       //a1
         * 0000000012       //b1
         * 00004334.0       //c1
         * 000123.234       //d1
         * 0000001234       //e
         * ZZZ              //g
         * ABC              //h
         * */
        return "0000001654000003432300001233.000000012.20000000003000000001200004334.0000123.2340000001234            ZZZABC  ";
    }


    /**
     * FixedDataTestInfo 검증
     * @param data 검증 데이터
     */
    public static void checkFixedDataTestInfo(FixedDataTestInfo data) {
        assertEquals(data.getA(), 1654);
        assertEquals(data.getB(), 34323);
        assertEquals(data.getC(), 1233.0);
        assertEquals(data.getD(), 12.2);
        assertEquals(data.getA1(), 3);
        assertEquals(data.getB1(), 12);
        assertEquals(data.getC1(), 4334.0f);
        assertEquals(data.getD1(), 123.234);
        assertEquals(data.getE(), new BigDecimal(1234));
        assertEquals(data.getG(), "ZZZ");
        assertEquals(data.getH(), "ABC");
    }

    /**
     * garbage 문자열이 추가된 FixedDataTestInfoString
     * @return 잘몬된 고정길이 문자열
     */
    public static String getInvalidLengthFixedDataTestInfoString() {
        /*
         * 0000001654       //a
         * 0000034323       //b
         * 00001233.0       //c
         * 00000012.2       //d
         * 0000000003       //a1
         * 0000000012       //b1
         * 00004334.0       //c1
         * 000123.234       //d1
         * 0000001234       //e
         *             ZZZ  //g
         * ABC              //h
         * */
        return "0000001654000003432300001233.000000012.20000000003000000001200004334.0000123.2340000001234            ZZZABC  xxxxxxxxxx";
    }

    /**
     * FixedDataTestInfo.a 필드 값이 잘못 세팅된 문자열 리턴
     * @return 잘못된 고정길이 문자열
     */
    public static String getInvalidTypeFixedDataTestInfoString() {
        return "000000abcd000003432300001233.000000012.20000000003000000001200004334.0000123.2340000001234            ZZZABC  ";
    }

    /**
     * FixedMessageBody 샘플
     * @return FixedMessageBody
     */
    public static FixedMessageBody getFixedMessageBody() {
        List<InnerMessage> field4 = new ArrayList<>();
        field4.add(
                InnerMessage.builder()
                        .field1("f4-field1-field1")
                        .field2("f4-field1-field2")
                        .field3("f4-field1-field3")
                        .field4(456)
                        .build()
        );
        field4.add(
                InnerMessage.builder()
                        .field1("f4-field2-field1")
                        .field2("f4-field2-field2")
                        .field3("f4-field2-field3")
                        .field4(789)
                        .build()
        );

        List<InnerMessageSpacePadChar> field5 = new ArrayList<>();
        field5.add(
                InnerMessageSpacePadChar.builder()
                        .f1("f5-field1-field1")
                        .f2("f5-field1-field2")
                        .f3("f5-field1-field3")
                        .f4(456)
                        .build()
        );
        field5.add(
                InnerMessageSpacePadChar.builder()
                        .f1("f5-field2-field1")
                        .f2("f5-field2-field2")
                        .f3("f5-field2-field3")
                        .f4(789)
                        .build()
        );
        field5.add(
                InnerMessageSpacePadChar.builder()
                        .f1("f5-field3-field1")
                        .f2("f5-field3-field2")
                        .f3("f5-field3-field3")
                        .f4(123)
                        .build()
        );
        List<FixedDataTestInfo> field6 = new ArrayList<>();
        field6.add(getFixedDataTestInfo());

        return FixedMessageBody.builder()
                .field1("item")
                .field2(2)
                .field3(
                        InnerMessage.builder()
                                .field1("field3-field1")
                                .field2("field3-field2")
                                .field3("field3-field3")
                                .field4(123)
                                .build()
                )
                .field4(field4)
                .field5(field5.get(0))
//            .field6(field6)
                .build();
    }

    /**
     * FixedMessageBody 데이터가 정상적으로 변환된 고정길이 문자열
     * @return 고정길이 문자열
     */
    public static String getFixedMessageString() {
        /*
        000000item
        2000000000
        0000000field3-field1
        0000000field3-field2
        0000000field3-field3
        00000000000000000123
            f5-field1-field1
            f5-field1-field2
            f5-field1-field3
            456
        0000f4-field1-field1
        0000f4-field1-field2
        0000f4-field1-field3
        00000000000000000456
        0000f4-field2-field1
        0000f4-field2-field2
        0000f4-field2-field3
        00000000000000000789
         * */
        return "000000item20000000000000000field3-field10000000field3-field20000000field3-field300000000000000000123    f5-field1-field1    f5-field1-field2    f5-field1-field3                 4560000f4-field1-field10000f4-field1-field20000f4-field1-field3000000000000000004560000f4-field2-field10000f4-field2-field20000f4-field2-field300000000000000000789";
    }

    public static String getFixedMessageInvalidString() {
        return "000000item20000000000000000field3-field10000000field3-field20000000field3-field300000000000abc000123    f5-field1-field1    f5-field1-field2    f5-field1-field3                 ab60000f4-field1-field10000f4-field1-field20000f4-field1-field3000000000000000ab4560000f4-field2-field10000f4-field2-field20000f4-field2-field3000000000000000ab789abc  ";
    }

    /**
     * FixedMessageBody 샘플
     * @return FixedMessageBody
     */
    public static FixedMultiListMessageBody getFixedMultiListMessageBody() {
        // list1 2개
        List<InnerMessage> listField1 = new ArrayList<>();
        listField1.add(
                InnerMessage.builder()
                        .field1("lf1-e1-field1")
                        .field2("lf1-e1-field2")
                        .field3("lf1-e1-field3")
                        .field4(123)
                        .build()
        );
        listField1.add(
                InnerMessage.builder()
                        .field1("lf1-e2-field1")
                        .field2("lf1-e2-field2")
                        .field3("lf1-e2-field3")
                        .field4(456)
                        .build()
        );
        // list2 3개
        List<InnerMessage> listField2 = new ArrayList<>();
        listField2.add(
                InnerMessage.builder()
                        .field1("lf2-e1-field1")
                        .field2("lf2-e1-field2")
                        .field3("lf2-e1-field3")
                        .field4(789)
                        .build()
        );
        listField2.add(
                InnerMessage.builder()
                        .field1("lf2-e2-field1")
                        .field2("lf2-e2-field2")
                        .field3("lf2-e2-field3")
                        .field4(789)
                        .build()
        );
        listField2.add(
                InnerMessage.builder()
                        .field1("lf2-e3-field1")
                        .field2("lf2-e3-field2")
                        .field3("lf2-e3-field3")
                        .field4(789)
                        .build()
        );
        // list3 3개
        List<InnerMessageSpacePadChar> listField3 = new ArrayList<>();
        listField3.add(
                InnerMessageSpacePadChar.builder()
                        .f1("f5-field1-field1")
                        .f2("f5-field1-field2")
                        .f3("f5-field1-field3")
                        .f4(456)
                        .build()
        );
        listField3.add(
                InnerMessageSpacePadChar.builder()
                        .f1("f5-field2-field1")
                        .f2("f5-field2-field2")
                        .f3("f5-field2-field3")
                        .f4(789)
                        .build()
        );
        listField3.add(
                InnerMessageSpacePadChar.builder()
                        .f1("f5-field3-field1")
                        .f2("f5-field3-field2")
                        .f3("f5-field3-field3")
                        .f4(123)
                        .build()
        );
//        List<FixedDataTestInfo> field6 = new ArrayList<>();
//        field6.add(getFixedDataTestInfo());

        return FixedMultiListMessageBody.builder()
                .field1("item")
                .field2(2)
                .field3(
                        InnerMessage.builder()
                                .field1("field3-field1")
                                .field2("field3-field2")
                                .field3("field3-field3")
                                .field4(123)
                                .build()
                )
                .listField1(listField1)
                .refListSize(3)
                .listField2(listField2)
                .listField3(listField3)
                .build();
    }

    public static String getFixedMultiListMessageString() {
        /*
         * FixedMultiListMessageBody(field1=item, field2=2, listField1=[InnerMessage(field1=lf1-e1-field1, field2=lf1-e1-field2, field3=lf1-e1-field3, field4=123), InnerMessage(field1=lf1-e2-field1, field2=lf1-e2-field2, field3=lf1-e2-field3, field4=456)], field3=InnerMessage(field1=field3-field1, field2=field3-field2, field3=field3-field3, field4=123), refListSize=3, listField2=[InnerMessage(field1=lf2-e1-field1, field2=lf2-e1-field2, field3=lf2-e1-field3, field4=789), InnerMessage(field1=lf2-e2-field1, field2=lf2-e2-field2, field3=lf2-e2-field3, field4=789), InnerMessage(field1=lf2-e3-field1, field2=lf2-e3-field2, field3=lf2-e3-field3, field4=789)], listField3=[InnerMessageSpacePadChar(f1=f5-field1-field1, f2=f5-field1-field2, f3=f5-field1-field3, f4=456), InnerMessageSpacePadChar(f1=f5-field2-field1, f2=f5-field2-field2, f3=f5-field2-field3, f4=789), InnerMessageSpacePadChar(f1=f5-field3-field1, f2=f5-field3-field2, f3=f5-field3-field3, f4=123)])
         * 000000item
         * 2000000000
         *
         *  0000000lf1-e1-field1
         *  0000000lf1-e1-field2
         *  0000000lf1-e1-field3
         *  00000000000000000123
         *  0000000lf1-e2-field1
         *  0000000lf1-e2-field2
         *  0000000lf1-e2-field3
         *  00000000000000000456
         *
         *  0000000field3-field1
         *  0000000field3-field2
         *  0000000field3-field3
         *  00000000000000000123
         *
         * 30
         *  0000000lf2-e1-field1
         *  0000000lf2-e1-field2
         *  0000000lf2-e1-field3
         *  00000000000000000789
         *  0000000lf2-e2-field1
         *  0000000lf2-e2-field2
         *  0000000lf2-e2-field3
         *  00000000000000000789
         *  0000000lf2-e3-field1
         *  0000000lf2-e3-field2
         *  0000000lf2-e3-field3
         *  00000000000000000789
         *
         *      f5-field1-field1
         *      f5-field1-field2
         *      f5-field1-field3
         *                   456
         *      f5-field2-field1
         *      f5-field2-field2
         *      f5-field2-field3
         *                   789
         *      f5-field3-field1
         *      f5-field3-field2
         *      f5-field3-field3
         *                   123
         */
        return "000000item20000000000000000lf1-e1-field10000000lf1-e1-field20000000lf1-e1-field3000000000000000001230000000lf1-e2-field10000000lf1-e2-field20000000lf1-e2-field3000000000000000004560000000field3-field10000000field3-field20000000field3-field300000000000000000123300000000lf2-e1-field10000000lf2-e1-field20000000lf2-e1-field3000000000000000007890000000lf2-e2-field10000000lf2-e2-field20000000lf2-e2-field3000000000000000007890000000lf2-e3-field10000000lf2-e3-field20000000lf2-e3-field300000000000000000789    f5-field1-field1    f5-field1-field2    f5-field1-field3                 456    f5-field2-field1    f5-field2-field2    f5-field2-field3                 789    f5-field3-field1    f5-field3-field2    f5-field3-field3                 123";
    }


    /**
     * FixedMessageBody 검증
     * @param data 검증 데이터
     */
    public static void checkFixedMessageBody(FixedMessageBody data) {
        //check field1(FixedData)
        assertEquals("item", data.getField1());

        //check field2(FixedData)
        assertEquals(2, data.getField2());

        //check field3(FixedVo)
        assertEquals("field3-field1", data.getField3().getField1());
        assertEquals("field3-field2", data.getField3().getField2());
        assertEquals("field3-field3", data.getField3().getField3());
        assertEquals(123, data.getField3().getField4());

        //check field4(FixedList)
        assertEquals(2, data.getField4().size());
        for (int i = 0; i < 2; i++) {

            assertEquals("f4-field"+(i+1)+"-field1" ,data.getField4().get(i).getField1());
            assertEquals("f4-field"+(i+1)+"-field2", data.getField4().get(i).getField2());
            assertEquals("f4-field"+(i+1)+"-field3", data.getField4().get(i).getField3());
            if(i==0)
                assertEquals(456, data.getField4().get(i).getField4());
            else
                assertEquals(789, data.getField4().get(i).getField4());
        }


        //check field5(FixedList)
//        assertEquals(3, data.getField5().size());
//        for (int i = 0; i < 1; i++) {
        assertEquals("f5-field"+(1)+"-field1" ,data.getField5().getF1());
        assertEquals("f5-field"+(1)+"-field2" ,data.getField5().getF2());
        assertEquals("f5-field"+(1)+"-field3" ,data.getField5().getF3());
//            if(i==0)
        assertEquals(456, data.getField5().getF4());
//            else if (i==1)
//                assertEquals(789, data.getField5().get(i).getF4());
//            else
//                assertEquals(123, data.getField5().get(i).getF4());
//        }

        //check field6(FixedList)
//        assertEquals(1, data.getField6().size());
//        for (int i = 0; i < 1; i++) {
//            FixedDataTestInfo fixedDataInfo = data.getField6().get(i);
//
//            assertEquals(fixedDataInfo.getA(), 1654);
//            assertEquals(fixedDataInfo.getB(), 34323);
//            assertEquals(fixedDataInfo.getC(), 1233.0);
//            assertEquals(fixedDataInfo.getD(), 12.2);
//            assertEquals(fixedDataInfo.getA1(), 3);
//            assertEquals(fixedDataInfo.getB1(), 12);
//            assertEquals(fixedDataInfo.getC1(), 4334.0f);
//            assertEquals(fixedDataInfo.getD1(), 123.234);
//            assertEquals(fixedDataInfo.getE(), new BigDecimal(1234));
//            assertEquals(fixedDataInfo.getG(), "ZZZ");
//            assertEquals(fixedDataInfo.getH(), "ABC");
//        }
    }

    /**
     * GenericMessage 샘플
     * @return GenericMessage<InnerMessage>
     */
    public static GenericMessage<InnerMessage> getGenericMessage(){
        List<InnerMessage> field4 = new ArrayList<>();
        field4.add(
                InnerMessage.builder()
                        .field1("f4-field1-field1")
                        .field2("f4-field1-field2")
                        .field3("f4-field1-field3")
                        .field4(456)
                        .build()
        );
        field4.add(
                InnerMessage.builder()
                        .field1("f4-field2-field1")
                        .field2("f4-field2-field2")
                        .field3("f4-field2-field3")
                        .field4(789)
                        .build()
        );

        return GenericMessage.<InnerMessage>builder()
                .field1("item")
                .field2(2)
                .field3(
                        InnerMessage.builder()
                                .field1("field3-field1")
                                .field2("field3-field2")
                                .field3("field3-field3")
                                .field4(123)
                                .build()
                )
                .field4(field4)
                .build();
    }

    /**
     * GenericMessage<InnerMessage> 데이터가 정상적으로 변환된 고정길이 문자열
     * @return 고정길이 문자열
     */
    public static String getGenericMessageString() {
        /*
            000000item
            2000000000
            0000000field3-field1
            0000000field3-field2
            0000000field3-field3
            00000000000000000123
            0000f4-field1-field1
            0000f4-field1-field2
            0000f4-field1-field3
            00000000000000000456
            0000f4-field2-field1
            0000f4-field2-field2
            0000f4-field2-field3
            00000000000000000789
         * */
        return "000000item20000000000000000field3-field10000000field3-field20000000field3-field3000000000000000001230000f4-field1-field10000f4-field1-field20000f4-field1-field3000000000000000004560000f4-field2-field10000f4-field2-field20000f4-field2-field300000000000000000789";
    }

    public static void checkGenericMessage(GenericMessage<InnerMessage> data) {
        //check field1(FixedData)
        assertEquals("item", data.getField1());

        //check field3(FixedVo)
        assertEquals("field3-field1", data.getField3().getField1());
        assertEquals("field3-field2", data.getField3().getField2());
        assertEquals("field3-field3", data.getField3().getField3());
        assertEquals(123, data.getField3().getField4());

        //check field4(FixedList)
        assertEquals(2, data.getField4().size());
        for (int i = 0; i < 2; i++) {
            assertEquals("f4-field"+(i+1)+"-field1", data.getField4().get(i).getField1());
            assertEquals("f4-field"+(i+1)+"-field2", data.getField4().get(i).getField2());
            assertEquals("f4-field"+(i+1)+"-field3", data.getField4().get(i).getField3());
            if(i==0)
                assertEquals(456, data.getField4().get(i).getField4());
            else
                assertEquals(789, data.getField4().get(i).getField4());
        }
    }


    public static GenericMessage<FixedMessageBody> getMultipleFixedListData(){
        List<FixedMessageBody> field4 = new ArrayList<>();
        field4.add(getFixedMessageBody());
        field4.add(getFixedMessageBody());

        return GenericMessage.<FixedMessageBody>builder()
                .field1("item")
                .field2(2)
                .field3(getFixedMessageBody())
                .field4(field4)
                .build();
    }

    public static String getMultipleFixedListDataString() {
        /*
         *   000000item
         *   2000000000
         *     000000item
         *     2000000000
         *     0000000field3-field1
         *     0000000field3-field2
         *     0000000field3-field3
         *     00000000000000000123
         *         f5-field1-field1
         *         f5-field1-field2
         *         f5-field1-field3
         *                      456
         *     0000f4-field1-field1
         *     0000f4-field1-field2
         *     0000f4-field1-field3
         *     00000000000000000456
         *     0000f4-field2-field1
         *     0000f4-field2-field2
         *     0000f4-field2-field3
         *     00000000000000000789
         *
         *     000000item
         *     2000000000
         *     0000000field3-field1
         *     0000000field3-field2
         *     0000000field3-field3
         *     00000000000000000123
         *         f5-field1-field1
         *         f5-field1-field2
         *         f5-field1-field3
         *                      456
         *     0000f4-field1-field1
         *     0000f4-field1-field2
         *     0000f4-field1-field3
         *     00000000000000000456
         *     0000f4-field2-field1
         *     0000f4-field2-field2
         *     0000f4-field2-field3
         *     00000000000000000789
         *     000000item
         *     2000000000
         *     0000000field3-field1
         *     0000000field3-field2
         *     0000000field3-field3
         *     00000000000000000123
         *         f5-field1-field1
         *         f5-field1-field2
         *         f5-field1-field3
         *                      456
         *     0000f4-field1-field1
         *     0000f4-field1-field2
         *     0000f4-field1-field3
         *     00000000000000000456
         *     0000f4-field2-field1
         *     0000f4-field2-field2
         *     0000f4-field2-field3
         *     00000000000000000789
         */
        // length : 10 + 10 + (340 * 3) = 1040
        return "000000item2000000000000000item20000000000000000field3-field10000000field3-field20000000field3-field300000000000000000123    f5-field1-field1    f5-field1-field2    f5-field1-field3                 4560000f4-field1-field10000f4-field1-field20000f4-field1-field3000000000000000004560000f4-field2-field10000f4-field2-field20000f4-field2-field300000000000000000789000000item20000000000000000field3-field10000000field3-field20000000field3-field300000000000000000123    f5-field1-field1    f5-field1-field2    f5-field1-field3                 4560000f4-field1-field10000f4-field1-field20000f4-field1-field3000000000000000004560000f4-field2-field10000f4-field2-field20000f4-field2-field300000000000000000789000000item20000000000000000field3-field10000000field3-field20000000field3-field300000000000000000123    f5-field1-field1    f5-field1-field2    f5-field1-field3                 4560000f4-field1-field10000f4-field1-field20000f4-field1-field3000000000000000004560000f4-field2-field10000f4-field2-field20000f4-field2-field300000000000000000789";
    }

    public static InvalidFixedListMessage getInvalidFixedListMessage() {
        List<InnerMessage> field3 = new ArrayList<>();
        field3.add(
                InnerMessage.builder()
                        .field1("lf3-field1-field1")
                        .field2("lf3-field1-field2")
                        .field3("lf3-field1-field3")
                        .field4(456)
                        .build()
        );
        field3.add(
                InnerMessage.builder()
                        .field1("lf3-field2-field1")
                        .field2("lf3-field2-field2")
                        .field3("lf3-field2-field3")
                        .field4(789)
                        .build()
        );

        return InvalidFixedListMessage.builder()
                .field1("item")
                .field2(2)
                .field4(
                        InnerMessageSpacePadChar.builder()
                                .f1("field3-field1")
                                .f2("field3-field2")
                                .f3("field3-field3")
                                .f4(123)
                                .build()
                )
                .field3(field3)
                .build();
    }


    /**
     * FixedDataTestInfo 샘플(한글포함)
     * @return FixedDataTestInfo
     *
     * EUC-KR
     */
    public static FixedDataTestInfo getKoreanEucKrFixedDataTestInfo() {
        return FixedDataTestInfo.builder()
                .a(1654)
                .b(34323)
                .c(1233.0f)
                .d(12.2)
                .a1(3)
                .b1(12L)
                .c1(4334.0f)
                .d1(123.234)
                .e(new BigDecimal(1234))
                .g("테스트-1")
                .h("한글")
                .build();
    }
    /**
     * FixedDataTestInfo 샘플(한글포함)
     * @return FixedDataTestInfo
     *
     * UTF-8
     */
    public static FixedDataTestInfo getKoreanUTF8FixedDataTestInfo() {
        return FixedDataTestInfo.builder()
                .a(1654)
                .b(34323)
                .c(1233.0f)
                .d(12.2)
                .a1(3)
                .b1(12L)
                .c1(4334.0f)
                .d1(123.234)
                .e(new BigDecimal(1234))
                .g("테스트-1")
                .h("한")
                .build();
    }

    /**
     *
     * @return EUC-KR String
     */
    public static String getKoreanEucKrFixedDataTestInfoString() {
        return "0000001654000003432300001233.000000012.20000000003000000001200004334.0000123.2340000001234       테스트-1한글 ";
    }

    /**
     *
     * @return UTF-8 String
     */
    public static String getKoreanUTF8FixedDataTestInfoString() {
        return "0000001654000003432300001233.000000012.20000000003000000001200004334.0000123.2340000001234    테스트-1한  ";
    }

    public static void checkKoreanUTF8FixedDataTestInfo(FixedDataTestInfo data) {
        assertEquals(data.getA(), 1654);
        assertEquals(data.getB(), 34323);
        assertEquals(data.getC(), 1233.0);
        assertEquals(data.getD(), 12.2);
        assertEquals(data.getA1(), 3);
        assertEquals(data.getB1(), 12);
        assertEquals(data.getC1(), 4334.0f);
        assertEquals(data.getD1(), 123.234);
        assertEquals(data.getE(), new BigDecimal(1234));
        assertEquals(data.getG(), "테스트-1");
        assertEquals(data.getH(), "한");
    }

    public static void checkKoreanEucKrFixedDataTestInfo(FixedDataTestInfo data) {
        assertEquals(data.getA(), 1654);
        assertEquals(data.getB(), 34323);
        assertEquals(data.getC(), 1233.0);
        assertEquals(data.getD(), 12.2);
        assertEquals(data.getA1(), 3);
        assertEquals(data.getB1(), 12);
        assertEquals(data.getC1(), 4334.0f);
        assertEquals(data.getD1(), 123.234);
        assertEquals(data.getE(), new BigDecimal(1234));
        assertEquals(data.getG(), "테스트-1");
        assertEquals(data.getH(), "한글");
    }

    public static FixedMessageBody getKoreanFixedMessageBody() {
        List<InnerMessage> field4 = new ArrayList<>();
        field4.add(
                InnerMessage.builder()
                        .field1("f4-field1-한글1")
                        .field2("f4-field1-한글2")
                        .field3("f4-field1-한글3")
                        .field4(456)
                        .build()
        );
        field4.add(
                InnerMessage.builder()
                        .field1("f4-field2-한글1")
                        .field2("f4-field2-한글2")
                        .field3("f4-field2-한글3")
                        .field4(789)
                        .build()
        );

        List<InnerMessageSpacePadChar> field5 = new ArrayList<>();
        field5.add(
                InnerMessageSpacePadChar.builder()
                        .f1("f5-field1-한글1")
                        .f2("f5-field1-한글2")
                        .f3("f5-field1-한글3")
                        .f4(456)
                        .build()
        );
        field5.add(
                InnerMessageSpacePadChar.builder()
                        .f1("f5-field2-한글1")
                        .f2("f5-field2-한글2")
                        .f3("f5-field2-한글3")
                        .f4(789)
                        .build()
        );
        field5.add(
                InnerMessageSpacePadChar.builder()
                        .f1("f5-field3-한글1")
                        .f2("f5-field3-한글2")
                        .f3("f5-field3-한글3")
                        .f4(123)
                        .build()
        );
        List<FixedDataTestInfo> field6 = new ArrayList<>();
        field6.add(getFixedDataTestInfo());

        return FixedMessageBody.builder()
                .field1("item")
                .field2(2)
                .field3(
                        InnerMessage.builder()
                                .field1("field3-한글1")
                                .field2("field3-한글2")
                                .field3("field3-한글3")
                                .field4(123)
                                .build()
                )
                .field4(field4)
                .field5(field5.get(0))
//            .field6(field6)
                .build();
    }

    public static String getKoreanFixedMessageString() {
        return "000000item200000000000000000field3-한글100000000field3-한글200000000field3-한글300000000000000000123     f5-field1-한글1     f5-field1-한글2     f5-field1-한글3                 45600000f4-field1-한글100000f4-field1-한글200000f4-field1-한글30000000000000000045600000f4-field2-한글100000f4-field2-한글200000f4-field2-한글300000000000000000789";
    }

    public static void checkKoreanFixedMessageBody(FixedMessageBody data) {
        //check field1(FixedData)
        assertEquals("item", data.getField1());

        //check field2(FixedData)
        assertEquals(2, data.getField2());

        //check field3(FixedVo)
        assertEquals("field3-한글1", data.getField3().getField1());
        assertEquals("field3-한글2", data.getField3().getField2());
        assertEquals("field3-한글3", data.getField3().getField3());
        assertEquals(123, data.getField3().getField4());

        //check field4(FixedList)
        assertEquals(2, data.getField4().size());
        for (int i = 0; i < 2; i++) {

            assertEquals("f4-field"+(i+1)+"-한글1" ,data.getField4().get(i).getField1());
            assertEquals("f4-field"+(i+1)+"-한글2", data.getField4().get(i).getField2());
            assertEquals("f4-field"+(i+1)+"-한글3", data.getField4().get(i).getField3());
            if(i==0)
                assertEquals(456, data.getField4().get(i).getField4());
            else
                assertEquals(789, data.getField4().get(i).getField4());
        }

        assertEquals("f5-field"+(1)+"-한글1" ,data.getField5().getF1());
        assertEquals("f5-field"+(1)+"-한글2" ,data.getField5().getF2());
        assertEquals("f5-field"+(1)+"-한글3" ,data.getField5().getF3());
        assertEquals(456, data.getField5().getF4());
    }

    public static GenericMessage<InnerMessage> getKoreanGenericMessage(){
        List<InnerMessage> field4 = new ArrayList<>();
        field4.add(
                InnerMessage.builder()
                        .field1("f4-field1-한글1")
                        .field2("f4-field1-한글2")
                        .field3("f4-field1-한글3")
                        .field4(456)
                        .build()
        );
        field4.add(
                InnerMessage.builder()
                        .field1("f4-field2-한글1")
                        .field2("f4-field2-한글2")
                        .field3("f4-field2-한글3")
                        .field4(789)
                        .build()
        );

        return GenericMessage.<InnerMessage>builder()
                .field1("아이템")
                .field2(2)
                .field3(
                        InnerMessage.builder()
                                .field1("field3-한글1")
                                .field2("field3-한글2")
                                .field3("field3-한글3")
                                .field4(123)
                                .build()
                )
                .field4(field4)
                .build();
    }


    /**
     * UTF-8 검증 데이터
     */
    public static String getKoreanGenericMessageUTF8String() {
        return "0아이템2000000000000000field3-한글1000000field3-한글2000000field3-한글300000000000000000123000f4-field1-한글1000f4-field1-한글2000f4-field1-한글300000000000000000456000f4-field2-한글1000f4-field2-한글2000f4-field2-한글300000000000000000789";
    }

    /**
     * EUC-KR 검증 데이터
     */
    public static String getKoreanGenericMessageEucKrString() {
        return "0000아이템200000000000000000field3-한글100000000field3-한글200000000field3-한글30000000000000000012300000f4-field1-한글100000f4-field1-한글200000f4-field1-한글30000000000000000045600000f4-field2-한글100000f4-field2-한글200000f4-field2-한글300000000000000000789";
    }

    public static void checkKoreanGenericMessage(GenericMessage<InnerMessage> data) {
        //check field1(FixedData)
        assertEquals("아이템", data.getField1());

        //check field3(FixedVo)
        assertEquals("field3-한글1", data.getField3().getField1());
        assertEquals("field3-한글2", data.getField3().getField2());
        assertEquals("field3-한글3", data.getField3().getField3());
        assertEquals(123, data.getField3().getField4());

        //check field4(FixedList)
        assertEquals(2, data.getField4().size());
        for (int i = 0; i < 2; i++) {
            assertEquals("f4-field"+(i+1)+"-한글1", data.getField4().get(i).getField1());
            assertEquals("f4-field"+(i+1)+"-한글2", data.getField4().get(i).getField2());
            assertEquals("f4-field"+(i+1)+"-한글3", data.getField4().get(i).getField3());
            if(i==0)
                assertEquals(456, data.getField4().get(i).getField4());
            else
                assertEquals(789, data.getField4().get(i).getField4());
        }
    }
}