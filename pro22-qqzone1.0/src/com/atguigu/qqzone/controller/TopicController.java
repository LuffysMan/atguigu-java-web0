package com.atguigu.qqzone.controller;

import com.atguigu.qqzone.pojo.Topic;
import com.atguigu.qqzone.service.TopicService;

import javax.servlet.http.HttpSession;

public class TopicController {
    private TopicService topicService;

    public String topicDetail(Integer id, HttpSession session) {
        Topic topic = topicService.getTopicById(id);
        session.setAttribute("topic", topic);
        return "frames/detail";
    }
}
