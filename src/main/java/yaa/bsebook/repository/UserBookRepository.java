package yaa.bsebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yaa.bsebook.model.UserBook;

public interface UserBookRepository extends JpaRepository<UserBook, Integer> {
    // Phương thức quan trọng để kiểm tra quyền sở hữu
    // Trả về true nếu có bản ghi khớp với user và book
    boolean existsByUser_IdAndBook_Id(Integer userId, Integer bookId);
}