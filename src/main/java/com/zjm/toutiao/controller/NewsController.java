package com.zjm.toutiao.controller;

import com.zjm.toutiao.model.*;
import com.zjm.toutiao.service.*;
import com.zjm.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class NewsController {
    @Autowired
    NewsService newsService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QiniuService qiniuService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    private static final Logger logger= LoggerFactory.getLogger(NewsController.class);

    @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+imageName)),
                    response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片错误"+e.getMessage());
        }
    }

    @RequestMapping(path = {"/user/addNews/"},method = RequestMethod.POST)
    @ResponseBody
    public String addNews(@RequestParam("image") String  image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link
                          ){
        try {
            News news=new News();
            if(hostHolder.getUser()!=null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                news.setUserId(3);//匿名用户
            }
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);
            news.setCommentCount(0);
            newsService.addNews(news);
            return  ToutiaoUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("添加资讯错误"+e.getMessage());
            return  ToutiaoUtil.getJSONString(1,"发布失败");
        }
    }

    @RequestMapping(path = {"/uploadImage/" },method = RequestMethod.POST)
    @ResponseBody
    public String upLoadImage(@RequestParam("file") MultipartFile file){//MultipartFile类是多部分请求的上传文件的表示
        try{
            //String fileUrl=newsService.saveImage(file);
            String fileUrl=qiniuService.saveImage(file);
            if(fileUrl==null){
                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            }else{
                return ToutiaoUtil.getJSONString(0,fileUrl);
            }
        }catch (Exception e){
            logger.error("上传图片失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传失败");
        }
    }

    @RequestMapping(path = "/news/{newsId}",method = RequestMethod.GET)
    public String newsDetail(@PathVariable("newsId") int newsId , Model model){
        News news=newsService.getById(newsId);
        if(news!=null){
            int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
            if(localUserId!=0){
                model.addAttribute("like",likeService.getLikeStatus(localUserId,EntityType.ENTITY_NEWS,newsId));
            }else{
                model.addAttribute("like",0);
            }
            //评论
            List<Comment> comments=commentService.getCommentsByEntity(newsId, EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs=new ArrayList<>();
            for(Comment comment:comments){
                ViewObject viewObject=new ViewObject();
                viewObject.set("comment",comment);
                viewObject.set("user",userService.getUser(comment.getUserId()));
                commentVOs.add(viewObject);
            }
            model.addAttribute("comments",commentVOs);
        }
        model.addAttribute("news",news);
        model.addAttribute("owner",userService.getUser(news.getUserId()));
        model.addAttribute("user",hostHolder.getUser());
        return "detail";
    }

    @RequestMapping(path = {"/addComment"},method = RequestMethod.POST)
    public String addComment(@RequestParam("newsId") int newsId,@RequestParam("content") String content){
       try {
           Comment comment = new Comment();
           comment.setUserId(hostHolder.getUser().getId());
           comment.setContent(content);
           comment.setCreatedDate(new Date());
           comment.setEntityId(newsId);
           comment.setEntityType(EntityType.ENTITY_NEWS);
           comment.setStatus(0);
           commentService.addComment(comment);
            //更新news里的评论数量
           int count=commentService.getCommntCount(comment.getEntityId(),comment.getEntityType());
           newsService.updateCommentCount(newsId,count);

       }catch (Exception e){
           logger.error("增加评论失败"+e.getMessage());
       }
       return "redirect:/news/"+String.valueOf(newsId);
    }
}
