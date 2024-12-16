package com.atguigu.qqzone.dao;

import com.atguigu.qqzone.pojo.Topic;
import com.atguigu.qqzone.pojo.UserBasic;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface TopicDAO {
    // 获取指定用户的所有日志
    public List<Topic> getTopicList(UserBasic userBasic);

    // 添加日志
    public void addTopic(Topic topic);

    // 删除日志
    public void delTopic(Topic topic);

    // 获取特定日志信息
    public Topic getTopic(Integer id);
}
