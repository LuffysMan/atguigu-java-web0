package com.atguigu.qqzone.service;

import com.atguigu.qqzone.pojo.HostReply;

public interface HostReplyService {
    public HostReply getHostReplyByReplyId(Integer replyId);

    void delHostReply(Integer id);
}
