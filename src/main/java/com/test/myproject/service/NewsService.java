package com.test.myproject.service;

import com.test.myproject.dao.NewsDAO;
import com.test.myproject.model.News;
import com.test.myproject.util.MyProjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public void updateCommentCount(int entityId,int count){
        newsDAO.updateCommentCount(entityId, count);
    }

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public News getById(int newsId){
        return newsDAO.selectByNewsId(newsId);
    }

    public void addNews(News news){
        newsDAO.addNews(news);
    }

    public String saveImage(MultipartFile file)throws IOException{
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if(dotPos<0){
            return null;
        }

        String fileExt = file.getOriginalFilename().substring(dotPos+1).toLowerCase();
        if(!MyProjectUtil.isFileAllowed(fileExt)){
            return null;
        }

        String fileName = UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        Files.copy(file.getInputStream(),new File(MyProjectUtil.IMAGE_DIR+fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return MyProjectUtil.MyProject_DOMAIN+"image?name="+fileName;

    }
}
