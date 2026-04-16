package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

/*  Each task is shown as a checkbox
    If done → text is crossed out
    Swing reuses this component to draw each row
*/

public class TaskRenderer extends JCheckBox implements ListCellRenderer<Task> {
    private static final Font FONT = new Font("Roboto", Font.PLAIN, 32);

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Task> list,
            Task task,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        setText(task.getTitle());
        setSelected(task.isDone());
        setFont(FONT);
        setBorder(new EmptyBorder(6, 6, 6, 6));

        if (task.isDone()) {
            setText("<html><strike>" + task.getTitle() + "</strike></html>");
        }

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setEnabled(true);
        setOpaque(true);

        return this;
    }
}
