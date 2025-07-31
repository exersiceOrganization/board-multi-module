package com.example.model.core;

import com.example.internal.annotations.FixedData;
import lombok.*;

/** value를 고정 길이 문자열로 만들기 위한 object */
@Getter @Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FixedFieldData {

    private FixedData.PAD_TYPE padType;
    private int length;
    private char padChar;
    private String dataType;
    private Object value; // not collection
}
