package com.zjm.toutiao.async;

import java.util.List;

public interface EventHandler {
    void doHandle(EventModel model);//处理各种事件
    List<EventType> getSupportEventType();//关注某些EventType
}
