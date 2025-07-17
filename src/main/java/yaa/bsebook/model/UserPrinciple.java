
// File: src/main/java/yaa/bsebook/model/UserPrinciple.java
package yaa.bsebook.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrinciple implements UserDetails {
    // ... các trường cũ ...
    private Integer id;
    private String username;
    private String password;
    private double balance;
    private boolean locked; // Thêm trường locked
    private Collection<? extends GrantedAuthority> roles;

    public UserPrinciple(Integer id, String username, String password, double balance, boolean locked, Collection<? extends GrantedAuthority> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.locked = locked; // Gán giá trị
        this.roles = roles;
    }

    public static UserPrinciple build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new UserPrinciple(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getBalance(),
                user.isLocked(), // Lấy trạng thái locked từ User
                authorities
        );
    }

    // --- SỬA LẠI PHƯƠNG THỨC NÀY ---
    // Spring Security sẽ dùng phương thức này để kiểm tra tài khoản có bị khóa không
    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    // ... các phương thức và getter/setter khác giữ nguyên ...
    public double getBalance() { return balance; }
    public Integer getId() { return id; }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return roles; }
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return username; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
