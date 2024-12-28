package com.atguigu.qqzone.dao.impl;

import com.atguigu.myssm.basedao.BaseDAO;
import com.atguigu.qqzone.dao.UserBasicDAO;
import com.atguigu.qqzone.pojo.UserBasic;

import java.util.List;

public class UserBasicDAOImpl extends BaseDAO<UserBasic> implements UserBasicDAO {
    public UserBasicDAOImpl() throws ClassNotFoundException {
    }

    @Override
    public UserBasic getUserBasic(String loginId, String pwd) {
        try {
            return super.load("select * from t_user_basic where loginId = ? and pwd = ?", loginId, pwd);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getUserBasic ERROR!");
        }
    }

    @Override
    public List<UserBasic> getUserBasicList(UserBasic userBasic) {
        try {
            String sql = "SELECT fid as id FROM t_friend WHERE uid = ?";
            return super.executeQuery(sql, userBasic.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getUserBasicList ERROR!");
        }
    }

    @Override
    public UserBasic getUserBasicById(Integer id) {
        try {
            return super.load("SELECT * FROM t_user_basic WHERE id = ?", id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("getUserBasicById ERROR!");
        }
    }

}
