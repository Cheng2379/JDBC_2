package connectionpool;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import dao.CustomersDaoImpl;
import dao.bean.Customers;
import org.junit.jupiter.api.Test;
import utils.JDBCUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * Druid（德鲁伊）数据库连接池:
 * 阿里巴巴开源平台上一个数据库连接池实现，它结合了C3P0、DBCP、Proxool等DB池的优点，
 * 同时加入了日志监控，可以很好的监控DB池连接和SQL的执行情况，可以说是针对监控而生的DB连接池，可以说是目前最好的连接池之一。
 */

public class DruidTest {

    @Test
    public void testDruid() throws Exception {

        Properties pro = new Properties();

        InputStream is = ClassLoader.getSystemResourceAsStream("druid.properties");
        pro.load(is);

        DataSource dataSource = DruidDataSourceFactory.createDataSource(pro);

        Connection connection = dataSource.getConnection();

        System.out.println(connection);

    }

    @Test
    public void test2Druid(){
        CustomersDaoImpl dao = new CustomersDaoImpl();
        Connection connection = null;
        try {
            connection = JDBCUtils.getDruidConnection();

            Customers customers = dao.getCustomersById(connection, 10);

            System.out.println(customers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(connection,null);
        }
    }






}
