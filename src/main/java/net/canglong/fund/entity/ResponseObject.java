package net.canglong.fund.entity;

import lombok.Data;

@Data
public class ResponseObject {

  private boolean success;
  private String message;
  private Object data;

}
