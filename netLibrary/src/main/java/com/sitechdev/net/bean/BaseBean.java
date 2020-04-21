package com.sitechdev.net.bean;

import java.io.Serializable;

public class BaseBean<T> implements Serializable {
    public String code;
    public String message;
    public T data;


}
