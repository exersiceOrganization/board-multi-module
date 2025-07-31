package com.example.model;

import java.math.BigDecimal;

import com.example.internal.annotations.FixedData;
import com.example.internal.annotations.FixedData.PAD_TYPE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FixedDataTestInfo {
    @FixedData(10)
    private int a;
    @FixedData(10)
    private long b;
    @FixedData(10)
    private float c;
    @FixedData(10)
    private double d;
    
    @FixedData(10)
    private Integer a1;
    @FixedData(10)
    private Long b1;
    @FixedData(10)
    private Float c1;
    @FixedData(length = 10)
    private Double d1;
    
    @FixedData(length = 10)
    private BigDecimal e;
    
    @FixedData(15)
    private String g;
    @FixedData(length = 5, padType = PAD_TYPE.RIGHT)
    private String h;
}
