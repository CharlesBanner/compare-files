package com.charles.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ResultExcel {
    @ExcelProperty(value = "包名", index = 0)
    private String fileName;
    @ExcelProperty(value = "内容", index = 1)
    private String content;
    @ExcelProperty(value = "本地", index = 2)
    private String lcoal;
    @ExcelProperty(value = "现网", index = 3)
    private String prod;
}
