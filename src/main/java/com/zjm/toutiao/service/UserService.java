package com.zjm.toutiao.service;
import com.zjm.toutiao.dao.LoginTicketDAO;
import com.zjm.toutiao.dao.UserDAO;
import com.zjm.toutiao.model.HostHolder;
import com.zjm.toutiao.model.LoginTicket;
import com.zjm.toutiao.model.User;
import com.zjm.toutiao.util.JedisAdapter;
import com.zjm.toutiao.util.MailSender;
import com.zjm.toutiao.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zjm.toutiao.util.ToutiaoUtil;

import javax.xml.crypto.Data;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private JedisAdapter jedisAdapter;


    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public Map<String,Object> register(String username, String password){
        Map<String,Object> map=new HashMap<String, Object>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user!=null){
            map.put("msgname","用户名已经被注册");
            return  map;
        }
        user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(String.format(head));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        user.setActive(0);
        userDAO.addUser(user);

        //向用户发送验证码
        sendComnfireEmail(username);
        map.put("active",0);
//        //用户注册以后直接登陆
//        String ticket=addLoginTicket(user.getId());
//        map.put("ticket",ticket);
        return map;
    }

    public Map<String,Object> ConfirmCode(String confirmCode,String key){
        Map<String,Object> map=new HashMap<String, Object>();
        String localCode=jedisAdapter.get(key);
        if(localCode.equals(confirmCode)){
            String email=key.split(":")[1];
            User user=userDAO.selectByName(email);
            user.setActive(1);
            userDAO.updateActive(user);

            String ticket=addLoginTicket(user.getId());
            map.put("ticket",ticket);
        }
        return map;
    }

    public Map<String,Object> login(String username, String password){
        Map<String,Object> map=new HashMap<String, Object>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }

        User user=userDAO.selectByName(username);

        if(user==null){
            map.put("msgname","用户名不存在");
            return  map;
        }

        if(!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码不正确");
            return  map;
        }

        if(user.getActive()==0){
            sendComnfireEmail(username);
            map.put("msgname","请登陆邮箱激活账号");
            return map;
        }
        map.put("userId",user.getId());

        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    private String addLoginTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replace("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public void sendComnfireEmail(String username){//向用户发送注册邮件
        Map<String,Object> emialMap=new HashMap<>();
        emialMap.put("username",username);
        String activeCode=UUID.randomUUID().toString();
        String key=RedisKeyUtil.getConfirmCodeKey(username);
        jedisAdapter.set(key,activeCode);
        jedisAdapter.expire(key,3600);
        String url=ToutiaoUtil.COMFIRM_EMAIL_PREFIX+activeCode+"?key="+key;
        emialMap.put("url",url);
        mailSender.sendWithHTMLTemplate(username,"验证电子邮箱",
                "mails/confirmEmail.ftl",emialMap);
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
}
