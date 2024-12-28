package com.atguigu.qqzone.dao.impl;

import com.atguigu.myssm.basedao.BaseDAO;
import com.atguigu.qqzone.dao.ReplyDAO;
import com.atguigu.qqzone.pojo.Reply;
import com.atguigu.qqzone.pojo.Topic;

import java.util.List;

public class ReplyDAOImpl extends BaseDAO<Reply> implements ReplyDAO {
    public ReplyDAOImpl() throws ClassNotFoundException {
    }

    @Override
    public List<Reply> getReplyList(Topic topic) {
        try {
            return super.executeQuery("SELECT * FROM t_reply WHERE topic = ?", topic.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getReplyList ERROR");
        }
    }

    @Override
    public void addReply(Reply reply) {
        try {
            super.executeUpdate("INSERT INTO t_reply VALUES(0,?,?,?,?)", reply.getContent(), reply.getReplyDate(), reply.getAuthor().getId(), reply.getTopic().getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("addReply ERROR");
        }
    }

    @Override
    public void delReply(Integer id) {
        try {
            super.executeUpdate("DELETE FROM t_reply WHERE id= ? ", id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("delReply ERROR");
        }
    }

    @Override
    public Reply getReply(Integer id) {
        try {
            return super.load("SELECT * FROM t_reply WHERE id = ?", id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getReply ERROR");
        }
    }
}
