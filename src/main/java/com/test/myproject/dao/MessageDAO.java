package com.test.myproject.dao;

import com.test.myproject.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {
    String TABLE_NAME=" message ";
    String INSERT_FIELDS=" from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELDS="id, "+INSERT_FIELDS;

    @Insert({" insert into ",TABLE_NAME,"(",INSERT_FIELDS,
    ")values (#{fromId}, #{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select * from(select conversation_id,max(created_date) as created_date from (select *  from message where to_id=#{userId})t group by conversation_id order by max(created_date))tt inner join message on tt.conversation_id = message.conversation_id and tt.created_date= message.created_date order by tt.created_date desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId")int userId,@Param("offset")int offset,
                                      @Param("limit")int limit);

    @Select({" select count(id) from ",TABLE_NAME," where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnReadCount(@Param("userId")int userId,@Param("conversationId")String conversationId);

    @Select({" select count(id) from ",TABLE_NAME," where conversation_id=#{conversationId}"})
    int getConversationTotalCount(@Param("conversationId")String conversationId);


    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where conversation_id=#{conversationId} order by id desc limit #{offset},#{limit}" })
    List<Message> getConversationDetail(@Param("conversationId")String conversationId,
                                        @Param("offset")int offset, @Param("limit")int limit);


    @Update({" update ",TABLE_NAME," set has_Read=1 where id=#{id}"})
    void hasRead(@Param("id")int id);
}
