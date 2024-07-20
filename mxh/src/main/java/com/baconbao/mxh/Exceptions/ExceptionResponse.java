package com.baconbao.mxh.Exceptions;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ExceptionResponse {
  private Date timestamp;
  private String message;
  private String details;
  private boolean success;

  public ExceptionResponse(Date timestamp, String message, String details, boolean success) {
    super();
    this.timestamp = timestamp;
    this.message = message;
    this.details = details;
    this.success = success;
  }

 
}
