package com.example.model;

import com.example.internal.annotations.FixedData;
import com.example.internal.annotations.FixedData.PAD_TYPE;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InnerMessageSpacePadChar {

    @Parameter(description = "필드1", required = true)
    @FixedData(length = 20, padType = PAD_TYPE.LEFT, padChar = ' ')
    private String f1;

    @Parameter(description = "필드2", required = false)
    @FixedData(length = 20, padType = PAD_TYPE.LEFT, padChar = ' ')
    private String f2;

    @Parameter(description = "필드3", required = false)
    @FixedData(length = 20, padType = PAD_TYPE.LEFT, padChar = ' ')
    private String f3;

    @Parameter(description = "필드4", required = false)
    @FixedData(length = 20, padType = PAD_TYPE.LEFT, padChar = ' ')
    private int f4;
}
