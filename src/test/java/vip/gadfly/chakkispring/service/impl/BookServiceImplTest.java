package vip.gadfly.chakkispring.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import vip.gadfly.chakkispring.dto.book.CreateOrUpdateBookDTO;
import vip.gadfly.chakkispring.mapper.BookMapper;
import vip.gadfly.chakkispring.model.BookDO;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class BookServiceImplTest {

    private final String title = "千里之外";
    private final String author = "pedro";
    private final String image = "千里之外.png";
    private final String summary = "千里之外，是周杰伦和费玉清一起发售的歌曲";
    @Autowired
    private BookServiceImpl bookService;
    @Autowired
    private BookMapper bookMapper;

    @Test
    public void createBook() {

        CreateOrUpdateBookDTO validator = new CreateOrUpdateBookDTO();
        validator.setAuthor(author);
        validator.setImage(image);
        validator.setSummary(summary);
        validator.setTitle(title);
        bookService.createBook(validator);

        List<BookDO> books = bookMapper.selectByTitle(title);
        boolean anyMatch = books.stream().anyMatch(bo -> bo.getTitle().equals(title) && bo.getAuthor().equals(author));
        assertTrue(anyMatch);
    }

    @Test
    public void getBookByKeyword() {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(title);
        bookDO.setAuthor(author);
        bookDO.setImage(image);
        bookDO.setSummary(summary);
        bookMapper.insert(bookDO);

        List<BookDO> books = bookService.getBookByKeyword("%千里%");
        boolean anyMatch = books.stream().anyMatch(bo -> bo.getTitle().equals(title) && bo.getAuthor().equals(author));
        assertTrue(anyMatch);
    }

    @Test
    public void updateBook() {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(title);
        bookDO.setAuthor(author);
        bookDO.setImage(image);
        bookDO.setSummary(summary);
        bookMapper.insert(bookDO);

        String newTitle = "tttttttt";

        CreateOrUpdateBookDTO validator = new CreateOrUpdateBookDTO();
        validator.setAuthor(author);
        validator.setImage(image);
        validator.setSummary(summary);
        validator.setTitle(newTitle);

        BookDO found = bookMapper.selectById(bookDO.getId());
        bookService.updateBook(found, validator);

        BookDO found1 = bookMapper.selectById(bookDO.getId());
        assertEquals(found1.getTitle(), newTitle);
    }

    @Test
    public void deleteById() {
        BookDO bookDO = new BookDO();
        bookDO.setTitle(title);
        bookDO.setAuthor(author);
        bookDO.setImage(image);
        bookDO.setSummary(summary);
        bookMapper.insert(bookDO);

        bookService.deleteById(bookDO.getId());
        BookDO hit = bookService.getById(bookDO.getId());
        assertNull(hit);
    }
}