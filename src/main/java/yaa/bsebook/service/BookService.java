package yaa.bsebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yaa.bsebook.model.Book;
import yaa.bsebook.model.BookForm;
import yaa.bsebook.model.User;
import yaa.bsebook.model.UserBook;
import yaa.bsebook.model.dto.BookDTO;
import yaa.bsebook.repository.BookRepository;
import yaa.bsebook.repository.UserBookRepository;
import yaa.bsebook.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService implements IBookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserBookRepository userBookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IStorageService storageService;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(BookForm bookForm, User author) {
        String coverImageUrl = null;
        if (bookForm.getCoverImageFile() != null && !bookForm.getCoverImageFile().isEmpty()) {
            coverImageUrl = storageService.store(bookForm.getCoverImageFile());
        }

        String pdfFileUrl = null;
        if (bookForm.getPdfContentFile() != null && !bookForm.getPdfContentFile().isEmpty()) {
            pdfFileUrl = storageService.store(bookForm.getPdfContentFile());
        }


        Book book;
        if (bookForm.getId() != null) {
            // Cập nhật sách đã có
            book = bookRepository.findById(bookForm.getId()).orElse(new Book());
        } else {
            // Tạo sách mới
            book = new Book();
            book.setUploadDate(new Date());
        }

        book.setTitle(bookForm.getTitle());
        book.setDescription(bookForm.getDescription());
        book.setPrice(bookForm.getPrice());
        book.setAuthor(author);

        // Chỉ cập nhật ảnh nếu có file mới được tải lên
        if (coverImageUrl != null) {
            book.setCoverImageUrl(coverImageUrl);
        }
        // Chỉ cập nhật file PDF nếu có file mới được tải lên
        if (pdfFileUrl != null) {
            book.setPdfFileUrl(pdfFileUrl);
        }

        bookRepository.save(book);
    }

    @Override
    public void deleteById(Integer id) {
        bookRepository.deleteById(id);
    }

    @Override
    public boolean userOwnsBook(Integer userId, Integer bookId) {
        if (userId == null || bookId == null) {
            return false;
        }
        return userBookRepository.existsByUser_IdAndBook_Id(userId, bookId);
    }

    @Override
    @Transactional
    public void purchaseBook(Integer userId, Integer bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với ID: " + bookId));

        if (userOwnsBook(userId, bookId)) {
            throw new RuntimeException("Bạn đã sở hữu cuốn sách này rồi.");
        }

        if (user.getBalance() < book.getPrice()) {
            throw new RuntimeException("Tài khoản của bạn không đủ để thực hiện giao dịch.");
        }

        user.setBalance(user.getBalance() - book.getPrice());
        userRepository.save(user);

        UserBook userBook = new UserBook();
        userBook.setUser(user);
        userBook.setBook(book);
        userBook.setPurchaseDate(new Date());
        userBookRepository.save(userBook);
    }

    @Override
    public boolean isAuthor(Integer userId, Integer bookId) {
        if (userId == null || bookId == null) {
            return false;
        }
        Book book = bookRepository.findById(bookId).orElse(null);
        return book != null && book.getAuthor().getId().equals(userId);
    }

    @Override
    public List<Book> findByAuthor(Integer authorId) {
        return bookRepository.findAllByAuthor_Id(authorId);
    }

    @Override
    public List<BookDTO> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());
    }
}