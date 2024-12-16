package com.atguigu.myssm.basedao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnUtil {
    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/qqzone?userSSL=false";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "lf@123";
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private static Connection createConn() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Connection getConn() {
        Connection conn = threadLocal.get();
        if (conn == null) {
            conn = createConn();
            threadLocal.set(conn);
        }
        return threadLocal.get();
    }

    public static void closeConn() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn == null) {
            return;
        }
        if (!conn.isClosed()) {
            conn.close();
//            threadLocal.set(null);
            threadLocal.remove();
        }

    }
}
