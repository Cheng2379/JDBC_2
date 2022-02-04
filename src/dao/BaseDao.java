package dao;

import utils.JDBCUtils;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装了针对于数据表的通用操作
 */

public abstract class BaseDao<T> {

    private Class<T> clazz = null;

    {
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        Type[] typeArguments = paramType.getActualTypeArguments();//获取父类的泛型参数
        clazz = (Class<T>)typeArguments[0];//泛型第一个参数
    }

    /**
     * 通用的增删改操作,支持数据库的事务处理
     * 需手动开启/关闭连接：JDBCUtil.getConnection();
     *                  JDBCUtil.closeResourse(connection,null);
     * @param sql  SQL增删改语句
     * @param args 需要填充的占位符
     * @return 返回的int数据代表修改的数据内容, 0则代表无返回值
     */
    public static int update(Connection connection, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            //预编译sql语句，返回PreparedStatement
            ps = connection.prepareStatement(sql);
            //填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            try {
//                //修改其为自动提交数据，主要针对于使用数据库连接池的使用
//                connection.setAutoCommit(true);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
            //关闭资源
            JDBCUtils.closeResourse(null, ps);
        }
        return 0;
    }

    /**
     * 通用的查询操作，可查询单条数据
     * 支持数据库的事务处理
     * 需手动开启/关闭连接：JDBCUtil.getConnection();
     *                    JDBCUtil.closeResourse(connection,null);
     * @param sql   sql查询语句
     * @param args  要填充的占位符
     * @return 查询后返回的数据，没有查询结果则返回null
     */
    public  T query(Connection connection,String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            resultSet = ps.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();

            if (resultSet.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(null, ps, resultSet);
        }
        return null;
    }

    /**
     * 通用的查询操作，可查询多条数据
     * 支持数据库的事务处理
     * 要需手动开启/关闭连接：JDBCUtil.getConnection();
     *                    JDBCUtil.closeResourse(connection,null);
     * @param sql   sql查询语句
     * @param args  要填充的占位符
     * @return 查询后返回的数据(List集合)，没有查询结果则返回一个空集合
     */
    public List<T> queryList(Connection connection, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            resultSet = ps.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            ArrayList<T> list = new ArrayList<>();
            while (resultSet.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(null, ps, resultSet);
        }
        return null;
    }

    /**
     * 用于查询特殊值的方法
     * @param connection 连接数据库
     * @param sql   SQL语句
     * @param args  需要填充的占位符
     * @param <E>   返回的数据类型
     * @return 返回的数据，没有查询到数据则返回null
     */
    public <E> E getValue(Connection connection,String sql,Object...args) {

        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            resultSet = ps.executeQuery();

            if(resultSet.next()){
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(null,ps,resultSet);
        }
        return null;
    }





}
