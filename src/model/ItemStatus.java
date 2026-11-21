package model;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * ItemStatus - custom cell renderer for the Status column in tables.
 * 
 * This class changes the background color of a table cell depending
 * on the text inside it:
 *   "Expired"     -> bright red
 *   "Near expiry" -> orange
 *   "Good"        -> green
 * 
 * How it works:
 * JTable asks this renderer "what should this cell look like?"
 * We look at the value, set the color, and return the component.
 * 
 * This is a common way in Swing to make tables more visual.
 */
public class ItemStatus extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        // Let the parent class create the basic cell first
        Component cell = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        // Center the text inside the cell
        setHorizontalAlignment(CENTER);

        // Only change color when the row is NOT selected
        // (selected rows usually have their own blue highlight)
        if (!isSelected) {
            String status = String.valueOf(value);

            if (status.equals("Expired")) {
                cell.setBackground(new Color(255, 40, 40));   // red
            } else if (status.equals("Near expiry")) {
                cell.setBackground(new Color(255, 165, 0));   // orange
            } else {
                cell.setBackground(new Color(39, 213, 39));   // green
            }

            cell.setForeground(Color.BLACK);  // text stays black for readability
        }

        return cell;
    }
}
