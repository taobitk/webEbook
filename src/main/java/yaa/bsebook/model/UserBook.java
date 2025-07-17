package yaa.bsebook.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_books")
public class UserBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Quan hệ Nhiều-Một với User (Nhiều bản ghi sở hữu thuộc về một User)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Quan hệ Nhiều-Một với Book (Nhiều bản ghi sở hữu trỏ đến một Book)
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Temporal(TemporalType.DATE)
    private Date purchaseDate;

    // Constructors
    public UserBook() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}