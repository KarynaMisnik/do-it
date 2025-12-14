package src;

import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class TaskManager {

    /*
     * gson → the translator
     * file → where tasks live on disk
     */
    private final Gson gson = new Gson();
    private final Path file = Paths.get(System.getProperty("user.home"), ".todoapp", "tasks.json");

    private ArrayList<Task> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public void remove(Task task) {
        tasks.remove(task);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /*
     * Ensure folder exists
     * Convert tasks → JSON
     * Write to file
     */

    public void save() throws IOException {
        Files.createDirectories(file.getParent());
        try (Writer writer = Files.newBufferedWriter(file)) {
            gson.toJson(tasks, writer);
        }
    }

    /*
     * If file doesn’t exist → nothing to load
     * Read JSON
     * Convert text→
     * ArrayList<Task>
     * Replace in-memory list
     */
    public void load() throws IOException {
        if (Files.notExists(file))
            return;

        try (Reader reader = Files.newBufferedReader(file)) {
            Type type = new TypeToken<ArrayList<Task>>() {
            }.getType();
            ArrayList<Task> loaded = gson.fromJson(reader, type);

            if (loaded != null) {
                tasks.clear();
                tasks.addAll(loaded);
            }
        }
    }

}
