package lf.bookstore.user.web.filter;

import lf.bookstore.user.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.http.HttpResponse;

@WebFilter(urlPatterns={"/jsps/cart/*","/jsps/order/*"})
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /*
        1.从session中获取用户信息
        2.判断session中存在用户信息则放行
        3.否则，保存错误信息，转发到login.jsp
         */
        HttpServletRequest httpRequest= (HttpServletRequest) servletRequest;
        User user= (User) httpRequest.getSession().getAttribute("User");
        if(user!=null){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            httpRequest.setAttribute("msg","请先登录！");
            httpRequest.getRequestDispatcher("/jsps/user/login.jsp")
                    .forward(httpRequest, servletResponse);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
