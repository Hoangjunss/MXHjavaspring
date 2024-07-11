package com.baconbao.mxh.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

/**
 * Enum representing application-specific error codes, messages, and HTTP status codes.
 */
@Getter
public enum ErrorCode {
    // Generic errors: Unclassified errors, typically unexpected errors not falling
    // into other error categories. Return HTTP status code 500 (Internal Server Error).
    UNCATEGORIZED_EXCEPTION(9999, "Unclassified error", HttpStatus.INTERNAL_SERVER_ERROR),
    // Database access error
    DATABASE_ACCESS_ERROR(9998, "Database access error", HttpStatus.INTERNAL_SERVER_ERROR),
    QUERY_TIMEOUT(9997, "Query timed out", HttpStatus.REQUEST_TIMEOUT), //QueryTimeoutException: Được ném ra khi một truy vấn cơ sở dữ liệu vượt quá thời gian giới hạn đã định. 
    DUPLICATE_KEY(9996, "Duplicate key found", HttpStatus.CONFLICT), //DuplicateKeyException: Được ném ra khi cố gắng chèn một bản ghi với một khóa chính hoặc khóa duy nhất đã tồn tại.
    EMPTY_RESULT(9995, "No result found", HttpStatus.NOT_FOUND), //NoResultException: Được ném ra khi một truy vấn JPA không trả về kết quả nào nhưng lại được mong đợi trả về một kết quả duy nhất.
    NON_UNIQUE_RESULT(9994, "Non-unique result found", HttpStatus.CONFLICT), //NonUniqueResultException: Được ném ra khi một truy vấn JPA trả về nhiều kết quả nhưng lại được mong đợi trả về một kết quả duy nhất.
    OPTIMISTIC_LOCK_ERROR(9993, "Optimistic lock error", HttpStatus.CONFLICT), //OptimisticLockException: Được ném ra khi một thao tác cập nhật không thành công do xung đột khóa lạc quan.
    PESSIMISTIC_LOCK_ERROR(9992, "Pessimistic lock error", HttpStatus.CONFLICT), //PessimisticLockException:  Khi cố gắng khóa một bản ghi để cập nhật và bản ghi đó đã bị khóa bởi một giao dịch khác.
    TRANSACTION_ERROR(9991, "Transaction error", HttpStatus.INTERNAL_SERVER_ERROR), //TransactionException

    // User-related errors
    USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1002, "User already exists", HttpStatus.BAD_REQUEST),
    USER_UNABLE_TO_SAVE(1003, "Unable to save user", HttpStatus.INTERNAL_SERVER_ERROR),

    // Relationship-related errors
    RELATIONSHIP_NOT_FOUND(2001, "Relationship not found", HttpStatus.NOT_FOUND),
    RELATIONSHIP_UNABLE_TO_SAVE(2002, "Unable to save relationship", HttpStatus.INTERNAL_SERVER_ERROR),

    // Status-related errors
    STATUS_NOT_FOUND(3001, "Status not found", HttpStatus.NOT_FOUND),
    STATUS_UNABLE_TO_SAVE(3002, "Unable to save status", HttpStatus.INTERNAL_SERVER_ERROR),

    //StatusRelationship
    STATUS_RELATIONSHIP_NOT_FOUND(3999, "Status relationship not found", HttpStatus.NOT_FOUND),
    STATUS_RELATIONSHIP_UNABLE_TO_SAVE(3998, "Unable to save status relationship", HttpStatus.INTERNAL_SERVER_ERROR),

    // Post-related errors
    POST_NOT_FOUND(4001, "Post not found", HttpStatus.NOT_FOUND),
    POST_UNABLE_TO_SAVE(4002, "Unable to save post", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_UNABLE_TO_UPDATE(4003, "Unable to update post", HttpStatus.INTERNAL_SERVER_ERROR),

    // Image-related errors
    IMAGE_NOT_FOUND(5001, "Image not found", HttpStatus.NOT_FOUND),
    IMAGE_ALREADY_EXISTS(5002, "Image already exists", HttpStatus.BAD_REQUEST),
    IMAGE_UNABLE_TO_SAVE(5003, "Unable to save image", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_UNABLE_TO_UPDATE(5004, "Unable to update image", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_URL_NOT_FOUND(5005, "Unable to find image URL", HttpStatus.INTERNAL_SERVER_ERROR),

    // Message-related errors
    MESSAGE_NOT_FOUND(6001, "Message not found", HttpStatus.NOT_FOUND),
    MESSAGE_UNABLE_TO_SAVE(6002, "Unable to save message", HttpStatus.INTERNAL_SERVER_ERROR),
    MESSAGE_UNABLE_TO_UPDATE(6003, "Unable to update message", HttpStatus.INTERNAL_SERVER_ERROR),

    // About-related errors
    ABOUT_NOT_FOUND(7001, "About not found", HttpStatus.NOT_FOUND),

    // User about-related errors
    USER_ABOUT_NOT_FOUND(8001, "User about not found", HttpStatus.NOT_FOUND),
    USER_ABOUT_UNABLE_TO_SAVE(8002, "Unable to save user about", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ABOUT_UNABLE_TO_UPDATE(8003, "Unable to update user about", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ABOUT_ALREADY_EXISTS(8005, "User about already exists", HttpStatus.BAD_REQUEST),

    // Interact-related errors
    INTERACT_NOT_FOUND(9001, "Interact not found", HttpStatus.NOT_FOUND),
    INTERACT_UNABLE_TO_SAVE(9002, "Unable to save interact", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERACT_UNABLE_TO_UPDATE(9003, "Unable to update interact", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERACT_ALREADY_EXISTS(9005, "Interact already exists", HttpStatus.BAD_REQUEST),

    // Notifications related errors
    NOTIFICATION_NOT_FOUND(10001, "Notification not found", HttpStatus.NOT_FOUND),
    NOTIFICATION_UNABLE_TO_SAVE(10002, "Unable to save notification", HttpStatus.INTERNAL_SERVER_ERROR),
    NOTIFICATION_UNABLE_TO_UPDATE(10003, "Unable to update notification", HttpStatus.INTERNAL_SERVER_ERROR),
    NOTIFICATION_ALREADY_EXISTS(10005, "Notification already exists", HttpStatus.BAD_REQUEST),
    NOTIFICATION_TOO_MANY_REQUESTS(10006, "Too many requests for notifications", HttpStatus.TOO_MANY_REQUESTS),

    //Comment related exceptions
    COMMENT_NOT_FOUND(11001, "Comment not found", HttpStatus.NOT_FOUND),
    COMMENT_UNABLE_TO_SAVE(11002, "Unable to save comment", HttpStatus.INTERNAL_SERVER_ERROR),
    COMMENT_UNABLE_TO_UPDATE(11003, "Unable to update comment", HttpStatus.INTERNAL_SERVER_ERROR),
    COMMENT_ALREADY_EXISTS(11005, "Comment already exists", HttpStatus.BAD_REQUEST),
    COMMENT_TOO_MANY_REQUESTS(11006, "Too many requests for comments", HttpStatus.TOO_MANY_REQUESTS), 

    //Verification related exceptions
    VERIFICATION_FAILED(13001, "Verification failed", HttpStatus.FORBIDDEN),
    VERIFICATION_UNABLE_TO_SAVE(13002, "Unable to save Verification", HttpStatus.INTERNAL_SERVER_ERROR),
    VERIFICATION_UNABLE_TO_UPDATE(13003, "Unable to update Verification", HttpStatus.INTERNAL_SERVER_ERROR),
    VERIFICATION_TOKEN_ALREADY_EXISTS(13004, "Verification token already exists", HttpStatus.BAD_REQUEST),
    VERIFICATION_TOKEN_NOT_FOUND(13005, "Verification token not found",HttpStatus.NOT_FOUND),
    VERIFICATION_TOKEN_EXPIRED(13006, "Verification token expired", HttpStatus.UNAUTHORIZED),
    // Download related exceptions
    DOWNLOAD_FAILED(12001, "Download failed", HttpStatus.FAILED_DEPENDENCY),

    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    /**
     * Constructor for ErrorCode.
     *
     * @param code       the error code
     * @param message    the error message
     * @param statusCode the corresponding HTTP status code
     */
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
