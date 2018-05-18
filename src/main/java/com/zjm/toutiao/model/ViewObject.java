package com.zjm.toutiao.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ViewObject {
    private Map<String,Object> objs=new HashMap<>();
    public void set(String key, Object values){
        objs.put(key,values);
    }
    public Object get(String key){
        return objs.get(key);
    }
}

