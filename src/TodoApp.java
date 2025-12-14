package src;

import javax.swing.*;
import java.awt.*;

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
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JList<Task> taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        JTextField input = new JTextField();
        JButton addButton = new JButton("Add");
        taskList.setCellRenderer(new TaskRenderer());

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

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(input, BorderLayout.CENTER);
        bottomPanel.add(addButton, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TodoApp::new);
    }
}
