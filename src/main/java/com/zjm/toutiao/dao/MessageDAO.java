package com.zjm.toutiao.dao;

import com.zjm.toutiao.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDAO {
     String TABLE_NAME="message";
     String INSERT_FIELDS=" from_id, to_id, content, created_date, has_read, conversation_id, status";
     String SELECT_FIELDS=" id,"+INSERT_FIELDS;

     @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values (#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId},#{status})"})
     int addMessage(Message message);

     @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where conversation_id=#{conversationId} and status=0 order by id desc limit #{offset},#{limit}"})
     List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

     @Select({"select" ,INSERT_FIELDS ,",count(id) as id from (select * from ", TABLE_NAME , "where (from_id=#{userId} or to_id=#{userId}) and status=0 order by id desc )tt group by conversation_id order by id limit #{offset},#{limit}"})
     List<Message> getConversationList(@Param("userId") int userId,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);

     @Select({"select count(id) from ",TABLE_NAME ,"where has_read=0 and conversation_id=#{conversationId} and to_id=#{userId} and status=0" })
     int getConversationUnreadCount(@Param("userId")int userId,@Param("conversationId") String conversationId);

     @Update({"update ",TABLE_NAME ,"set has_read=#{hasRead} where conversation_id=#{conversationId} and to_id=#{userId}"})
     int updateHasReadMessage(@Param("hasRead") int hasRead,
                              @Param("userId")int userId,
                              @Param("conversationId") String conversationId);

     @Update({"update ",TABLE_NAME ,"set status=#{status} where id=#{id}"})
     int updateMessageStatus(@Param("id")int id,@Param("status") int status);
}
