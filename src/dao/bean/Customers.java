package dao.bean;

import java.sql.Date;

/**
 * ORM编程思想：(Object Relational Mapping) 对象关系映射
 * 一个数据表对应一个java类
 * 表中的一条记录对应Java类的一个对象
 * 表中的一个字段对应Java类的一个属性
 *
 */

public class Fruit {

    private int id;
    private String name;
    private String email;
    private Date birth;

    public Fruit() {
        super();
    }

    public Fruit(int id, String name, String email, Date birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "Customers  [" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                ']';
    }
}
