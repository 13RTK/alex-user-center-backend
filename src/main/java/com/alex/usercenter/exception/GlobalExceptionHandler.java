package com.alex.usercenter.exception;

import com.alex.usercenter.common.BaseResponse;
import com.alex.usercenter.common.ErrorCode;
import com.alex.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException businessException) {
        log.error("runtimeException" + businessException.getMessage(), businessException);
        return ResultUtils.error(businessException.getCode(), businessException.getMessage(), businessException.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse businessExceptionHandler(RuntimeException runtimeException) {
        log.error("runtimeException", runtimeException);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, runtimeException.getMessage(), "");
    }
}
