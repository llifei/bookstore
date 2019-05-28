package lf.bookstore.user.service;

import lf.bookstore.user.dao.UserDao;
import lf.bookstore.user.domain.User;

/**
 * User业务层
 * @author lifei
 */
public class UserService {
    private UserDao userDao=new UserDao();

    /**
     * 注册功能
     * @param form
     */
    public void regist(User form ) throws UserException {

        //校验用户名
        User user=userDao.findByUsername(form.getUsername());
        if(user!=null)  throw new UserException("用户名已被注册!");

        //校验邮箱
        user=userDao.findByEmail(form.getEmail());
        if(user!=null) throw new UserException("邮箱已被注册!");

        //插入用户到数据库
        userDao.add(form);
    }

    /**
     * 激活功能
     * @param code
     * @throws UserException
     */
    public void actice(String code) throws UserException {

        //使用code查询数据库得到User对象
        User user=userDao.findByCode(code);

        //如果数据库返回的是null，抛出异常
        if(user==null)  throw new UserException("链接已失效！");

        //查看用户的状态
        if(user.isState()) {
            throw new UserException("已激活无需再激活！");
        } else{
            userDao.updateState(user.getUid(), true);
        }

    }

    public User login(User form) throws UserException {

        User user=userDao.findByUsername(form.getUsername());
        if(user==null)  throw new UserException("用户不存在！");
        if(!user.getPassword().equals(form.getPassword()))  throw new UserException("用户名或密码错误！");
        if(!user.isState()) throw new UserException("用户邮箱未激活");

        return  user;
    }
}
