package com.charles.Enums;

/**
 * @author dell
 */
public enum OperationEnum {
    /**
     * 现网代码操作枚举
     */
    INSERT("leftorphan","现网新增:"),
    DELETE("rightorphan","现网删除:"),
    MODIFY("different","现网修改:");

    /**
     * key
     */
    private final String key;
    /**
     * 操作
     */
    private final String operation;
    public String getKey() {
        return key;
    }

    public String getOperation() {
        return operation;
    }

    OperationEnum(String key, String operation) {
        this.key = key;
        this.operation = operation;
    }
}
