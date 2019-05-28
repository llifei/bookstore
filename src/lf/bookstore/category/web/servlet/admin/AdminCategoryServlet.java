package lf.bookstore.category.web.servlet.admin;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import lf.bookstore.category.domain.Category;
import lf.bookstore.category.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/AdminCategoryServlet")
public class AdminCategoryServlet extends BaseServlet {
    private CategoryService categoryService=new CategoryService();

    /**
     * 查询所有分类
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /*
        1.调用service方法，得到所有分类
        2.保存到request域，转发到 /adminjsps/admin/category/list.jsp
         */
        request.setAttribute("categoryList",categoryService.findAll());
        return "f:/adminjsps/admin/category/list.jsp";
    }

    /**
     * 添加功能
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        /*
        1.封装表单数据
        2.调用service()方法通过cname查询分类
            >如果返回不为null:保存错误信息到request域，转发到 /adminjsps/msg.jsp
        3.补全cid
        4.调用service方法完成添加工作
        5.调用findAll()
         */
        Category category= CommonUtils.toBean(request.getParameterMap(),Category.class);
        if(categoryService.findByCname(category.getCname())!=null){
            request.setAttribute("msg","分类已存在！不可重复添加！");
            return "f:/adminjsps/msg.jsp";
        }

        category.setCid(CommonUtils.uuid());
        categoryService.add(category);

        return findAll(request,response);
    }

    /**
     * 删除功能
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        /*
        1.获取cid参数
        2.调用service方法传递cid参数
            >如果抛出异常，保存异常信息，转发到msg.jsp
        3.调用findAll()方法
         */
        String cid=request.getParameter("cid");
        try{
            categoryService.delete(cid);
            return findAll(request,response);
        }catch (CategoryException e){
            request.setAttribute("msg",e.getMessage());
            return "f:/adminjsps/msg.jsp";
        }
    }

    /**
     * 修改功能之加载对象
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String editPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        /*
        1.获取cid
        2.调用service方法，得到Category对象
        3.保存到request域，转发到mod.jsp
         */
        String cid=request.getParameter("cid");
        Category category=categoryService.findByCid(cid);
        request.setAttribute("category",category);
        return"f:/adminjsps/admin/category/mod.jsp";
    }

    /**
     * 修改功能
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        /*
        1.获取cid与cname
        2.调用service方法修改分类
            >如果抛出异常，保存错误信息到request，转发到/adminjsps/msg.jsp
        3.返回findAll()
         */
        String cid=request.getParameter("cid");
        String cname=request.getParameter("cname");

        try{
            categoryService.edit(cid,cname);
        }catch (CategoryException e){
            request.setAttribute("msg",e.getMessage());
            return "f:/adminjsps/msg.jsp";
        }

        return findAll(request,response);
    }
}
