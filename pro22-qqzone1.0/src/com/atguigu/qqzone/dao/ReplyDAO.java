package com.atguigu.qqzone.dao;

import com.atguigu.qqzone.pojo.Reply;
import com.atguigu.qqzone.pojo.Topic;

import java.util.List;

public interface ReplyDAO {
    List<Reply> getReplyList(Topic topic);

    void addReply(Reply reply);

    void delReply(Integer id);

    Reply getReply(Integer id);
}
