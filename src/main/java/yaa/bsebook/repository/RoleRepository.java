package yaa.bsebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yaa.bsebook.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Thêm phương thức này để tìm vai trò theo tên
    Role findByName(String name);
}