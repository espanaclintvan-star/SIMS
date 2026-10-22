import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;

public class Dashboard extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final Color NAVY = new Color(0, 0, 98);
    private static final Color PURPLE = new Color(120, 0, 98);
    private static final Color LIGHT_BACKGROUND = new Color(245, 247, 251);

    private static final String[] STUDENT_COLUMNS = { "Student ID", "Name", "Email", "Program", "Year" };
    private static final String[] GRADE_COLUMNS = { "Student ID", "Course", "Grade", "Term" };
    private static final String[] SCHEDULE_COLUMNS = { "Student ID", "Course", "Day", "Time", "Room" };
    private static final String[] ENROLLMENT_COLUMNS = { "Student ID", "Course", "Status", "Term" };

    private static final List<String[]> STUDENTS = new ArrayList<>();
    private static final List<String[]> GRADES = new ArrayList<>();
    private static final List<String[]> SCHEDULES = new ArrayList<>();
    private static final List<String[]> ENROLLMENTS = new ArrayList<>();

    private transient final List<Runnable> adminTableRefreshers = new ArrayList<>();
    static {
        STUDENTS.add(new String[] { "S-1001", "Jamie Smith", "student@example.com", "Computer Science", "2" });
        GRADES.add(new String[] { "S-1001", "Programming I", "A-", "Fall 2026" });
        GRADES.add(new String[] { "S-1001", "College Algebra", "B+", "Fall 2026" });
        SCHEDULES.add(new String[] { "S-1001", "Programming I", "Monday", "9:00 AM - 10:30 AM", "Room 204" });
        SCHEDULES.add(new String[] { "S-1001", "College Algebra", "Wednesday", "1:00 PM - 2:30 PM", "Room 118" });
        ENROLLMENTS.add(new String[] { "S-1001", "Programming I", "Enrolled", "Fall 2026" });
        ENROLLMENTS.add(new String[] { "S-1001", "College Algebra", "Enrolled", "Fall 2026" });
    }

    private final String email;
    private final boolean admin;

    public static boolean hasStudent(String email) {
        return findStudent(email) != null;
    }

    private static String[] findStudent(String email) {
        for (String[] student : STUDENTS) {
            if (student[2].equalsIgnoreCase(email.trim())) {
                return student;
            }
        }
        return null;
    }

    Dashboard(String email, boolean admin) {
        this.email = email;
        this.admin = admin;
        setTitle("Student Information Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(850, 560));
        setSize(1050, 680);
        setLocationRelativeTo(null);
        getContentPane().setBackground(LIGHT_BACKGROUND);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(admin ? createAdminWorkspace() : createStudentWorkspace(), BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 20));

        String welcome = admin ? "Administrator" : findStudent(email)[1];
        JLabel title = new JLabel("Welcome, " + welcome);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actions.setOpaque(false);
        JLabel role = new JLabel(admin ? "ADMIN" : "STUDENT");
        role.setForeground(Color.WHITE);
        role.setFont(new Font("SansSerif", Font.BOLD, 12));
        JButton logout = new JButton("Log out");
        styleButton(logout);
        logout.addActionListener(event -> {
            dispose();
            new Login();
        });
        actions.add(role);
        actions.add(logout);
        header.add(actions, BorderLayout.EAST);
        return header;
    }

    private Component createAdminWorkspace() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        tabs.addTab("Students", createManagementPanel("Students", STUDENT_COLUMNS, STUDENTS));
        tabs.addTab("Grades", createManagementPanel("Grades", GRADE_COLUMNS, GRADES));
        tabs.addTab("Schedules", createManagementPanel("Schedules", SCHEDULE_COLUMNS, SCHEDULES));
        tabs.addTab("Enrollment", createManagementPanel("Enrollment", ENROLLMENT_COLUMNS, ENROLLMENTS));
        return tabs;
    }

    private Component createStudentWorkspace() {
        String[] student = findStudent(email);
        String studentId = student[0];
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        tabs.addTab("My Information", createProfilePanel(student));
        tabs.addTab("Grades", createReadOnlyTable(GRADE_COLUMNS, GRADES, studentId));
        tabs.addTab("Schedule", createReadOnlyTable(SCHEDULE_COLUMNS, SCHEDULES, studentId));
        tabs.addTab("Enrollment", createReadOnlyTable(ENROLLMENT_COLUMNS, ENROLLMENTS, studentId));
        return tabs;
    }

    private JPanel createProfilePanel(String[] student) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(LIGHT_BACKGROUND);
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 228, 236)),
                BorderFactory.createEmptyBorder(22, 28, 22, 28)));

        String[] labels = { "Student ID", "Name", "Email", "Program", "Year" };
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(9, 8, 9, 16);
        constraints.anchor = GridBagConstraints.WEST;
        for (int i = 0; i < labels.length; i++) {
            constraints.gridx = 0;
            constraints.gridy = i;
            JLabel label = new JLabel(labels[i]);
            label.setForeground(new Color(90, 98, 112));
            card.add(label, constraints);

            constraints.gridx = 1;
            JLabel value = new JLabel(student[i]);
            value.setFont(new Font("SansSerif", Font.BOLD, 14));
            card.add(value, constraints);
        }
        panel.add(card);
        return panel;
    }

    private Component createReadOnlyTable(String[] columns, List<String[]> data, String studentId) {
        JTable table = createTable(columns, filteredRows(data, studentId));
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BACKGROUND);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createManagementPanel(String title, String[] columns, List<String[]> data) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(LIGHT_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 8, 8, 8));

        JLabel heading = new JLabel(title + " management");
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        heading.setForeground(NAVY);
        panel.add(heading, BorderLayout.NORTH);

        JTable table = createTable(columns, data);
        adminTableRefreshers.add(() -> refreshTable(table, columns, data));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        controls.setOpaque(false);
        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit selected");
        JButton remove = new JButton("Delete selected");
        styleButton(add);
        styleButton(edit);
        styleButton(remove);
        add.addActionListener(event -> {
            String[] values = showRowEditor(title, columns, null);
            if (values != null && validateRow(title, data, values, -1)) {
                data.add(values);
                refreshAdminTables();
            }
        });
        edit.addActionListener(event -> {
            int selected = table.getSelectedRow();
            if (selected < 0) {
                showSelectRowMessage();
                return;
            }
            String[] original = data.get(table.convertRowIndexToModel(selected));
            String[] values = showRowEditor(title, columns, original);
            if (values != null && validateRow(title, data, values, data.indexOf(original))) {
                if ("Students".equals(title) && !original[0].equals(values[0])) {
                    updateStudentReferences(original[0], values[0]);
                }
                data.set(data.indexOf(original), values);
                refreshAdminTables();
            }
        });
        remove.addActionListener(event -> {
            int selected = table.getSelectedRow();
            if (selected < 0) {
                showSelectRowMessage();
                return;
            }
            int confirmed = JOptionPane.showConfirmDialog(this, "Delete the selected " + title.toLowerCase() + " row?",
                    "Confirm deletion", JOptionPane.YES_NO_OPTION);
            if (confirmed == JOptionPane.YES_OPTION) {
                String[] row = data.remove(table.convertRowIndexToModel(selected));
                if ("Students".equals(title)) {
                    removeStudentReferences(row[0]);
                }
                refreshAdminTables();
            }
        });
        controls.add(add);
        controls.add(edit);
        controls.add(remove);
        panel.add(controls, BorderLayout.SOUTH);
        return panel;
    }

    private JTable createTable(String[] columns, List<String[]> rows) {
        DefaultTableModel model = new DefaultTableModel(rows.toArray(new Object[0][]), columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(235, 238, 245));
        return table;
    }

    private void refreshTable(JTable table, String[] columns, List<String[]> rows) {
        DefaultTableModel model = new DefaultTableModel(rows.toArray(new Object[0][]), columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
    }

    private void refreshAdminTables() {
        for (Runnable refresh : adminTableRefreshers) {
            refresh.run();
        }
    }

    private List<String[]> filteredRows(List<String[]> rows, String studentId) {
        List<String[]> matching = new ArrayList<>();
        for (String[] row : rows) {
            if (row[0].equals(studentId)) {
                matching.add(row);
            }
        }
        return matching;
    }

    private String[] showRowEditor(String title, String[] columns, String[] current) {
        JTextField[] fields = new JTextField[columns.length];
        JPanel form = new JPanel(new GridBagLayout());
        for (int i = 0; i < columns.length; i++) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = i;
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(5, 5, 5, 10);
            form.add(new JLabel(columns[i] + ":"), constraints);
            fields[i] = new JTextField(current == null ? "" : current[i], 20);
            constraints.gridx = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1;
            form.add(fields[i], constraints);
        }
        int result = JOptionPane.showConfirmDialog(this, form, (current == null ? "Add " : "Edit ") + title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return null;
        }
        String[] values = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            values[i] = fields[i].getText().trim();
        }
        return values;
    }

    private boolean validateRow(String title, List<String[]> data, String[] values, int ignoredIndex) {
        for (String value : values) {
            if (value.isEmpty()) {
                showError("Every field is required.");
                return false;
            }
        }
        if (!"Students".equals(title)) {
            if (findStudentById(values[0]) == null) {
                showError("Student ID " + values[0] + " does not exist. Add the student first.");
                return false;
            }
            return true;
        }
        for (int i = 0; i < data.size(); i++) {
            if (i != ignoredIndex && (data.get(i)[0].equalsIgnoreCase(values[0])
                    || data.get(i)[2].equalsIgnoreCase(values[2]))) {
                showError("Student IDs and email addresses must be unique.");
                return false;
            }
        }
        return true;
    }

    private String[] findStudentById(String studentId) {
        for (String[] student : STUDENTS) {
            if (student[0].equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    private void updateStudentReferences(String oldId, String newId) {
        updateReferences(GRADES, oldId, newId);
        updateReferences(SCHEDULES, oldId, newId);
        updateReferences(ENROLLMENTS, oldId, newId);
    }

    private void updateReferences(List<String[]> rows, String oldId, String newId) {
        for (String[] row : rows) {
            if (row[0].equals(oldId)) {
                row[0] = newId;
            }
        }
    }

    private void removeStudentReferences(String studentId) {
        removeReferences(GRADES, studentId);
        removeReferences(SCHEDULES, studentId);
        removeReferences(ENROLLMENTS, studentId);
    }

    private void removeReferences(List<String[]> rows, String studentId) {
        rows.removeIf(row -> row[0].equals(studentId));
    }

    private void showSelectRowMessage() {
        JOptionPane.showMessageDialog(this, "Select a row first.", "No row selected", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Unable to save", JOptionPane.ERROR_MESSAGE);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(PURPLE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
    }
}
