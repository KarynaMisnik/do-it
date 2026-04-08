package src;

public class Task {
    private int id;
    private String title;
    private boolean done;

    public Task(String title) {
        this.title = title;
        this.done = false;
    }

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

    public void markDone() {
        done = true;
    }

    public void toggleDone() {
        done = !done;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return (done ? "[x] " : "[ ] ") + title;
    }
}
