import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Login extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton loginButton = new JButton("Log in");
    private final JRadioButton studentRole = new JRadioButton("Student");
    private final JRadioButton adminRole = new JRadioButton("Admin");
    private final ImageIcon logo = new ImageIcon("logo_edited.png");

    Login() {
        setTitle("Student Information Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(500, 390);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(0, 0, 98));
        setIconImage(logo.getImage());

        JLabel logoLabel = new JLabel(logo);
        logoLabel.setBounds(185, 5, 130, 110);
        add(logoLabel);

        JLabel title = new JLabel("Student Information Management System", SwingConstants.CENTER);
        title.setBounds(35, 105, 430, 30);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(title);

        addLabel("Email:", 75, 163);
        emailField.setBounds(150, 155, 275, 32);
        add(emailField);

        addLabel("Password:", 75, 211);
        passwordField.setBounds(150, 203, 275, 32);
        add(passwordField);

        ButtonGroup roles = new ButtonGroup();
        roles.add(studentRole);
        roles.add(adminRole);
        styleRole(studentRole);
        styleRole(adminRole);
        studentRole.setBounds(150, 247, 105, 28);
        adminRole.setBounds(260, 247, 95, 28);
        studentRole.setSelected(true);
        add(studentRole);
        add(adminRole);

        loginButton.setBounds(185, 292, 130, 36);
        loginButton.setFocusPainted(false);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(120, 0, 98));
        loginButton.setBorder(null);
        loginButton.addActionListener(this);
        add(loginButton);
        getRootPane().setDefaultButton(loginButton);

        setVisible(true);
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 75, 25);
        label.setForeground(Color.WHITE);
        add(label);
    }

    private void styleRole(JRadioButton role) {
        role.setOpaque(false);
        role.setForeground(Color.WHITE);
        role.setFocusPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter your email and password.", "Missing information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean isAdmin = adminRole.isSelected();
        if (!isAdmin && !Dashboard.hasStudent(email)) {
            JOptionPane.showMessageDialog(this,
                    "No student account was found for that email. Try student@example.com.",
                    "Student not found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new Dashboard(email, isAdmin);
        dispose();
    }
}
