package vip.gadfly.chakkispring.controller.v1;

import io.github.talelin.core.annotation.GroupMeta;
import vip.gadfly.chakkispring.model.BookDO;
import vip.gadfly.chakkispring.service.BookService;
import vip.gadfly.chakkispring.dto.book.CreateOrUpdateBookDTO;
import vip.gadfly.chakkispring.vo.UnifyResponseVO;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import vip.gadfly.chakkispring.common.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/v1/book")
@Validated
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/{id}")
    public BookDO getBook(@PathVariable(value = "id") @Positive(message = "{id}") Long id) {
        BookDO book = bookService.getById(id);
        if (book == null) {
            throw new NotFoundException("book not found", 10022);
        }
        return book;
    }

    @GetMapping("")
    public List<BookDO> getBooks() {
        List<BookDO> books = bookService.findAll();
        return books;
    }


    @GetMapping("/search")
    public List<BookDO> searchBook(@RequestParam(value = "q", required = false, defaultValue = "") String q) {
        List<BookDO> books = bookService.getBookByKeyword("%" + q + "%");
        return books;
    }


    @PostMapping("")
    public UnifyResponseVO createBook(@RequestBody @Validated CreateOrUpdateBookDTO validator) {
        bookService.createBook(validator);
        return ResponseUtil.generateUnifyResponse(10);
    }


    @PutMapping("/{id}")
    public UnifyResponseVO updateBook(@PathVariable("id") @Positive(message = "{id}") Long id, @RequestBody @Validated CreateOrUpdateBookDTO validator) {
        BookDO book = bookService.getById(id);
        if (book == null) {
            throw new NotFoundException("book not found", 10022);
        }
        bookService.updateBook(book, validator);
        return ResponseUtil.generateUnifyResponse(11);
    }


    @DeleteMapping("/{id}")
    @GroupMeta(permission = "删除图书", module = "图书", mount = true)
    public UnifyResponseVO deleteBook(@PathVariable("id") @Positive(message = "{id}") Long id) {
        BookDO book = bookService.getById(id);
        if (book == null) {
            throw new NotFoundException("book not found", 10022);
        }
        bookService.deleteById(book.getId());
        return ResponseUtil.generateUnifyResponse(12);
    }


}
