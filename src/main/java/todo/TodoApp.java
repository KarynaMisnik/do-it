package todo;

import javax.swing.*;
import java.awt.*;

public class TodoApp {

    private TaskManager manager;
    private DefaultListModel<Task> listModel;

    public TodoApp() {
        System.out.println("TodoApp starting ...");

        manager = new TaskManager();
        listModel = new DefaultListModel<>();

        JFrame frame = new JFrame("Todo App (JPA Hibernate CRUD)");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // =========================
        // READ FROM DB
        // =========================
        loadTasksFromDatabase();

        JList<Task> taskList = new JList<>(listModel);
        taskList.setFixedCellHeight(32);
        taskList.setCellRenderer(new TaskRenderer());

        JScrollPane scrollPane = new JScrollPane(taskList);

        // =========================
        // INPUT + PRIORITY
        // =========================
        JTextField input = new JTextField();
        input.setFont(new Font("Roboto", Font.PLAIN, 32));

        String[] priorities = { "LOW", "MEDIUM", "HIGH" };
        JComboBox<String> priorityBox = new JComboBox<>(priorities);
        priorityBox.setFont(new Font("Roboto", Font.BOLD, 18));

        // =========================
        // BUTTONS
        // =========================
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton editButton = new JButton("Edit");
        JButton commentButton = new JButton("Add Comment");

        Font btnFont = new Font("Roboto", Font.BOLD, 22);
        addButton.setFont(btnFont);
        deleteButton.setFont(btnFont);
        editButton.setFont(btnFont);
        commentButton.setFont(btnFont);

        editButton.setEnabled(false);

        // ===========================
        // CATEGORY
        // ===========================
        String[] categories = { "General", "Shopping", "Work", "Study" };
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setFont(new Font("Roboto", Font.BOLD, 18));

        // =========================
        // ADD TASK (WITH PRIORITY AND CATEGORY)
        // =========================
        addButton.addActionListener(e -> {

            String text = input.getText().trim();

            if (!text.isEmpty()) {

                Task t = new Task(text);

                // priority
                String selectedPriority = (String) priorityBox.getSelectedItem();
                t.setPriority(new Priority(selectedPriority));

                // category
                String selectedCategory = (String) categoryBox.getSelectedItem();
                t.addCategory(new Category(selectedCategory));

                manager.add(t);

                listModel.addElement(t);
                input.setText("");
            }
        });

        // =========================
        // DELETE
        // =========================
        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();

            if (index >= 0) {
                Task t = listModel.getElementAt(index);
                manager.remove(t);
                listModel.remove(index);
            }
        });

        // =========================
        // EDIT
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
                    manager.update(t);
                    taskList.repaint();
                }
            }
        });

        // =========================
        // COMMENTS
        // =========================
        commentButton.addActionListener(e -> {

            int index = taskList.getSelectedIndex();

            if (index >= 0) {
                Task t = listModel.getElementAt(index);

                String text = JOptionPane.showInputDialog(frame, "Enter comment:");

                if (text != null && !text.trim().isEmpty()) {
                    manager.addCommentToTask(t.getId(), text.trim());
                }
            }
        });

        // =========================
        // TOGGLE DONE
        // =========================
        taskList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                int index = taskList.locationToIndex(e.getPoint());

                if (index >= 0) {
                    Task t = listModel.getElementAt(index);

                    t.toggleDone();
                    manager.update(t);

                    taskList.repaint();
                }
            }
        });

        // =========================
        // ENABLE BUTTONS
        // =========================
        taskList.addListSelectionListener(e -> {
            boolean selected = taskList.getSelectedIndex() >= 0;
            deleteButton.setEnabled(selected);
            editButton.setEnabled(selected);
        });

        // =========================
        // LAYOUT
        // =========================
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout());

        JPanel rightPanel = new JPanel();
        rightPanel.add(priorityBox);
        rightPanel.add(categoryBox);

        inputPanel.add(input, BorderLayout.CENTER);
        inputPanel.add(rightPanel, BorderLayout.EAST);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(commentButton);

        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonsPanel, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        System.out.println("GUI ready to show");
        frame.setVisible(true);
    }

    // =========================
    // LOAD TASKS
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
