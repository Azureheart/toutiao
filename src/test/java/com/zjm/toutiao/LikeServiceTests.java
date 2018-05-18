package com.zjm.toutiao;

import com.zjm.toutiao.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LikeServiceTests {

    @Autowired
    LikeService likeService;

    @Test
    public void testLike(){
        likeService.like(1,1,3);
        Assert.assertEquals(1,likeService.getLikeStatus(1,1,3));
    }

    @Test
    public void testDislike(){
        likeService.disLike(2,1,3);
        Assert.assertEquals(-1,likeService.getLikeStatus(2,1,3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException(){
        throw  new IllegalArgumentException();
    }

    @Before
    public void setUp(){
        System.out.println("setUp");
    }

    @After
    public  void tearDown(){
        System.out.println("tearDown");
    }

    @BeforeClass
    public static void beforeClass(){
        System.out.println("beforeClass");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("afterClass");
    }
}
