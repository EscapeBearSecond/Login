package com.rongke.config;

import com.rongke.util.AlertException;
import com.rongke.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerConfig {

    @ExceptionHandler(AlertException.class)
    public Result<String> alertExceptionHandler(AlertException alertException){
        return alertException.fail();
    }

   /* @ExceptionHandler(RuntimeException.class)
    public Result<String> runtimeExceptionHandler(RuntimeException e){
        return Result.failure(ResultCode.SYSTEM_ERROR, e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public Result<String> throwAbleHandler(Throwable throwable){
        return Result.failure(ResultCode.SYSTEM_ERROR, throwable.getMessage());
    }*/

}
