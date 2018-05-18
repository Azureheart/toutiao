package com.zjm.toutiao.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    private EventType type;//触发事件的类型
    private int actorId;//触发者
    private int entityType;//触发对象
    private int entityId;
    private int entityOwerId;//触发对象的应有者
    private Map<String,String> exts=new HashMap<String,String>();//触发时间的现场（保存参数）

    public String getExt(String key){
        return  exts.get(key);
    }

    public EventModel setExt(String key,String value){
         exts.put(key,value);
         return this;
    }

    public EventModel(EventType type){
        this.type=type;
    }

    public EventModel(){

    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return  this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return  this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return  this;
    }

    public int getEntityOwerId() {
        return entityOwerId;
    }

    public EventModel setEntityOwerId(int entityOwerId) {
        this.entityOwerId = entityOwerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
