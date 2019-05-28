package lf.bookstore.order.web.servlet.admin;

import cn.itcast.servlet.BaseServlet;
import lf.bookstore.order.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/AdminOrderServlet")
public class AdminOrderServlet extends BaseServlet {
    private OrderService orderService=new OrderService();

    public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        1.调用service方法查询所有订单
        2.保存到request
        3.转发到f:/adminjsps/admin/order/list.jsp
         */

        request.setAttribute("orderList",orderService.findAll());
        return "f:/adminjsps/admin/order/list.jsp";
    }

    public String findByState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        1.获取state
        2.调用service方法查询并返回订单
        3.保存到request
        4.转发到f:/adminjsps/admin/order/list.jsp
         */
        String state=request.getParameter("state");
        request.setAttribute("orderList",orderService.findByState(state));
        return "f:/adminjsps/admin/order/list.jsp";
    }
}
