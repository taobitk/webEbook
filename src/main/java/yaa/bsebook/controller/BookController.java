package yaa.bsebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import yaa.bsebook.model.Book;
import yaa.bsebook.model.BookForm;
import yaa.bsebook.model.User;
import yaa.bsebook.model.UserPrinciple;
import yaa.bsebook.service.IBookService;
import yaa.bsebook.service.IUserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private IBookService bookService;
    @Autowired
    private IUserService userService;

    /**
     * Hiển thị form để tải sách mới lên.
     * Yêu cầu người dùng phải đăng nhập.
     */
    @GetMapping("/upload")
    public ModelAndView showUploadForm() {
        ModelAndView modelAndView = new ModelAndView("book-form");
        modelAndView.addObject("bookForm", new BookForm());
        modelAndView.addObject("formTitle", "Đăng tải sách mới");
        return modelAndView;
    }

    /**
     * Xử lý việc lưu sách mới hoặc cập nhật sách đã có.
     */
    @PostMapping("/save")
    public ModelAndView saveBook(@Valid @ModelAttribute BookForm bookForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // Nếu có lỗi validation, trả về lại form và hiển thị lỗi
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("book-form");
            // Giữ lại tiêu đề form cho đúng ngữ cảnh (Thêm mới/Chỉnh sửa)
            modelAndView.addObject("formTitle", bookForm.getId() == null ? "Đăng tải sách mới" : "Chỉnh sửa sách");
            return modelAndView;
        }

        // Lấy thông tin tác giả từ user đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User author = userService.findByUsername(currentUsername);

        // Gọi service để xử lý toàn bộ logic lưu sách
        bookService.save(bookForm, author);

        // Gửi thông báo thành công và chuyển hướng về trang quản lý sách
        redirectAttributes.addFlashAttribute("message", "Lưu sách thành công!");
        return new ModelAndView("redirect:/dashboard/my-books");
    }

    /**
     * Hiển thị trang chi tiết của một cuốn sách.
     */
    @GetMapping("/{id}")
    public ModelAndView showBookDetails(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("book-detail");
        Book book = bookService.findById(id);
        modelAndView.addObject("book", book);

        // Kiểm tra xem người dùng hiện tại có sở hữu sách hoặc là tác giả không
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean userOwnsThisBook = false;
        boolean isAuthor = false;

        // Chỉ kiểm tra nếu người dùng đã đăng nhập
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            Integer currentUserId = userPrinciple.getId();

            userOwnsThisBook = bookService.userOwnsBook(currentUserId, id);
            isAuthor = bookService.isAuthor(currentUserId, id);
        }
        modelAndView.addObject("userOwnsBook", userOwnsThisBook);
        modelAndView.addObject("isAuthor", isAuthor);

        return modelAndView;
    }

    @GetMapping("/buy/{id}")
    public String buyBook(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String) {
            return "redirect:/login"; // Yêu cầu đăng nhập nếu chưa
        }

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Integer currentUserId = userPrinciple.getId();

        try {
            bookService.purchaseBook(currentUserId, id);
            redirectAttributes.addFlashAttribute("successMessage", "Mua sách thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/books/" + id;
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("hasRole('ADMIN') or @bookService.userOwnsBook(principal.id, #id) or @bookService.isAuthor(principal.id, #id)")
    public ModelAndView readBook(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("read-book");
        Book book = bookService.findById(id);

        // --- SỬA LỖI Ở ĐÂY: Truyền toàn bộ đối tượng book sang view ---
        modelAndView.addObject("book", book);

        if (book != null) {
            modelAndView.addObject("bookTitle", book.getTitle());
            modelAndView.addObject("pdfUrl", book.getPdfFileUrl());
        } else {
            modelAndView.addObject("errorMessage", "Không tìm thấy sách.");
        }
        return modelAndView;
    }
    /**
     * Hiển thị form để chỉnh sửa sách.
     * Yêu cầu người dùng phải là ADMIN hoặc là tác giả của sách.
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or @bookService.isAuthor(principal.id, #id)")
    public ModelAndView showEditForm(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("book-form"); // Tái sử dụng form
        Book book = bookService.findById(id);

        // Chuyển từ Book sang BookForm để hiển thị
        BookForm bookForm = new BookForm();
        bookForm.setId(book.getId());
        bookForm.setTitle(book.getTitle());
        bookForm.setDescription(book.getDescription());
        bookForm.setPrice(book.getPrice());

        modelAndView.addObject("bookForm", bookForm);
        modelAndView.addObject("formTitle", "Chỉnh sửa sách");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or @bookService.isAuthor(principal.id, #id)")
    public String deleteBook(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa sách thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa sách.");
        }
        return "redirect:/dashboard/my-books";
    }
}
