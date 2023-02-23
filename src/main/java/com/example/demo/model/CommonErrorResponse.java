package com.example.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonErrorResponse extends BasicResponse {
    private String resultData = "ERROR";
    private int resultCode = 0;

    public CommonErrorResponse(String resultData) {
        this.resultData = resultData;
    }

    public CommonErrorResponse(int resultCode, String resultData) {
        this.resultData = resultData;
        this.resultCode = resultCode;
    }
}
