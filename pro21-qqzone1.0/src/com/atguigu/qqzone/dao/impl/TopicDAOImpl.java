package com.atguigu.qqzone.dao.impl;

import com.atguigu.myssm.basedao.BaseDAO;
import com.atguigu.qqzone.dao.TopicDAO;
import com.atguigu.qqzone.pojo.Topic;
import com.atguigu.qqzone.pojo.UserBasic;

import java.util.List;

public class TopicDAOImpl extends BaseDAO<Topic> implements TopicDAO {
    public TopicDAOImpl() throws ClassNotFoundException {
    }

    @Override
    public List<Topic> getTopicList(UserBasic userBasic) {
        try {
            return super.executeQuery("SELECT * FROM t_topic WHERE author = ?", userBasic.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getTopicList ERROR");
        }
    }

    @Override
    public void addTopic(Topic topic) {

    }

    @Override
    public void delTopic(Topic topic) {

    }

    @Override
    public Topic getTopic(Integer id) {
        return null;
    }
}
