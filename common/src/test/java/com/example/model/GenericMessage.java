package com.example.model;

import java.util.List;

import com.example.internal.FixedData;
import com.example.internal.FixedData.PAD_TYPE;
import com.example.internal.FixedList;
import com.example.internal.FixedVo;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericMessage<T> {
    @FixedData(length = 10, padType = PAD_TYPE.LEFT, padChar = '0')
    private String field1;

    @Parameter(description = "필드1", required = true)
    @FixedData(length = 10, padType = PAD_TYPE.RIGHT, padChar = '0')
    private int field2;

    @Parameter(description = "VO 타입(Message3)", required = true)
    @FixedVo
    private T field3;

    @Parameter(description = "리스트 타입(Message3)", required = true)
    @FixedList
    private List<T> field4;
}
