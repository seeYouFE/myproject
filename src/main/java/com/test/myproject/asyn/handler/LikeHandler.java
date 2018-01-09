package com.test.myproject.asyn.handler;

import com.test.myproject.asyn.EventHandler;
import com.test.myproject.asyn.EventModel;
import com.test.myproject.asyn.EventType;
import com.test.myproject.model.Message;
import com.test.myproject.model.User;
import com.test.myproject.service.MessageService;
import com.test.myproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        User user = userService.getUser(eventModel.getActorId());
        message.setToId(eventModel.getGetEntityOwnerId());
        message.setContent("用户"+user.getName()+"赞了你的资讯"+String.valueOf(eventModel.getEntityId()));
        message.setFromId(99);
        message.setCreatedDate(new Date());
        message.setConversationId(String.format("%d_%d",eventModel.getGetEntityOwnerId(),99));
        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportTypes() {
        List<EventType> list = new ArrayList<>();
        list.add(EventType.LIKE);
        return list;
    }
}
