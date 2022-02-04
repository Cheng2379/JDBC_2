package dao.bean;

import java.sql.Date;

public class Order {

    private int orderId;
    private String orderName;
    private Date orderDate;

    public Order() {
        super();
    }

    public Order(int id, String name, Date date) {
        this.orderId = id;
        this.orderName = name;
        this.orderDate = date;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order表数据: \n"+
                "id: " + orderId +
                ", name: " + orderName + '\'' +
                ", date: " + orderDate;
    }
}
