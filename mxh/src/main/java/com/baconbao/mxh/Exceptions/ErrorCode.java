package com.baconbao.mxh.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

//Cung cap thong tin ve loi
@Getter
public enum ErrorCode {
    // Generic errors: Unclassified errors, typically unexpected errors not falling
    // into other error categories. Return HTTP status code 500 (Internal Server
    // Error).
    UNCATEGORIZED_EXCEPTION(9999, "Unclassified error", HttpStatus.INTERNAL_SERVER_ERROR),

    // User-related errors
    USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1002, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_SAVE(1003, "Unable to save user", HttpStatus.INTERNAL_SERVER_ERROR),

    // Relationship-related errors
    RELATIONSHIP_NOT_FOUND(2001, "Relationship not found", HttpStatus.NOT_FOUND),
    RELATIONSHIP_NOT_SAVED(2002, "Unable to save relationship", HttpStatus.INTERNAL_SERVER_ERROR),

    // Status-related errors
    STATUS_NOT_FOUND(3001, "Status not found", HttpStatus.NOT_FOUND),
    STATUS_NOT_SAVED(3002, "Unable to save status", HttpStatus.INTERNAL_SERVER_ERROR),

    // Post-related errors
    POST_NOT_FOUND(4001, "Post not found", HttpStatus.NOT_FOUND),
    POST_NOT_SAVED(4002, "Unable to save post", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_NOT_UPDATE(4003, "Unable to update post", HttpStatus.INTERNAL_SERVER_ERROR),

    // Image-related errors
    IMAGE_NOT_FOUND(5001, "Image not found", HttpStatus.NOT_FOUND),
    IMAGE_ALREADY_EXISTS(5002, "Image already exists", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_SAVED(5003, "Unable to save image", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_NOT_UPDATE(5004, "Unable to update image", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_URL_NOT_FOUND(5005, "Unable to find image URL", HttpStatus.INTERNAL_SERVER_ERROR),

    // Message-related errors
    MESSAGE_NOT_FOUND(6001, "Message not found", HttpStatus.NOT_FOUND),
    MESSAGE_NOT_SAVED(6002, "Unable to save message", HttpStatus.INTERNAL_SERVER_ERROR),
    MESSAGE_NOT_UPDATE(6003, "Unable to update message", HttpStatus.INTERNAL_SERVER_ERROR),

    //About-related errors
    ABOUT_NOT_FOUND(7001, "About not found", HttpStatus.NOT_FOUND),
    
    //User about - related errors
    USER_ABOUT_NOT_FOUND(8001, "User about not found", HttpStatus.NOT_FOUND),
    USER_ABOUT_NOT_SAVED(8002, "Unable to save user about", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ABOUT_NOT_UPDATE(8003, "Unable to update user about", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ABOUT_ALREADY_EXISTS(8005, "User about already exists", HttpStatus.BAD_REQUEST),

    //Interact - error
    INTERACT_NOT_FOUND(9001, "Interact not found", HttpStatus.NOT_FOUND),
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
