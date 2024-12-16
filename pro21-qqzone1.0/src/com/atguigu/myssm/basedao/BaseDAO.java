package com.atguigu.myssm.basedao;

import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    private Class entityClass;
    protected Connection conn;
    protected PreparedStatement psmt;
    protected ResultSet rs;
    private static final List<String> typeNameList = new ArrayList<>();
    static {
        typeNameList.add("java.lang.Integer");
        typeNameList.add("java.lang.Long");
        typeNameList.add("java.lang.Short");
        typeNameList.add("java.lang.Double");
        typeNameList.add("java.lang.Float");
        typeNameList.add("java.lang.String");
        typeNameList.add("java.sql.Timestamp");
        typeNameList.add("java.sql.Date");
    }

    public BaseDAO() throws ClassNotFoundException {
        // getClass() 获取 Class 对象, 当前我们执行的是 new FruitDAOImpl(), 创建的是 FruitDAOImpl 的实例
        // 那么子类构造方法内部首先会调用父类(BaseDAO)的无参构造方法
        // 因此此处的 getClass() 会被执行, 但是 getClass() 获取的是 FruitDAOImpl 的 class
        // 所以 getGenericSuperclass() 获取到的是 BaseDAO 的 class
        Type genericType = getClass().getGenericSuperclass();
        if (genericType instanceof ParameterizedType) {
            // ParameterizedType 参数化类型
            Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            if (actualTypeArguments.length > 0) {
                // 获取到<T>中的真实的类型
                Type actualType = actualTypeArguments[0];
                entityClass = Class.forName(actualType.getTypeName());
            }
        }
    }

    protected Connection getConnection() throws SQLException {
        return ConnUtil.getConn();
    }

    public List<T> executeQuery(String query, Object... params) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException, ClassNotFoundException {
        List<T> resultList = new ArrayList<>();
        conn = getConnection();
        psmt = conn.prepareStatement(query);
        setParams(psmt, params);
        rs = psmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        while (rs.next()) {
            T entity = (T)entityClass.getDeclaredConstructor().newInstance();
            for (int i = 1; i <= columnCount; i++) {
                String columnLabel = rsmd.getColumnLabel(i);
                Object columnValue = rs.getObject(i);
                setValue(entity, columnLabel, columnValue);
            }
            resultList.add(entity);
        }
        return resultList;
    }

    protected Object[] executeComplexQuery(String query, Object... params) throws SQLException {
        conn = getConnection();
        PreparedStatement psmt = conn.prepareStatement(query);
        setParams(psmt, params);
        rs = psmt.executeQuery();
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
        return null;
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

    protected int executeUpdate(String sql, Object... params) throws SQLException {
        boolean insertFlag = false;
        insertFlag = sql.trim().toUpperCase().startsWith("INSERT");
        conn = getConnection();
        if (insertFlag) {
            psmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            psmt = conn.prepareStatement(sql);
        }
        setParams(psmt, params);
        int count = psmt.executeUpdate();
        if (insertFlag) {
            rs = psmt.getGeneratedKeys();
            if (rs.next()) {
                return ((Long) rs.getLong(1)).intValue();
            }
        }
        return count;
    }

    public T load(String sql, Object... params) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        conn = getConnection();
        psmt = conn.prepareStatement(sql);
        setParams(psmt, params);
        rs = psmt.executeQuery();

        // 通过 rs 可以获取结果集的元数据
        // 元数据: 描述结果集数据的数据. 简单讲, 就是这个结果集有哪些列, 什么类型等等

        ResultSetMetaData rsmd = rs.getMetaData();
        //获取结果集的列数
        int columnCount = rsmd.getColumnCount();
        // 6. 解析 rs
        if (rs.next()) {
            T entity = (T)entityClass.getDeclaredConstructor().newInstance();
            for (int i = 1; i <= columnCount; i++) {
                setValue(entity, rsmd.getColumnName(i), rs.getObject(i));
            }
            return entity;
        }
        return null;
    }

    private void setValue(Object obj, String property, Object propertyValue) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class clazz = obj.getClass();
        //获取property 这个字符串对应的属性名, 比如 "fid" 去找 obj 对象中的 fid 属性
        Field field = clazz.getDeclaredField(property);

        // 获取当前字段类型名称
        String typeName = field.getType().getName();
        // 如果是自定义类型, 则需要调用这个自定义类的带一个参数的构造方法
        if (isMyType(typeName)) {
            //假设 typeName 是 "com.at.guigu.qqzone.pojo.UserBasic"
            Class typeNameClass = Class.forName(typeName);
            Constructor constructor = typeNameClass.getDeclaredConstructor(java.lang.Integer.class);
            propertyValue = constructor.newInstance(propertyValue);
        }

        field.setAccessible(true);
        field.set(obj, propertyValue);
    }

    private static boolean isMyType(String typeName) {
        return !isNotMyType(typeName);
    }

    private static boolean isNotMyType(String typeName) {
        return typeNameList.contains(typeName);
    }

    private T resultSetToEntity(ResultSet resultSet) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        T entity = (T) entityClass.getDeclaredConstructor().newInstance();
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            // 从 resultSet 中获取字段值
            Object value = resultSet.getObject(field.getName());
            field.set(entity, value);
        }
        return entity;
    }
}