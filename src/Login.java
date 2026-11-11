import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Login extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final Color NAVY = new Color(20, 42, 84);
    private static final Color BLUE = new Color(39, 93, 171);
    private static final Color BACKGROUND = new Color(239, 243, 249);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JRadioButton studentRole;
    private JRadioButton adminRole;
    private JCheckBox showPassword;
    private JButton loginButton;

    Login() {
        DatabaseInitializer.initializeDatabase();

        setTitle("Student Information Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 600));
        setSize(1100, 720);
        setLocationRelativeTo(null);

        ImageIcon logo = new ImageIcon("logo_edited.png");
        setIconImage(logo.getImage());

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);
        root.add(createHeader(logo), BorderLayout.NORTH);

        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(BACKGROUND);
        page.add(createLoginCard(logo));
        root.add(page, BorderLayout.CENTER);
        setContentPane(root);

        getRootPane().setDefaultButton(loginButton);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private JPanel createHeader(ImageIcon logo) {
        JPanel header = new JPanel(new BorderLayout(14, 0));
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel brand = new JLabel("STUDENT INFORMATION MANAGEMENT SYSTEM");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("Arial", Font.BOLD, 20));
        if (logo.getIconWidth() > 0) {
            brand.setIcon(logo);
            brand.setIconTextGap(14);
        }
        header.add(brand, BorderLayout.WEST);
        return header;
    }

    private JPanel createLoginCard(ImageIcon logo) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 227, 237)),
                BorderFactory.createEmptyBorder(30, 38, 30, 38)));
        card.setPreferredSize(new Dimension(480, 560));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 12, 0);

        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (logo.getIconWidth() > 0) {
            logoLabel.setIcon(new ImageIcon(logo.getImage().getScaledInstance(88, 88, java.awt.Image.SCALE_SMOOTH)));
        }
        card.add(logoLabel, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 6, 0);
        JLabel heading = new JLabel("Welcome back", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(NAVY);
        card.add(heading, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 24, 0);
        JLabel subtitle = new JLabel("Sign in to your SIMS account", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(96, 108, 126));
        card.add(subtitle, c);

        c.gridy++;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 7, 0);
        JLabel usernameLabel = createFieldLabel("Username");
        card.add(usernameLabel, c);

        c.gridy++;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 17, 0);
        usernameField = new JTextField();
        styleTextField(usernameField);
        card.add(usernameField, c);

        c.gridy++;
        c.gridwidth = 1;
        c.insets = new Insets(0, 0, 7, 0);
        JLabel passwordLabel = createFieldLabel("Password");
        card.add(passwordLabel, c);

        c.gridy++;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 5, 0);
        passwordField = new JPasswordField();
        styleTextField(passwordField);
        card.add(passwordField, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 18, 0);
        showPassword = new JCheckBox("Show password");
        showPassword.setBackground(Color.WHITE);
        showPassword.setForeground(new Color(75, 88, 108));
        showPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        showPassword.addActionListener(event ->
                passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '\u2022'));
        card.add(showPassword, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 8, 0);
        JLabel roleLabel = createFieldLabel("Sign in as");
        card.add(roleLabel, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 18, 0);
        JPanel rolePanel = new JPanel(new GridBagLayout());
        rolePanel.setBackground(Color.WHITE);
        studentRole = new JRadioButton("Student");
        adminRole = new JRadioButton("Admin");
        styleRoleButton(studentRole);
        styleRoleButton(adminRole);
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(studentRole);
        roleGroup.add(adminRole);
        GridBagConstraints roleConstraints = new GridBagConstraints();
        roleConstraints.anchor = GridBagConstraints.WEST;
        roleConstraints.insets = new Insets(0, 0, 0, 28);
        rolePanel.add(studentRole, roleConstraints);
        roleConstraints.gridx = 1;
        roleConstraints.insets = new Insets(0, 0, 0, 0);
        rolePanel.add(adminRole, roleConstraints);
        card.add(rolePanel, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 14, 0);
        loginButton = new JButton("Sign in");
        loginButton.setFont(new Font("Arial", Font.BOLD, 15));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(BLUE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        loginButton.addActionListener(this);
        card.add(loginButton, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        JLabel accountHint = new JLabel(
                "<html><div style='text-align:center'>No default credentials. Use the username and password<br>"
                        + "for an account created by your SIMS administrator.</div></html>",
                SwingConstants.CENTER);
        accountHint.setFont(new Font("Arial", Font.PLAIN, 12));
        accountHint.setForeground(new Color(96, 108, 126));
        card.add(accountHint, c);

        return card;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(45, 59, 79));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(390, 42));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 215, 229)),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));
    }

    private void styleRoleButton(JRadioButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(45, 59, 79));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter both your username and password.",
                    "Missing information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!studentRole.isSelected() && !adminRole.isSelected()) {
            JOptionPane.showMessageDialog(this, "Choose Student or Admin to continue.",
                    "Select account type", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String role = studentRole.isSelected() ? "student" : "admin";
        if (checkAdminCredentials(username, password, role)) {
            if ("student".equals(role)) {
                new mainPageStudent(username);
            } else {
                new mainPageAdmin();
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "The username, password, or account type is incorrect.",
                    "Sign-in failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean checkAdminCredentials(String username, String password, String role) {
        String query = "SELECT 1 FROM login WHERE username = ? AND passwords = ? AND roles = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);
            try (ResultSet results = statement.executeQuery()) {
                return results.next();
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, "Could not connect to the SIMS database:\n"
                    + exception.getMessage(), "Database connection error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
