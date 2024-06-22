package com.baconbao.mxh.Exceptions;

import org.springframework.http.HttpStatusCode;

//Custom exception sử dụng một mã lỗi (ErrorCode) để cung cấp thông tin chi tiết về lỗi.
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public int getCode() {
        return errorCode.getCode();
    }

    public HttpStatusCode getStatusCode() {
        return errorCode.getStatusCode();
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
