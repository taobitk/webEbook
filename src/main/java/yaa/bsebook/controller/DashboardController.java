// File: src/main/java/yaa/bsebook/controller/DashboardController.java
package yaa.bsebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import yaa.bsebook.model.Book;
import yaa.bsebook.model.UserPrinciple;
import yaa.bsebook.service.IBookService;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private IBookService bookService;
    @GetMapping("/my-books")
    public ModelAndView showMyBooks() {
        ModelAndView modelAndView = new ModelAndView("my-books");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Integer currentUserId = userPrinciple.getId();
        List<Book> myBooks = bookService.findByAuthor(currentUserId);

        modelAndView.addObject("myBooks", myBooks);
        return modelAndView;
    }
    @GetMapping("/user-info")
    public ModelAndView showUserInfo() {
        ModelAndView modelAndView = new ModelAndView("user-info");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Lấy đối tượng principal của người dùng đã xác thực
        if (authentication != null && authentication.getPrincipal() instanceof UserPrinciple) {
            UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
            // Đưa toàn bộ đối tượng người dùng vào model
            modelAndView.addObject("user", userDetails);
        }
        return modelAndView;
    }
}
