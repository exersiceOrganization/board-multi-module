package com.example.model.core;

import com.example.internal.annotations.FixedData;
import com.example.internal.annotations.FixedList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class FixedField {

    private String name;
    private Class<?> type;
    private FixedData fixedData;
    private FixedList fixedList;
    private List<FixedField> fixedFieldList;
    @Builder.Default
    private boolean isFixedVo = false;
    @Builder.Default
    private boolean isSuperClassField = false;
}
