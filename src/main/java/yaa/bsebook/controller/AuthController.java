package yaa.bsebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yaa.bsebook.model.User;
import yaa.bsebook.service.IUserService;

@Controller
public class AuthController {

    @Autowired
    private IUserService userService;
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // --- THÊM PHƯƠNG THỨC HIỂN THỊ TRANG ĐĂNG KÝ ---
    @GetMapping("/register")
    public ModelAndView showRegisterPage() {
        // Gửi một đối tượng User trống sang view để Thymeleaf binding
        return new ModelAndView("register", "user", new User());
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        // Kiểm tra xem username đã tồn tại chưa
        if (userService.findByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Tên đăng nhập đã tồn tại!");
            return "redirect:/register";
        }
        // Lưu người dùng mới, gán vai trò và mã hóa mật khẩu
        userService.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/login";
    }
}