package com.rongke.util;

/**
 * 返回码定义
 */
public enum ResultCode {
    SUCCESS(200,"成功",true),
    COMMON_FAIL(999, "失败",false),
    PARAM_IS_BLANK(1002, "参数为空",false),
    USER_NOT_LOGIN(2001, "用户未登录",false),
    USER_CREDENTIALS_ERROR(2003, "密码错误",false),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定",false),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在",false),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在",false);
    private Integer code;
    private String message;
    private boolean success;
    ResultCode(Integer code, String message,boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据状态码获取错误信息
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }

    public boolean isSuccess() {
        return this.success;
    }
}
