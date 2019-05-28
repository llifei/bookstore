package lf.bookstore.book.web.servlet.admin;

import cn.itcast.commons.CommonUtils;
import lf.bookstore.book.domain.Book;
import lf.bookstore.book.service.BookService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/AdminAddBookServlet")
public class AdminAddBookServlet extends HttpServlet {
    private BookService bookService=new BookService();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");

        /*
        1.把表单数据封装到Book对象中
            *上传三步
         */
        //创建工厂
        DiskFileItemFactory factory=new DiskFileItemFactory();
        //得到解析器
        ServletFileUpload sfu=new ServletFileUpload(factory);
        //使用解析器去解析request对象，得到List<FileItem>
        try{
            List<FileItem> fileItemList=sfu.parseRequest(req);
            /*
            1.把fileItemList中的数据封装到Book对象中
                >把所有的普通表单字段数据封装到Map中
                >再把map中的数据封装到Book对象中
             */
            Map<String,String>map=new HashMap<String,String>();
            for(FileItem fileItem:fileItemList){
                if(fileItem.isFormField()){
                    map.put(fileItem.getFieldName(),fileItem.getString("utf-8"));
                }
            }
            Book book= CommonUtils.toBean(map,Book.class);
            book.setBid(CommonUtils.uuid());
            //把map中category的数据封装到
            /*
            2.保存上传的文件
                >保存的路径
                >保存的文件名
             */
            //得到保存目录
            String savePath=this.getServletContext().getRealPath("/book_img");
            //得到文件名称：加uuid前缀，避免文件名冲突
            String filename=CommonUtils.uuid()+fileItemList.get(1).getName();
            //使用目录和文件名创建文件
            File destFile=new File(savePath,filename);
            //保存上传文件到目标位置
            fileItemList.get(1).write(destFile);

            /*
            3.设置Book对象的image，即把图片的路径设置给Book的image
             */
            book.setImage("book_img/"+filename);

            /*
            4.使用BookService完成保存
             */
            bookService.add(book);

            /*
            5.返回到图书列表
             */
            req.getRequestDispatcher("/admin/AdminBookServlet?method=findAll").forward(req,resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
