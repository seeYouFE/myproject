package com.test.myproject.controller;

import com.test.myproject.asyn.EventModel;
import com.test.myproject.asyn.EventProducer;
import com.test.myproject.asyn.EventType;
import com.test.myproject.model.EntityType;
import com.test.myproject.model.HostHolder;
import com.test.myproject.model.News;
import com.test.myproject.service.LikeService;
import com.test.myproject.service.NewsService;
import com.test.myproject.util.MyProjectUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path={"/like"},method= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(@Param("newsId")int newsId){
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);

        News news = newsService.getById(newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE).setGetEntityOwnerId(news.getUserId())
        .setActorId(hostHolder.getUser().getId()).setEntityId(newsId));

        return MyProjectUtil.getJSONString(0,String.valueOf(likeCount));

    }

    @RequestMapping(path={"/dislike"},method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String disLike(@Param("newsId")int newsId){
        long disLikeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS,newsId);

        News news = newsService.getById(newsId);
        newsService.updateLikeCount(newsId,(int)disLikeCount);
        eventProducer.fireEvent(new EventModel(EventType.LIKE).setGetEntityOwnerId(news.getUserId())
                .setActorId(hostHolder.getUser().getId()).setEntityId(newsId));

        return MyProjectUtil.getJSONString(0,String.valueOf(disLikeCount));

    }



}
