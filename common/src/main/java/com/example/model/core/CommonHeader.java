package com.example.model.core;

import com.example.model.PagingInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema //  RESTful API 문서를 자동 생성할 때 사용
@Getter @Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommonHeader {
    /*
     * GUID (type:문자, 길이:32)
     *********************************
     * Global Uid
     * ex> 채널구분코드(3) + yyyyMMddHHmmssSSS(17) + 자체랜덤값생성(12)
     *********************************
     */
    @Schema(description = "GUID")
    private String guid;

    /*
     * 사용자ID (type:문자, 길이:9)
     *********************************
     * 사용자 계정 또는 배치 잡 이름 등이 들어감
     *********************************
     */
    @Schema(description = "사용자ID")
    private String userId;

    /*
     * 대상 DB 구분코드 (type:문자, 길이:1)
     *********************************
     * 서비스 처리할  DB를 지정
     * 1 : 홀수
     * 2 : 짝수
     *********************************
     */
    @Schema(description = "대상DB구분코드", example = "1")
    private String even;

    /*
     * 조회건수 (type:숫자, 길이:3)
     *********************************
     * 한번의 거래로 조회할 레코드 건수
     *********************************
     */
    @Schema(description = "조회건수", example = "10")
    @Builder.Default
    private Integer qryCnt = 10;

    /*
     * 조회 Page 숫자 (type:숫자, 길이:3)
     *********************************
     * 페이징 조회 처리 시 현재 반환받고자 하는 페이지 숫자
     * 연속거래 키 방식 사용 시에는 contTrkey field 를 이용
     *********************************
     */
    @Schema(description = "조회 Page 숫자", example = "1")
    @Builder.Default
    private Integer pageCnt = 1;

    /*
     * 연속거래키 값 리스트(type:문자 LIST, 길이:50)
     *********************************
     * 연속거래키를 이용하여 페이징 처리 시 서비스에서 채워서 응답전문을 보내고,
     * 다시 다음 페이지 요청 시 연속거래키 data 를 응답받은 data로 채워서 전송
     *********************************
     */
    @Schema(description = "연속거래키 값 리스트")
    private List<Object> contTrkeyValues;

    /*
     * online timeout (type:숫자, 길이:6)
     *********************************
     * 온라인 거래 시 timeout 숫자. 단위는 ms.
     *********************************
     */
    @Schema(description = "timeout 숫자", example = "5000")
    @Builder.Default
    private Integer timeout = 0;

    /*
     * online start time (type:숫자, 길이:13)
     *********************************
     * 온라인 거래 시 거래 시작 시간. 단위는 ms.
     *********************************
     */
    @Schema(description = "start time 숫자", example = "0")
    @Builder.Default
    private Long startTime = 0L;

    @JsonIgnore
    private PagingInfo pagingInfo;
}
