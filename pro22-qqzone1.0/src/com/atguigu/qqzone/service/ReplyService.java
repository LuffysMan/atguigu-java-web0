package com.atguigu.qqzone.service;

import com.atguigu.qqzone.pojo.Reply;

import java.util.List;

public interface ReplyService {
    //根据topic的id获取关联的所有的回复
    List<Reply> getReplyListByTopicId(Integer topicId);

    void addReply(Reply reply);

    void delReply(Integer id);
}
