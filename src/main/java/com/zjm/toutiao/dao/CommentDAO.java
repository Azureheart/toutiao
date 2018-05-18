package com.zjm.toutiao.dao;

import com.zjm.toutiao.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDAO {
    String TABLE_NAME="comment";
    String INSERT_FIELD=" user_id, entity_id, entity_type, content, created_date, status";
    String SELECT_FIELD=" id,"+INSERT_FIELD;

    @Insert({"insert into ",TABLE_NAME ,"(",INSERT_FIELD ,")","values (#{userId},#{entityId},#{entityType},#{content},#{createdDate},#{status})"})
    int addComment(Comment comment);

    @Select({"select ",SELECT_FIELD ,"from ",TABLE_NAME ," " +
            "where entity_id=#{entityId} and entity_type=#{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Select({"select count(id) from ",TABLE_NAME ,"" +
            " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Update({"update ",TABLE_NAME,"set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType}"})
    void updateStatus(@Param("entityId") int entityId,@Param("entityType") int entityType,@Param("status") int status);

}
