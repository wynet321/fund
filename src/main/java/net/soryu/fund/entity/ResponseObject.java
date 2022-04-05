package net.soryu.fund.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseObject {

    private boolean success;
    private String message;
    private Object data;

}
