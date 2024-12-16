package com.atguigu.qqzone.dao;

import com.atguigu.qqzone.pojo.HostReply;
import com.atguigu.qqzone.pojo.Reply;
import com.atguigu.qqzone.pojo.UserBasic;

import java.util.List;

public interface HostReplyDAO {
    public HostReply getHostReply(Reply reply);

    public List<HostReply> getHostReplyList(UserBasic userBasic);

    public void addHostReply(HostReply hostReply);

    public void delHostReply(Integer id);
}
