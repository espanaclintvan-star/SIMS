import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class SettingsPage extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color NAVY = new Color(20, 42, 84);
    private static final Color BACKGROUND = new Color(244, 247, 252);
    private static final Color BLUE = new Color(39, 93, 171);

    private final boolean administrator;
    private final String username;
    private final JLabel message;
    private DefaultTableModel courseModel;
    private JTable courseTable;
    private JTextField courseNameField;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    SettingsPage(boolean administrator, String username) {
        this.administrator = administrator;
        this.username = username;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(22, 28, 20, 28));
        JLabel title = new JLabel("Settings");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel subtitle = new JLabel(administrator
                ? "Manage the course catalog used for student enrollment."
                : "Manage your SIMS account security.");
        subtitle.setForeground(new Color(216, 226, 242));
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        JPanel heading = new JPanel(new BorderLayout(0, 5));
        heading.setOpaque(false);
        heading.add(title, BorderLayout.NORTH);
        heading.add(subtitle, BorderLayout.CENTER);
        header.add(heading, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        message = new JLabel(" ");
        message.setFont(new Font("Arial", Font.PLAIN, 12));
        message.setForeground(new Color(94, 108, 128));
        if (administrator) {
            add(createCourseSettings(), BorderLayout.CENTER);
        } else {
            add(createStudentSettings(), BorderLayout.CENTER);
        }

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BACKGROUND);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 28, 18, 28));
        footer.add(message, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createCourseSettings() {
        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(22, 28, 16, 28));

        JPanel description = new JPanel(new BorderLayout(0, 5));
        description.setOpaque(false);
        JLabel sectionTitle = new JLabel("Course catalog");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 17));
        sectionTitle.setForeground(NAVY);
        JLabel sectionDescription = new JLabel(
                "Add subjects students can request. Courses already used in grades, enrollments, or schedules cannot be removed.");
        sectionDescription.setFont(new Font("Arial", Font.PLAIN, 12));
        sectionDescription.setForeground(new Color(94, 108, 128));
        description.add(sectionTitle, BorderLayout.NORTH);
        description.add(sectionDescription, BorderLayout.CENTER);
        content.add(description, BorderLayout.NORTH);

        courseModel = new DefaultTableModel(new String[] {"Course", "Course ID"}, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courseTable = new JTable(courseModel);
        courseTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        courseTable.setFont(new Font("Arial", Font.PLAIN, 13));
        courseTable.setRowHeight(34);
        courseTable.setShowVerticalLines(false);
        courseTable.setFillsViewportHeight(true);
        courseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        courseTable.getTableHeader().setForeground(NAVY);
        courseTable.getTableHeader().setBackground(new Color(237, 242, 249));
        courseTable.getTableHeader().setPreferredSize(new Dimension(0, 36));
        courseTable.removeColumn(courseTable.getColumnModel().getColumn(1));
        JScrollPane tableScroll = new JScrollPane(courseTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(222, 229, 239)));
        tableScroll.getViewport().setBackground(Color.WHITE);
        content.add(tableScroll, BorderLayout.CENTER);

        JPanel actions = new JPanel(new BorderLayout(10, 0));
        actions.setOpaque(false);
        courseNameField = new JTextField();
        courseNameField.setToolTipText("Enter a new course subject");
        courseNameField.setFont(new Font("Arial", Font.PLAIN, 13));
        courseNameField.setPreferredSize(new Dimension(240, 36));
        courseNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(211, 219, 231)),
                BorderFactory.createEmptyBorder(5, 9, 5, 9)));
        actions.add(courseNameField, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        JButton addButton = createButton("Add course", true);
        addButton.addActionListener(event -> addCourse());
        JButton removeButton = createButton("Remove selected", false);
        removeButton.addActionListener(event -> removeSelectedCourse());
        JButton refreshButton = createButton("Refresh", false);
        refreshButton.addActionListener(event -> loadCourses());
        buttons.add(addButton);
        buttons.add(removeButton);
        buttons.add(refreshButton);
        actions.add(buttons, BorderLayout.EAST);
        content.add(actions, BorderLayout.SOUTH);

        loadCourses();
        return content;
    }

    private JPanel createStudentSettings() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(BACKGROUND);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 229, 239)),
                BorderFactory.createEmptyBorder(22, 26, 22, 26)));
        card.setPreferredSize(new Dimension(480, 390));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 7, 0);
        JLabel sectionTitle = new JLabel("Account security");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sectionTitle.setForeground(NAVY);
        card.add(sectionTitle, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 18, 0);
        JLabel account = new JLabel("Signed in as: " + (username == null ? "Student account" : username));
        account.setFont(new Font("Arial", Font.PLAIN, 13));
        account.setForeground(new Color(94, 108, 128));
        card.add(account, c);

        currentPasswordField = new JPasswordField();
        newPasswordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        addPasswordField(card, c, "Current password", currentPasswordField);
        addPasswordField(card, c, "New password", newPasswordField);
        addPasswordField(card, c, "Confirm new password", confirmPasswordField);

        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        c.insets = new Insets(8, 0, 0, 0);
        JButton changePasswordButton = createButton("Change password", true);
        changePasswordButton.addActionListener(event -> changeStudentPassword());
        card.add(changePasswordButton, c);

        c.gridy++;
        c.insets = new Insets(12, 0, 0, 0);
        JLabel note = new JLabel("<html>Password changes apply to the SIMS account used to sign in.</html>");
        note.setFont(new Font("Arial", Font.PLAIN, 12));
        note.setForeground(new Color(94, 108, 128));
        card.add(note, c);

        page.add(card);
        return page;
    }

    private void addPasswordField(JPanel card, GridBagConstraints c, String labelText, JPasswordField field) {
        c.gridy++;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 6, 0);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(45, 59, 79));
        card.add(label, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 12, 0);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(410, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 215, 229)),
                BorderFactory.createEmptyBorder(5, 9, 5, 9)));
        card.add(field, c);
    }

    private JButton createButton(String text, boolean primary) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusable(false);
        button.setForeground(primary ? Color.WHITE : NAVY);
        button.setBackground(primary ? BLUE : Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primary ? BLUE : new Color(211, 219, 231)),
                BorderFactory.createEmptyBorder(8, 13, 8, 13)));
        return button;
    }

    private void loadCourses() {
        courseModel.setRowCount(0);
        String sql = "SELECT course_id, course_name FROM courses ORDER BY course_name";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                courseModel.addRow(new Object[] {
                        results.getString("course_name"),
                        Integer.valueOf(results.getInt("course_id"))
                });
            }
            message.setForeground(new Color(94, 108, 128));
            message.setText(courseModel.getRowCount() + " course(s) in the catalog.");
        } catch (SQLException exception) {
            showError("Could not load courses: " + exception.getMessage());
        }
    }

    private void addCourse() {
        String courseName = courseNameField.getText().trim();
        if (courseName.isEmpty()) {
            showWarning("Enter a course name first.");
            return;
        }
        if (courseName.length() > 50) {
            showWarning("Course names must be 50 characters or fewer.");
            return;
        }

        String sql = "INSERT INTO courses (course_name) "
                + "SELECT ? WHERE NOT EXISTS "
                + "(SELECT 1 FROM courses WHERE LOWER(course_name) = LOWER(?))";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, courseName);
            statement.setString(2, courseName);
            if (statement.executeUpdate() == 0) {
                showWarning("That course is already in the catalog.");
                return;
            }
            courseNameField.setText("");
            loadCourses();
            showSuccess("Course added to the enrollment catalog.");
        } catch (SQLException exception) {
            showError("Could not add course: " + exception.getMessage());
        }
    }

    private void removeSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Select a course from the catalog first.");
            return;
        }
        int modelRow = courseTable.convertRowIndexToModel(selectedRow);
        int courseId = ((Integer) courseModel.getValueAt(modelRow, 1)).intValue();
        String courseName = String.valueOf(courseModel.getValueAt(modelRow, 0));
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Remove \"" + courseName + "\" from the catalog?",
                "Confirm course removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM courses c WHERE c.course_id = ? "
                + "AND NOT EXISTS (SELECT 1 FROM grades g WHERE g.course_id = c.course_id) "
                + "AND NOT EXISTS (SELECT 1 FROM enrollments e WHERE e.course_id = c.course_id) "
                + "AND NOT EXISTS (SELECT 1 FROM schedules s WHERE s.course_id = c.course_id)";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            if (statement.executeUpdate() == 0) {
                showWarning("This course is already used by a grade, enrollment, or schedule and cannot be removed.");
                return;
            }
            loadCourses();
            showSuccess("Course removed from the catalog.");
        } catch (SQLException exception) {
            showError("Could not remove course: " + exception.getMessage());
        }
    }

    private void changeStudentPassword() {
        if (username == null || username.trim().isEmpty()) {
            showError("The signed-in student account could not be identified. Sign in again and retry.");
            return;
        }
        String currentPassword = String.valueOf(currentPasswordField.getPassword());
        String newPassword = String.valueOf(newPasswordField.getPassword());
        String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showWarning("Complete all password fields.");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            showWarning("The new password and confirmation do not match.");
            return;
        }

        String sql = "UPDATE login SET passwords = ? WHERE username = ? AND roles = 'student' AND passwords = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newPassword);
            statement.setString(2, username);
            statement.setString(3, currentPassword);
            if (statement.executeUpdate() == 0) {
                showWarning("Current password is incorrect. No changes were made.");
                currentPasswordField.setText("");
                return;
            }
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
            showSuccess("Password changed successfully.");
        } catch (SQLException exception) {
            showError("Could not change password: " + exception.getMessage());
        }
    }

    private void showWarning(String text) {
        message.setForeground(new Color(170, 95, 25));
        message.setText(text);
    }

    private void showSuccess(String text) {
        message.setForeground(new Color(38, 125, 75));
        message.setText(text);
    }

    private void showError(String text) {
        message.setForeground(new Color(170, 45, 45));
        message.setText(text);
    }
}
