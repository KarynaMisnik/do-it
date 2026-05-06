package todo;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "priority")
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String level; // Low, Medium, High

    @OneToMany(mappedBy = "priority")
    private List<Task> tasks;

    public Priority() {
    }

    public Priority(String level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}