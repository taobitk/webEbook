
package yaa.bsebook.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private double balance;

    // --- THÊM TRƯỜNG MỚI ĐỂ QUẢN LÝ TRẠNG THÁI TÀI KHOẢN ---
    private boolean locked = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "author")
    private Set<Book> authoredBooks;

    @OneToMany(mappedBy = "user")
    private Set<UserBook> ownedUserBooks;

    public User() {
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
    public Set<Book> getAuthoredBooks() { return authoredBooks; }
    public void setAuthoredBooks(Set<Book> authoredBooks) { this.authoredBooks = authoredBooks; }
    public Set<UserBook> getOwnedUserBooks() { return ownedUserBooks; }
    public void setOwnedUserBooks(Set<UserBook> ownedUserBooks) { this.ownedUserBooks = ownedUserBooks; }
}
