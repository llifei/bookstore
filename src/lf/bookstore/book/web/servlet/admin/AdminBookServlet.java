package lf.bookstore.book.web.servlet.admin;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import lf.bookstore.book.domain.Book;
import lf.bookstore.book.service.BookService;
import lf.bookstore.category.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/AdminBookServlet")
public class AdminBookServlet extends BaseServlet {
    private BookService bookService=new BookService();
    private CategoryService categoryService=new CategoryService();
    /**
     * 查询所有图书
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        1.调用service方法查询所有图书
        2.保存到request域
        3，返回 /adminjsps/admin/book/list.jsp
         */
        request.setAttribute("bookList",bookService.findAll());
        return "f:/adminjsps/admin/book/list.jsp";
    }

    /**
     * 加载图书
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String loadBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        1.获取bid
        2.调用bookService方法查询图书并返回
        3.调用categoryService方法查询图书的分类并返回
        3.保存到request域
        4.转发到 /adminjsps/admin/book/desc.jsp
         */
        String bid=request.getParameter("bid");
        Book book=bookService.findbookByBid(bid);
        request.setAttribute("book",book);
        request.setAttribute("category",categoryService.findByCid(book.getCid()));
        return "f:/adminjsps/admin/book/desc.jsp";
    }

    /**
     * 删除图书
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        1.得到bid
        2.调用service方法删除图书
        3.调用findAll方法
         */
        String bid=request.getParameter("bid");
        bookService.delete(bid);
        return findAll(request,response);
    }

    /**
     * 编辑图书
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        1.封装表单book,获取cid
        2.调用service方法编辑图书
        3.调用findAll方法
         */
        Book book= CommonUtils.toBean(request.getParameterMap(),Book.class);
        String cid=request.getParameter("cid");
        bookService.edit(book,cid);
        return findAll(request,response);
    }

    public String addPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        查询所有分类，保存到request域中，转发到add.jsp
         */
        request.setAttribute("categoryList",categoryService.findAll());
        return "f:/adminjsps/admin/book/add.jsp";
    }
}
