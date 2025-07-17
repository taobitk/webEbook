// File: src/main/java/yaa/bsebook/security/SecurityConfig.java
package yaa.bsebook.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import yaa.bsebook.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // --- CÁC URL CÔNG KHAI ---
                .antMatchers("/", "/home", "/books/{id}", "/uploads/**", "/register", "/login", "/api/books/search").permitAll()
                // --- CÁC URL YÊU CẦU ĐĂNG NHẬP ---
                .antMatchers("/dashboard/**", "/books/upload").authenticated()
                // --- CÁC URL CHỈ DÀNH CHO ADMIN ---
                .antMatchers("/admin/**").hasRole("ADMIN")
                // --- MỌI REQUEST KHÁC ĐỀU CẦN XÁC THỰC ---
                .anyRequest().authenticated()
                .and()
                // --- CẤU HÌNH FORM ĐĂNG NHẬP TÙY CHỈNH ---
                .formLogin()
                .loginPage("/login") // Đường dẫn đến trang đăng nhập của bạn
                .loginProcessingUrl("/perform_login") // URL xử lý việc đăng nhập
                .successHandler(customSuccessHandler) // Xử lý sau khi đăng nhập thành công
                .failureUrl("/login?error=true") // URL khi đăng nhập thất bại
                .permitAll() // Cho phép tất cả mọi người truy cập trang đăng nhập
                .and()
                // --- CẤU HÌNH ĐĂNG XUẤT ---
                .logout()
                .logoutUrl("/logout") // URL để thực hiện đăng xuất
                .logoutSuccessUrl("/login?logout=true") // URL chuyển hướng sau khi đăng xuất thành công
                .permitAll()
                .and()
                .csrf().disable(); // Tắt CSRF để đơn giản hóa (có thể bật lại trong môi trường production)
    }
}