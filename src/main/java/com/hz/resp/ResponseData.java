package com.hz.resp;


import lombok.Data;

/**
 * @author xtf
 * @date 2020/2/27 18:15
 * @description 接口返回数据类型
 */
@Data
public class ResponseData<T> {
    private int code;
    private String message;
    private T data;


}
