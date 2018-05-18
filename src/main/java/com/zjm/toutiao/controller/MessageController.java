package com.zjm.toutiao.controller;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.zjm.toutiao.dao.MessageDAO;
import com.zjm.toutiao.model.HostHolder;
import com.zjm.toutiao.model.Message;
import com.zjm.toutiao.model.User;
import com.zjm.toutiao.model.ViewObject;
import com.zjm.toutiao.service.MessageService;
import com.zjm.toutiao.service.UserService;
import com.zjm.toutiao.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger= LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/addMessage"},method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@Param("fromId") int fromId,
                             @Param("toId") int toId,
                             @Param("content") String content){
        try {
            Message message = new Message();
            message.setFromId(fromId);
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setHasRead(0);
            message.setToId(toId);
            message.setConversationId(fromId<toId?String.format("%d_%d",fromId,toId):String.format("%d_%d",toId,fromId));
            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(message.getId());
        }catch (Exception e){
            logger.error("增加消息失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"添加评论失败");
        }
    }

    @RequestMapping(path = {"/msg/detail"},method = RequestMethod.GET)
    public String conversationDetail(Model model, @Param("conversationId") String conversationId){
        try{
            List<Message> conversationList=messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages= new ArrayList<>();
            for(Message msg:conversationList){
                ViewObject vo=new ViewObject();
                vo.set("message",msg);
                User user=userService.getUser(msg.getFromId());
                if(user==null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e) {
            logger.error("获取消息详情失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/list"},method = RequestMethod.GET)
    public String conversationDetail(Model model){
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<ViewObject>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user", user);
                vo.set("unread", messageService.getConversationUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);

        }catch (Exception e){
            logger.error("获取会话列表出错"+e.getMessage());
        }
        return  "letter";
    }
}
