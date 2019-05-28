package lf.bookstore.user.dao;

import cn.itcast.jdbc.TxQueryRunner;
import lf.bookstore.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * User持久层
 * @author lifei
 */
public class UserDao {
    private QueryRunner qr=new TxQueryRunner();

    /**
     * 按用户名查询
     * @param username
     * @return
     */
    public User findByUsername(String username){
        try{
            String sql="SELECT * FROM tb_user WHERE username=?";
            return qr.query(sql,new BeanHandler<User>(User.class),username);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 按邮箱查询
     * @param email
     * @return
     */
    public User findByEmail(String email){
        try{
            String sql="SELECT * FROM tb_user WHERE email=?";
            return qr.query(sql,new BeanHandler<User>(User.class),email);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 按激活码查询
     * @param code
     * @return
     */
    public User findByCode(String code){
        String sql="SELECT * FROM tb_user WHERE code=?";
        try {
            return qr.query(sql,new BeanHandler<User>(User.class),code);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 插入User
     * @param user
     */
    public void add(User user){
        try{
            String sql="INSERT INTO tb_user VALUES(?,?,?,?,?,?)";
            Object[] params={user.getUid(),user.getUsername(),
                    user.getPassword(),user.getEmail(),
                    user.getCode(),user.isState()};
            qr.update(sql,params);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改用户激活状态
     * @param uid
     * @param state
     */
    public void updateState(String uid,boolean state){
        String sql="UPDATE tb_user SET state=? WHERE uid=?";
        try {
            qr.update(sql,state,uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
