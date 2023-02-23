package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonSuccessResponse<T> extends BasicResponse {
    private T resultData;
    private int resultCode;

    public CommonSuccessResponse(T data) {
        this.resultData = data;
        this.resultCode = 1;
    }

    public CommonSuccessResponse(int resultCode, T data) {
        this.resultData = data;
        this.resultCode = resultCode;
    }
}
