package com.example.model.common;

import com.example.utils.core.RequestScopeUtils;
import jakarta.validation.Valid;
import lombok.*;

/**
 * common request class
 *
 * @param <T>
 */
@Getter @Setter
@Builder
@ToString
@AllArgsConstructor
public class CommonRequest<T> {
    /**
     * RequestContextHolder에 CommonRequest 저장 시 사용되는 key
     */
    public static final String COMMON_REQUEST_KEY = "COMMON_REQUEST_KEY";

    // 공통 data
    @Valid
    private CommonHeader commonHeader;
    // 업무 data
    @Valid
    private T data;

    public CommonRequest() {
        // RequestContextHolder에 CommonRequest 저장 (request scope)
        RequestScopeUtils.setAttribute(COMMON_REQUEST_KEY, this);
    }
}
