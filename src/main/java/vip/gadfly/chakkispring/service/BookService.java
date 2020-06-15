package vip.gadfly.chakkispring.service;

import vip.gadfly.chakkispring.model.BookDO;
import vip.gadfly.chakkispring.dto.book.CreateOrUpdateBookDTO;

import java.util.List;

public interface BookService {

    boolean createBook(CreateOrUpdateBookDTO validator);

    List<BookDO> getBookByKeyword(String q);

    boolean updateBook(BookDO book, CreateOrUpdateBookDTO validator);

    BookDO getById(Long id);

    List<BookDO> findAll();

    boolean deleteById(Long id);
}