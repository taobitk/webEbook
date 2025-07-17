// File: src/main/java/yaa/bsebook/service/IUserService.java
package yaa.bsebook.service;

import yaa.bsebook.model.User;
import java.util.List;

public interface IUserService {
    void save(User user);
    User findByUsername(String username);

    // --- CÁC PHƯƠNG THỨC MỚI CHO ADMIN ---
    List<User> findAll();
    void toggleUserLock(Integer userId);
    void addBalance(Integer userId, double amount);
}
