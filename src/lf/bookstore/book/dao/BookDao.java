package lf.bookstore.book.dao;

import cn.itcast.jdbc.TxQueryRunner;
import lf.bookstore.book.domain.Book;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class BookDao {
    private QueryRunner qr=new TxQueryRunner();

    /**
     * 按分类查询图书
     * @param cid
     * @return
     */
    public List<Book> findbookByCid(String cid) {
        String sql="SELECT * FROM Book WHERE cid=? AND del=FALSE";
        try {
            return qr.query(sql,new BeanListHandler<Book>(Book.class),cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询所有图书
     * @return
     */
    public List<Book> findAll() {
        String sql="SELECT * FROM Book WHERE del=FALSE";
        try {
            return qr.query(sql,new BeanListHandler<Book>(Book.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载图书
     * @param bid
     * @return
     */
    public Book findbookByBid(String bid) {
        String sql="SELECT * FROM Book WHERE bid=?";
        try {
            return qr.query(sql,new BeanHandler<Book>(Book.class),bid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询制定分类下的图书数量
     * @param cid
     * @return
     */
    public int getCountByCid(String cid) {
        String sql="SELECT COUNT(*) FROM Book WHERE cid=? AND del=FALSE";
        try {
            Number number= (Number) qr.query(sql,new ScalarHandler(),cid);
            return number.intValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除图书
     * @param bid
     */
    public void delete(String bid){
        try{
            String sql="UPDATE book SET del=true WHERE bid=?";
            qr.update(sql,bid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 编辑图书
     * @param book
     * @param cid
     */
    public void edit(Book book,String cid) {
        try{
            String sql="UPDATE book SET bname=?,price=?,author=?,cid=? WHERE bid=?";
            Object[] params={book.getBname(),book.getPrice(),book.getAuthor(),book.getCid(),book.getBid()};
            qr.update(sql,params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加图书
     * @param book
     */
    public void add(Book book) {
        try{
            String sql="INSERT INTO book (bid,bname,price,author,image,cid) VALUES(?,?,?,?,?,?)";
            Object[] params={book.getBid(),book.getBname(),book.getPrice(),
                    book.getAuthor(),book.getImage(),book.getCid()};
            qr.update(sql,params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
