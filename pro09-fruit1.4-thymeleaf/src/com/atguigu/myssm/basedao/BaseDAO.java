package com.atguigu.myssm.basedao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/atguigu?userSSL=false";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "lf@123";
    private static final String DRIVER = "com.mysql.jdbc.Driver";

    private final String url;
    private final String username;
    private final String password;
    private Connection connection;
    private Class<T> entityClass;

    public BaseDAO() {
        this.url = DEFAULT_URL;
        this.username = DEFAULT_USERNAME;
        this.password = DEFAULT_PASSWORD;

        // 获取T的真实类型
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            if (typeArguments.length > 0) {
                entityClass = (Class<T>) typeArguments[0];
            }
        }
    }

    protected Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName(DRIVER);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    public void setParams(PreparedStatement psmt, Object... params) throws SQLException {
        int idx = 1;
        for (Object obj : params) {
            if (obj.getClass() == Integer.class) {
                psmt.setInt(idx, (Integer) obj);
            } else if (obj.getClass() == String.class) {
                psmt.setString(idx, (String) obj);
            } else if (obj.getClass() == Long.class) {
                psmt.setLong(idx, (Long) obj);
            } else if (obj.getClass() == Float.class) {
                psmt.setFloat(idx, (Float) obj);
            } else if (obj.getClass() == Double.class) {
                psmt.setDouble(idx, (Double) obj);
            }
            idx += 1;
        }
    }

    public List<T> executeQuery(String sql, Object... params) {
        List<T> resultList = new ArrayList<>();

        try {
            Connection connection = getConnection();
            PreparedStatement psmt = connection.prepareStatement(sql);
            setParams(psmt, params);
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                T entity = resultSetToEntity(rs);
                resultList.add(entity);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    protected Object[] executeComplexQuery(String query, Object... params) {
        try {
            Connection conn = getConnection();
            PreparedStatement psmt = conn.prepareStatement(query);
            setParams(psmt, params);
            ResultSet rs = psmt.executeQuery();
            //通过resultset可以获取结果集的元数据
            //元数据: 描述结果集数据的数据, 简单讲, 就是这个结果集有哪些列, 什么类型等
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            Object[] columnValueArr = new Object[columnCount];
            // 6. 解析rs
            if (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    columnValueArr[i] = columnValue;
                }
                return columnValueArr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setValue(T entity, String columnName, Object columnValue) throws IllegalAccessException, NoSuchFieldException {
        for (Field field :
                entityClass.getDeclaredFields()) {
            if (columnName.equals(field.getName())) {
                field.setAccessible(true);
                field.set(entity, columnValue);
            }
        }
    }

    private T resultSetToEntity(ResultSet resultSet) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        T entity = entityClass.getDeclaredConstructor().newInstance();
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            // 从 resultSet 中获取字段值
            Object value = resultSet.getObject(field.getName());
            field.set(entity, value);
        }
        return entity;
    }
}