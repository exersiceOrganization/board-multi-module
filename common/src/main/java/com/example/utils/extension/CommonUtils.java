package com.example.utils.extension;

import com.example.model.core.PagingInfo;
import com.example.model.common.CommonHeader;
import com.example.model.common.CommonRequest;
import com.example.model.common.CommonResponse;
import com.example.model.common.ResponseMessage;
import com.example.utils.core.DateUtils;
import com.example.utils.core.RequestScopeUtils;
import com.example.utils.core.StringUtils;
import io.opentelemetry.api.trace.TraceId;
import lombok.experimental.UtilityClass;

/**
 * 공통 관련 utility class
 */
// static 선언되어 인스턴스 생성 없이 직접 접근하여 사용
@UtilityClass
public class CommonUtils {

    /**
     * RequestContextHolder에 Response Message 저장 시 사용되는 key
     */
    public static final String RESPONSE_MESSAGE_KEY = "RESPONSE_MESSAGE_KEY";

    /**
     * CommonResponse 생성.
     * 기본적인 CommonResponse 정보 설정(CommonHeader / ResponseMessage).
     * <pre>
     * example>
     *      CommonResponse<String> response =
     *                  CommonUtils.createCommonResponse("test");
     * </pre>
     *
     * @param data response data
     * @return CommonResponse
     */
    public static <T> CommonResponse<T> createCommonResponse(T data) {
        return setResponseInfo(CommonResponse.<T>builder().data(data).build());
    }


    /**
     * CommonResponse 생성.
     * 기본적인 CommonResponse 정보 설정(CommonHeader / ResponseMessage).
     * <pre>
     * example>
     *      CommonResponse<TestModel> response =
     *                  CommonUtils.createCommonResponse(commonRequest, testModel);
     * </pre>
     *
     * @param request CommonRequest
     * @param data response data
     * @return CommonResponse
     */
    public static <T> CommonResponse<T> createCommonResponse(CommonRequest<?> request, T data){
        return setResponseInfo(
                CommonResponse.<T>builder()
                        .commonHeader(request.getCommonHeader())
                        .data(data)
                        .build()
        );
    }

    /**
     * CommonResponse 생성.
     * 기본적인 CommonResponse 정보 설정(CommonHeader / ResponseMessage).
     * <pre>
     * example>
     *      CommonResponse<TestModel> response =
     *                  CommonUtils.createCommonResponse(commonRequest);
     * </pre>
     *
     * @param request CommonRequest
     * @return CommonResponse
     */
    public static <T> CommonResponse<T> createCommonResponse(CommonRequest<T> request){
        return setResponseInfo(
                CommonResponse.<T>builder()
                        .commonHeader(request.getCommonHeader())
                        .build()
        );
    }

    /**
     * CommonResponse에 Response 메시지 설정
     * <pre>
     * example>
     *      setResponseMessage(commonResponse, true, "BIZM0003");
     * </pre>
     *
     * @param commonResponse 설정될 CommonResponse
     * @param isSucess 업무 성공 여부
     * @param code 메시지 코드
     * @param args 메시지 내용에 설정될 메시지 변수
     */
    public static <T> void setResponseMessage(CommonResponse<T> commonResponse, boolean isSucess, String code, String... args) {
        ResponseMessage resposeMessage = commonResponse.getMessage();
        if (resposeMessage == null) {
            resposeMessage = ResponseMessage.builder().build();
            commonResponse.setMessage(resposeMessage);
        }

        resposeMessage.setCode(code);
        resposeMessage.setMessage(MessageUtils.getMessage(code, args));
        if (isSucess) {
            resposeMessage.setSuccessCd(0);
        } else {
            resposeMessage.setSuccessCd(1);
        }
    }

    /**
     * Response 메시지 설정.
     * 이후 createCommonResponse() 로 CommonResponse 생성 시 사용됨.
     * <pre>
     * example>
     *      CommonUtils.setResponseMessage(true, "FWKE0001", "test");
     * </pre>
     *
     * @param isSucess 업무 성공 여부
     * @param code     메시지 코드
     * @param args     메시지 내용에 설정될 메시지 변수
     */
    public static void setResponseMessage(boolean isSucess, String code, String... args) {
        ResponseMessage resposeMessage = ResponseMessage.builder().build();
        resposeMessage.setCode(code);
        resposeMessage.setMessage(MessageUtils.getMessage(code, args));
        if (isSucess) {
            resposeMessage.setSuccessCd(0);
        } else {
            resposeMessage.setSuccessCd(1);
        }

        // RequestContextHolder에 ResponseMessage 저장 (request scope)
        RequestScopeUtils.setAttribute(RESPONSE_MESSAGE_KEY, resposeMessage);
    }

    /**
     * GUID 생성 함수.
     * <pre>
     * trace id와 통합하기 위해, 32 byte 의 HEX String으로 fix됨.(Propagator 규약)
     * prefix(숫자3) + 현재일시(YYYYMMDDhh24miss(14)+millisecond(3)) + random numbers(12)
     * </pre>
     * <pre>
     * example>
     *      CommonUtils.generateGuid(123);
     *      result : 12320231213181757838afc2ab2b581f
     * </pre>
     *
     * @param prefix    GUID 구분을 위한 prefix value
     * @return 정상 처리시 32 byte 의 HEX String. 오류 발생시 TraceId invalid값 00000000000000000000000000000000.
     */
    public static String generateGuid(Integer prefix) {
        if (prefix == null || prefix.toString().length() != 3) {
            return TraceId.getInvalid();
        }
        String randomString = StringUtils.generateSecretString(12);
        StringBuilder result = new StringBuilder(Integer.toString(prefix));
        return result.append(DateUtils.getCurrentDatetimemilli())
                .append(randomString).toString();
    }

/**
     * CommonResponse 정보 설정(CommonHeader / ResponseMessage)
     * 객체 생성 시, Data 설정 시 호출
     *
     * @param commonResponse
     * @return
     */
    private static <T> CommonResponse<T> setResponseInfo(CommonResponse<T> commonResponse){
        CommonRequest<?> commonRequest = (CommonRequest<?>) RequestScopeUtils.getAttribute(CommonRequest.COMMON_REQUEST_KEY);

        if (commonRequest != null){
            CommonHeader requestCommonHeader = commonRequest.getCommonHeader();
            if (commonResponse.getCommonHeader() == null){
                commonResponse.setCommonHeader(requestCommonHeader);
            }

            // ResponseMessage 조회 후 설정
            ResponseMessage responseMessage = (ResponseMessage) RequestScopeUtils.getAttribute(RESPONSE_MESSAGE_KEY);
            commonResponse.setMessage(responseMessage);

            // default message 설정
            if (commonResponse.getMessage() == null){
                PagingInfo pagingInfo = commonResponse.getCommonHeader().getPagingInfo();
                if (pagingInfo != null) {
                    if (pagingInfo.isHasNextPage()) {
                        setResponseMessage(commonResponse, true, "BIZM0003"); // 조회가 계속됩니다.
                    } else {
                        setResponseMessage(commonResponse, true, "BIZM0002"); // 조회가 완료되었습니다.
                    }
                } else {
                    setResponseMessage(commonResponse, true, "BIZM0001"); // 처리되었습니다.
                }
            }
        }

        return commonResponse;
    }
}