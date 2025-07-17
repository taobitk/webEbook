// File: src/main/java/yaa/bsebook/controller/BookRestController.java
package yaa.bsebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yaa.bsebook.model.dto.BookDTO;
import yaa.bsebook.service.IBookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    @Autowired
    private IBookService bookService;

    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam("query") String query) {
        List<BookDTO> books = bookService.searchByTitle(query);
        return ResponseEntity.ok(books);
    }
}