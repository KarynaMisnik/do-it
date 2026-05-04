package src;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import src.Category;
import src.User;
import src.Comment;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private boolean done;

    // User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Categories
    @ManyToMany
    @JoinTable(name = "task_category", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    // Comments
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Task() {
    } // REQUIRED by Hibernate

    public Task(String title) {
        this.title = title;
        this.done = false;
    }

    public void markDone() {
        this.done = true;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return done;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setTask(this);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void toggleDone() {
        done = !done;
    }
}