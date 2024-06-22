package com.baconbao.mxh.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Exception này được ném ra khi có lỗi xảy ra do yêu cầu không hợp lệ từ phía người dùng (client).
//Gắn kèm với HttpStatus.BAD_REQUEST để tự động trả về mã trạng thái 400 khi exception này được ném ra.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}