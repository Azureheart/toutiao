package com.zjm.toutiao;

import com.zjm.toutiao.dao.CommentDAO;
import com.zjm.toutiao.dao.LoginTicketDAO;
import com.zjm.toutiao.dao.NewsDAO;
import com.zjm.toutiao.dao.UserDAO;
import com.zjm.toutiao.model.*;
import com.zjm.toutiao.service.MessageService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseTests {

	@Autowired
	MessageService messageService;


	@Test
	public void  test(){
		System.out.println(messageService.getConversationUnreadCount(13,"4_13"));
	}

}
