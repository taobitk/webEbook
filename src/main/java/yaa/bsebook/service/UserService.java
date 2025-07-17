

// File: src/main/java/yaa/bsebook/service/UserService.java
package yaa.bsebook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yaa.bsebook.model.User;
import yaa.bsebook.model.UserPrinciple;
import yaa.bsebook.repository.UserRepository;

import java.util.List;

@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return UserPrinciple.build(user);
    }

    // --- TRIỂN KHAI CÁC PHƯƠNG THỨC MỚI ---
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void toggleUserLock(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setLocked(!user.isLocked()); // Đảo ngược trạng thái khóa
            userRepository.save(user);
        }
    }

    @Override
    public void addBalance(Integer userId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Số tiền nạp phải lớn hơn 0");
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setBalance(user.getBalance() + amount);
            userRepository.save(user);
        }
    }
}
