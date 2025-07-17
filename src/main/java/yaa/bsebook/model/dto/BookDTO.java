// File: src/main/java/yaa/bsebook/model/dto/BookDTO.java
package yaa.bsebook.model.dto;
import yaa.bsebook.model.Book;


public class BookDTO {
    private Integer id;
    private String title;
    private String coverImageUrl;
    private String authorName;
    private double price;

    public BookDTO() {
    }

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.coverImageUrl = book.getCoverImageUrl();
        // Lấy tên tác giả một cách an toàn, tránh NullPointerException
        if (book.getAuthor() != null) {
            this.authorName = book.getAuthor().getUsername();
        } else {
            this.authorName = "Không rõ";
        }
        this.price = book.getPrice();
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

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}