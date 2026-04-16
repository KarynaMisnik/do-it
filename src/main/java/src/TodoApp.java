package src;

import javax.swing.*;
import java.awt.*;

public class TodoApp {

    private TaskManager manager;
    private DefaultListModel<Task> listModel;

    public TodoApp() {

        manager = new TaskManager();

        listModel = new DefaultListModel<>();

        JFrame frame = new JFrame("Todo App (JDBC CRUD)");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // =========================
        // READ (SELECT from DB)
        // =========================
        loadTasksFromDatabase();

        JList<Task> taskList = new JList<>(listModel);
        taskList.setFixedCellHeight(32);
        taskList.setCellRenderer(new TaskRenderer());

        JScrollPane scrollPane = new JScrollPane(taskList);

        JTextField input = new JTextField();
        input.setFont(new Font("Roboto", Font.PLAIN, 32));

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton editButton = new JButton("Edit");

        editButton.setEnabled(false);

        Font btnFont = new Font("Roboto", Font.BOLD, 28);
        addButton.setFont(btnFont);
        deleteButton.setFont(btnFont);
        editButton.setFont(btnFont);

        // =========================
        // CREATE (INSERT)
        // =========================
        addButton.addActionListener(e -> {
            String text = input.getText().trim();

            if (!text.isEmpty()) {
                Task t = new Task(text);

                manager.add(t); // INSERT INTO DB
                listModel.addElement(t); // show in UI

                input.setText("");
            }
        });

        // =========================
        // DELETE (DELETE)
        // =========================
        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();

            if (index >= 0) {
                Task t = listModel.getElementAt(index);

                manager.remove(t); // DELETE FROM DB
                listModel.remove(index);
            }
        });

        // =========================
        // UPDATE (EDIT title)
        // =========================
        editButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();

            if (index >= 0) {
                Task t = listModel.getElementAt(index);

                String newTitle = JOptionPane.showInputDialog(
                        frame,
                        "Edit task:",
                        t.getTitle());

                if (newTitle != null && !newTitle.trim().isEmpty()) {
                    t.setTitle(newTitle.trim());

                    manager.update(t); // UPDATE DB
                    taskList.repaint();
                }
            }
        });

        // Toggle done (also UPDATE)
        taskList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int index = taskList.locationToIndex(e.getPoint());

                if (index >= 0) {
                    Task t = listModel.getElementAt(index);

                    t.toggleDone();

                    manager.update(t); // UPDATE DB (done state)
                    taskList.repaint();
                }
            }
        });

        // Enable buttons when item selected
        taskList.addListSelectionListener(e -> {
            boolean selected = taskList.getSelectedIndex() >= 0;
            deleteButton.setEnabled(selected);
            editButton.setEnabled(selected);
        });

        // Layout
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(input, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(editButton);

        bottomPanel.add(buttonsPanel, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // =========================
    // READ (SELECT helper method)
    // =========================
    private void loadTasksFromDatabase() {
        listModel.clear();

        for (Task t : manager.getTasks()) {
            listModel.addElement(t);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoApp::new);

    }
}