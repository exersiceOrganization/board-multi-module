package com.example.model.core;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommonResponse<T> {
    // 공통 data
    private CommonHeader commonHeader;
    // 공통 응답 message
    private ResponseMessage message;
    // 업무 data
    private T data;

    /**
     * 성공여부코드(successCd) 확인
     * 0 : true
     * 그 외 : false
     *
     * @return boolen
     */
    public boolean isSuccess() {
        boolean isSuccess = false;
        if (this.message != null && this.message.getSuccessCd() == 0) {
            isSuccess = true;
        }
        return isSuccess;
    }
}
