package transaction;

import bean.User;
import org.testng.annotations.Test;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库事务:
 * · 事务：一组逻辑操作单元,使数据从一种状态变换到另一种状态。**
 * · 事务处理（事务操作）：
 * 1.保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。
 *       当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，
 *       那么这些修改就永久地保存下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态。
 * 2.为确保数据库中数据的一致性，数据的操纵应当是离散的成组的逻辑单元：
 *       当它全部完成时，数据的一致性可以保持，而当这个单元中的一部分操作失败，
 *       整个事务应全部视为错误，所有从起始点以后的操作应全部回退到开始状态。
 * 3.数据一旦提交，就不可回滚
 * 4.哪些操作会导致数据的自动提交:
 *       >DDL操作一旦执行，都会自动提交
 *       >DML默认情况下，一旦执行，就会自动提交;可以通过set autocommit = false的方式取消DML操作的自动提交
 *       >默认在关闭连接时，会自动提交数据
 *
 *  事务的ACID属性:
 * 1. 原子性（Atomicity）:
 *      原子性是指事务是一个不可分割的工作单位，事务中的操作要么都发生，要么都不发生。
 * 2. 一致性（Consistency）:
 *    事务必须使数据库从一个一致性状态变换到另外一个一致性状态。
 * 3. 隔离性（Isolation）:
 *    事务的隔离性是指一个事务的执行不能被其他事务干扰，即一个事务内部的操作及使用的数据对并发的其他事务是隔离的，
 *    并发执行的各个事务之间不能互相干扰。
 * 4. 持久性（Durability）:
 *    持久性是指一个事务一旦被提交，它对数据库中数据的改变就是永久性的，接下来的其他操作和数据库故障不应该对其有任何影响。
 *
 *
 */

public class TransactionTest {

    /**
     * 针对于数据表user_table来说，AA用户给BB用户转账100
     */
    @Test
    public void testUpdate() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            connection.setAutoCommit(false);
            String sql = "update user_table set balance = balance - 100 where user = ?";
            JDBCUtils.update(connection,sql, "AA");

            //模拟网络异常
//            System.out.println(10 / 0);

            String sql2 = "update user_table set balance = balance + 100 where user = ?";
            JDBCUtils.update(connection,sql2, "BB");

            System.out.println("转账成功！");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            JDBCUtils.closeResourse(connection,null);
        }
    }

    @Test
    public void testTransactionSelect() throws Exception {

        Connection connection = JDBCUtils.getConnection();

        //获取当前的隔离级别
        System.out.println("当前隔离级别："+connection.getTransactionIsolation());

        //设置数据库的隔离级别
//        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//        System.out.println("更改后隔离级别："+connection.getTransactionIsolation());

        connection.setAutoCommit(false);

        String sql = "select user,password,balance from user_table where user = ?";

        User user = JDBCUtils.query(connection, User.class, sql, "CC");

        System.out.println(user);

    }

    @Test
    public void testTransactionUpdate() throws Exception {

        Connection connection = JDBCUtils.getConnection();

        //获取当前的隔离级别
        System.out.println(connection.getTransactionIsolation());

        //设置数据库的隔离级别
//        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
//        System.out.println("更改后隔离级别："+connection.getTransactionIsolation());

        connection.setAutoCommit(false);

        String sql = "update user_table set balance = ? where user = ?";
        JDBCUtils.update(connection,sql,5000,"CC");

        System.out.println("修改成功！");


    }



}
