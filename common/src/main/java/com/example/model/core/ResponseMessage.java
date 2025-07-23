package com.example.model.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * response 시 전달되는 공통 message
 *
 * @author openlabs
 *
 */
@Data
@Schema
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {
    /*
     * 메시지코드 (type:문자, 길이:8)
     *********************************
     * 메세지 코드(ex> FWKE0001)
     *********************************
     */
    @Schema(description = "메시지코드", example = "BIZM0001")
    private String code;

    /*
     * 메시지내용 (type:문자, 길이:80)
     *********************************
     * 처리 결과 메시지
     *********************************
     */
    @Schema(description = "처리 결과 메시지", example = "처리되었습니다.")
    private String message;

    /*
     * 성공여부코드 (type:숫자, 길이:1)
     *********************************
     * 요청 처리 결과 값
     * 0 : 정상처리
     * 1 : 실패
     *********************************
     */
    @Builder.Default
    @Schema(description = "요청 처리 결과 값", allowableValues = {"0","1"} ,example = "0")
    private Integer successCd = 0;
}
