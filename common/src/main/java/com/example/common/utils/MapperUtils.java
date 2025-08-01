package com.example.common.utils;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MapperUtils {

    // 데이터베이스에서 가져온 0Entity 객체를 클라이언트에 전달하기 위한 DTO 객체로 변환하거나
    // , 그 반대로 변환할 때 유용하게 사용 (Java 객체 간의 데이터 매핑을 쉽게 해주는 라이브러리)
    // 필드 이름을 일일이 getter / setter로 설정할 필요 없이 자동으로 매핑해주는 기능을 제공
    private static ModelMapper modelMapper = new ModelMapper();
    private static ModelMapper modelMapperUnderscoreToCamel = new ModelMapper();

    static {
        // 원본 필드와 대상 필드의 이름이 정확히 일치해야만 매핑
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // DB에서 underscore(USER_NAME)을 camel(userName)로 변환
        modelMapperUnderscoreToCamel.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                // 원본 fieldname 토큰화 정의 ex> user_name -> [user, name]
                .setSourceNameTokenizer(NameTokenizers.UNDERSCORE)
                // 대상 fieldname 토큰화 정의 ex> userName -> [user, name]
                .setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
    }

    /**
     * 원본 객체에서 대상 Class의 동일한 변수명에 값을 매핑하여 객체를 생성.
     * <pre>
     *     SampleModelToConvert converted =
     *              MapperUtils.convert(sampleModel, SampleModelToConvert.class)
     * </pre>
     *
     * @param source            원본 객체
     * @param destinationType   대상 Class
     * @return                  생성된 객체
     */
    // D는 제네릭 타입 파라미터로 Destination의 약자 ()
    public static <D> D convert(Object source, Class<D> destinationType){
        return modelMapper.map(source, destinationType);
    }

    /**
     * 원본 객체에서 대상 타입의 동일한 변수명에 값을 매핑하여 생성.
     * <pre>
     *     Innermessage convertmessage = MapperUtils.convert(
     *              genericMessage.getInner(), new TypeToken<InnerMessage>(){}.getType));
     * </pre>
     *
     * @param source            원본 객체
     * @param destinationType   대상 Class Type
     * @return                  생성된 객체
     */
    public static <D> D convert(Object source, Type destinationType){
        return modelMapper.map(source, destinationType);
    }

    /**
     * 원본 객체에서 대상 객체의 동일한 변수명에 값을 매핑.
     * <pre>
     *     SampleModelToConvert converted = new SampleModelToConvert();
     *     MapperUtils.convert(sampleModel, converted);
     * </pre>
     *
     * @param source            원본 객체
     * @param destinationObj    매핑할 대상 객체
     */
    public static void convert(Object source, Object destinationObj){
        modelMapper.map(source, destinationObj);
    }

    /**
     * List의 원본 객체들에서 대상 Class의 동일한 변수명에 값을 매핑한 객체를 생성하여 새로운 List를 반환.
     * <pre>
     *     List<SampleModel> samplemodelList = new ArrayList<>();
     *     sampleModelList.add(samplemodel);
     *     List<SampleModelToConvert> convertedList =
     *          MapperUtils.convertList(sampleModelList, SampleModelToConvert.class);
     * </pre>
     *
     * @param list              원본 객체들이 저장된 list
     * @param destinationType   매핑할 대상 객체
     * @return                  생성된 객체들이 저장된 list
     */
    // S: source(원본)타입의 약자
    // <D, S> : 제네릭 타입 파라미터 선언
    public static <D, S> List<D> convertList(List<S> list, Class<D> destinationType){
        return list.stream()
                // param을 destinationType로 변환
                .map(param -> modelMapper.map(param, destinationType))
                // list로 수집
                .collect(Collectors.toList());
    }

    /**
     * 원본 객체에서 대상 Class의 동일한 변수명에 값을 매핑하여 객체를 생성.
     * <pre>
     *     SampleModelToConvert converted =
     *          MapperUtils.convertFromUnderscoreToCalmel(sampleModel, SampleModelToConvert.class);
     * </pre>
     *
     * @param source            원본 객체
     * @param destinationType   대상 Class
     * @return                  생성된 객체
     */
    public static <D> D convertFromUnderscoreToCalmel(Object source, Class<D> destinationType){
        return modelMapperUnderscoreToCamel.map(source, destinationType);
    }


    /**
     * 원본 객체에서 대상 타입의 동일한 변수명에 값을 매핑하여 생성합니다.
     * 원본 객체 변수명은 Underscore, 대상 Class 변수명은 CamelCase 이여야 합니다.
     * <pre>
     * example>
     *      InnerMessage converMessage = MapperUtils.convertFromUnderscoreToCamel(
     *              genericMessage.getInner(), new TypeToken<InnerMessage>(){}.getType());
     * </pre>
     *
     * @param source            원본 객체
     * @param destinationType   대상 Class의 Type
     * @return  생성된 객체
     */
    public static <D> D convertFromUnderscoreToCamel(Object source, Type destinationType){
        return modelMapperUnderscoreToCamel.map(source, destinationType);
    }

    /**
     * 원본 객체에서 대상 객체의 동일한 변수명에 값을 매핑합니다.
     * 원본 객체 변수명은 Underscore, 대상 Class 변수명은 CamelCase 이여야 합니다.
     * <pre>
     * example>
     *      SampleModelToConvert converted = new SampleModelToConvert();
     *      MapperUtils.convertFromUnderscoreToCamel(sampleModel, converted);
     * </pre>
     *
     * @param source            원본 객체
     * @param destinationObj    매핑할 대상 객체
     */
    public static void convertFromUnderscoreToCamel(Object source, Object destinationObj){
        modelMapperUnderscoreToCamel.map(source, destinationObj);
    }


    /**
     * List의 원본 객체들에서 대상 Class의 동일한 변수명에 값을 매핑한 객체를 생성하여 새로운 List를 반환합니다.
     * 원본 객체 변수명은 Underscore, 대상 Class 변수명은 CamelCase 이여야 합니다.
     * <pre>{@code
     * example>
     *      List<SampleModel> sampleModelList = new ArrayList<>();
     *      sampleModelList.add(sampleModel);
     *      List<SampleModelToConvert> convertedList =
     *              MapperUtils.convertListFromUnderscoreToCamel(sampleModelList, SampleModelToConvert.class);
     * }</pre>
     *
     * @param list              원본 객체들이 저장된 list
     * @param destinationType   매핑할 대상 객체
     * @return  생성된 객체들이 저장된 list
     */
    public static <D, S> List<D> convertListFromUnderscoreToCamel(List<S> list, Class<D> destinationType){
        return list.stream().map(param -> modelMapperUnderscoreToCamel.map(param, destinationType)).collect(Collectors.toList());
    }
}
