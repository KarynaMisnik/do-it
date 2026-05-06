package todo;

import jakarta.persistence.*;
import todo.Category;
import todo.Comment;
import todo.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private boolean done;

    // User
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // Categories
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "task_category", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    // Comments
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    // Priority
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "priority_id")
    private Priority priority;

    // REQUIRED by Hibernate
    public Task() {
    }

    public Task(String title) {
        this.title = title;
        this.done = false;
    }

    // =========================
    // BASIC GETTERS / SETTERS
    // =========================

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    // =========================
    // RELATIONSHIPS
    // =========================

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setTask(this);
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    // =========================
    // LOGIC
    // =========================

    public void markDone() {
        this.done = true;
    }

    public void toggleDone() {
        this.done = !this.done;
    }
}