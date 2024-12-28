package com.atguigu.qqzone.service.impl;

import com.atguigu.qqzone.dao.HostReplyDAO;
import com.atguigu.qqzone.dao.ReplyDAO;
import com.atguigu.qqzone.pojo.HostReply;
import com.atguigu.qqzone.pojo.Reply;
import com.atguigu.qqzone.pojo.Topic;
import com.atguigu.qqzone.pojo.UserBasic;
import com.atguigu.qqzone.service.HostReplyService;
import com.atguigu.qqzone.service.ReplyService;
import com.atguigu.qqzone.service.UserBasicService;

import java.util.List;

public class ReplyServiceImpl implements ReplyService {
    private ReplyDAO replyDAO;
    // 此处引入的是其他POJO对应的Service接口, 而不是DAO接口
    // 其他POJO对应的业务逻辑是封装在service层的, 我需要调用别人的业务逻辑方法, 而不是去深入考虑人家内部的细节
    private HostReplyService hostReplyService;
    private UserBasicService userBasicService;

    @Override
    public List<Reply> getReplyListByTopicId(Integer topicId) {
        List<Reply> replyList = replyDAO.getReplyList(new Topic(topicId));
        for (Reply reply :
                replyList) {
            // 1. 将关联的作者设置进去
            UserBasic author = userBasicService.getUserBasicById(reply.getAuthor().getId());
            reply.setAuthor(author);

            // 2. 将关联的HostReply设置进去
            HostReply hostReply = hostReplyService.getHostReplyByReplyId(reply.getId());
            reply.setHostReply(hostReply);
        }
        return replyList;
    }

    @Override
    public void addReply(Reply reply) {
        replyDAO.addReply(reply);
    }

    @Override
    public void delReply(Integer id) {
        //1. 根据id获取到reply
        Reply reply = replyDAO.getReply(id);
        if (reply != null) {
            //2. 如果reply有关联的hostReply, 则先删除hostReply
            if (reply.getHostReply() != null) {
                hostReplyService.delHostReply(reply.getHostReply().getId());
            }
            replyDAO.delReply(id);
        }
    }
}
