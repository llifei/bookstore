package lf.bookstore.user.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;
import lf.bookstore.cart.domain.Cart;
import lf.bookstore.user.domain.User;
import lf.bookstore.user.service.UserException;
import lf.bookstore.user.service.UserService;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User表述层
 * @author lifei
 */
@WebServlet("/UserServlet")
public class UserServlet extends BaseServlet {
    UserService userService = new UserService();

    /**
     * 激活功能
     *
     * @param req
     * @param res
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String active(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //获取激活码参数
        String code=req.getParameter("code");
        try {
            //调用userService的active方法
            userService.actice(code);
        }catch(UserException e){
            //激活失败保存信息并转发
            req.setAttribute("msg",e.getMessage());
            return "f:/jsps/msg.jsp";
        }
        //激活成功，保存成功信息并转发
        req.setAttribute("msg","已成功激活！");
        return "f:/jsps/msg.jsp";

    }

    /**
     * 注册功能
     *
     * @param req
     * @param res
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String regist(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        /*
        1. 封装表单到form对象中
        2. 补全：uuid、code
        3. 输入校验
            >保存错误信息、form到request域，转发到msg.jsp
        4. 调用service方法进行注册
            >保存错误信息、form到request域，转发到msg.jsp
        5.发送激活邮件
        6.保存成功信息转发到msg.jsp
         */
        //封装表单数据
        User form = CommonUtils.toBean(req.getParameterMap(), User.class);
        //补全uuid、code
        form.setUid(CommonUtils.uuid());
        form.setCode(CommonUtils.uuid() + CommonUtils.uuid());
        /*
        输入校验
        1. 创建一个Map，用来封装错误信息，其中key为表单字段名称，值为错误信息
         */
        Map<String, String> errors = new HashMap<String, String>();
        /*
        2.获取form中的username、password、email进行校验
         */
        String username = form.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "用户名不能为空!");
        } else if (username.length() < 3 || username.length() > 10) {
            errors.put("username", "用户名长度必须在3~10之间！");
        }

        String password = form.getPassword();
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "密码不能为空!");
        }

        String email = form.getEmail();
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "邮箱不能为空!");
        } else if (!email.matches("\\w+@\\w+\\.\\w+")) {
            errors.put("email", "邮箱格式错误！");
        }

        /*
        3.判断是否存在错误信息
         */
        if (errors.size() > 0) {
            //1.保存错误信息
            //2.保存表单数据
            //3.转发到regist.jsp
            req.setAttribute("errors", errors);
            req.setAttribute("form", form);
            return "f:/jsps/user/regist.jsp";
        }

        /*
        调用service的regist()方法
         */
        try {
            userService.regist(form);
        } catch (UserException e) {
            /*
            1.保存异常信息
            2.保存form
            3.转发到regist.jsp
             */
            req.setAttribute("msg", e.getMessage());
            req.setAttribute("form", form);
            return "f:/jsps/user/regist.jsp";
        }

            /*
            发邮件
            准备配置文件
             */
        //获取配置文件内容
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().
                getResourceAsStream("email_template.properties"));
        String host = props.getProperty("host");//获取服务器主机
        String uname = props.getProperty("uname");//获取用户名
        String pwd = props.getProperty("pwd");//获取密码
        String from = props.getProperty("from");//获取发件人
        String to = form.getEmail();//获取收件人
        String subject = props.getProperty("subject");//获取主题
        String content = props.getProperty("content");//获取邮件内容
        content = MessageFormat.format(content, form.getCode());//替换{0}

        Session session = MailUtils.createSession(host, uname, pwd);//得到Session
        Mail mail = new Mail(from, to, new String(subject.getBytes("ISO-8859-1"),"UTF-8"),
                new String(content.getBytes("ISO-8859-1"),"UTF-8"));//创建邮件对象
        try {
            MailUtils.send(session, mail);//发邮件
        } catch (MessagingException e) {
        }

            /*
            执行到这里，说明userService执行成功，没有异常
            1.保存成功信息
            2.转发到msg.jsp
             */
        req.setAttribute("msg", "注册成功！请到邮箱激活！");
        return "f:/jsps/msg.jsp";
    }

    /**
     * 登录功能
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //封装User表单对象
        User form=CommonUtils.toBean(request.getParameterMap(),User.class);
         /*
        输入校验
        1. 创建一个Map，用来封装错误信息，其中key为表单字段名称，值为错误信息
         */
        Map<String, String> errors = new HashMap<String, String>();
        /*
        2.获取form中的username、password、email进行校验
         */
        String username = form.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "用户名不能为空!");
        } else if (username.length() < 3 || username.length() > 10) {
            errors.put("username", "用户名长度必须在3~10之间！");
        }

        String password = form.getPassword();
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "密码不能为空!");
        }

        /*
        3.判断是否存在错误信息
         */
        if (errors.size() > 0) {
            //1.保存错误信息
            //2.保存表单数据
            //3.转发到regist.jsp
            request.setAttribute("errors", errors);
            request.setAttribute("form", form);
            return "f:/jsps/user/login.jsp";
        }

        //调用service方法完成登录
        User user;
        try{
            user=userService.login(form);
            request.getSession().setAttribute("User",user);

            /*
            给用户添加一个购物车，即向session中保存一个Cart对象
             */
            request.getSession().setAttribute("cart",new Cart());

            return "r:/index.jsp";
        }catch (UserException e){
            request.setAttribute("msg",e.getMessage());
            request.setAttribute("form",form);
            return "f:/jsps/user/login.jsp";
        }
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        return "r:/index.jsp";
    }
}