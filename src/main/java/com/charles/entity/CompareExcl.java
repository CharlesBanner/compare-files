package com.charles.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareExcl {

    private String filename;

    private CompareExcelLine compareExcelLine;

}
