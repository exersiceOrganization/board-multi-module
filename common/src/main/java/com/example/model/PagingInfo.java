package com.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.session.RowBounds;

import java.util.Arrays;
import java.util.List;

@Getter @Setter
@Builder
@ToString
public class PagingInfo extends RowBounds {
    @Builder.Default
    protected int offset = 0;
    @Builder.Default
    protected int limit = 10000;
    @Builder.Default
    private boolean isContTrKey = false;
    @Builder.Default
    private boolean hasNextPage = false;

    private List<String> contTrKeyFields;
    private List<Object> contTrKeyValues;

    /**
     * 연속거래키 설정
     *
     * @param contTrKeyFields
     * @return
     */
    public PagingInfo setContTrKeyFields(String... contTrKeyFields){
        this.contTrKeyFields = Arrays.asList(contTrKeyFields);
        this.isContTrKey = true;
        return this;
    }

    /**
     * 페이지와 페이지 사이즈로 {@link PagingInfo} 생성
     *
     * @param page 페이지 수. 첫 페이지는 1로 시작.
     * @parma pagesize 한 페이지의 사이즈
     * */
    public static PagingInfo getPagingInfo(final int page, final int pageSize){
        int limit = pageSize;
        int offset = (page - 1) * limit;
        return PagingInfo.builder()
                .limit(limit)
                .offset(offset)
                .build();
    }
}
