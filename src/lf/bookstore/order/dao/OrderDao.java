package lf.bookstore.order.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import lf.bookstore.book.domain.Book;
import lf.bookstore.cart.domain.Cart;
import lf.bookstore.cart.domain.CartItem;
import lf.bookstore.order.domain.Order;
import lf.bookstore.order.domain.OrderItem;
import lf.bookstore.order.service.OrderException;
import lf.bookstore.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Order持久层
 * @author lifei
 */
public class OrderDao {

    private QueryRunner qr= new TxQueryRunner();

    public Order add(User user, Cart cart) {
    /*
    1.添加order对象
     */
        //新建order对象
        Order order=new Order();

        //获取订单编号
       String oid=CommonUtils.uuid();
        order.setOid(oid);

        //设置订单状态
        order.setState(1);

        //获取订单时间
        Date date=new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String ordertime=sdf.format(date);
        order.setOrdertime(ordertime);

        //设置订单合计
        order.setTotal(cart.getTotal());

        //设置订单uid
        order.setUid(user.getUid());


        //数据库添加订单操作
        String sql="INSERT INTO orders VALUES(?,?,?,?,?,?)";
        Object[] params={oid,ordertime,cart.getTotal(),1,user.getUid(),""};
        try {
            qr.update(sql,params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        /*
        2.  添加orderItem
         */
        List<CartItem> cartItems= new ArrayList<CartItem>(cart.getCartItems());


        for(CartItem cartItem:cartItems) {


            //确定并设置iid
            String iid = CommonUtils.uuid();
            int count = cartItem.getCount();
            double subtotal = cartItem.getSubtotal();
            String bid = cartItem.getBook().getBid();

            sql = "INSERT INTO orderitem VALUES(?,?,?,?,?)";
            Object[] param = {iid, count, subtotal, oid, bid};

            try {
                qr.update(sql, param);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //为order对象添加它所有的订单条目
        loadOrderItem(order);

        //返回订单
        return order;

    }


    /**
     * 查询所有订单
     * @return
     */
    public List<Order> findByUid(String uid) {
        try {
            /*
            1.得到当前用户所有的订单
             */
            String sql="SELECT * FROM orders WHERE uid=?";
            List<Order> orders = qr.query(sql,new BeanListHandler<Order>(Order.class),uid);
            /*
            2.循环遍历每个订单，为其加载所有的订单条目
             */
            for(Order order:orders){
                loadOrderItem(order);//为order对象添加它所有的订单条目
            }
            /*
            3.返回订单列表
             */
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载指定订单的所有条目
     * @param order
     */
    private void loadOrderItem(Order order) {
        try {
            String sql= "SELECT * FROM orderitem i, book b WHERE i.bid=b.bid AND oid=?";
            List <Map<String,Object>> mapList=qr.query(sql,new MapListHandler(),order.getOid());

            List<OrderItem> orderItemList=toOrderItemList(mapList);
            order.setOrderItemList(orderItemList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
        List<OrderItem> orderItemList=new ArrayList<OrderItem>();
        for(Map<String,Object>map:mapList){
            OrderItem item=toOrderItem(map);
            orderItemList.add(item);
        }
        return orderItemList;
    }

    private OrderItem toOrderItem(Map<String, Object> map) {
        OrderItem orderItem = CommonUtils.toBean(map,OrderItem.class);
        Book book= CommonUtils.toBean(map,Book.class);
        orderItem.setBook(book);
        return orderItem;
    }

    /**
     * 加载订单
     * @param oid
     * @return
     */
    public Order findByOid(String oid) {
        try {
            /*
            1.得到当前订单
             */
            String sql="SELECT * FROM orders WHERE oid=?";
            Order order = qr.query(sql,new BeanHandler<Order>(Order.class),oid);
            /*
            2.为order对象添加它所有的订单条目
             */
            loadOrderItem(order);

            /*
            3.返回订单列表
             */
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取订单状态
     * @param oid
     * @return
     */
    public int getStateByOid(String oid) {
        String sql="SELECT state From orders WHERE oid=?";
        try {
            Number number= (Number) qr.query(sql,new ScalarHandler(),oid);
            return number.intValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改订单状态
     * @param oid
     * @param state
     */
    public void updateState(String oid,int state) throws OrderException {
        String sql="UPDATE orders SET state="+(state)+" WHERE oid=?";
        try {
            qr.update(sql,oid);
        } catch (SQLException e) {
            throw new OrderException();
        }
    }

    /**
     * 不能支付的支付功能
     * @param oid
     * @param address
     * @throws OrderException
     */
    public void pay(String oid, String address) throws OrderException {
        String sql="UPDATE orders SET address=? WHERE oid=?";
        try{
            qr.update(sql,address,oid);
        } catch (SQLException e) {
            throw new OrderException();
        }
    }

    /**
     * 删除订单
     * @param oid
     * @throws OrderException
     */
    public void deleteOrder(String oid) throws OrderException {

        try {
            String sql="DELETE FROM orderitem WHERE oid=?";
            qr.update(sql,oid);
            sql="DELETE FROM orders WHERE oid=?";
            qr.update(sql,oid);

        } catch (SQLException e) {
            throw new OrderException();
        }
    }

    /**
     * 管理员查询所有订单
     * @return
     */
    public List<Order> findAll() {
        String sql="SELECT * FROM orders";
        try {
            //获取所有订单
            List<Order>orderList= qr.query(sql,new BeanListHandler<Order>(Order.class));
            //为每一个订单加载其条目
            for(Order order:orderList){
                loadOrderItem(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 按订单状态查询
     * @param state
     * @return
     */
    public List<Order> findByState(String state) {
        String sql="SELECT * FROM orders WHERE state=?";
        try {
            //获取所有订单
            List<Order>orderList= qr.query(sql,new BeanListHandler<Order>(Order.class),Integer.parseInt(state));
            //为每一个订单加载其条目
            for(Order order:orderList){
                loadOrderItem(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
