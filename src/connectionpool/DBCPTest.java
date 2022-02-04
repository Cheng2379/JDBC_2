package connectionpool;

import dao.CustomersDaoImpl;
import dao.bean.Customers;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.jupiter.api.Test;
import utils.JDBCUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * DBCP: Apache提供的数据库连接池。tomcat 服务器自带dbcp数据库连接池。速度相对c3p0较快，
 *       但因自身存在BUG，Hibernate3已不再提供支持。
 */

public class DBCPTest {

    //方式一：
    @Test
    public void testDBCP() throws Exception {
        //创建DBCP数据库连接池
        BasicDataSource source = new BasicDataSource();

        //设置基本信息
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3307/test");
        source.setUsername("root");
        source.setPassword("123456");

        //设置设计数据库连接池管理的相关属性
        source.setInitialSize(5);
        source.setMaxActive(10);

        Connection connection = source.getConnection();
        System.out.println(connection);
    }

    //方法二：使用配置文件
    @Test
    public void test2DBCP() throws Exception {
        Properties pros = new Properties();

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");

        pros.load(is);
        DataSource dataSource = BasicDataSourceFactory.createDataSource(pros);

        Connection connection = dataSource.getConnection();
        System.out.println(connection);

    }

    @Test
    public void test3DBCP(){
        CustomersDaoImpl dao = new CustomersDaoImpl();
        Connection connection = null;
        try {
            connection = JDBCUtils.getDBCPConnection();

            Customers customers = dao.getCustomersById(connection, 10);

            System.out.println(customers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(connection,null);
        }
    }


}
