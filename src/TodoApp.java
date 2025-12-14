package src;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class TodoApp {

    private TaskManager manager;
    private DefaultListModel<Task> listModel;

    public TodoApp() {
        manager = new TaskManager();
        listModel = new DefaultListModel<>();

        try {
            manager.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Task t : manager.getTasks()) {
            listModel.addElement(t);
        }

        JFrame frame = new JFrame("Todo App");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JList<Task> taskList = new JList<>(listModel);
        taskList.setFixedCellHeight(32);
        JScrollPane scrollPane = new JScrollPane(taskList);
        JTextField input = new JTextField();
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton editButton = new JButton("Edit");
        editButton.setEnabled(false); // initially disabled
        taskList.setCellRenderer(new TaskRenderer());

        /* Swing "styling" */

        addButton.setBackground(new Color(0, 153, 0)); // green
        addButton.setForeground(Color.WHITE);

        editButton.setBackground(new Color(51, 153, 255)); // blue
        editButton.setForeground(Color.WHITE);

        deleteButton.setBackground(new Color(204, 0, 0)); // red
        deleteButton.setForeground(Color.WHITE);

        /* Fonts */
        Font btnFont = new Font("Roboto", Font.BOLD, 14);
        addButton.setFont(btnFont);
        editButton.setFont(btnFont);
        deleteButton.setFont(btnFont);

        /* Disable default style */

        addButton.setFocusPainted(false);
        editButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);

        addButton.setOpaque(true);
        editButton.setOpaque(true);
        deleteButton.setOpaque(true);

        taskList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int index = taskList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    Task t = listModel.getElementAt(index);
                    t.toggleDone();
                    taskList.repaint();
                    try {
                        manager.save();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        taskList.addListSelectionListener(e -> {
            boolean selected = taskList.getSelectedIndex() >= 0;
            deleteButton.setEnabled(selected);
            editButton.setEnabled(selected);
        });

        addButton.addActionListener(e -> {
            String text = input.getText().trim();
            if (!text.isEmpty()) {
                Task t = new Task(text);
                manager.add(t);
                listModel.addElement(t);
                input.setText("");
                try {
                    manager.save();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index >= 0) {
                Task t = listModel.getElementAt(index);
                manager.remove(t);
                listModel.remove(index);
                try {
                    manager.save();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

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
                    taskList.repaint();
                    try {
                        manager.save();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

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
