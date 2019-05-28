package lf.bookstore.order.service;

import lf.bookstore.cart.domain.Cart;
import lf.bookstore.cart.domain.CartItem;
import lf.bookstore.order.dao.OrderDao;
import lf.bookstore.order.domain.Order;
import lf.bookstore.order.domain.OrderItem;
import lf.bookstore.user.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Order业务层
 * @author lifei
 */
public class OrderService {

    private OrderDao orderDao=new OrderDao();

    /**
     * 添加订单
     * @param user
     * @param cart
     */
    public Order add(User user, Cart cart) {
        return orderDao.add(user,cart);
    }

    /**
     * 查询所有订单
     * @return
     */
    public List<Order> findByUid(String uid) {
        return orderDao.findByUid(uid);
    }


    public Order findByOid(String oid) {
        return orderDao.findByOid(oid);
    }

    public void confirm(String oid) throws OrderException {
        //获取订单状态
        int state=orderDao.getStateByOid(oid);
        //订单状态码不为3即不能确认收货，抛出异常
        if(state!=3) throw new OrderException("确认收货失败！");

        //修改订单状态
        orderDao.updateState(oid,4);

    }

    /**
     * 不能支付的支付功能
     * @param oid
     * @param address
     * @throws OrderException
     */
    public void pay(String oid, String address) throws OrderException {

        //订单状态不为1，说明不能付款，抛出异常
        if(orderDao.getStateByOid(oid)!=1)  throw new OrderException();

        //调用orderDao的pay方法
        try {
            orderDao.pay(oid, address);
        }catch (OrderException e){
            throw new OrderException();
        }

        //修改订单状态
        orderDao.updateState(oid,2);
    }

    /**
     * 删除订单
     * @param oid
     * @throws OrderException
     */
    public void deleteOrder(String oid) throws OrderException {
        try{
            orderDao.deleteOrder(oid);
        }catch (OrderException e){
            throw new OrderException("删除订单失败！");
        }
    }

    /**
     * 查询所有订单
     * @return
     */
    public List<Order> findAll() {
        return orderDao.findAll();
    }

    /**
     * 按状态查询订单
     * @param state
     * @return
     */
    public List<Order> findByState(String state) {
        return orderDao.findByState(state);
    }
}
