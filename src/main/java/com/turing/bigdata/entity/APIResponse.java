package com.turing.bigdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {

    private static final String SUCCESS = "success";

    private static final String FAIL = "fail";

    private String code;

    private T data;

    private String message;

    public static APIResponse success(){
        return APIResponse.builder()
                .code(SUCCESS)
                .message("request success")
                .build();
    }

    public static APIResponse success(Object data) {
        return APIResponse.builder()
                .code(SUCCESS)
                .message("request success")
                .data(data)
                .build();
    }

    public static APIResponse fail(String message) {
        return APIResponse.builder()
                .code(FAIL)
                .message(message)
                .build();
    }
}
