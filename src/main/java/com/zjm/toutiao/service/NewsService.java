package com.zjm.toutiao.service;

import com.zjm.toutiao.dao.NewsDAO;
import com.zjm.toutiao.model.News;
import com.zjm.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public String saveImage(MultipartFile file) throws IOException{
        int doPos=file.getOriginalFilename().lastIndexOf(".");
        if(doPos<0){
            return  null;
        }
        String fileExt=file.getOriginalFilename().substring(doPos+1).toLowerCase();//获取文件后缀名
        if(!ToutiaoUtil.isFileAllowed(fileExt)){//判断后缀名是否合法
            return null;
        }
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;//采用UUI给文件重新命名
        Files.copy(file.getInputStream(),new File((ToutiaoUtil.IMAGE_DIR)+fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);//从文件流复制该文件
        return ToutiaoUtil.TouTiao_DOMAIN+"image?name="+fileName;
    }

    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int id) {
        return newsDAO.getById(id);
    }

    public int updateCommentCount(int id,int commentCount){
        return newsDAO.updateCommentCount(id,commentCount);
    }

    public int updateLikeCount(int id,int likeCount){
        return newsDAO.updateLikeCount(id,likeCount);
    }
}
