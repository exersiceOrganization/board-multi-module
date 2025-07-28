package com.example.utils.core;

import com.example.exception.CommonException;
import com.example.internal.FixedData;
import com.example.internal.FixedList;
import com.example.internal.FixedVo;
import com.example.model.core.FixedField;
import com.example.repository.ThreadRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.FixedValue;

import javax.management.monitor.CounterMonitor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FormatterUtils {
    // FormatterUtils parse과정에서 발생한 error data를 thread local에서 조회하기 위한 key
    public static final String FORMATTER_ERROR_DATA = "FORMATTER_ERROR_DATA";

    /**
     * FormatterUtils parse과정에서 전체 길이 비교 시 대상 Object와 입력된 string byte가 다를 때
     * 설정하는 error key.
     * (List 포함 시 해당 error key는 설정되지 않음.)
     * <pre>
     *     각 경우에 따라 저장되는 value는 다음과 같음.
     *     - 입력 string byte가 over 시: 'OVERSIZE:{입력 string byte}/{대상 object size}'
     *     - 입력 string byte가 under 시 : 'UNDERSIZE:{입력 string byte}/{대상 object size}'
     * </pre>
     */
    public static final String FORMATTER_LENGTH_ERROR = "FORMATTER_LENGTH_ERROR";
    public static final String FORMATTER_LENGTH_OVERSIZE = "OVERSIZE";
    public static final String FORMATTER_LENGTH_UNDERSIZE = "UNDERSIZE";

    private static Map<String, List<FixedField>> map = new ConcurrentHashMap<>();
    private static final char NUMBER_TYPE_PADDING_CHAR = 48;
    private static final char CHARACTER_TYPE_PADDING_CHAR = 32;
    private static final String DEFAULT_CHAR_ENCODING = "EUC-KR";

    private static boolean dataParsingThrowException = true;

    private static boolean trimPaddingCharacters = true;

    private FormatterUtils() { throw new IllegalStateException("Utility class"); }

    /**
     * data parsing에서 error 발생 시, throw exception 여부 설정
     *
     * @param dataParsingThrowException true : exception 설정
     */
    public static void setDataParsingThrowExecption(boolean dataParsingThrowException){
        FormatterUtils.dataParsingThrowException = dataParsingThrowException;
    }

    /**
     * 고정 길이 문자열을 Object로 변환 시, padding characters를 trim하여 저장할지 여부 설정
     *
     * @param trimPaddingCharacters true : trim
     */
    public static void setTrimPaddingCharacters(boolean trimPaddingCharacters){
        FormatterUtils.trimPaddingCharacters = trimPaddingCharacters;
    }

    /**
     * Fixed Data Object를 고정 길이 문자열로 변환
     * <pre>
     *     Formatterutils.getFixedData(fixedDataTestInfo)
     *     result : 0000001654000003432300001233.000000012.20000000003000000001200004334.0000123.2340000001234            ZZZABC
     * </pre>
     *
     * @param obj      고정 길이 문자열로 변환할 java object
     * @return String  fixed-length-data
     * @see FixedData
     */
    public static String getFixedData(Object obj){
        return getFixedData(obj, DEFAULT_CHAR_ENCODING, false, createErrorDataMap());
    }

    /**
     * Fixed data Object를 고정 길이 문자열로 변환
     * <pre>
     *     FormatterUtils.getFixedData(fixedDataTestInfo, Constants.CHARSET_UTF_8)
     *     result : 0000001654000003432300001233.000000012.20000000003000000001200004334.0000123.2340000001234            ZZZABC
     * </pre>
     *
     * @param obj        고정 길이 문자열로 변환할 java object
     * @param encoding   변환 시 사용할 encoding
     * @return String    fixec-length-data
     * @see FixedData
     */
    public static String getFixedData(Object obj, String encoding){
        return getFixedData(obj, encoding, false, createErrorDataMap());
    }

    /**
     * Fixed Data Object를 고정 길이 문자열로 변환
     *
     * @param obj       고정 길이 문자열로 변환 할 java object
     * @param encoding  변환 시 사용할 encoding
     * @param isNull    obj가 null 값인 경우 true
     * @return String   fixec-length-data
     * @see FixedData
     */
    @SuppressWarnings("unchecked")
    private static String getFixedData(Object obj, String encoding, boolean isNull, Map<String, Object>){

        log.debug("PARAM : {}", obj);

        StringBuilder sb = new StringBuilder();
        List<FixedField> fieldList = getFildList(obj.getClass(), null);
        int fieldListIndex = 0;
        int fieldListSize = fieldList.size();

        for (FixedField fixedField : fieldList){
            fieldListIndex++;

            if (fixedField.isFixedVo()){
                Object subObj = getFixedObject(obj, fixedField);
                if (subObj == null){
                    try{
                        subObj = fixedField.getType().newInstance();
                    } catch (InstantiationException | IllegalAccessException ex) {
                        log.debug("FixedVo Error", ex);
                        throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
                    }
                    sb.append(getFixedData(subObj, encoding, true, formatterErrorData));
                }else{
                    sb.append(getFixedData(subObj, encoding, false, formatterErrorData));
                }
            }else if(fixedField.getFixedList() != null){
                // FixedList 타입인 경우 List item을 추출해서 String으로 변환.
                List<FixedField> subFieldList = null;
                List<Object> listObj = (List<Object>) getFieldObject(obj, fixedField);

                // list size validation check
                int listSize = getFixedListSize(obj, fixedField, fieldListIndex, fieldListSize, false, fomatterErrorData);
                // 마지막에 위치한 list 또는 size가 0이고, list object가 null 또는 empty이면 skip
                if ( (listSize == Integer.MAX_VALUE || listSize == 0)
                    && (listObj == null || listObj.isEmpty()) ){
                    continue;
                }else if (listObj == null){
                    throw CommonException.builder().message("invalid data(list field is null").build();
                }

                // listSize가 설정된 list면 validation check
                int listObjSize = listObj.size();
                if (listSize != listObjSize && listSize != Integer.MAX_VALUE){
                    log.warn("list size : {}, list object size : {}", listSize, listObjSize);
                    if (dataParsingThrowException){
                        throw CommonException.builder().message("invalid data(list field)").build();
                    }
                }

                for (Object listItem : listObj){
                    // list item의 field를 sTring으로 변환
                    if (!fixedField.getFixedFieldList().isEmpty()){
                        subFieldList = fixedField.getFixedFieldList();
                    }else {
                        /*
                        list item의 타입이 generic type인 경우 feflection api를 통해 item type을 가져올 수 없다.
                        getFixedFieldList 함수를 통해 FixedFieldList를 가져오지 못했을 때
                        list item의 field list를 직접 가져오도록 수정.
                         */
                        subFieldList = getFieldList(listItem.getClass(), null);
                    }

                    for (FixedField subField : subFieldList){
                        if (subField.isFixedVo()){
                            // VO일때 Object를 다시 구해서 reculsive call
                            Object subFieldObject = getFieldObject(listItem, subField);
                            sb.append(getFixedData(subFieldObject, encoding, isNull, formatterErrorData));
                        }else if (subField.getFixedList() != null){
                            // List일때 바로 변환 안되므로, List element마다 각각 변환해야 함.
                            List<Object> subFieldListObject = (List<Object>) getFieldObject(listItem, subField);

                            for (Object subFieldObject : subFieldListObject){
                                sb.append(getFixedData(subFieldObject, encoding, isNull, formatterErrorData));
                            }
                        }else{
                            sb.append(padFixedData(lsitItem, subField, encoding, isNull));
                        }
                    }
                }
            }else{
                sb.append(padFixedData(obj, fixedField, encoding, isNull));
            }
        }

        log.debug("FIXED_DATA : {}", sb);
        return sb.toString();
    }

    /**
     * Object에서 field name의 value값 조회
     *
     * @param obj        조회 대상 object
     * @param fieldName  조회할 field naem
     * @return           field value
     */
    private static Object getFieldObject(Object obj, FixedField fixedField){

        Object fieldObj = new Object();
        try{
            Class<?> clazz = obj.getClass();
            if (fixedField.isSuperClassField()){
                clazz = clazz.getSuperclass();
            }
            Field field = clazz.getDeclaredField(fixedField.getName());
            field.setAccessible(true);
            fieldObj = field.get(obj);

        }catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.debug("[ERROR]FILED:{}", fixedField.getName(), e);
            throw CommonException.builder().message(e.getMessage()).cause(e).build();
        }
        return fieldObj;
    }

    /**
     * 기존 error data map을 삭제 후, 신규 error data map을 생성
     *
     * @return error data map
     */
    public static Map<String, Object> createErrorDataMap(){
        // 기존 error data map 삭제
        ThreadRepository.remove(FORMATTER_ERROR_DATA);
        // 신규 error data map을 thread local에 등록
        Map<String, Object> errorData = new LinkedHashMap<>();
        ThreadRepository.set(FORMATTER_ERROR_DATA, errorData);

        return errorData;
    }

    /**
     * 대상 class field의 FixedData annotation에서 FixedField list 조회
     *
     * @param clazz
     * @return
     */
    private static List<FixedField> getFieldList(Class<?> clazz, Class<?> genericType){

        String key = clazz.getName();

        if (genericType != null) key = key.concat(genericType.getName());

        if(map.containsKey(key)) return map.get(key);

        List<FixedField> fieldList = new ArrayList<>();

        Class<?> superClass = clazz.getSuperclass();
        if(superClass != null){
            Field[] superFields = superClass.getDeclaredFields();
            for (Field field : superFields){
                if (field.isAnnotationPresent(FixedData.class)){
                    FixedData fixedData = field.getDeclaredAnnotation(FixedData.class);
                    FixedField fixedField = FixedField.builder().name(field.getName())
                            .type(field.getType()).isSuperClassField(true).fixedData(fixedData)
                            .build();
                    fieldList.add(fixedField);
                }else if (field.isAnnotationPresent(FixedVo.class)){
                    List<FixedField> subFieldList = getFieldList(field.getClass(), genericType);
                    Class<?> type = field.getType();
                    if (Object.class.equals(type) && genericType != null){
                        type = genericType;
                    }
                    FixedField fixedField = FixedField.builder().name(field.getName())
                            .type(type).isSuperClassField(true).isFixedVo(true)
                            .fixedFieldList(subFieldList).build();
                    fieldList.add(fixedField);

                }
            }
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields){
            if (field.isAnnotationPresent(FixedData.class)){
                FixedData fixedData = field.getDeclaredAnnotation((FixedData.class));
                FixedField fixedField = FixedField.builder().name(field.getName())
                        .type(field.getType()).fixedData(fixedData).build();
                fieldList.add(fixedField);
            }else if (field.isAnnotationPresent(FixedVo.class)){
                List<FixedField> subFieldList = getFieldList(field.getType(), genericType);
                Class<?> type = field.getType();
                if (Object.class.equals(type) && genericType != null){
                    type = genericType;
                }
                FixedField fixedField = FixedField.builder().name(field.getName()).type(type).isFixedVo(true).fixedFieldList(subFieldList).build();
                fieldList.add(fixedField);
            }else if (field.isAnnotationPresent(FixedList.class)){
                Object typeArgument = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                Class<?> type = null;
                if (typeArgument instanceof TypeVariable<?>){
                    // FixedList field가 List<$Generic>일 때
                    type = (Class<?>)((TypeVariable<?>)typeArgument).getBounds()[0];
                    if (Object.class.equals(type) && genericType != null){
                        type = genericType;
                    }
                }else{
                    type = (Class<?>)  ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                }

                List<FixedField> subFieldList = getFieldList(type, genericType);
                FixedField fixedField = FixedField.builder().name(field.getName()).type(type)
                        .fixedList(field.getDeclaredAnnotation(FixedList.class))
                        .fixedFieldList(subFieldList).build();
                fieldList.add(fixedField);
            }
        }

        map.put(key, fieldList);
        return fieldList;
    }

}