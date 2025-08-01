package com.example.common.model;

import com.example.common.internal.annotations.FixedData;
import com.example.common.internal.annotations.FixedData.PAD_TYPE;
import com.example.common.internal.annotations.FixedList;
import com.example.common.internal.annotations.FixedVo;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FixedMultiListMessageBody {
    @Parameter(description = "필드1", required = true)
    @FixedData(length = 10, padType = PAD_TYPE.LEFT, padChar = '0')
    private String field1;

    @Parameter(description = "필드2", required = true)
    @FixedData(length = 10, padType = PAD_TYPE.RIGHT, padChar = '0')
    private int field2;

    @Parameter(description = "리스트 타입(listField1)", required = true)
    @FixedList(size = 2)
    private List<InnerMessage> listField1;

    @Parameter(description = "VO 타입(Message3)", required = true)
    @FixedVo
    private InnerMessage field3;

    @Parameter(description = "list size reference", required = true)
    @FixedData(length = 2, padType = PAD_TYPE.RIGHT, padChar = '0')
    private Integer refListSize;

    @Parameter(description = "리스트 타입(listField2)", required = true)
    @FixedList(sizeRef = "refListSize")
    private List<InnerMessage> listField2;

    @Parameter(description = "리스트 타입(listField3)", required = true)
    @FixedList
    private List<InnerMessageSpacePadChar> listField3;
}
