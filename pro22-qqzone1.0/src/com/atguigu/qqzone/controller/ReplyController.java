package com.atguigu.qqzone.controller;

import com.atguigu.qqzone.pojo.Reply;
import com.atguigu.qqzone.pojo.Topic;
import com.atguigu.qqzone.pojo.UserBasic;
import com.atguigu.qqzone.service.ReplyService;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

public class ReplyController {
    private ReplyService replyService;
    public String addReply(String content, Integer topicId, HttpSession session) {
        UserBasic author = (UserBasic) session.getAttribute("userBasic");
        Reply reply = new Reply(content, new Timestamp(System.currentTimeMillis()), author, new Topic(topicId));
        replyService.addReply(reply);
        return "redirect:topic.do?operate=topicDetail&id="+topicId;
    }

    public String delReply(Integer replyId, Integer topicId) {
        replyService.delReply(replyId);
        return "redirect:topic.do?operate=topicDetail&id="+topicId;
    }
}
