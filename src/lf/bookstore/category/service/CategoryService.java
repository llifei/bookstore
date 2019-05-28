package lf.bookstore.category.service;

import lf.bookstore.book.dao.BookDao;
import lf.bookstore.category.dao.CategoryDao;
import lf.bookstore.category.domain.Category;
import lf.bookstore.category.web.servlet.admin.CategoryException;

import java.util.List;

/**
 * Category业务层
 * @author lifei
 */
public class CategoryService {
    private CategoryDao categoryDao=new CategoryDao();
    private BookDao bookDao=new BookDao();
    /**
     * 查询所有分类
     * @return
     */
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    /**
     * 添加分类
     * @param category
     */
    public void add(Category category) {
        categoryDao.add(category);
    }

    /**
     * 通过cname查询分类
     * @param cname
     * @return
     */
    public Category findByCname(String cname) {
        return categoryDao.finByCname(cname);
    }

    /**
     * 删除分类
     * @param cid
     */
    public void delete(String cid) throws CategoryException {
        //获取该分类下图书的数量
        int count = bookDao.getCountByCid(cid);
        //如果该分类下存在图书，则不让删除，抛出异常
        if(count>0) throw new CategoryException("该分类下还有图书，不能删除！");
        //删除该分类
        categoryDao.delete(cid);

    }

    /**
     * 通过cid查询分类
     * @param cid
     * @return
     */
    public Category findByCid(String cid) {
        return categoryDao.finByCid(cid);
    }

    public void edit(String cid, String cname) throws CategoryException {
        /*
        1.调用findByCname方法返回Category对象
            >如果不为null，说明此类名已存在，不可修改，抛出异常
        2.调用categoryDao方法完成修改
         */
        if(findByCname(cname)!=null) throw new CategoryException("修改失败！此类名已存在！");
        categoryDao.edit(cid,cname);
    }
}
