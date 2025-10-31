package com.banking.payloads;

public class ApiResponse {
    private String message;
    private String success;

    ApiResponse(){}

    ApiResponse(String message, String success){
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
