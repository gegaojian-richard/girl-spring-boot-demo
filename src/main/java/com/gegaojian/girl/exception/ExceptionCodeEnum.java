package com.gegaojian.girl.exception;

/**
 * 异常代码统一维护
 */
public enum ExceptionCodeEnum {
    UNKONW_ERROR(-1, "未知错误而"),
    SUCCESS(0, "成功"),
    PRIMARY_SCHOOL(100, "小学"),
    MIDDLE_SCHOOL(101, "中学");


    private Integer code;
    private String msg;

    ExceptionCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
