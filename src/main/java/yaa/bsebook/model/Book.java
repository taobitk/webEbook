package yaa.bsebook.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
// --- THÊM CÁC IMPORT CHO VALIDATION ---

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // --- THÊM QUY TẮC VALIDATION ---
    @NotBlank(message = "Tên sách không được để trống")
    @Size(min = 5, max = 200, message = "Tên sách phải có từ 5 đến 200 ký tự")
    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    // --- THÊM QUY TẮC VALIDATION ---
    @NotNull(message = "Giá bán không được để trống")
    @Min(value = 0, message = "Giá bán phải là một số không âm")
    private double price;

    // ... các trường và phương thức còn lại giữ nguyên
    @Temporal(TemporalType.DATE)
    @Column(name = "upload_date")
    private Date uploadDate;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "pdf_file_url")
    private String pdfFileUrl;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "book")
    private Set<UserBook> userBooks;

    // Getters and Setters...
    public Book() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getPdfFileUrl() {
        return pdfFileUrl;
    }

    public void setPdfFileUrl(String pdfFileUrl) {
        this.pdfFileUrl = pdfFileUrl;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<UserBook> getUserBooks() {
        return userBooks;
    }

    public void setUserBooks(Set<UserBook> userBooks) {
        this.userBooks = userBooks;
    }
}