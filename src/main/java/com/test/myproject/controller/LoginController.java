package com.test.myproject.controller;

import com.test.myproject.asyn.EventModel;
import com.test.myproject.asyn.EventProducer;
import com.test.myproject.asyn.EventType;
import com.test.myproject.model.User;
import com.test.myproject.service.UserService;
import com.test.myproject.util.MyProjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username")String username,
                      @RequestParam("password")String password,
                      @RequestParam(value="rember",defaultValue = "0")int rememberme,
                      HttpServletResponse response){
        try{
            Map<String,Object> map = userService.register(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return MyProjectUtil.getJSONString(0,"注册成功");
            }else{
                return MyProjectUtil.getJSONString(1,"注册失败");
            }

        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return MyProjectUtil.getJSONString(1,"注册异常");
        }
    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model,@RequestParam("username")String username,
                        @RequestParam("password")String password,
                        @RequestParam(value ="rember",defaultValue = "0")int rememberme,
                        HttpServletResponse response){
        try{
            Map<String,Object> map = userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberme>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                User user = userService.getUser(username);
                eventProducer.fireEvent(new
                        EventModel(EventType.LOGIN).setActorId(user.getId())
                        .setExt("username", "牛客").setExt("to", "304513696@qq.com"));
                return MyProjectUtil.getJSONString(0, "成功");

            }else{
                return MyProjectUtil.getJSONString(1,"登录失败");
            }

        }catch(Exception e){
            logger.error("登录异常"+e.getMessage());
            return MyProjectUtil.getJSONString(1,"登录失败");
        }
    }

    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket")String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

}
