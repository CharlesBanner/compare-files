package com.charles.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CompareExcelLine {


    private StringBuilder content = new StringBuilder();
    private StringBuilder proText = new StringBuilder();
    private StringBuilder localText = new StringBuilder();
}
