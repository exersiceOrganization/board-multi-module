package com.example.common.utils;

import com.example.common.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.modelmapper.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class MapperUtilsTest {

    @Test
    void testConvertObject(){
        SampleModel sampleModel = SampleModel.builder()
                .name("test")
                .phone_number("010-1111-2222")
                .home_address("seoul")
                .fax_number("031-111-2222")
                .build();

        SampleModelToConvert converted = MapperUtils.convert(sampleModel, SampleModelToConvert.class);
        log.debug("converted(obj, clazz) : {}", converted);
        assertNotNull(converted.getName());
        assertNull(converted.getHomeAddress());

        GenericMessage<InnerMessage> genericMessage = GenericMessage.<InnerMessage>builder()
                .field1("item")
                .field2(2)
                .field3(InnerMessage.builder()
                        .field1("field3-1")
                        .field2("field3-2")
                        .field3("field3-3")
                        .field4(123).build())
                .build();
        InnerMessageSpacePadChar convertmassage = MapperUtils.convert(genericMessage.getField3(), new TypeToken<InnerMessageSpacePadChar>(){}.getType());
        log.debug("convertedInnerMessage : {}", convertmassage);
        assertNotNull(convertmassage);

        converted = new SampleModelToConvert();
        MapperUtils.convert(sampleModel, converted);
        log.debug("converted : {}", converted);
        assertNotNull(converted.getName());
    }

    @Test
    void testConvertList(){
        List<SampleModel> sampleModelList = new ArrayList<>();
        sampleModelList.add(SampleModel.builder()
                .name("test1")
                .phone_number("010-1111-2222")
                .home_address("seoul")
                .fax_number("031-111-2222")
                .build());
        sampleModelList.add(SampleModel.builder()
                .name("test2")
                .phone_number("010-2222-3333")
                .home_address("busan")
                .fax_number("031-222-3333")
                .build());
        List<SampleModelToConvert> convertedList =
                MapperUtils.convertList(sampleModelList, SampleModelToConvert.class);
        log.debug("convertedList : {}", convertedList);
        assertEquals(2, convertedList.size());
    }

    @Test
    void testConvertFromUnderscoreToCamelObject(){

        SampleModel sampleModel = SampleModel.builder()
                .name("test")
                .phone_number("010-1111-2222")
                .home_address("seoul")
                .fax_number("031-111-2222")
                .build();

        SampleModelToConvert converted = MapperUtils.convertFromUnderscoreToCalmel(sampleModel, SampleModelToConvert.class);
        log.debug("converted : {}", converted);
        assertNotNull(converted.getHomeAddress());
    }
}
