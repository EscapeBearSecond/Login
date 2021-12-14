package com.rongke.util;

import lombok.Data;

@Data
public class AlertException extends RuntimeException{
    private ResultCode resultCode;
    public AlertException(ResultCode resultCode){
        this.resultCode = resultCode;
    }
    public Result<String> fail(){
        return Result.failure(this.resultCode,getMessage());
    }
}
