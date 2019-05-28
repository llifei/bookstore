package lf.bookstore.cart.web.servlet;

import cn.itcast.servlet.BaseServlet;
import lf.bookstore.book.domain.Book;
import lf.bookstore.book.service.BookService;
import lf.bookstore.cart.domain.Cart;
import lf.bookstore.cart.domain.CartItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/CartServlet")
public class CartServlet extends BaseServlet {


    /**
     * 添加购物条目
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        1.得到购物车
        2.得到条目（图书和数量）
        3.把条目添加到车中
         */
        /*
        1.得到车
         */
        Cart cart= (Cart) request.getSession().getAttribute("cart");
        //若session中不存在cart，则转发到login.jsp
        if(cart==null){
            response.getWriter().print("请先登录！");
            return null;
        }

        /*
        表单传递的只有数量和bid
        2.得到条目
            得到图书和数量
            先得到图书的bid，然后我们需要通过bid查询数据库得到Book
            数量表单中有
         */
        String bid=request.getParameter("bid");
        Book book= new BookService().findbookByBid(bid);
        int count=Integer.parseInt(request.getParameter("count"));

        CartItem cartItem=new CartItem();
        cartItem.setBook(book);
        cartItem.setCount(count);
        /*
        3.把条目添加到车中
         */
        cart.add(cartItem);
        return "f:/jsps/cart/list.jsp";
    }

    /**
     * 清空购物条目
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //得到车
        Cart cart= (Cart) request.getSession().getAttribute("cart");
        //调用cart的clear
        cart.clear();

        return "f:/jsps/cart/list.jsp";
    }

    /**
     * 删除购物条目
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //得到车
        Cart cart= (Cart) request.getSession().getAttribute("cart");
        //得到要删除的bid
        String bid=request.getParameter("bid");
        //调用cart的delete
        cart.delete(bid);

        return "f:/jsps/cart/list.jsp";

    }

}

