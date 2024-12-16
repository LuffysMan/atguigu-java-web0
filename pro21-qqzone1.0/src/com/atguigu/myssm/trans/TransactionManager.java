package com.atguigu.myssm.trans;

import com.atguigu.myssm.basedao.ConnUtil;

import java.sql.SQLException;

public class TransactionManager {
    // 开启事务
    public static void beginTrans() throws SQLException {
        ConnUtil.getConn().setAutoCommit(false);
    }

    // 提交事务
    public static void commit() throws SQLException {
        ConnUtil.getConn().commit();
        ConnUtil.closeConn();
    }

    // 回滚事务
    public static void rollback() throws SQLException {
        ConnUtil.getConn().rollback();
        ConnUtil.closeConn();
    }
}
