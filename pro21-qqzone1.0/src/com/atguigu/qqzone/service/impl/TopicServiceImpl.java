package com.atguigu.qqzone.service.impl;

import com.atguigu.qqzone.dao.TopicDAO;
import com.atguigu.qqzone.dao.UserBasicDAO;
import com.atguigu.qqzone.pojo.Topic;
import com.atguigu.qqzone.pojo.UserBasic;
import com.atguigu.qqzone.service.TopicService;
import com.atguigu.qqzone.service.UserBasicService;

import java.util.ArrayList;
import java.util.List;

public class TopicServiceImpl implements TopicService {
    private TopicDAO topicDAO = null;

    @Override
    public List<Topic> getTopicList(UserBasic userBasic) {
        return topicDAO.getTopicList(userBasic);
    }
}
