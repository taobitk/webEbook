// File: src/main/java/yaa/bsebook/repository/BookRepository.java
package yaa.bsebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yaa.bsebook.model.Book;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByAuthor_Id(Integer authorId);

    List<Book> findByTitleContainingIgnoreCase(String title);
}
