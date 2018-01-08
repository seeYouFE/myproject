package com.test.myproject.controller;

import com.test.myproject.model.HostHolder;
import com.test.myproject.model.Message;
import com.test.myproject.model.User;
import com.test.myproject.model.ViewObject;
import com.test.myproject.service.MessageService;
import com.test.myproject.service.UserService;
import com.test.myproject.util.MyProjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @RequestMapping(path={"/msg/addMessage"},method= RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("fromId")int fromId,
                             @RequestParam("toId")int toId,
                             @RequestParam("content")String content){
        Message message = new Message();
        message.setContent(content);
        message.setConversationId(fromId<toId?String.format("%d_%d",fromId,toId):
                String.format("%d_%d",toId,fromId));
        message.setFromId(fromId);
        message.setToId(toId);
        message.setCreatedDate(new Date());
        message.setHasRead(0);
        messageService.addMessage(message);
        return MyProjectUtil.getJSONString(message.getId());
    }

    @RequestMapping(path={"/msg/list"},method=RequestMethod.GET)
    public String conversationList(Model model){
        try{
            User localUser = hostHolder.getUser();
            List<Message> messages = messageService.getConversationList(localUser.getId(),0,10);
            List<ViewObject> conversations = new ArrayList<>();
            for(Message message:messages){
                ViewObject vo = new ViewObject();
                vo.set("conversation",message);
                int targetId=message.getFromId()==localUser.getId()?message.getToId():message.getFromId();
                User user = userService.getUser(targetId);
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userName",user.getName());
                vo.set("targetId",targetId);
                vo.set("totalCount",messageService.getTotalCount(message.getConversationId()));
                vo.set("unreadCount",messageService.getUnReadCount(localUser.getId(),message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
            return "letter";
        }catch(Exception e){
            logger.error("获取对话列表错误"+e.getMessage());
        }
        return "letter";
    }


    @RequestMapping(path={"/msg/detail"},method=RequestMethod.GET)
    public String conversationDetail(Model model,@RequestParam("conversationId")String conversationId){
        try{
            List<ViewObject> messages=new ArrayList<>();
            List<Message> conversationList = messageService.getConversationDetail(conversationId,0,10);
            for(Message message:conversationList){
                messageService.hasRead(message.getId());
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                User user = userService.getUser(message.getFromId());
                if(user==null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userName",user.getName());
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
            return "letterDetail";
        }catch(Exception e){
            logger.error("获取站内信失败"+e.getMessage());
        }
        return "letterDetail";
    }

}
