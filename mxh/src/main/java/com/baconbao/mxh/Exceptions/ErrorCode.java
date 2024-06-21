package com.baconbao.mxh.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Generic errors
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    // User-related errors
    USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1002, "User already exists", HttpStatus.BAD_REQUEST),
    // Relationship-related errors
    RELATIONSHIP_NOT_FOUND(2001, "Relationship not found", HttpStatus.NOT_FOUND),
    // Status-related errors
    STATUS_NOT_FOUND(3001, "Status not found", HttpStatus.NOT_FOUND),
    // Post-related errors
    POST_NOT_FOUND(4001, "Post not found", HttpStatus.NOT_FOUND),
    // Image-related errors
    IMAGE_NOT_FOUND(5001, "Image not found", HttpStatus.NOT_FOUND),
    IMAGE_ALREADY_EXISTS(5002, "Image already exists", HttpStatus.BAD_REQUEST);
    ;
    
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
    private int code;
    private String message;
    private HttpStatusCode statusCode;
}
