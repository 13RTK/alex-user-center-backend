package com.alex.usercenter.common;


/**
 * This util class used to create the packaged response instance.
 *
 * @author alex
 */
public class ResultUtils {
    /**
     * Request success
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(20000, data, "ok");
    }

    /**
     * Request failed
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse(errorCode.getCode(), null, message, description);
    }

    public static BaseResponse error(int errorCode, String message, String description) {
        return new BaseResponse(errorCode, null, message, description);
    }
}
