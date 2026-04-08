package src;

import javax.swing.*;
import java.awt.*;

public class TodoApp {

    private TaskManager manager;
    private DefaultListModel<Task> listModel;

    public TodoApp() {

        manager = new TaskManager();
        listModel = new DefaultListModel<>();

        // Load tasks from database
        for (Task t : manager.getTasks()) {
            listModel.addElement(t);
        }

        JFrame frame = new JFrame("Todo App");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Task list
        JList<Task> taskList = new JList<>(listModel);
        taskList.setFixedCellHeight(32);
        taskList.setCellRenderer(new TaskRenderer());

        JScrollPane scrollPane = new JScrollPane(taskList);

        // Input field
        JTextField input = new JTextField();
        input.setFont(new Font("Roboto", Font.PLAIN, 32));

        // Buttons
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton editButton = new JButton("Edit");

        editButton.setEnabled(false);

        // Styling
        Font btnFont = new Font("Roboto", Font.BOLD, 28);

        addButton.setFont(btnFont);
        deleteButton.setFont(btnFont);
        editButton.setFont(btnFont);

        addButton.setBackground(new Color(0, 153, 0));
        addButton.setForeground(Color.WHITE);

        deleteButton.setBackground(new Color(204, 0, 0));
        deleteButton.setForeground(Color.WHITE);

        editButton.setBackground(new Color(51, 153, 255));
        editButton.setForeground(Color.WHITE);

        addButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);
        editButton.setFocusPainted(false);

        addButton.setOpaque(true);
        deleteButton.setOpaque(true);
        editButton.setOpaque(true);

        // Toggle done on click
        taskList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int index = taskList.locationToIndex(e.getPoint());

                if (index >= 0) {
                    Task t = listModel.getElementAt(index);

                    t.toggleDone();
                    manager.update(t); // DB update

                    taskList.repaint();
                }
            }
        });

        // Enable/disable buttons
        taskList.addListSelectionListener(e -> {
            boolean selected = taskList.getSelectedIndex() >= 0;
            deleteButton.setEnabled(selected);
            editButton.setEnabled(selected);
        });

        // ADD TASK
        addButton.addActionListener(e -> {
            String text = input.getText().trim();

            if (!text.isEmpty()) {
                Task t = new Task(text);

                manager.add(t); // INSERT INTO DB
                listModel.addElement(t);

                input.setText("");
            }
        });

        // DELETE TASK
        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();

            if (index >= 0) {
                Task t = listModel.getElementAt(index);

                manager.remove(t); // DELETE FROM DB
                listModel.remove(index);
            }
        });

        // EDIT TASK
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

        // Bottom panel
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoApp::new);
    }
}