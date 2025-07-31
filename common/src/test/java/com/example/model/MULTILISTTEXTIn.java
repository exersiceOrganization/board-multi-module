package com.example.model;

import java.util.List;

import com.example.internal.annotations.FixedData;
import com.example.internal.annotations.FixedData.PAD_TYPE;
import com.example.internal.annotations.FixedList;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class MULTILISTTEXTIn {
    @Schema(description = "")
    @FixedData(length = 5, padType = PAD_TYPE.LEFT, padChar = '0')
    private Integer rootField;

    @Schema(description = "첫번째 리스트", nullable = true)
    @FixedList(size = 1)
    private List<MULTILISTTEXTFirstList> firstList;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MULTILISTTEXTFirstList {
        @Schema(description = "첫번째 필드")
        @FixedData(length = 10, padType = PAD_TYPE.RIGHT, padChar = '#')
        private String firstField;

        @Schema(description = "두번째 리스트", nullable = true)
        @FixedList(size = 1)
        private List<MULTILISTTEXTSecondList> secondList;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MULTILISTTEXTSecondList {
            @Schema(description = "두번째 필드")
            @FixedData(length = 10, padType = PAD_TYPE.RIGHT, padChar = '#')
            private String secondField;

        }

        @Schema(description = "세번째 리스트 길이")
        @FixedData(length = 5, padType = PAD_TYPE.LEFT, padChar = '0')
        private Integer thirdLength;

        @Schema(description = "세번째 리스트", nullable = true)
        @FixedList(sizeRef = "thirdLength")
        private List<MULTILISTTEXTThirdList> thirdList;

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class MULTILISTTEXTThirdList {
            @Schema(description = "세번째 필드")
            @FixedData(length = 5, padType = PAD_TYPE.LEFT, padChar = '0')
            private Integer thirdField;

        }
    }

}
