package com.zjm.toutiao.interceptor;

import com.zjm.toutiao.dao.LoginTicketDAO;
import com.zjm.toutiao.dao.UserDAO;
import com.zjm.toutiao.model.HostHolder;
import com.zjm.toutiao.model.LoginTicket;
import com.zjm.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.UserDataHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor{
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket=null;
        if(request.getCookies()!=null){
            for(Cookie cookie:request.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket=cookie.getValue();
                    break;
                }
            }
        }
        if(ticket!=null){
            LoginTicket loginTicket= loginTicketDAO.selectByTicket(ticket);
            if(loginTicket==null||loginTicket.getExpired().before(new Date())||loginTicket.getStatus()!=0){//不能自动登陆
                return true;
            }
            User user=userDAO.selectById(loginTicket.getUserId());//该用户能自动登陆，将用户记录下来传给controller
            //request.setAttribute();一种办法
            hostHolder.setUser(user);//使用依赖注入，这样整个会话都能使用user
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            if(modelAndView!=null&&hostHolder.getUser()!=null){//如果用户已经登陆，渲染页面
                modelAndView.addObject("user",hostHolder.getUser());
            }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();//请求完成，清除本地线程
    }
}
