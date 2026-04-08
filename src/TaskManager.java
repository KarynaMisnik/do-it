package src;

import java.sql.*;
import java.util.ArrayList;

public class TaskManager {

    private final String url = "jdbc:mysql://localhost:3306/todo";
    private final String user = "root";
    private final String password = "";

    private ArrayList<Task> tasks = new ArrayList<>();

    // CREATE
    public void add(Task task) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String sql = "INSERT INTO tasks (title, done) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, task.getTitle());
            stmt.setBoolean(2, task.isDone());

            stmt.executeUpdate();

            // get generated ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                task.setId(rs.getInt(1));
            }

            tasks.add(task);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void remove(Task task) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String sql = "DELETE FROM tasks WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, task.getId());

            stmt.executeUpdate();

            tasks.remove(task);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    public ArrayList<Task> getTasks() {
        tasks.clear();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String sql = "SELECT * FROM tasks";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Task t = new Task(rs.getString("title"));
                t.setId(rs.getInt("id"));

                if (rs.getBoolean("done")) {
                    t.markDone();
                }

                tasks.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    // UPDATE (mark done / toggle)
    public void update(Task task) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String sql = "UPDATE tasks SET title=?, done=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, task.getTitle());
            stmt.setBoolean(2, task.isDone());
            stmt.setInt(3, task.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}