package dbutils;

import dao.bean.Customers;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.Test;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public class QueryRunnerTest {

    //测试插入数据
    @Test
    public void updateRunner() {

        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();

            connection = JDBCUtils.getDruidConnection();
            String sql = "insert into customers(name,email,birth) values(?,?,?)";
            int update = runner.update(connection, sql, "鸡你太美", "jntm@gmail.com", "1995-09-19");
            System.out.println("添加了" + update + "条数据");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(connection, null);
        }
    }

    //测试查询数据
    @Test
    public void queryRunner() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();

            connection = JDBCUtils.getDruidConnection();

            String sql = "select id,name,email,birth from customers where id < ?";

            BeanListHandler<Customers> handler = new BeanListHandler<>(Customers.class);

            List<Customers> customers = runner.query(connection, sql, handler, 31);
            customers.forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            JDBCUtils.closeResourse(connection,null);
        }
    }

    //查询特殊值，例如查询数据表的数据条目数、查询某字段的最大数...
    @Test
    public void testRunner(){
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();

            connection = JDBCUtils.getDruidConnection();

            String sql = "select min(birth) from customers";

            ScalarHandler handler = new ScalarHandler();

            Date count = (Date) runner.query(connection, sql, handler);

            System.out.println(count);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            JDBCUtils.closeResourse(connection,null);
        }
    }

    /*
     * 自定义ResultSetHandler的实现类
     */

    @Test
    public void testDIY(){
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getDruidConnection();

            String sql = "select id,name,email,birth from customers where id = ?";

            ResultSetHandler<Customers> handler = rs -> {
                if (rs.next()){
                    int id = rs.getInt("Id");
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    Date birth = rs.getDate("birth");

                    Customers customers = new Customers(id, name, email, birth);
                    return  customers;
                }
                return null;
            };

            Customers customers = runner.query(connection, sql, handler, 5);
            System.out.println(customers);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            DbUtils.closeQuietly(connection);
        }
    }



}
