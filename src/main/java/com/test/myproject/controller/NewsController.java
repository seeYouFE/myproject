package com.test.myproject.controller;

import com.test.myproject.model.*;
import com.test.myproject.service.ALiService;
import com.test.myproject.service.CommentService;
import com.test.myproject.service.NewsService;
import com.test.myproject.service.UserService;
import com.test.myproject.util.MyProjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    ALiService aLiService;
    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/addComment"},method = RequestMethod.POST)
    public String addComment(@RequestParam("newsId")int newsId,
                             @RequestParam("content")String content){
        try{
//            content = HtmlUtils.htmlEscape(content);

            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);

            commentService.addComment(comment);

            int count =commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updateCommentCount(comment.getEntityId(),count);
        }catch(Exception e){
            logger.error("增加评论失败"+e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }

    @RequestMapping(path = {"/news/{newsId}"},method = RequestMethod.GET)
    public String newsDetail(@PathVariable("newsId")int newsId,Model model){
        try {
            News news = newsService.getById(newsId);
            if (news != null) {
                List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
                List<ViewObject> commentVOs = new ArrayList<>();

                for (Comment comment : comments) {
                    ViewObject vo = new ViewObject();
                    vo.set("comment", comment);
                    vo.set("user", userService.getUser(comment.getUserId()));
                    commentVOs.add(vo);
                }

                model.addAttribute("comments", commentVOs);

            }
            model.addAttribute("news", news);
            model.addAttribute("owner", userService.getUser(news.getUserId()));
        }catch (Exception e){
            logger.error("获取资讯明细错误"+e.getMessage());
        }
        return "detail";
    }

    @RequestMapping(path={"/user/addNews/"},method={RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image")String image,
                          @RequestParam("title")String title,
                          @RequestParam("link")String link){
        try{
            News news = new News();
            news.setImage(image);
            news.setTitle(title);
            news.setLink(link);
            news.setCreatedDate(new Date());
            if(hostHolder.getUser()!=null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                news.setUserId(99);
            }
            newsService.addNews(news);
            return MyProjectUtil.getJSONString(0);
        }catch(Exception e){
            logger.error("添加咨询失败"+e.getMessage());
            return MyProjectUtil.getJSONString(1,"发布失败");
        }
    }


    @RequestMapping(path={"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file")MultipartFile file){
        try{
            String fileUrl= aLiService.saveImage(file);
            if(fileUrl==null){
                return MyProjectUtil.getJSONString(1,"上传图片失败");
            }
            return MyProjectUtil.getJSONString(0,fileUrl);
        }catch(Exception e){
            logger.error("上传图片失败"+e.getMessage());
            return MyProjectUtil.getJSONString(1,"上传图片失败");
        }
    }


    @RequestMapping(path={"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name")String imageName,
                         HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(MyProjectUtil.IMAGE_DIR+imageName)),response.getOutputStream());

        }catch(Exception e){
            logger.error("读取图片错误"+imageName+e.getMessage());
        }


    }


}
