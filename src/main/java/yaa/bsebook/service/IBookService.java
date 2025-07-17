// File: src/main/java/yaa/bsebook/service/IBookService.java
package yaa.bsebook.service;

import yaa.bsebook.model.Book;
import yaa.bsebook.model.BookForm;
import yaa.bsebook.model.User;
import yaa.bsebook.model.dto.BookDTO;

import java.util.List;


public interface IBookService {

    List<Book> findAll();
    Book findById(Integer id);
    void save(BookForm bookForm, User author);
    void deleteById(Integer id);
    boolean userOwnsBook(Integer userId, Integer bookId);
    void purchaseBook(Integer userId, Integer bookId);
    boolean isAuthor(Integer userId, Integer bookId);
    List<Book> findByAuthor(Integer authorId);

    // --- THÊM PHƯƠNG THỨC TÌM KIẾM ---
    List<BookDTO> searchByTitle(String title);
}