package lf.bookstore.order.web.servlet;

import cn.itcast.servlet.BaseServlet;
import lf.bookstore.cart.domain.Cart;
import lf.bookstore.cart.domain.CartItem;

import lf.bookstore.order.domain.Order;
import lf.bookstore.order.service.OrderException;
import lf.bookstore.order.service.OrderService;
import lf.bookstore.user.domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Order表述层
 * @author lifei
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends BaseServlet {

    private OrderService orderService=new OrderService();

    /**
     * 新建订单
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取session中的cart和User
        Cart cart= (Cart) request.getSession().getAttribute("cart");
        User user= (User) request.getSession().getAttribute("User");

        //user==null说明未登录
        if(user==null){
            response.getWriter().print("请先登录！");
        }

        //获取收货地址
//        String address=request.getParameter("adress");

        //添加新订单
        Order order=orderService.add(user,cart);

        //清空购物车
        cart.clear();

        //保存order到request
        request.setAttribute("Order",order);

        return "f:/jsps/order/desc.jsp";
    }

    /**
     * 查询所有
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String myOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user=(User)request.getSession().getAttribute("User");
        if(user==null){
            response.getWriter().print("登陆信息失效，请重新登录！");
            return null;
        }
        request.setAttribute("orderList",orderService.findByUid(user.getUid()));

        return "f:/jsps/order/list.jsp";
    }

    /**
     * 加载详细订单
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String loadOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取订单编号
        String oid=request.getParameter("oid");

        request.setAttribute("Order",orderService.findByOid(oid));
        return "f:/jsps/order/desc.jsp";
    }

    /**
     * 仅仅添加收获地址，不能跳转银行页面
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public String pay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取订单号和收货地址
        String oid=request.getParameter("oid");
        String address=request.getParameter("address");
        try {
            orderService.pay(oid, address);
        }catch (OrderException e){
            response.getWriter().write("<script> alert('很遗憾！支付失败了!'); </script>");
            return null;
        }

        request.setAttribute("msg","就算你支付成功吧！");
        return "f:/jsps/msg.jsp";
    }

    /**
     * 确认收货
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String confirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String oid=request.getParameter("oid");

        try{
            orderService.confirm(oid);
        }catch (OrderException e){
            //出现异常
            request.setAttribute("msg",e.getMessage());
            return "f:/jsps/msg.jsp";
        }

        //确认成功
        request.setAttribute("msg","交易成功！");
        return "f:/jsps/msg.jsp";
    }

    public String cancerOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid=request.getParameter("oid");
        try{
            orderService.deleteOrder(oid);
        }catch (OrderException e){
            request.setAttribute("msg",e.getMessage());
            return "f:/jsps/msg.jsp";
        }

        return "f:/OrderServlet?method=myOrders";
    }
}
