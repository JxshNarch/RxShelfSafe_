package popups;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

// A small calendar window to pick a date (so nobody has to type 12/21/2026).
// Use it like this:   LocalDate picked = CalendarPopup.pick(this, LocalDate.now());
// It gives back the date the user clicked, or null if the window was closed.
public class CalendarPopup extends JDialog implements ActionListener {

    private YearMonth shown;              // the month that is on the screen now
    private LocalDate selected;           // the date that was picked before (it is shown in black)
    private LocalDate result = null;      // the date that the user clicked (null = nothing picked)

    private JLabel monthLabel;
    private JPanel grid;                  // the 7 x 7 table: the day names and the days

    // opens the calendar and waits until it is closed
    public static LocalDate pick(Component parent, LocalDate start) {
        Window owner = SwingUtilities.getWindowAncestor(parent);
        CalendarPopup calendar = new CalendarPopup(owner, start);
        calendar.setVisible(true);                 // the program waits here until the window is closed
        return calendar.result;
    }

    private CalendarPopup(Window owner, LocalDate start) {
        super(owner, "Pick a date", ModalityType.APPLICATION_MODAL);
        this.selected = start;
        this.shown = YearMonth.from(start);

        // top: << < Month Year > >>
        JButton prevYear = makeNavButton("<<", "prevYear");
        JButton prevMonth = makeNavButton("<", "prevMonth");
        JButton nextMonth = makeNavButton(">", "nextMonth");
        JButton nextYear = makeNavButton(">>", "nextYear");

        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        left.setBackground(Color.WHITE);
        left.add(prevYear);
        left.add(prevMonth);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        right.setBackground(Color.WHITE);
        right.add(nextMonth);
        right.add(nextYear);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        top.add(left, BorderLayout.WEST);
        top.add(monthLabel, BorderLayout.CENTER);
        top.add(right, BorderLayout.EAST);

        // middle: the days
        grid = new JPanel(new GridLayout(7, 7, 4, 4));
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // bottom: the Today button
        JButton todayButton = new JButton("Today");
        todayButton.setActionCommand("today");
        todayButton.setFont(new Font("Arial", Font.PLAIN, 16));
        todayButton.setPreferredSize(new Dimension(120, 35));
        todayButton.setBackground(Color.BLACK);
        todayButton.setForeground(Color.WHITE);
        todayButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        todayButton.setFocusPainted(false);
        todayButton.addActionListener(this);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 0, 12, 0));
        bottom.add(todayButton);

        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        add(top, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        draw();
        setSize(420, 430);                 // the size of the window (width, height)
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    // one small button of the top row
    private JButton makeNavButton(String text, String command) {
        JButton button = new JButton(text);
        button.setActionCommand(command);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(45, 30));
        button.setBackground(new Color(215, 215, 215));
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setFocusPainted(false);
        button.addActionListener(this);
        return button;
    }

    // fill the table with the days of the month that is shown
    private void draw() {
        monthLabel.setText(shown.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + shown.getYear());
        grid.removeAll();

        String[] names = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        for (int i = 0; i < names.length; i++) {
            JLabel name = new JLabel(names[i], SwingConstants.CENTER);
            name.setFont(new Font("Arial", Font.BOLD, 14));
            grid.add(name);
        }

        int blanks = shown.atDay(1).getDayOfWeek().getValue() % 7;     // Sunday = 0, Monday = 1 ...
        for (int i = 0; i < blanks; i++) {
            grid.add(new JLabel(""));
        }

        LocalDate today = LocalDate.now();
        for (int day = 1; day <= shown.lengthOfMonth(); day++) {
            LocalDate date = shown.atDay(day);
            JButton button = new JButton("" + day);
            button.setActionCommand("day" + day);
            button.setFont(new Font("Arial", Font.PLAIN, 15));
            button.setFocusPainted(false);
            button.addActionListener(this);
            if (date.equals(selected)) {
                button.setBackground(Color.BLACK);                         // the date that was picked before
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            } else if (date.equals(today)) {
                button.setBackground(Color.WHITE);                         // today
                button.setForeground(Color.BLACK);
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            } else {
                button.setBackground(new Color(240, 240, 240));
                button.setForeground(Color.BLACK);
                button.setBorder(BorderFactory.createLineBorder(new Color(190, 190, 190), 1));
            }
            grid.add(button);
        }

        int cells = 7 + blanks + shown.lengthOfMonth();
        for (int i = cells; i < 49; i++) {                                 // fill the rest so the table keeps its size
            grid.add(new JLabel(""));
        }

        grid.revalidate();
        grid.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("prevYear")) {
            shown = shown.minusYears(1);
            draw();
        } else if (command.equals("prevMonth")) {
            shown = shown.minusMonths(1);
            draw();
        } else if (command.equals("nextMonth")) {
            shown = shown.plusMonths(1);
            draw();
        } else if (command.equals("nextYear")) {
            shown = shown.plusYears(1);
            draw();
        } else if (command.equals("today")) {
            result = LocalDate.now();
            dispose();
        } else if (command.startsWith("day")) {
            int day = Integer.parseInt(command.substring(3));
            result = shown.atDay(day);                                     // the day that was clicked
            dispose();
        }
    }
}