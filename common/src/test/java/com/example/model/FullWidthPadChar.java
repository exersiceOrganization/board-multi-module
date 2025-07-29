package com.example.model;

import com.example.internal.FixedData;
import com.example.internal.FixedData.PAD_TYPE;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullWidthPadChar {

    @Parameter(description = "필드1-전각 스페이스", required = true)
    @FixedData(length = 10, padType = PAD_TYPE.RIGHT, padChar = '　')
    private String f1;

    @Parameter(description = "필드2-반각 스페이스", required = false)
    @FixedData(length = 10, padType = PAD_TYPE.RIGHT, padChar = ' ')
    private String f2;

    @Parameter(description = "필드3-전각 0", required = false)
    @FixedData(length = 10, padType = PAD_TYPE.LEFT, padChar = '０')
    private String f3;

    @Parameter(description = "필드4-반각 0", required = false)
    @FixedData(length = 10, padType = PAD_TYPE.LEFT, padChar = '0')
    private int f4;
}
