package src;

public class Task {
    private String title;
    private boolean done;

    public Task(String title) {
        this.title = title;
        this.done = false;
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

    @Override
    public String toString() {
        return (done ? "[x] " : "[ ] ") + title;
    }
}
