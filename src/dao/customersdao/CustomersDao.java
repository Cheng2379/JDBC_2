package dao.customersdao;

import dao.bean.Customers;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * 此接口用于规范针对于customers表的常用操作
 */

public interface CustomersDao {

    //将Customers对象添加到数据库中(添加、插入数据)
    void insert(Connection connection, Customers customers);

    //根据指定的id，删除表中的一条记录
    void deleteById(Connection connection, int id);

    //针对内存中的Customers对象，取修改数据表中指定的记录
    void update(Connection connection,Customers customers);

    //根据指定的Id查询对应的Customers对象
    Customers getCustomersById(Connection connection,int id);

    //查询表中的所有记录构成的集合
    List<Customers> getAll(Connection connection);

    //返回数据中的数据的条目数
    Long getCount(Connection connection);

    //返回数据表中最大的生日
    Date getMaxBirth(Connection connection);


}
