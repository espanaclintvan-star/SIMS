import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

class AcademicDataPage extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    enum PageType {
        ENROLLMENT,
        SCHEDULE
    }

    private static final Color NAVY = new Color(20, 42, 84);
    private static final Color BACKGROUND = new Color(244, 247, 252);
    private static final Color BLUE = new Color(39, 93, 171);
    private static final Color MUTED = new Color(94, 108, 128);

    private final String username;
    private final boolean administrator;
    private final PageType pageType;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JLabel summary;
    private final JLabel message;
    private final JButton actionButton;
    private final JTextField searchField;
    private final transient TableRowSorter<DefaultTableModel> sorter;
    private boolean sampleScheduleDisplayed;

    AcademicDataPage(String username, boolean administrator, PageType pageType) {
        this.username = username;
        this.administrator = administrator;
        this.pageType = pageType;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        String title = pageType == PageType.ENROLLMENT ? "Course enrollment" : "Class schedule";
        String subtitle = pageType == PageType.ENROLLMENT
                ? (administrator ? "Review student course registrations." : "Browse courses and review your registration status.")
                : (administrator ? "Review scheduled classes across the student body." : "Your scheduled classes, in date and time order.");

        JPanel pageHeader = new JPanel(new BorderLayout(8, 4));
        pageHeader.setBackground(NAVY);
        pageHeader.setBorder(BorderFactory.createEmptyBorder(22, 28, 20, 28));
        JLabel heading = new JLabel(title);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel description = new JLabel(subtitle);
        description.setForeground(new Color(216, 226, 242));
        description.setFont(new Font("Arial", Font.PLAIN, 13));
        JPanel headingText = new JPanel(new BorderLayout(0, 5));
        headingText.setOpaque(false);
        headingText.add(heading, BorderLayout.NORTH);
        headingText.add(description, BorderLayout.CENTER);
        pageHeader.add(headingText, BorderLayout.WEST);
        add(pageHeader, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));

        JPanel toolbar = new JPanel(new BorderLayout(12, 0));
        toolbar.setOpaque(false);
        summary = new JLabel("Loading...");
        summary.setFont(new Font("Arial", Font.BOLD, 14));
        summary.setForeground(NAVY);
        toolbar.add(summary, BorderLayout.WEST);

        JPanel controls = new JPanel(new BorderLayout(8, 0));
        controls.setOpaque(false);
        searchField = new JTextField();
        searchField.setToolTipText("Filter the table");
        searchField.setPreferredSize(new Dimension(190, 34));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(211, 219, 231)),
                BorderFactory.createEmptyBorder(4, 9, 4, 9)));
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        controls.add(searchField, BorderLayout.CENTER);
        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton, false);
        refreshButton.addActionListener(this);
        controls.add(refreshButton, BorderLayout.EAST);
        toolbar.add(controls, BorderLayout.EAST);
        content.add(toolbar, BorderLayout.NORTH);

        String[] columns = pageType == PageType.ENROLLMENT
                ? (administrator ? new String[] {"Student", "Course", "Status"}
                        : new String[] {"Course", "Status", "Course ID"})
                : (administrator ? new String[] {"Student", "Course", "Date and time"}
                        : new String[] {"Course", "Date and time"});
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setForeground(new Color(42, 53, 70));
        table.setBackground(Color.WHITE);
        table.setRowHeight(36);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(232, 237, 244));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setForeground(NAVY);
        table.getTableHeader().setBackground(new Color(237, 242, 249));
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
        sorter = new TableRowSorter<DefaultTableModel>(tableModel);
        table.setRowSorter(sorter);
        if (pageType == PageType.ENROLLMENT && !administrator) {
            table.removeColumn(table.getColumnModel().getColumn(2));
        }

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(222, 229, 239)));
        tableScroll.getViewport().setBackground(Color.WHITE);
        content.add(tableScroll, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout(12, 0));
        footer.setOpaque(false);
        message = new JLabel(" ");
        message.setForeground(MUTED);
        message.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.add(message, BorderLayout.CENTER);
        actionButton = new JButton("Enroll in selected course");
        styleButton(actionButton, true);
        actionButton.addActionListener(this);
        actionButton.setVisible(pageType == PageType.ENROLLMENT && !administrator);
        footer.add(actionButton, BorderLayout.EAST);
        content.add(footer, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                filterRows();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                filterRows();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                filterRows();
            }
        });
        loadData();
    }

    private void styleButton(JButton button, boolean primary) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusable(false);
        button.setForeground(primary ? Color.WHITE : NAVY);
        button.setBackground(primary ? BLUE : Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primary ? BLUE : new Color(211, 219, 231)),
                BorderFactory.createEmptyBorder(8, 13, 8, 13)));
    }

    private void filterRows() {
        String text = searchField.getText().trim();
        sorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text)));
    }

    private boolean loadData() {
        tableModel.setRowCount(0);
        message.setText(" ");
        sampleScheduleDisplayed = false;
        try (Connection connection = database.getConnection()) {
            if (pageType == PageType.ENROLLMENT) {
                if (administrator) {
                    loadAllEnrollments(connection);
                } else {
                    Integer studentId = findStudentId(connection);
                    if (studentId == null) {
                        showMissingStudent();
                        return false;
                    }
                    loadStudentCourses(connection, studentId);
                }
            } else if (administrator) {
                loadAllSchedules(connection);
            } else {
                Integer studentId = findStudentId(connection);
                if (studentId == null) {
                    showMissingStudent();
                    return false;
                }
                loadStudentSchedules(connection, studentId);
            }
            updateSummary();
            return true;
        } catch (SQLException exception) {
            summary.setText("Unable to load " + (pageType == PageType.ENROLLMENT ? "enrollments" : "schedule"));
            message.setForeground(new Color(170, 45, 45));
            message.setText("Database error: " + exception.getMessage());
            return false;
        }
    }

    private Integer findStudentId(Connection connection) throws SQLException {
        String sql = "SELECT s.student_id FROM StudentsInfo s "
                + "JOIN login l ON l.login_id = s.login_id WHERE l.username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet results = statement.executeQuery()) {
                return results.next() ? Integer.valueOf(results.getInt("student_id")) : null;
            }
        }
    }

    private void loadStudentCourses(Connection connection, int studentId) throws SQLException {
        String sql = "SELECT c.course_id, c.course_name, COALESCE(e.status, 'Available') AS status "
                + "FROM courses c LEFT JOIN enrollments e ON e.course_id = c.course_id "
                + "AND e.student_id = ? ORDER BY c.course_name";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    tableModel.addRow(new Object[] {
                            results.getString("course_name"),
                            results.getString("status"),
                            Integer.valueOf(results.getInt("course_id"))
                    });
                }
            }
        }
    }

    private void loadAllEnrollments(Connection connection) throws SQLException {
        String sql = "SELECT COALESCE(s.s_name, l.username, 'Student ' || e.student_id) AS student_name, "
                + "c.course_name, e.status FROM enrollments e "
                + "LEFT JOIN StudentsInfo s ON s.student_id = e.student_id "
                + "LEFT JOIN login l ON l.login_id = s.login_id "
                + "JOIN courses c ON c.course_id = e.course_id ORDER BY student_name, c.course_name";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                tableModel.addRow(new Object[] {
                        results.getString("student_name"),
                        results.getString("course_name"),
                        results.getString("status")
                });
            }
        }
    }

    private void loadStudentSchedules(Connection connection, int studentId) throws SQLException {
        String sql = "SELECT c.course_name, TO_CHAR(s.schedule, 'Dy, Mon DD, YYYY - HH12:MI AM') AS meeting_time "
                + "FROM schedules s JOIN courses c ON c.course_id = s.course_id "
                + "WHERE s.student_id = ? ORDER BY s.schedule";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    tableModel.addRow(new Object[] {
                            results.getString("course_name"),
                            results.getString("meeting_time")
                    });
                }
            }
        }
        if (tableModel.getRowCount() == 0) {
            addSampleSchedule();
        }
    }

    private void addSampleSchedule() {
        sampleScheduleDisplayed = true;
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "EEE, MMM d, yyyy - hh:mm a", Locale.ENGLISH);
        String[] courses = {
                "Programming Fundamentals",
                "Database Management Systems",
                "Web Development",
                "Computer Networks"
        };
        String[] startTimes = {"08:00 AM", "10:00 AM", "01:00 PM", "03:00 PM"};
        for (int i = 0; i < courses.length; i++) {
            LocalDate classDate = nextMonday.plusDays(i);
            tableModel.addRow(new Object[] {
                    courses[i],
                    classDate.format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy", Locale.ENGLISH))
                            + " - " + startTimes[i]
            });
        }
    }

    private void loadAllSchedules(Connection connection) throws SQLException {
        String sql = "SELECT COALESCE(sinfo.s_name, l.username, 'Student ' || s.student_id) AS student_name, "
                + "c.course_name, TO_CHAR(s.schedule, 'Dy, Mon DD, YYYY - HH12:MI AM') AS meeting_time "
                + "FROM schedules s JOIN courses c ON c.course_id = s.course_id "
                + "LEFT JOIN StudentsInfo sinfo ON sinfo.student_id = s.student_id "
                + "LEFT JOIN login l ON l.login_id = sinfo.login_id ORDER BY s.schedule";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                tableModel.addRow(new Object[] {
                        results.getString("student_name"),
                        results.getString("course_name"),
                        results.getString("meeting_time")
                });
            }
        }
    }

    private void showMissingStudent() {
        summary.setText("Student record not found");
        message.setForeground(new Color(170, 95, 25));
        message.setText("This login is not linked to a student profile yet. Please contact an administrator.");
    }

    private void updateSummary() {
        int count = tableModel.getRowCount();
        String noun;
        if (pageType == PageType.ENROLLMENT) {
            noun = administrator
                    ? (count == 1 ? "registration" : "registrations")
                    : (count == 1 ? "course" : "courses");
        } else {
            noun = count == 1 ? "scheduled class" : "scheduled classes";
        }
        summary.setText(count + " " + noun);
        if (sampleScheduleDisplayed) {
            message.setText("Sample schedule matching the subjects shown in Grades. Saved schedule entries will replace this preview.");
        } else if (count == 0) {
            message.setText(pageType == PageType.ENROLLMENT
                    ? (administrator ? "No student registrations to show." : "No courses are available yet.")
                    : "No scheduled classes to show yet.");
        } else if (pageType == PageType.ENROLLMENT && !administrator) {
            message.setText("Select a course marked Available to request enrollment.");
        }
    }

    private void enrollInSelectedCourse() {
        int selectedViewRow = table.getSelectedRow();
        if (selectedViewRow < 0) {
            message.setForeground(new Color(170, 95, 25));
            message.setText("Select a course from the table first.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedViewRow);
        String status = String.valueOf(tableModel.getValueAt(modelRow, 1));
        if (!"Available".equalsIgnoreCase(status)) {
            message.setForeground(new Color(170, 95, 25));
            message.setText("You already have a registration for this course.");
            return;
        }

        int courseId = ((Integer) tableModel.getValueAt(modelRow, 2)).intValue();
        String sql = "INSERT INTO enrollments (student_id, status, course_id) "
                + "SELECT ?, 'Pending', ? WHERE NOT EXISTS "
                + "(SELECT 1 FROM enrollments WHERE student_id = ? AND course_id = ?)";
        boolean inserted;
        try (Connection connection = database.getConnection()) {
            Integer studentId = findStudentId(connection);
            if (studentId == null) {
                showMissingStudent();
                return;
            }
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, studentId.intValue());
                statement.setInt(2, courseId);
                statement.setInt(3, studentId.intValue());
                statement.setInt(4, courseId);
                inserted = statement.executeUpdate() == 1;
            }
        } catch (SQLException exception) {
            message.setForeground(new Color(170, 45, 45));
            message.setText("Could not submit registration: " + exception.getMessage());
            return;
        }
        if (!loadData()) {
            return;
        }
        message.setForeground(inserted ? new Color(38, 125, 75) : new Color(170, 95, 25));
        message.setText(inserted
                ? "Registration submitted. Its status is Pending."
                : "You already have a registration for this course.");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == actionButton) {
            enrollInSelectedCourse();
        } else {
            loadData();
        }
    }
}
