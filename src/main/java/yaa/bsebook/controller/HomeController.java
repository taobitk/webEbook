package yaa.bsebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import yaa.bsebook.model.Book;
import yaa.bsebook.service.IBookService;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private IBookService bookService;

    @GetMapping({"/", "/home"})
    public ModelAndView showHomePage() {
        // 1. Tạo một đối tượng ModelAndView, chỉ định file view sẽ được sử dụng là "index.html"
        ModelAndView modelAndView = new ModelAndView("index");

        // 2. Gọi service để lấy danh sách tất cả các cuốn sách
        List<Book> books = bookService.findAll();

        // 3. Đưa danh sách sách vào model để truyền sang view
        modelAndView.addObject("books", books);

        // 4. Trả về đối tượng ModelAndView
        return modelAndView;
    }
    @GetMapping("/navbar")
    public ModelAndView navbar(){
        ModelAndView modelAndView = new ModelAndView("fragments/_navbar.html");
        return modelAndView;
    }
}