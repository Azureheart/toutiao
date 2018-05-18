package com.zjm.toutiao;

import com.zjm.toutiao.dao.CommentDAO;
import com.zjm.toutiao.dao.LoginTicketDAO;
import com.zjm.toutiao.dao.NewsDAO;
import com.zjm.toutiao.dao.UserDAO;
import com.zjm.toutiao.model.*;
import com.zjm.toutiao.util.JedisAdapter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JedisTests {

	@Autowired
	JedisAdapter jedisAdapter;

	@Test
	public void testObject() {
		User user=new User();
		user.setHeadUrl("http://image.nowcoder.com/head/100t.png");
		user.setName("user100");
		user.setPassword("pwd");
		user.setSalt("salt");

		jedisAdapter.setObject("user1XX",user);

		User u=jedisAdapter.getObject("user1XX",User.class);

		System.out.print(ToStringBuilder.reflectionToString(u));
	}
}
