package com.atguigu.myssm.basedao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void save(T entity) {
        String query = "INSERT INTO " + getTableName(entity) + " VALUES " + getInsertValues(entity);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setStatementParameters(statement, entity, "insert");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(T entity) {
        String query = "UPDATE " + getTableName(entity) + " SET " + getUpdateColumns(entity) + " WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setStatementParameters(statement, entity, "update");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM " + getTableName(null) + " WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<T> executeQuery(String query) {
        List<T> resultList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                T entity = resultSetToEntity(resultSet);
                resultList.add(entity);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return resultList;
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

    private T resultSetToEntity(ResultSet resultSet) throws SQLException, InstantiationException, IllegalAccessException {
        T entity = entityClass.newInstance();
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();

            if (fieldName.equals("fid")) {
                field.set(entity, resultSet.getInt(fieldName));
            } else if (fieldName.equals("fname") || fieldName.equals("remark")) {
                field.set(entity, resultSet.getString(fieldName));
            } else if (fieldName.equals("price") || fieldName.equals("fcount")) {
                field.set(entity, resultSet.getInt(fieldName));
            }
        }
        return entity;
    }

    private String getTableName(T entity) {
        // 根据实体对象类型T返回表名，请根据实际情况进行实现
        return "";
    }

    private String getInsertValues(T entity) {
        // 根据实体对象类型T返回插入语句的值部分，请根据实际情况进行实现
        return "";
    }

    private String getUpdateColumns(T entity) {
        // 根据实体对象类型T返回更新语句的列部分，请根据实际情况进行实现
        return "";
    }

    private void setStatementParameters(PreparedStatement statement, T entity, String operation) throws SQLException {
    }
}