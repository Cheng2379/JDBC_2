package dao.customersdao;

import dao.BaseDao;
import dao.bean.Customers;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomersDaoImpl extends BaseDao<Customers> implements CustomersDao {

    @Override
    public void insert(Connection connection, Customers customers) {
        String sql = "insert into customers(name,email,birth) values(?,?,?)";
        update(connection,sql,customers.getName(),customers.getEmail(),customers.getBirth());
    }

    @Override
    public void deleteById(Connection connection, int id) {
        String sql = "delete from customers where id = ?";
        update(connection,sql,id);
    }

    @Override
    public void update(Connection connection, Customers customers) {
        String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
        update(connection,sql,customers.getName(),customers.getEmail(),customers.getBirth(),customers.getId());
    }

    @Override
    public Customers getCustomersById(Connection connection, int id) {
        String sql = "select id,name,email,birth from customers where id = ?";
        Customers customers = query(connection, sql,id);
        return customers;
    }

    @Override
    public List<Customers> getAll(Connection connection) {
        String sql = "select id,name,email,birth from customers";
        List<Customers> list = queryList(connection, sql);
        return list;
    }

    @Override
    public Long getCount(Connection connection) {
        String sql = "select count(*) from customers";
        return getValue(connection, sql);
    }

    @Override
    public Date getMaxBirth(Connection connection) {
        String sql = "select Min(birth) from customers";
        return getValue(connection,sql);
    }
}
