package com.baconbao.mxh.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Exception này được ném ra khi không tìm thấy người dùng theo yêu cầu.
// Gắn kèm với HttpStatus.NOT_FOUND để tự động trả về mã trạng thái 404 khi exception này được ném ra.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String exception) {
        super(exception);
    }
}