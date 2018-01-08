package com.test.myproject.service;

import com.test.myproject.dao.MessageDAO;
import com.test.myproject.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }

    public int getUnReadCount(int userId,String conversationId){
        return messageDAO.getConversationUnReadCount(userId, conversationId);
    }

    public int getTotalCount(String conversationId){
        return messageDAO.getConversationTotalCount(conversationId);
    }

    public void hasRead(int messageId){
        messageDAO.hasRead(messageId);
    }
}
