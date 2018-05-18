package com.zjm.toutiao.async.handler;

import com.zjm.toutiao.async.EventHandler;
import com.zjm.toutiao.async.EventModel;
import com.zjm.toutiao.async.EventType;
import com.zjm.toutiao.model.Message;
import com.zjm.toutiao.model.User;
import com.zjm.toutiao.service.MessageService;
import com.zjm.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class LikeHandler implements EventHandler{
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message=new Message();
        message.setFromId(12);
        message.setToId(model.getEntityOwerId());
        User user=userService.getUser(model.getEntityOwerId());
        message.setContent("用户" + user.getName()
                + "赞了你的资讯,http://127.0.0.1:8080/news/" + model.getEntityId());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}
