package com.zjm.toutiao.controller;

import com.zjm.toutiao.async.EventModel;
import com.zjm.toutiao.async.EventProducer;
import com.zjm.toutiao.async.EventType;
import com.zjm.toutiao.model.HostHolder;
import com.zjm.toutiao.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.zjm.toutiao.util.ToutiaoUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                   @RequestParam("password") String password,
                   @RequestParam(value="rember",defaultValue = "0") int rememberme,
                      HttpServletResponse response) {
        try {
            Map<String,Object> map=userService.register(username,password);
           if(map.containsKey("active")){
//                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
//                cookie.setPath("/");//设置cookie路径全部有效
//                if(rememberme>0){
//                    cookie.setMaxAge(3600*24*5);
//                }
//                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0,"注册成功");
            }else{
                return ToutiaoUtil.getJSONString(1,map);
            }
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常");
        }
    }

    @RequestMapping(path = {"/login/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value="rember",defaultValue = "0") int rememberme,
                        HttpServletResponse response) {
        try {
            Map<String,Object> map=userService.login(username,password);
                if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");//设置cookie路径全部有效
                if(rememberme>0) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);

                 eventProducer.fireEvent(new EventModel(EventType.LOGIN).setActorId((int)map.get("userId"))
                            .setEntityOwerId((int)map.get("userId"))
                            .setExt("username",username).setExt("email","13027477060@163.com"));

                 return ToutiaoUtil.getJSONString(0,"登陆成功");
            }else{
                return ToutiaoUtil.getJSONString(1,map);
            }
        }catch (Exception e){
            logger.error("登陆异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登陆异常");
        }
    }

    @RequestMapping(path = {"/logout/"},method = {RequestMethod.GET,RequestMethod.POST})
    public String login(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

    @RequestMapping(path = "/confirm_email/{confirmCode}",method = RequestMethod.GET)
    public String ConfirmUser(@PathVariable("confirmCode") String confirmCode,
                              @Param("key") String key,
                              HttpServletResponse response){
            Map<String,Object> map=userService.ConfirmCode(confirmCode, key);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");//设置cookie路径全部有效
                cookie.setMaxAge(3600*24*5);
                response.addCookie(cookie);
            }
            return "redirect:/";
    }
}
