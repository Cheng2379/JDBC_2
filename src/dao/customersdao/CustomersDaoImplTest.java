package dao.customersdao;

import dao.bean.Customers;
import org.junit.jupiter.api.Test;
import utils.JDBCUtils;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

class CustomersDaoImplTest {

    private CustomersDaoImpl dao = new CustomersDaoImpl();

    @Test
    void insert() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Customers customers = new Customers(1, "2", "2@gmail.com", new Date(19970717));
            dao.insert(connection,customers);
            System.out.println("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(connection,null);
        }

    }

    @Test
    void deleteById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            dao.deleteById(connection,32);
            System.out.println("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(connection,null);
        }
    }

    @Test
    void update() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            Customers customers = new Customers(31,"girl","gl@gmail.com",new Date(20001020));

            dao.update(connection,customers);
            System.out.println("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(connection,null);
        }
    }

    @Test
    void getCustomersById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            Customers customers = dao.getCustomersById(connection, 10);

            System.out.println(customers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(connection,null);
        }
    }

    @Test
    void getAll() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            List<Customers> customersList = dao.getAll(connection);

            customersList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(connection,null);
        }
    }

    @Test
    void getCount() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            Long count = dao.getCount(connection);

            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(connection,null);
        }
    }

    @Test
    void getMaxBirth() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            Date maxBirth = dao.getMaxBirth(connection);

            System.out.println("最大生日："+maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResourse(connection,null);
        }
    }
}