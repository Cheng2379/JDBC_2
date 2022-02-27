package utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 连接数据库以及对数据库操作的工具类
 */

public class JDBCUtils {
    /**
     * 连接资源
     *
     * @return 返回获取的连接
     * @throws Exception 抛出的异常
     */
    public static Connection getConnection() throws Exception {
        //1.读取配置文件信息
        //2.在JavaEE中，配置文件得放在target/classes文件当中,并且只能通过类本身调用getResourceAsStream方法,路径前加/
//        InputStream is = JDBCUtil.class.getResourceAsStream("/jdbc.properties");
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("utils/jdbc.properties");

        Properties pro = new Properties();
        pro.load(is);

        String url = pro.getProperty("url");
        String user = pro.getProperty("user");
        String password = pro.getProperty("password");
        String driverClass = pro.getProperty("driverClass");

        //加载驱动
        Class.forName(driverClass);

        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    /**
     * 使用C3P0的数据库连接池技术连接数据库
     * 数据库连接池只需提供一个
     */
    private static ComboPooledDataSource c3p0Source = new ComboPooledDataSource("myc3p0");
    public static Connection getC0P0Connection() throws SQLException {
        Connection connection = c3p0Source.getConnection();
        return connection;
    }

    /**
     * 使用DBCP的数据库连接池技术连接数据库
     * 数据库连接池只需提供一个
     */
    private static DataSource dbcpSource;
    static {
        try {
            Properties pros = new Properties();

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");

            pros.load(is);
            dbcpSource = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getDBCPConnection() throws Exception {
        Connection connection = dbcpSource.getConnection();
        return connection;
    }

    /**
     * 使用Druid(德鲁伊)的数据库连接池技术连接数据库
     * 数据库连接池只需提供一个
     *
     */
    private static DataSource druidSource;
    static {
        try {
            Properties pro = new Properties();

            InputStream is = ClassLoader.getSystemResourceAsStream("druid.properties");
            pro.load(is);

            druidSource = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getDruidConnection() throws SQLException {
        Connection connection = druidSource.getConnection();
        return connection;
    }

    /**
     * 通用的增删改操作,不支持事务的处理
     *
     * @param sql  SQL增删改语句
     * @param args 需要填充的占位符
     * @return 返回的int数据代表修改的数据内容, 0则代表无返回值
     */
    public static int update(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //获取数据库连接
            connection = JDBCUtils.getConnection();
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
            //关闭资源
            JDBCUtils.closeResourse(connection, ps);
        }
        return 0;
    }

    /**
     * 通用的增删改操作,支持数据库的事务处理
     * 需手动开启/关闭连接：JDBCUtil.getConnection();
     * JDBCUtil.closeResourse(connection,null);
     *
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
            try {
                //修改其为自动提交数据，主要针对于使用数据库连接池的使用
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭资源
            JDBCUtils.closeResourse(null, ps);
        }
        return 0;
    }

    /**
     * 通用的查询操作，可查询单条数据，不支持数据库的事务处理
     *
     * @param clazz 存储数据的类
     * @param sql   sql查询语句
     * @param args  要填充的占位符
     * @param <T>   返回的数据类型
     * @return 查询后返回的数据，没有查询结果则返回null
     */
    public static <T> T query(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
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
            JDBCUtils.closeResourse(connection, ps, resultSet);
        }
        return null;
    }

    /**
     * 通用的查询操作，可查询单条数据
     * 支持数据库的事务处理
     * 需手动开启/关闭连接：JDBCUtil.getConnection();
     * JDBCUtil.closeResourse(connection,null);
     *
     * @param clazz 存储数据的类
     * @param sql   sql查询语句
     * @param args  要填充的占位符
     * @param <T>   返回的数据类型
     * @return 查询后返回的数据，没有查询结果则返回null
     */
    public static <T> T query(Connection connection, Class<T> clazz, String sql, Object... args) {
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
     * 不支持数据库的事务处理
     *
     * @param clazz 存储数据的类
     * @param sql   sql查询语句
     * @param args  要填充的占位符
     * @param <T>   返回的数据类型
     * @return 查询后返回的数据(List集合)，没有查询结果则返回一个空集合
     */
    public static <T> List<T> queryList(Class<T> clazz, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
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
            JDBCUtils.closeResourse(connection, ps, resultSet);
        }
        return null;
    }

    /**
     * 通用的查询操作，可查询多条数据
     * 支持数据库的事务处理
     * 要需手动开启/关闭连接：JDBCUtil.getConnection();
     * JDBCUtil.closeResourse(connection,null);
     *
     * @param clazz 存储数据的类
     * @param sql   sql查询语句
     * @param args  要填充的占位符
     * @param <T>   返回的数据类型
     * @return 查询后返回的数据(List集合)，没有查询结果则返回一个空集合
     */
    public static <T> List<T> queryList(Connection connection, Class<T> clazz, String sql, Object... args) {
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
     * 关闭资源操作(增删改)
     * 使用DbUtils工具类实现资源的关闭
     *
     * @param connection 连接数据库
     * @param ps         PreparedStatement 准备好的声明
     */
    public static void closeResourse(Connection connection, Statement ps) {
        DbUtils.closeQuietly(connection);
        DbUtils.closeQuietly(ps);
    }

    /**
     * 关闭资源操作(查询)
     * 使用DbUtils工具类实现资源的关闭
     *
     * @param connection 连接数据库
     * @param ps         PreparedStatement 准备好的声明
     * @param resultSet  结果集
     */
    public static void closeResourse(Connection connection, Statement ps, ResultSet resultSet) {
        DbUtils.closeQuietly(connection);
        DbUtils.closeQuietly(ps);
        DbUtils.closeQuietly(resultSet);
    }

}
