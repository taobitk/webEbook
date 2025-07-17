// File: src/main/java/yaa/bsebook/security/CustomSuccessHandler.java
package yaa.bsebook.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * Xử lý việc chuyển hướng người dùng sau khi đăng nhập thành công.
 * Dựa trên vai trò (Role) của người dùng để quyết định trang đích.
 */
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            System.out.println("Can't redirect");
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    /**
     * Xác định URL đích dựa trên vai trò của người dùng.
     * @param authentication Đối tượng chứa thông tin xác thực của người dùng.
     * @return Chuỗi URL để chuyển hướng.
     */
    protected String determineTargetUrl(Authentication authentication) {
        String url;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Kiểm tra xem người dùng có vai trò ADMIN không
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // Nếu là ADMIN, chuyển hướng đến trang dashboard
            url = "/admin/dashboard";
        } else {
            // Nếu là USER thông thường, chuyển hướng về trang chủ
            url = "/home";
        }

        return url;
    }
}
