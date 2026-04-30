package src;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import src.Category;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Categories
    @ManyToMany
    @JoinTable(name = "task_category", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    public Task() {
    }

    public Task(String title) {
        this.title = title;
        this.done = false;
    }

    public void markDone() {
        this.done = true;
    }

    public void toggleDone() {
        done = !done;
    }

    // add new category
    public void addCategory(Category category) {
        categories.add(category);
    }

    // getters/setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return (done ? "[x] " : "[ ] ") + title;
    }
}