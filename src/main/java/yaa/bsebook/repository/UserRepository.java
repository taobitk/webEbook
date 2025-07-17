package yaa.bsebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yaa.bsebook.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    // Spring Data JPA sẽ tự hiểu và tạo ra một phương thức
    // để tìm User theo username
    User findByUsername(String username);
}