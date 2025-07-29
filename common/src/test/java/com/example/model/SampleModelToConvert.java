package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SampleModelToConvert {
    private String name;
    private String phoneNumber;
    private String homeAddress;
    private String faxNumber;
}
