package lf.bookstore.category.dao;

import cn.itcast.jdbc.TxQueryRunner;
import lf.bookstore.category.domain.Category;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * Category持久层
 */
public class CategoryDao {
    private QueryRunner qr=new TxQueryRunner();

    /**
     * 查询所有分类
     * @return
     */
    public List<Category> findAll() {
        String sql="SELECT * FROM category";
        try {
            return qr.query(sql,new BeanListHandler<Category>(Category.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加功能
     * @param category
     */
    public void add(Category category) {
        String sql="INSERT INTO category VALUES(?,?)";
        try {
             qr.update(sql,category.getCid(),category.getCname());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * cname查询分类
     * @param cname
     * @return
     */
    public Category finByCname(String cname) {
        String sql="SELECT * FROM category WHERE cname=?";
        try {
            return qr.query(sql,new BeanHandler<Category>(Category.class),cname);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除分类
     * @param cid
     */
    public void delete(String cid) {
        String sql="DELETE FROM category WHERE cid=?";
        try {
            qr.update(sql,cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * cid获取分类
     * @param cid
     * @return
     */
    public Category finByCid(String cid) {
        String sql="SELECT * FROM category WHERE cid=?";
        try {
            return qr.query(sql,new BeanHandler<Category>(Category.class),cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改分类
     * @param cid
     * @param cname
     */
    public void edit(String cid, String cname) {
        String sql="UPDATE category SET cname=? WHERE cid=?";
        try {
            qr.update(sql,cname,cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
