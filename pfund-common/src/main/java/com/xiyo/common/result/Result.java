package com.xiyo.common.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

    private Integer code;

    private String message;

    private Map<String,Object> data = new HashMap<>();

    private Result(){}

    public static Result ok(){
        Result result = new Result();
        result.setCode(ResponseEnum.SUCCESS.getCode());
        result.setMessage(ResponseEnum.SUCCESS.getMessage());
        return result;
    }

    public static Result error(){
        Result result = new Result();
        result.setCode(ResponseEnum.ERROR.getCode());
        result.setMessage(ResponseEnum.ERROR.getMessage());
        return result;
    }

    public static Result setResult(ResponseEnum responseEnum){
        Result result = new Result();
        result.setCode(responseEnum.getCode());
        result.setMessage(responseEnum.getMessage());
        return result;
    }

    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    public Result data(String key,Object value){
        this.data.put(key, value);
        return this;
    }

    public Result data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

    public Result code(Integer code){
        this.setCode(code);
        return this;
    }
}
