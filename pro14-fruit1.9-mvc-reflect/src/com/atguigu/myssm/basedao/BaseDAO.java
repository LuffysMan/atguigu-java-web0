package com.atguigu.myssm.basedao;

import java.lang.reflect.Field;
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

    public List<T> executeQuery(String query, Object... params) {
        List<T> resultList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);) {
            setParams(statement, params);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T entity = resultSetToEntity(resultSet);
                resultList.add(entity);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    protected Object[] executeComplexQuery(String query, Object... params) {
        List<Object> resultList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);) {
            setParams(statement, params);
            ResultSet rs = statement.executeQuery();
            //通过resultset可以获取结果集的元数据
            //元数据: 描述结果集数据的数据, 简单讲, 就是这个结果集有哪些列, 什么类型等
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取结果集的列数
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columName = rsmd.getColumnName(i);
                    Object colValue = rs.getObject(i);
                    System.out.println(columName + '\t' + colValue.toString());
                    resultList.add(colValue);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList.toArray(new Object[0]);
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

    protected int executeUpdate(String sql, Object... params) {
        boolean insertFlag = false;
        try {

            insertFlag = sql.trim().toUpperCase().startsWith("INSERT");
            Connection conn = getConnection();
            PreparedStatement psmt = null;
            if (insertFlag) {
                psmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                psmt = conn.prepareStatement(sql);
            }
            setParams(psmt, params);
            int count = psmt.executeUpdate();
            if (insertFlag) {
                ResultSet rs = psmt.getGeneratedKeys();
                if (rs.next()) {
                    return ((Long) rs.getLong(1)).intValue();
                }
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public T load(String query, Integer fid) {
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, fid);
            ResultSet resultSet = statement.executeQuery();
            T entity = entityClass.newInstance();
            while (resultSet.next()) {
                entity = resultSetToEntity(resultSet);
            }
            return entity;
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
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