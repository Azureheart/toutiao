package com.zjm.toutiao.service;

import com.zjm.toutiao.dao.CommentDAO;
import com.zjm.toutiao.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;

    public int addComment(Comment comment){
        return  commentDAO.addComment(comment);
    }

    public List<Comment> getCommentsByEntity(int entityId,int entityType){
        return commentDAO.selectByEntity(entityId,entityType);
    }

    public int getCommntCount(int entityId,int entityType){
        return  commentDAO.getCommentCount(entityId,entityType);
    }

    public void deleteComment(int entityId,int entityType){
         commentDAO.updateStatus(entityId,entityType,1);
    }
}
