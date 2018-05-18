package com.zjm.toutiao.async.handler;

import com.zjm.toutiao.async.EventHandler;
import com.zjm.toutiao.async.EventModel;
import com.zjm.toutiao.async.EventProducer;
import com.zjm.toutiao.async.EventType;
import com.zjm.toutiao.model.HostHolder;
import com.zjm.toutiao.model.Message;
import com.zjm.toutiao.model.User;
import com.zjm.toutiao.service.MessageService;
import com.zjm.toutiao.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoginExceptionHandler implements EventHandler{
    @Autowired
    EventProducer eventProducer;

    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Message message=new Message();
        message.setToId(model.getEntityOwerId());
        message.setFromId(3);
        message.setCreatedDate(new Date());
        message.setContent("您好,"+model.getExt("username")+",您的账号登陆异常！");
        messageService.addMessage(message);

        Map<String,Object> map=new HashMap<>();
        map.put("username",model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"),"登陆异常",
                "mails/loginException.ftl",map);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}
