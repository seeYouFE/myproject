package com.test.myproject.asyn.handler;

import com.test.myproject.asyn.EventHandler;
import com.test.myproject.asyn.EventModel;
import com.test.myproject.asyn.EventType;
import com.test.myproject.model.Message;
import com.test.myproject.service.MessageService;
import com.test.myproject.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setContent("你上次的登录IP异常");
        message.setFromId(99);
        message.setToId(eventModel.getActorId());
        message.setCreatedDate(new Date());
        message.setConversationId(String.format("%d_%d",99,eventModel.getActorId()));
        messageService.addMessage(message);

        Map<String,Object> map = new HashMap<>();
        map.put("username",eventModel.getExt("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExt("to"), "登陆异常",
                "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
