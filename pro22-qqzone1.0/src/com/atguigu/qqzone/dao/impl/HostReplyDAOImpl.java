package com.atguigu.qqzone.dao.impl;

import com.atguigu.myssm.basedao.BaseDAO;
import com.atguigu.qqzone.dao.HostReplyDAO;
import com.atguigu.qqzone.pojo.HostReply;
import com.atguigu.qqzone.pojo.Reply;
import com.atguigu.qqzone.pojo.UserBasic;

import java.util.List;

public class HostReplyDAOImpl extends BaseDAO<HostReply> implements HostReplyDAO {
    public HostReplyDAOImpl() throws ClassNotFoundException {
    }

    @Override
    public HostReply getHostReply(Reply reply) {
        try {
            return super.load("SELECT * FROM t_host_reply WHERE reply = ?", reply.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getHostReply ERROR!");
        }
    }

    @Override
    public HostReply getHostReplyByReplyId(Integer replyId) {
        try {
            return super.load("SELECT * FROM t_host_reply WHERE reply = ?", replyId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getHostReply ERROR!");
        }
    }

    @Override
    public List<HostReply> getHostReplyList(UserBasic userBasic) {
        return null;
    }

    @Override
    public void addHostReply(HostReply hostReply) {
        try {
            super.executeUpdate("INSERT INTO t_host_reply VALUES(0,?,?,?,?)", hostReply.getContent(), hostReply.getHotsReplyDate(), hostReply.getAuthor().getId(), hostReply.getReply().getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("addReply ERROR");
        }
    }

    @Override
    public void delHostReply(Integer id) {
        try {
            super.executeUpdate("DELETE FROM t_host_reply WHERE id= ? ", id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("delHostReply ERROR");
        }
    }
}
