package yaa.bsebook.model;

import org.springframework.web.multipart.MultipartFile;

public class BookForm {
    // Thêm ID để có thể tái sử dụng form cho cả việc cập nhật
    private Integer id;
    private String title;
    private String description;
    private double price;
    private MultipartFile coverImageFile;
    private MultipartFile pdfContentFile;

    // Constructors
    public BookForm() {
    }

    // Constructor đầy đủ đã được cập nhật với id
    public BookForm(Integer id, String title, String description, double price, MultipartFile coverImageFile, MultipartFile pdfContentFile) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.coverImageFile = coverImageFile;
        this.pdfContentFile = pdfContentFile;
    }

    // Getters and Setters

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

    public MultipartFile getCoverImageFile() {
        return coverImageFile;
    }

    public void setCoverImageFile(MultipartFile coverImageFile) {
        this.coverImageFile = coverImageFile;
    }

    public MultipartFile getPdfContentFile() {
        return pdfContentFile;
    }

    public void setPdfContentFile(MultipartFile pdfContentFile) {
        this.pdfContentFile = pdfContentFile;
    }
}