package com.example.utils.core;

import com.example.exception.CommonException;
import com.example.internal.FixedData;
import com.example.internal.FixedData.PAD_TYPE;
import com.example.internal.FixedList;
import com.example.internal.FixedVo;
import com.example.model.core.FixedField;
import com.example.model.core.FixedFieldData;
import com.example.repository.ThreadRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

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

    private FormatterUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * data parsing에서 error 발생 시, throw exception 여부 설정
     *
     * @param dataParsingThrowException true : exception 설정
     */
    public static void setDataParsingThrowException(boolean dataParsingThrowException) {
        FormatterUtils.dataParsingThrowException = dataParsingThrowException;
    }

    /**
     * 고정 길이 문자열을 Object로 변환 시, padding characters를 trim하여 저장할지 여부 설정
     *
     * @param trimPaddingCharacters true : trim
     */
    public static void setTrimPaddingCharacters(boolean trimPaddingCharacters) {
        FormatterUtils.trimPaddingCharacters = trimPaddingCharacters;
    }

    /**
     * Fixed Data Object를 고정 길이 문자열로 변환
     * <pre>
     *     Formatterutils.getFixedData(fixedDataTestInfo)
     *     result : 0000001654000003432300001233.000000012.20000000003000000001200004334.0000123.2340000001234            ZZZABC
     * </pre>
     *
     * @param obj 고정 길이 문자열로 변환할 java object
     * @return String  fixed-length-data
     * @see FixedData
     */
    public static String getFixedData(Object obj) {
        return getFixedData(obj, DEFAULT_CHAR_ENCODING, false, createErrorDataMap());
    }

    /**
     * Fixed data Object를 고정 길이 문자열로 변환
     * <pre>
     *     FormatterUtils.getFixedData(fixedDataTestInfo, Constants.CHARSET_UTF_8)
     *     result : 0000001654000003432300001233.000000012.20000000003000000001200004334.0000123.2340000001234            ZZZABC
     * </pre>
     *
     * @param obj      고정 길이 문자열로 변환할 java object
     * @param encoding 변환 시 사용할 encoding
     * @return String    fixec-length-data
     * @see FixedData
     */
    @SuppressWarnings("unchecked")
    public static String getFixedData(Object obj, String encoding) {
        return getFixedData(obj, encoding, false, createErrorDataMap());
    }

    /**
     * Fixed Data Object를 고정 길이 문자열로 변환
     *
     * @param obj      고정 길이 문자열로 변환 할 java object
     * @param encoding 변환 시 사용할 encoding
     * @param isNull   obj가 null 값인 경우 true
     * @return String   fixec-length-data
     * @see FixedData
     */
    @SuppressWarnings("unchecked")
    private static String getFixedData(Object obj, String encoding, boolean isNull, Map<String, Object> formatterErrorData) {

        log.debug("PARAM : {}", obj);

        StringBuilder sb = new StringBuilder();
        List<FixedField> fieldList = getFieldList(obj.getClass(), null);
        int fieldListIndex = 0;
        int fieldListSize = fieldList.size();

        int i = 0;

        for (FixedField fixedField : fieldList) {
            fieldListIndex++;

            if (fixedField.isFixedVo()) {
                Object subObj = getFieldObject(obj, fixedField);
                if (subObj == null) {
                    try {
                        Constructor<?> constructor = fixedField.getType().getDeclaredConstructor();
                        constructor.setAccessible(true); // private 생성자도 호출 가능하게 설정
                        subObj = constructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                        log.debug("FixedVo Error", ex);
                        throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
                    }
                    sb.append(getFixedData(subObj, encoding, true, formatterErrorData));
                } else {
                    sb.append(getFixedData(subObj, encoding, false, formatterErrorData));
                }
                log.debug("sb_if : {}");
            } else if (fixedField.getFixedList() != null) {
                // FixedList 타입인 경우 List item을 추출해서 String으로 변환.
                List<FixedField> subFieldList = null;
                List<Object> listObj = (List<Object>) getFieldObject(obj, fixedField);

                // list size validation check
                int listSize = getFixedListSize(obj, fixedField, fieldListIndex, fieldListSize, false, formatterErrorData);
                // 마지막에 위치한 list 또는 size가 0이고, list object가 null 또는 empty이면 skip
                if ((listSize == Integer.MAX_VALUE || listSize == 0)
                        && (listObj == null || listObj.isEmpty())) {
                    continue;
                } else if (listObj == null) {
                    throw CommonException.builder().message("invalid data(list field is null").build();
                }

                // listSize가 설정된 list면 validation check
                int listObjSize = listObj.size();
                if (listSize != listObjSize && listSize != Integer.MAX_VALUE) {
                    log.warn("list size : {}, list object size : {}", listSize, listObjSize);
                    if (dataParsingThrowException) {
                        throw CommonException.builder().message("invalid data(list field)").build();
                    }
                }

                for (Object listItem : listObj) {
                    // list item의 field를 string으로 변환
                    if (!fixedField.getFixedFieldList().isEmpty()) {
                        subFieldList = fixedField.getFixedFieldList();
                    } else {
                        /*
                        list item의 타입이 generic type인 경우 feflection api를 통해 item type을 가져올 수 없다.
                        getFixedFieldList 함수를 통해 FixedFieldList를 가져오지 못했을 때
                        list item의 field list를 직접 가져오도록 수정.
                         */
                        subFieldList = getFieldList(listItem.getClass(), null);
                    }

                    for (FixedField subField : subFieldList) {
                        if (subField.isFixedVo()) {
                            // VO일때 Object를 다시 구해서 reculsive call
                            Object subFieldObject = getFieldObject(listItem, subField);
                            sb.append(getFixedData(subFieldObject, encoding, isNull, formatterErrorData));
                        } else if (subField.getFixedList() != null) {
                            // List일때 바로 변환 안되므로, List element마다 각각 변환해야 함.
                            List<Object> subFieldListObject = (List<Object>) getFieldObject(listItem, subField);

                            for (Object subFieldObject : subFieldListObject) {
                                sb.append(getFixedData(subFieldObject, encoding, isNull, formatterErrorData));
                            }
                        } else {
                            sb.append(padFixedData(listItem, subField, encoding, isNull));
                        }
                    }
                    log.debug("sb_elseIf_i : {}");
                    i++;
                }
            } else {
                sb.append(padFixedData(obj, fixedField, encoding, isNull));
                log.debug("sb_else : {}", sb);
            }
        }

        log.debug("FIXED_DATA : {}", sb);
        return sb.toString();
    }

    /**
     * list field 에 설정된 size 조회.
     *
     * @param obj                대상 object
     * @param fixedField         list 의 fixedField
     * @param fieldListIndex     대상 object 에서 계산 대상 list 의 index
     * @param fieldListSize      대상 object 의 field 수
     * @param mustThrowException size 조회 중 에러 발생시 반드시 throw exception 여부
     * @return list field 의 size.
     */
    private static int getFixedListSize(Object obj, FixedField fixedField, int fieldListIndex, int fieldListSize,
                                        boolean mustThrowException, Map<String, Object> formatterErrorData) {
        // size validation check
        FixedList fixedList = fixedField.getFixedList();
        int listSize = fixedList.size();
        if (listSize < 0) {
            String listSizeRef = fixedList.sizeRef();
            if (listSizeRef.isEmpty()) {
                /*
                 * FixedList 에 size / sizeRef 둘 다 설정되어있지 않다면,
                 * 마지막에 위치하고 size가 정해져있지 않은 list
                 */

                // 마지막에 위치하고 있지 않다면 error 발생
                if (fieldListIndex < fieldListSize) {
                    log.warn("field index : {}, field list size : {}", fieldListIndex, fieldListSize);
                    if (dataParsingThrowException || mustThrowException) {
                        throw CommonException.builder().message("invalid data(list field)").build();
                    }
                }
                // 마지막에 위치했다면, size 는 무한
                listSize = Integer.MAX_VALUE;
            } else {
                // sizeRef는 list 이전에 위치해야 함
                Object sizeRefObject = null;
                try {
                    Field field = obj.getClass().getDeclaredField(listSizeRef);
                    field.setAccessible(true);
                    sizeRefObject = field.get(obj);
                    // long 이면 int 로 변경
                    if (java.lang.Long.class.equals(sizeRefObject.getClass())) {
                        listSize = ((Long) sizeRefObject).intValue();
                    } else {
                        listSize = (int) field.get(obj);
                    }
                } catch (IllegalAccessException | NoSuchFieldException | NullPointerException e) {
                    log.warn("LIST TYPE:{}, field name : {}", fixedField.getType().getName(), fixedField.getName(), e);
                    formatterErrorData.put(fixedList.sizeRef(), sizeRefObject);
                    if (dataParsingThrowException || mustThrowException) {
                        throw CommonException.builder().message(e.getMessage()).cause(e).build();
                    }
                }
            }
        }
        log.debug("list size : {}", listSize);
        return listSize;
    }

    /**
     * FixedList가 2개 이상인지 확인. 2개 이상일 경우 false 리턴
     *
     * @param obj
     * @param count
     * @return
     * @see FixedList
     */
    @SuppressWarnings("unchecked")
    private static boolean checkFixedList(Object obj, int count) {

        if (count == 2) return false;

        boolean check = false;
        List<FixedField> fieldList = getFieldList(obj.getClass(), null);

        for (int i = 0; i < fieldList.size(); i++) {
            FixedField fixedField = fieldList.get(i);
            if (fixedField.isFixedVo()) {
                Object subObj = getFieldObject(obj, fixedField);
                check = checkFixedList(subObj, count);

                if (!check) return check;

            } else if (fixedField.getFixedFieldList() != null) {
                // FixedList field가 마지막에 있지 않을 경우 CommonException 발생
                if (i != fieldList.size() - 1)
                    throw CommonException.builder().message("invalid fixed data(list field)").build();

                List<Object> listObj = (List<Object>) getFieldObject(obj, fixedField);
                count++;

                if (count == 2) return false;

                // list가 null 일 경우 skip
                if (listObj == null) continue;

                for (Object listItem : listObj) {
                    check = checkFixedList(listItem, count);
                    if (!check) return check;
                }
            }
        }

        return true;
    }

    /**
     * Object에서 field name의 value값 조회
     *
     * @param obj        조회 대상 object
     * @param fixedField 조회할 field naem
     * @return field value
     */
    @SuppressWarnings("unchecked")
    private static Object getFieldObject(Object obj, FixedField fixedField) {

        Object fieldObj = new Object();
        try {
            Class<?> clazz = obj.getClass();
            if (fixedField.isSuperClassField()) {
                clazz = clazz.getSuperclass();
            }
            Field field = clazz.getDeclaredField(fixedField.getName());
            field.setAccessible(true);
            fieldObj = field.get(obj);

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.debug("[ERROR]FILED:{}", fixedField.getName(), e);
            throw CommonException.builder().message(e.getMessage()).cause(e).build();
        }
        return fieldObj;
    }

    /**
     * 대상 object 의 fixedField 정보를 가지고 고정 길이 문자열로 변환
     *
     * @param obj        변환 대상 object
     * @param fixedField object 의 fixedField
     * @param encoding   변환 시 사용할 encoding
     * @param isNull     obj 가 null 값인 경우 true
     * @return fixed-length-data
     */
    private static String padFixedData(Object obj, FixedField fixedField, String encoding, boolean isNull) {
        FixedData fixedData = fixedField.getFixedData();
        char padChar = getPadChar(fixedField);
        int[] lengthArr = getLength(fixedData);

        StringBuilder sb = new StringBuilder();
        String value = null;
        if (isNull) {
            value = String.valueOf(CHARACTER_TYPE_PADDING_CHAR);
            padChar = CHARACTER_TYPE_PADDING_CHAR;
        } else {
            Object fieldObj = getFieldObject(obj, fixedField);
            value = getStringValue(fieldObj, fixedField);
        }

        if (fixedField.getType().equals(BigDecimal.class)) {
            sb.append(getBigDecimalType(value, lengthArr, padChar, fixedData.signed(), encoding));
        } else {
            if (PAD_TYPE.RIGHT.equals(fixedData.padType())) {
                sb.append(paddingRight(value, lengthArr[0], padChar, encoding));
            } else {
                sb.append(paddingLeft(value, lengthArr[0], padChar, encoding));
            }
        }
        return sb.toString();
    }

    /**
     * BigDecimal을 고정 길이 문자열로 변환
     *
     * @param value
     * @param lengthArr
     * @param padCher
     * @param signed
     * @param encoding
     * @return
     */
    private static String getBigDecimalType(String value, int[] lengthArr, char padCher
            , boolean signed, String encoding) {

        StringBuilder sb = new StringBuilder();
        String[] valueArr = value.split("\\.");
        String precVal = valueArr[0];

        if (precVal.length() > 0) {
            char sign = precVal.charAt(0);

            if (sign == '*' || sign == '_') {
                sb.append(sign);
                precVal = precVal.substring(1);
            } else {
                if (signed) sb.append('+');
            }
        }

        sb.append(paddingLeft(precVal, lengthArr[0], padCher, encoding));
        if (lengthArr.length == 2 && lengthArr[1] > 0) {
            sb.append(".");

            String scaleVal = "";
            if (valueArr.length > 1) scaleVal = valueArr[1];

            sb.append(paddingRight(scaleVal, lengthArr[1], padCher, encoding));
        }

        return sb.toString();
    }

    /**
     * 고정 길이 문자열을 Fixed Data 객체로 변환
     * <pre>
     *     FixedDataTestInfo converted = FormatterUtils.getFixedData(fixedString, fixedDataTestInfo.class)
     * </pre>
     *
     * @param fld        변환 대상 문자열
     * @param objectType 변환 결과 object type
     * @return 변환결과 object
     */
    public static <T> T getFixedData(String fld, Class<T> objectType) {
        return getFixedData(fld, objectType, false);
    }

    /**
     * 고정 길이 문자열을 Fixed Data 객체로 변환
     * <pre>
     * example>
     *      FixedDataTestInfo converted =
     *                  FormatterUtils.getFixedData(fixedString, FixedDataTestInfo.class, false)
     * </pre>
     *
     * @param fld           변환 대상 문자열
     * @param objectType    변환 결과 object type
     * @param includeSigned signed 문자 포함 여부(+ / -)
     * @return 변환 결과 object
     */
    public static <T> T getFixedData(String fld, Class<T> objectType, boolean includeSigned) {
        Class<?> genericType = null;

        // 해당 objectType이 가질 수 있는 max length를 구함
        // FixedList가 포함된 경우 검사하지 않는다.
        Map<String, Object> errorMap = createErrorDataMap();
        checkMaxLength(size(fld, DEFAULT_CHAR_ENCODING), objectType, genericType, errorMap);

        return getFixedData(fld, new int[]{0}, objectType, DEFAULT_CHAR_ENCODING, genericType, includeSigned, errorMap);
    }

    /**
     * 고정 길이 문자열을 Fixed Data 객체로 변환
     * <pre>
     * example>
     *      GenericMessage<InnerMessage> converted =
     *              FormatterUtils.getFixedData(message, GenericMessage.class, InnerMessage.class);
     * </pre>
     *
     * @param fld         변환 대상 문자열
     * @param objectType  변환 결과 object type
     * @param genericType 변환 결과 object에 사용된 generic type
     * @return 변환 결과 object
     */
    public static <T> T getFixedData(String fld, Class<T> objectType, Class<?> genericType) {
        return getFixedData(fld, objectType, DEFAULT_CHAR_ENCODING, genericType);
    }

    /**
     * 고정 길이 문자열을 Fixed Data 객체로 변환
     * <pre>
     * example>
     *      GenericMessage<InnerMessage> converted =
     *              FormatterUtils.getFixedData(message, GenericMessage.class, "UTF-8", InnerMessage.class);
     * </pre>
     *
     * @param fld         변환 대상 문자열
     * @param objectType  변환 결과 object type
     * @param encoding    변환 시 사용할 encoding
     * @param genericType 변환 결과 object에 사용된 generic type
     * @return 변환 결과 object
     */
    public static <T> T getFixedData(String fld, Class<T> objectType, String encoding, Class<?> genericType) {
        // 해당 objectType이 가질 수 있는 max length를 구한다.
        // FixedList 가 포함된 경우 검사하지 않는다.
        Map<String, Object> errorMap = createErrorDataMap();
        checkMaxLength(size(fld, encoding), objectType, genericType, errorMap);

        return getFixedData(fld, new int[]{0}, objectType, encoding, genericType, false, errorMap);
    }

    /**
     * 고정 길이 문자열을 Fixed Data 객체로 변환
     *
     * @param fld
     * @param followIndex
     * @param objectType
     * @param encoding
     * @param genericType
     * @param includeSigned
     * @param formatterErrorData
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <T> T getFixedData(String fld, int[] followIndex, Class<T> objectType, String encoding,
                                      Class<?> genericType, boolean includeSigned, Map<String, Object> formatterErrorData) {
        log.debug("PARAM::TYPE:{}, [{}], {}", objectType, fld, followIndex);
        List<FixedField> fieldList = getFieldList(objectType, genericType);
        Object obj = null;
        try {
            Constructor<?> constructor = objectType.getDeclaredConstructor();
            constructor.setAccessible(true); // private 생성자도 호출 가능하게 설정
            obj = constructor.newInstance();
            int size = size(fld, encoding);
            int index = 0;
            int fieldListIndex = 0;
            int fieldListSize = fieldList.size();
            for (FixedField fixedField : fieldList) {
                fieldListIndex++;
                if (fixedField.isFixedVo()) {
                    // VO parsing 시 error data 저장할 LinkedHashMap
                    Map<String, Object> voErrorData = new LinkedHashMap<>();
                    formatterErrorData.put(fixedField.getName(), voErrorData);

                    Object subObj = getFixedData(substring(fld, index, encoding),
                            followIndex, fixedField.getType(),
                            encoding, genericType, includeSigned, voErrorData);
                    index += followIndex[0];
                    setField(obj, fixedField, subObj, voErrorData);
                    // 마지막 field 일 때, multi-depth를 대비하여 followIndex update
                    if (fieldListSize == fieldListIndex) {
                        followIndex[0] = index;
                    }
                } else if (fixedField.getFixedList() != null) {
                    // 변환되어 저장될 list
                    List<Object> list = new ArrayList<>();

                    // list parsing 시 error data 저장할 List<LinkedHashMap>
                    List<Map<String, Object>> listErrorData = new ArrayList<>();
                    formatterErrorData.put(fixedField.getName(), listErrorData);

                    // field 에 설정된 list size 조회
                    int listSize = getFixedListSize(obj, fixedField, fieldListIndex, fieldListSize, true, formatterErrorData);
                    for (int i = 0; i < listSize; i++) {
                        // 계산된 index가 변환할 문자열 byte length랑 같을 경우 변환이 완료된 것으로 판단
                        if (size <= index) {
                            break;
                        }

                        String subStr = substring(fld, index, encoding);

                        // list 내부 parsing 시 error data 저장할 LinkedHashMap
                        Map<String, Object> subListErrorData = new LinkedHashMap<>();
                        listErrorData.add(subListErrorData);

                        list.add(getFixedData(subStr
                                , followIndex
                                , fixedField.getType()
                                , encoding
                                , genericType
                                , includeSigned
                                , subListErrorData));
                        // parsing 후에 index 계산
                        index += followIndex[0];
                    }
                    setField(obj, fixedField, list, formatterErrorData);
                    // 마지막 field 일 때, multi-depth를 대비하여 followIndex update
                    if (fieldListSize == fieldListIndex) {
                        followIndex[0] = index;
                    }
                } else {
                    FixedData fixedData = fixedField.getFixedData();
                    int[] lengthArr = getLength(fixedData);
                    int endIndex = index + lengthArr[0];
                    if (fixedField.getType().equals(BigDecimal.class)) {
                        if (!includeSigned) {
                            char sign = substring(fld, index, index + 1, encoding).charAt(0);
                            if (sign == '+' || sign == '-') {
                                endIndex++;
                            }
                        }
                    }

                    if (lengthArr.length > 1) {
                        endIndex = endIndex + lengthArr[1] + 1;
                    }

                    if (endIndex > size) {
                        endIndex = size;
                    }

                    String value = substring(fld, index, endIndex, encoding);
                    setField(obj, fixedField, value, formatterErrorData);

                    if (index >= endIndex) {
                        break;
                    }
                    index = endIndex;
                    followIndex[0] = endIndex;
                }
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | IllegalArgumentException | SecurityException | InvocationTargetException e) {
            log.warn("[ERROR]TYPE:{}", objectType.getName(), e);
            throw CommonException.builder().message(e.getMessage()).cause(e).build();
        }
        log.debug("FIXED_DATA:{}", obj);
        return (T) obj;
    }

    /**
     * parsing 과정에서 생성된 error data map 조회
     *
     * @return erro data map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getErrorDataMap() {
        return (Map<String, Object>) ThreadRepository.get(FORMATTER_ERROR_DATA);
    }

    /**
     * 기존 error data map을 삭제 후, 신규 error data map을 생성
     *
     * @return error data map
     */
    public static Map<String, Object> createErrorDataMap() {
        // 기존 error data map 삭제
        ThreadRepository.remove(FORMATTER_ERROR_DATA);
        // 신규 error data map을 thread local에 등록
        Map<String, Object> errorData = new LinkedHashMap<>();
        ThreadRepository.set(FORMATTER_ERROR_DATA, errorData);

        return errorData;
    }

    /**
     * 변환 대상 fixed string의 byte 수와 변환 결과 object type 의 max length 비교.
     *
     * @param targetByteLength   변환 대상 fixed string의 byte 수
     * @param objectType         변환 결과 object type
     * @param genericType        변환 결과 object에 사용된 generic type
     * @param foramtterErrorData error data map
     */
    private static <T> void checkMaxLength(int targetByteLength, Class<T> objectType
            , Class<?> genericType, Map<String, Object> foramtterErrorData) {

        // 해당 objectType이 가질 수 있는 max length를 구함
        // FixeList가 포함된 경우 검사하지 않음
        Integer maxFieldLength = getObjectMaxLength(objectType, genericType);
        // 변환대상 string byte와 비교
        if (maxFieldLength != Integer.MAX_VALUE) {
            if (maxFieldLength < targetByteLength) {
                // OVERSIZE: {입력 string byte}/{대상 object size}
                String errorValue = new StringBuffer(FORMATTER_LENGTH_OVERSIZE)
                        .append(":")
                        .append(targetByteLength)
                        .append("/")
                        .append(maxFieldLength).toString();
                foramtterErrorData.put(FORMATTER_LENGTH_ERROR, errorValue);

                if (dataParsingThrowException) {
                    throw CommonException.builder()
                            .message("invalid fixed data(byte length). valid object length : " + maxFieldLength)
                            .build();
                }
            } else if (maxFieldLength > targetByteLength) {
                // undersize 시에는 exception 발생시키지 않고, 부족한 data는 각 field object 기본값(null, 0...)으로 둠
                // UNDERSIZE: {입력 stirng byte}/{대상 object size}
                String errorValue = new StringBuffer(FORMATTER_LENGTH_UNDERSIZE)
                        .append(":")
                        .append(targetByteLength)
                        .append("/")
                        .append(maxFieldLength).toString();
                foramtterErrorData.put(FORMATTER_LENGTH_ERROR, errorValue);
            }
        }
    }

    /**
     * byte length 조회
     *
     * @param value
     * @param encoding
     * @return
     */
    private static int size(String value, String encoding) {

        int size = 0;
        try {
            size = value.getBytes(encoding).length;
        } catch (UnsupportedEncodingException ex) {
            log.debug("{}", ex.getMessage());
            size = value.getBytes().length;
        }

        return size;
    }

    /**
     * substring byte index
     *
     * @param str
     * @param startIdx
     * @param encoding
     * @return
     */
    private static String substring(String str, int startIdx, String encoding) {
        return substring(str, startIdx, size(str, encoding), encoding);
    }

    /**
     * substring byte index
     *
     * @param str
     * @param startIdx
     * @param endIndex
     * @param encoding
     * @return
     */
    private static String substring(String str, int startIdx, int endIndex, String encoding) {
        int length = endIndex - startIdx;
        try {
            byte[] bytes = str.getBytes(encoding);
            byte[] value = new byte[length];
            if (bytes.length < startIdx + length) {
                return str;
            }
            for (int i = 0; i < length; i++) {
                value[i] = bytes[startIdx + i];
            }
            return new String(value, encoding);
        } catch (UnsupportedEncodingException e) {
            log.debug("{}", e.getMessage());
        }
        return str;
    }

    /**
     * 대상 object 의 field 에 data 값 설정
     *
     * @param obj
     * @param fixedField
     * @param data
     */
    private static void setField(Object obj, FixedField fixedField, Object data,
                                 Map<String, Object> formatterErrorData) {
        try {
            Class<?> clazz = obj.getClass();
            if (fixedField.isSuperClassField()) {
                clazz = clazz.getSuperclass();
            }

            Field field = clazz.getDeclaredField(fixedField.getName());
            // field type이 String 일 경우, trimPaddingCharacters 설정에 따라 Padding Char 값을 자르지 않고 설정
            if (fixedField.getFixedData() != null
                    && (!String.class.equals(field.getType()) || trimPaddingCharacters)) {
                // String to Object 로 변환 시 Padding Type(Left/Right)에 세팅된 Padding Char 값을 자르지 않고 Type Casting만 하고 있음 (getTypeValue 참조)
                // 현재 상태론 Padding Char가 문자이거나 Padding Type이 Right이면서 숫자 일 경우 모두 문제가 발생함.
                // Type Casting 전 추출된 data를 Padding Type 부터 Padding Char가 안나올 때 까지 자르는 로직 추가

                // field data 및 padding 정보 추출
                String dataStr = String.valueOf(data);
                PAD_TYPE padType = fixedField.getFixedData().padType();
                char padChar = getPadChar(fixedField);

                //padding type에 따라 문자열을 자르기 시작할 index 결정
                int startIndex = 0;
                if (PAD_TYPE.RIGHT.equals(padType)) {
                    startIndex = dataStr.length() - 1;
                } else {
                    startIndex = 0;
                }

                //문자열을 자르기 시작할 index 부터 padding type 방향으로 padding char가 안나올 때 까지 index 결정
                while (startIndex >= 0 && startIndex < dataStr.length() && dataStr.charAt(startIndex) == padChar) {
                    if (PAD_TYPE.RIGHT.equals(padType)) {
                        startIndex--;
                    } else {
                        startIndex++;
                    }
                }

                //padding type에 따라 문자열 자르기
                if (PAD_TYPE.RIGHT.equals(padType)) {
                    data = dataStr.substring(0, startIndex + 1);
                } else {
                    data = dataStr.substring(startIndex, dataStr.length());
                }
            }

            field.setAccessible(true);
            try {
                field.set(obj, getTypeValue(data, field.getType()));
            } catch (IllegalArgumentException | DateTimeParseException e) {
                log.debug("[ERROR]FILED:{}", fixedField.getName(), e);
                formatterErrorData.put(fixedField.getName(), data);
                if (dataParsingThrowException) {
                    throw e;
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.debug("[ERROR]FILED:{}", fixedField.getName(), e);
            throw CommonException.builder().message(e.getMessage()).cause(e).build();
        }
    }

    /**
     * 왼쪽에 padding char를 추가한 string 조회
     *
     * @param value
     * @param length
     * @param padChar
     * @param encoding
     * @return
     */
    private static String paddingLeft(String value, int length, char padChar, String encoding) {

        int size = size(value, encoding);

        if (size > length) throw CommonException.builder().message("invalid fixed values(length error)").build();

        StringBuilder sb = new StringBuilder(length);
        // 20250729 : 다중 byte 패딩 문자 지원
        int padCharSize = size(String.valueOf(padChar), encoding);

        // 루프마다 padChar를 추가하면서 16byte또는 16문자만큼의 간격을 건너뛰어 다음 패딩 위치를 계산
        // 일반적으로 블록 단위로 패딩을 채울 때 사용
        for (int i = size; i < length; i += padCharSize) {
            // TODO: byte 보정 또는 에러 발생 여부는 사이트의 정책에 따름
            if ((i + padCharSize) > length) {
                int correctionCnt = length - 1;
                for (int j = 0; j < correctionCnt; j++) {
                    // space로 byte 보정
                    sb.append(CHARACTER_TYPE_PADDING_CHAR);
                }
                break;
            }
            sb.append(padChar);
        }
        sb.append(value);

        return sb.toString();
    }

    /**
     * 오른졲에 padding char를 추가한 string 조회
     *
     * @param value
     * @param length
     * @param padChar
     * @param encoding
     * @return
     */
    private static String paddingRight(String value, int length, char padChar, String encoding) {

        int size = size(value, encoding);
        // Data 크기가 지정된 length보다 클 때 exception 발생
        if (size > length) throw CommonException.builder().message("invalide fixed value(length error)").build();

        StringBuilder sb = new StringBuilder(length);
        sb.append(value);
        // 20250729 : 다중 byte 패딩 문자 지원
        int padCharSize = size(String.valueOf(padChar), encoding);

        for (int i = size; i < length; i += padCharSize) {
            // TODO: byte 보정 또는 에러 발생 여부는 사이트의 정책에 따름
            if ((i + padCharSize) > length) {
                int correctionCnt = length - 1;
                for (int j = 0; j < correctionCnt; j++) {
                    // space로 byte 보정
                    sb.append(CHARACTER_TYPE_PADDING_CHAR);
                }
                break;
            }
            sb.append(padChar);
        }

        return sb.toString();
    }

    /**
     * FixedData에 설정된 length 조회
     *
     * @param fixedData
     * @return
     */
    private static int[] getLength(FixedData fixedData) {

        int[] lengthArr = fixedData.value();

        if (lengthArr.length == 1 && lengthArr[0] == 0) {
            lengthArr = fixedData.length();
        }

        return lengthArr;
    }

    /**
     * FixedData에 설정된 padding char 조회
     *
     * @param fixedField
     * @return
     */
    private static char getPadChar(FixedField fixedField) {

        char padChar = fixedField.getFixedData().padChar();

        // char에 대한 유효성 검사
        if (padChar == Character.UNASSIGNED) {
            if (fixedField.getType().isPrimitive()
                    || fixedField.getType().getSuperclass().equals(Number.class)) {
                padChar = NUMBER_TYPE_PADDING_CHAR;
            } else {
                padChar = CHARACTER_TYPE_PADDING_CHAR;
            }
        }

        return padChar;
    }

    /**
     * 대상 class field의 FixedData annotation에서 FixedField list 조회
     *
     * @param clazz
     * @return
     */
    private static List<FixedField> getFieldList(Class<?> clazz, Class<?> genericType) {

        String key = clazz.getName();

        if (genericType != null) key = key.concat(genericType.getName());

        if (map.containsKey(key)) return map.get(key);

        List<FixedField> fieldList = new ArrayList<>();

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            Field[] superFields = superClass.getDeclaredFields();
            for (Field field : superFields) {
                if (field.isAnnotationPresent(FixedData.class)) {
                    FixedData fixedData = field.getDeclaredAnnotation(FixedData.class);
                    FixedField fixedField = FixedField.builder().name(field.getName())
                            .type(field.getType()).isSuperClassField(true).fixedData(fixedData)
                            .build();
                    fieldList.add(fixedField);
                } else if (field.isAnnotationPresent(FixedVo.class)) {
                    List<FixedField> subFieldList = getFieldList(field.getClass(), genericType);
                    Class<?> type = field.getType();
                    if (Object.class.equals(type) && genericType != null) {
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
        for (Field field : fields) {
            if (field.isAnnotationPresent(FixedData.class)) {
                FixedData fixedData = field.getDeclaredAnnotation((FixedData.class));
                FixedField fixedField = FixedField.builder().name(field.getName())
                        .type(field.getType()).fixedData(fixedData).build();
                fieldList.add(fixedField);
            } else if (field.isAnnotationPresent(FixedVo.class)) {
                List<FixedField> subFieldList = getFieldList(field.getType(), genericType);
                Class<?> type = field.getType();
                if (Object.class.equals(type) && genericType != null) {
                    type = genericType;
                }
                FixedField fixedField = FixedField.builder().name(field.getName()).type(type).isFixedVo(true).fixedFieldList(subFieldList).build();
                fieldList.add(fixedField);
            } else if (field.isAnnotationPresent(FixedList.class)) {
                Object typeArgument = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                Class<?> type = null;
                if (typeArgument instanceof TypeVariable<?>) {
                    // FixedList field가 List<$Generic>일 때
                    type = (Class<?>) ((TypeVariable<?>) typeArgument).getBounds()[0];
                    if (Object.class.equals(type) && genericType != null) {
                        type = genericType;
                    }
                } else {
                    type = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
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

    /**
     * 입력 data에서 대상 type 의 value 조회
     *
     * @param data
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <T> T getTypeValue(Object data, Class<T> type) {
        log.debug("data:[{}]", data);
        Object value = null;
        if (type.isPrimitive()) {
            if (data == null) {
                data = 0;
            } else if (String.class.equals(data.getClass()) &&
                    (data.toString().trim().isEmpty())) {
                data = "0";
            }
            if (type == long.class) {
                value = Long.parseLong(String.valueOf(data).trim());
            } else if (type == int.class) {
                value = Integer.parseInt(String.valueOf(data).trim());
            } else if (type == float.class) {
                value = Float.parseFloat(String.valueOf(data).trim());
            } else if (type == double.class) {
                value = Double.parseDouble(String.valueOf(data).trim());
            } else {
                value = data;
            }
        } else {
            if (String.class.equals(type)) {
                value = data == null ? "" : String.valueOf(data);
            } else {
                if (data == null) {
                    data = 0;
                } else if ((String.class.equals(data.getClass()))
                        && (data.toString().trim().isEmpty())) {
                    data = "0";
                }
                if (Long.class.equals(type)) {
                    value = Long.valueOf(String.valueOf(data).trim());
                } else if (Integer.class.equals(type)) {
                    value = Integer.valueOf(String.valueOf(data).trim());
                } else if (BigDecimal.class.equals(type)) {
                    value = new BigDecimal(String.valueOf(data).trim());
                } else if (Float.class.equals(type)) {
                    value = Float.valueOf(String.valueOf(data).trim());
                } else if (Double.class.equals(type)) {
                    value = Double.valueOf(String.valueOf(data).trim());
                } else if (Boolean.class.equals(type)) {
                    value = Boolean.valueOf(String.valueOf(data).trim());
                } else if (LocalDateTime.class.equals(type)) {
                    value = LocalDateTime.parse(String.valueOf(data).trim());
                } else {
                    value = data;
                }
            }
        }
        return (T) value;
    }

    /**
     * 입력 data를 string으로 변환한 값 조회
     *
     * @param data
     * @param fixedField
     * @return
     */
    private static String getStringValue(Object data, FixedField fixedField) {

        String value = "";
        Class<?> type = fixedField.getType();

        if (type.isPrimitive()) {
            value = data == null ? "" : String.valueOf(data);
        } else {
            if (String.class.equals(type)) value = data == null ? "" : String.valueOf(data);
            else if (Long.class.equals(type)) value = data == null ? "0" : String.valueOf(data);
            else if (Integer.class.equals(type)) value = data == null ? "0" : String.valueOf(data);
            else if (BigDecimal.class.equals(type))
                value = data == null ? "0" : new BigDecimal(String.valueOf(data).trim()).toPlainString();
            else if (Float.class.equals(type)) value = data == null ? "0" : String.valueOf(data);
            else if (Double.class.equals(type)) value = data == null ? "0" : String.valueOf(data);
            else if (Boolean.class.equals(type)) value = data == null ? "false" : String.valueOf(data);
            else if (LocalDateTime.class.equals(type)) value = data == null ? "0" : String.valueOf(data);
            else value = data == null ? "" : String.valueOf(data);
        }

        return value;
    }

    /**
     * 고정 길이 대상 class의 max string length를 구함.
     * FixedList가 있는 ObjectType의 경우 데이터 길이가 무제한이므로 Integer.MAX_VALUE로 리턴
     * <pre>
     * example>
     *      FormatterUtils.getObjectMaxLength(FixedDataTestInfo.class, null)
     *      result : 110
     * </pre>
     *
     * @param objectType  변환 결과 object type
     * @param genericType 변환 결과 object에 사용된 generic type
     * @return 변환 대상 class가 가질 수 있는 max string length
     */
    public static <T> Integer getObjectMaxLength(Class<T> objectType, Class<?> genericType) {
        return getFieldList(objectType, genericType).stream()
                .map(mapper -> {
                    if (mapper.getFixedData() != null) {
                        //FixedData일 경우 field의 length를 구해서 return
                        return Arrays.stream(getLength(mapper.getFixedData()));
                    } else if (mapper.isFixedVo()) {
                        // multi-depth 일 경우 길이는 무제한
                        List<FixedField> fixedFieldList = getFieldList(mapper.getType(), genericType);
                        for (FixedField fixedField : fixedFieldList) {
                            // multi-depth 일 경우 길이는 무제한
                            if (fixedField.getFixedList() != null || fixedField.isFixedVo()) {
                                return IntStream.of(Integer.MAX_VALUE);
                            }
                        }

                        // FixedVO일 경우 VO 내부의 field length를 구해서 return
                        return fixedFieldList.stream()
                                .map(fixedVoField -> Arrays.stream(getLength(fixedVoField.getFixedData())))
                                .mapToInt(t -> t.sum());
                    } else if (mapper.getFixedList() != null) {
                        //FixedList가 있을 경우 길이는 무제한
                        return IntStream.of(Integer.MAX_VALUE);
                    }

                    //FixedData, FixedVO, FixedList 타입이 아닐경우 Exception throw
                    throw CommonException.builder().message("invalid fixed configuration(parse error)").build();
                })
                .map(mapper -> {
                    return mapper.sum();
                })
                .reduce(0, (prev, next) -> {
                    if (prev == Integer.MAX_VALUE || next == Integer.MAX_VALUE) {
                        return Integer.MAX_VALUE;
                    }

                    return prev + next;
                });


    }

    /**
     * VO Object의 empty(padchar로 채워짐) 고정길이 문자열 리턴
     */
    private static <T> String getEmptyFixedDataString(Class<T> objectType, Class<?> genericType) {
        return getFieldList(objectType, genericType).stream().map(fixedField -> {
                    FixedData fixedData = fixedField.getFixedData();
                    char padChar = getPadChar(fixedField);
                    int length = getLength(fixedData)[0];
                    StringBuilder ret = new StringBuilder();
                    for (int i = 0; i < length; i++) {
                        ret.append(padChar);
                    }

                    return ret.toString();
                })
                .reduce("", (prev, next) -> prev + next);
    }

    /**
     * primitive or String data를 고정길이 문자열로 변환.
     * <pre>
     * example>
     *   FixedFieldData fieldData = FixedFieldData.builder()
     *           .dataType("java.lang.Integer")
     *           .length(10)
     *           .padChar('0')
     *           .padType(PAD_TYPE.RIGHT)
     *           .value("0")
     *           .build();
     *   String fixedDataString = FormatterUtils.getFixedDataString(fieldData);
     *
     *   result : 0000000000
     * </pre>
     *
     * @param fieldData 변환 대상 data
     * @return fixed-length-data
     */
    public static String getFixedDataString(FixedFieldData fieldData) {
        return getFixedDataString(fieldData, DEFAULT_CHAR_ENCODING);
    }

    /**
     * primitive or String data를 고정길이 문자열로 변환
     * <pre>
     * example>
     *   FixedFieldData fieldData = FixedFieldData.builder()
     *           .dataType("java.lang.Integer")
     *           .length(10)
     *           .padChar('0')
     *           .padType(PAD_TYPE.RIGHT)
     *           .value("0")
     *           .build();
     *   String fixedDataString = FormatterUtils.getFixedDataString(fieldData, "UTF-8");
     *
     *   result : 0000000000
     * </pre>
     *
     * @param fieldData 변환 대상 data
     * @param encoding  변환 시 사용할 encoding
     * @return fixed-length-data
     */
    public static String getFixedDataString(FixedFieldData fieldData, String encoding) {
        try {
            Class<?> clazz = Class.forName(fieldData.getDataType());
            Object typeValue = getTypeValue(fieldData.getValue(), clazz);

            if (typeValue instanceof Iterable || typeValue instanceof Map) {
                throw CommonException.builder().message("value type은 Iterable 또는 Map type일 수 없습니다.")
                        .build();
            }

            String value = String.valueOf(typeValue);

            if (PAD_TYPE.LEFT.equals(fieldData.getPadType())) {
                return paddingLeft(value, fieldData.getLength(), fieldData.getPadChar(), encoding);
            } else {
                return paddingRight(value, fieldData.getLength(), fieldData.getPadChar(), encoding);
            }

        } catch (ClassNotFoundException e) {
            throw CommonException.builder().message(e.getMessage()).cause(e).build();
        }

    }
}