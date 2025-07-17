// File: src/main/java/yaa/bsebook/configuration/AppInitializer.java
package yaa.bsebook.configuration;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import yaa.bsebook.security.SecurityConfig; // Import lớp SecurityConfig của bạn

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        // Khai báo các lớp cấu hình chính của ứng dụng.
        // Chúng ta cần đăng ký cả AppConfiguration và SecurityConfig của chúng ta.
        return new Class[]{AppConfiguration.class, SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    // --- CẤU HÌNH FILE UPLOAD ---
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        long maxFileSize = 1073741824L; // 1GB
        long maxRequestSize = 1073741824L; // 1GB
        int fileSizeThreshold = 0;
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(null, maxFileSize, maxRequestSize, fileSizeThreshold);
        registration.setMultipartConfig(multipartConfigElement);
    }

    // --- BỘ LỌC ĐỂ SỬA LỖI TIẾNG VIỆT ---
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[]{characterEncodingFilter};
    }
}
