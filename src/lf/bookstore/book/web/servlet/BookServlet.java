package lf.bookstore.book.web.servlet;

import cn.itcast.servlet.BaseServlet;
import lf.bookstore.book.service.BookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/BookServlet")
public class BookServlet extends BaseServlet {
    private BookService bookService=new BookService();

    /**
     * 按分类查询图书
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String findBookByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid=request.getParameter("cid");
        request.setAttribute("bookList",bookService.findbookByCid(cid));
        return "f:/jsps/book/list.jsp";
    }

    public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("bookList",bookService.findAll());
        return "f:/jsps/book/list.jsp";
    }

    public String findBookByBid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bid=request.getParameter("bid");
        request.setAttribute("book",bookService.findbookByBid(bid));
        return "f:/jsps/book/desc.jsp";
    }

}
