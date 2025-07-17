// File: src/main/java/yaa/bsebook/controller/AdminController.java
package yaa.bsebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yaa.bsebook.model.Book;
import yaa.bsebook.model.User;
import yaa.bsebook.service.IBookService;
import yaa.bsebook.service.IUserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IBookService bookService;

    // ... các phương thức dashboard, user management giữ nguyên ...
    @GetMapping("/dashboard")
    public ModelAndView showAdminDashboard() {
        ModelAndView modelAndView = new ModelAndView("admin/dashboard");
        long totalUsers = userService.findAll().size();
        long totalBooks = bookService.findAll().size();
        modelAndView.addObject("totalUsers", totalUsers);
        modelAndView.addObject("totalBooks", totalBooks);
        return modelAndView;
    }

    @GetMapping("/users")
    public ModelAndView showUserList() {
        ModelAndView modelAndView = new ModelAndView("admin/user-list");
        List<User> users = userService.findAll();
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping("/users/toggle-lock/{id}")
    public String toggleUserLock(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        userService.toggleUserLock(id);
        redirectAttributes.addFlashAttribute("message", "Thay đổi trạng thái tài khoản thành công!");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/add-balance")
    public String addBalance(@RequestParam("userId") Integer userId, @RequestParam("amount") double amount, RedirectAttributes redirectAttributes) {
        try {
            userService.addBalance(userId, amount);
            redirectAttributes.addFlashAttribute("message", "Nạp tiền thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nạp tiền thất bại: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }


    /**
     * Hiển thị trang quản lý tất cả các sách cho Admin.
     */
    @GetMapping("/books")
    public ModelAndView showBookManagement() {
        ModelAndView modelAndView = new ModelAndView("admin/book-list");
        List<Book> allBooks = bookService.findAll();

        // --- THÊM LOG ĐỂ KIỂM TRA ---
        System.out.println("=========================================");
        System.out.println("Đã tìm thấy tổng cộng: " + allBooks.size() + " cuốn sách.");
        // In ra tên sách đầu tiên nếu có để kiểm tra
        if (!allBooks.isEmpty()) {
            System.out.println("Tên sách đầu tiên: " + allBooks.get(0).getTitle());
            if (allBooks.get(0).getAuthor() != null) {
                System.out.println("Tác giả sách đầu tiên: " + allBooks.get(0).getAuthor().getUsername());
            } else {
                System.out.println("LỖI: Sách đầu tiên không có thông tin tác giả (author is null).");
            }
        }
        System.out.println("=========================================");

        modelAndView.addObject("books", allBooks);
        return modelAndView;
    }
}
