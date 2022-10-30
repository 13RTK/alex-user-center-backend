package com.alex.usercenter.common;

/**
 * Error code
 *
 * @author alex
 */
public enum ErrorCode {
    SUCCESS(20000, "ok", ""),
    PARAM_ERROR(40000, "Request parameter error", ""),
    PARAM_NULL_ERROR(40001, "Request parameter is null", ""),
    OUT_LOGIN(40100, "Not login yet", ""),
    NO_AUTH(40101, "No authority", ""),
    SYSTEM_ERROR(50000, "Error inner system", ""),
    ;


    /**
     * Response code
     */
    private final int code;

    /**
     * Message for the error(category of response)
     */
    private final String message;

    /**
     * Detail of message
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
