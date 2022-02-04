package connectionpool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import dao.CustomersDaoImpl;
import dao.bean.Customers;
import org.junit.jupiter.api.Test;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * C3P0: 一个开源组织提供的一个数据库连接池，速度相对较慢，稳定性还可以。hibernate官方推荐使用
 */

public class C3P0Test {

    //方式一
    @Test
    public void test1() throws Exception {
        //获取C3P0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
        cpds.setJdbcUrl("jdbc:mysql://localhost:3307/test");
        cpds.setUser("root");
        cpds.setPassword("123456");

        //通过设置相关的参数，对数据库连接池进行管理
        //设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(10);

        Connection connection = cpds.getConnection();
        System.out.println("连接成功\n"+connection);

        //销毁C3P0数据库连接池
//        DataSources.destroy( cpds );
    }

    //方式二: 使用配置文件
    @Test
    public void test2() throws SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource("myc3p0");
        Connection connection = cpds.getConnection();
        System.out.println("连接成功\n"+connection);
    }

    @Test
    public void test3() {
        CustomersDaoImpl dao = new CustomersDaoImpl();
        Connection connection = null;
        try {
            connection = JDBCUtils.getC0P0Connection();

            Customers customers = dao.getCustomersById(connection, 10);

            System.out.println(customers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(connection,null);
        }
    }


}
