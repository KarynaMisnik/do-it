package todo;

import jakarta.persistence.FetchType;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TaskRenderer extends JCheckBox implements ListCellRenderer<Task> {

        private static final Font FONT = new Font("Roboto", Font.PLAIN, 24);

        @Override
        public Component getListCellRendererComponent(
                        JList<? extends Task> list,
                        Task task,
                        int index,
                        boolean isSelected,
                        boolean cellHasFocus) {

                String priority = "NO PRIORITY";
                if (task.getPriority() != null && task.getPriority().getLevel() != null) {
                        priority = task.getPriority().getLevel();
                }

                String category = "NO CATEGORY";
                if (task.getCategories() != null && !task.getCategories().isEmpty()
                                && task.getCategories().get(0) != null) {
                        category = task.getCategories().get(0).getName();
                }

                int comments = 0;
                if (task.getComments() != null) {
                        comments = task.getComments().size();
                }

                String text = "[" + priority + "] [" + category + "] "
                                + task.getTitle() + " (" + comments + ")";

                setText(text);
                setText(task.isDone() ? "✔ " + text : text);

                setSelected(task.isDone());
                setFont(FONT);
                setBorder(new EmptyBorder(6, 6, 6, 6));

                if (isSelected) {
                        setBackground(list.getSelectionBackground());
                        setForeground(list.getSelectionForeground());
                } else {
                        setBackground(list.getBackground());
                        setForeground(list.getForeground());
                }

                setOpaque(true);
                return this;
        }
}