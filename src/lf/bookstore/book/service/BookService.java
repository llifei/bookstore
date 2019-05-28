package lf.bookstore.book.service;

import lf.bookstore.book.dao.BookDao;
import lf.bookstore.book.domain.Book;

import java.util.List;

public class BookService {
    private BookDao bookDao=new BookDao();

    /**
     * 按分类查询图书
     * @param cid
     * @return
     */
    public List<Book> findbookByCid(String cid) {
        return bookDao.findbookByCid(cid);
    }

    /**
     * 查询所有图书
     * @return
     */
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    /**
     * 加载图书
     * @param bid
     * @return
     */
    public Book findbookByBid(String bid) {
        return (Book) bookDao.findbookByBid(bid);
    }

    /**
     * 删除图书
     * @param bid
     */
    public void delete(String bid){
        bookDao.delete(bid);
    }

    /**
     * 编辑图书
     * @param book
     * @param cid
     */
    public void edit(Book book, String cid) {
        bookDao.edit(book,cid);
    }

    /**
     * 添加图书
     * @param book
     */
    public void add(Book book) {
        bookDao.add(book);
    }
}
