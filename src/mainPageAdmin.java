import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

class mainPageAdmin extends JFrame implements ActionListener {
    //Login loginref;
    JPanel topNav;
    JPanel bottomRightNav;
    JPanel leftNav;
    JPanel mainPanel;
    JLabel MLogo;
    JLabel name;
    ImageIcon logo;
    JButton GradeButton;
    JButton ClearanceButton;
    JButton ScheduleButton;
    JButton EnrollmentButton;
    JButton ProfileButton;
    JButton logoutButton;
    JButton deleteButton;
    JButton createAccount;

    String adminId = "";   // ID of the logged-in admin

    public mainPageAdmin() {
        this("");
    }

    public mainPageAdmin(String adminId) {
        this.adminId = adminId;
        //this.loginref = logINS;
        
        UIManager.put("OptionPane.background", new Color(120, 0, 98));
    	UIManager.put("Panel.background", new Color(120, 0, 98));
    	UIManager.put("OptionPane.messageForeground", Color.WHITE);
    	UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
    	
    	UIManager.put("Button.background", new Color(0, 0, 98));
    	UIManager.put("Button.foreground", Color.WHITE);
    	UIManager.put("Button.focus", new Color(0, 0, 0, 0));
    	UIManager.put("Button.border", BorderFactory.createEmptyBorder(5, 15, 5, 15));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLayout(new BorderLayout(2, 2));
        logo = new ImageIcon("logo_edited.png");

        MLogo = new JLabel(logo);
        MLogo.setPreferredSize(new Dimension(100, 0));

        name = new JLabel("STUDENT INFORMATION MANAGEMENT SYSTEM (SIMS)");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 20));

        ProfileButton = new JButton();
        ProfileButton.setBounds(150, 80, 55, 20);
        ProfileButton.setBorder(null);
        ProfileButton.setBackground(new Color(120, 0, 98));
        ProfileButton.setForeground(Color.WHITE);
        ProfileButton.setFont(new Font("Arial", Font.PLAIN, 10));
        ProfileButton.setFocusable(false);
        ProfileButton.setText("PROFILE");
        ProfileButton.addActionListener(this);

        deleteButton = new JButton();
        deleteButton.setBounds(10, 80, 55, 20);
        deleteButton.setBorder(null);
        deleteButton.setBackground(new Color(120, 0, 98));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 6));
        deleteButton.setFocusable(false);
        deleteButton.setText("DELETE ACC");
        deleteButton.addActionListener(this);

        createAccount = new JButton();
        createAccount.setBounds(80, 80, 55, 20);
        createAccount.setBorder(null);
        createAccount.setBackground(new Color(120, 0, 98));
        createAccount.setForeground(Color.WHITE);
        createAccount.setFont(new Font("Arial", Font.PLAIN, 6));
        createAccount.setFocusable(false);
        createAccount.setText("ADD ACCOUNT");
        createAccount.addActionListener(this);

        logoutButton = new JButton();
        logoutButton.setBounds(220, 80, 55, 20);
        logoutButton.setBorder(null);
        logoutButton.setBackground(new Color(120, 0, 98));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 10));
        logoutButton.setFocusable(false);
        logoutButton.setText("LOGOUT");
        logoutButton.addActionListener(this);

        bottomRightNav = new JPanel();
        bottomRightNav.setLayout(null);
        bottomRightNav.setBackground(new Color(0,0,98));
        bottomRightNav.setPreferredSize(new Dimension(300, 0));
        bottomRightNav.add(ProfileButton);
        bottomRightNav.add(logoutButton);
        bottomRightNav.add(createAccount);
        bottomRightNav.add(deleteButton);

        topNav = new JPanel();
        topNav.setLayout(new BorderLayout());
        topNav.setPreferredSize(new Dimension(0, 120));
        topNav.setBackground(new Color(0,0,98));
        topNav.add(MLogo, BorderLayout.WEST);
        topNav.add(bottomRightNav, BorderLayout.EAST);
        topNav.add(name, BorderLayout.CENTER);
        add(topNav, BorderLayout.NORTH);

        GradeButton = new JButton();
        GradeButton.setBounds(5, 35, 160, 50);
        GradeButton.setBackground(new Color(120, 0, 98));
        GradeButton.setBorder(null);
        GradeButton.setText("GRADES");
        GradeButton.setFocusable(false);
        GradeButton.setForeground(Color.WHITE);
        GradeButton.setFont(new Font("Arial", Font.BOLD, 18));
        GradeButton.addActionListener(this);

        ClearanceButton = new JButton();
        ClearanceButton.setBounds(5, 110, 160, 50);
        ClearanceButton.setBackground(new Color(120, 0, 98));
        ClearanceButton.setBorder(null);
        ClearanceButton.setText("CLEARNACE");
        ClearanceButton.setFocusable(false);
        ClearanceButton.setForeground(Color.WHITE);
        ClearanceButton.setFont(new Font("Arial", Font.BOLD, 18));
        ClearanceButton.addActionListener(this);

        ScheduleButton = new JButton();
        ScheduleButton.setBounds(5, 185, 160, 50);
        ScheduleButton.setBackground(new Color(120, 0, 98));
        ScheduleButton.setBorder(null);
        ScheduleButton.setText("SCHEDULE");
        ScheduleButton.setFocusable(false);
        ScheduleButton.setForeground(Color.WHITE);
        ScheduleButton.setFont(new Font("Arial", Font.BOLD, 18));
        ScheduleButton.addActionListener(this);

        EnrollmentButton = new JButton();
        EnrollmentButton.setBounds(5, 260, 160, 50);
        EnrollmentButton.setBackground(new Color(120, 0, 98));
        EnrollmentButton.setBorder(null);
        EnrollmentButton.setText("ENROLLMENT");
        EnrollmentButton.setFocusable(false);
        EnrollmentButton.setForeground(Color.WHITE);
        EnrollmentButton.setFont(new Font("Arial", Font.BOLD, 18));
        EnrollmentButton.addActionListener(this);

        leftNav = new JPanel();
        leftNav.setPreferredSize(new Dimension(200, 0));
        leftNav.setBackground(new Color(0,0,98));
        leftNav.setLayout(null);
        leftNav.add(GradeButton);
        leftNav.add(ClearanceButton);
        leftNav.add(ScheduleButton);
        leftNav.add(EnrollmentButton);
        add(leftNav, BorderLayout.WEST);

        mainPanel = new JPanel();
            mainPanel.setBackground(new Color(0,0,98));

        add(mainPanel, BorderLayout.CENTER);

        getContentPane().setBackground(new Color(255, 255, 255));
        setTitle("Student Information Management System");
        setIconImage(logo.getImage());

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            new Login();
            this.setVisible(false);
        }
        if (e.getSource() == ProfileButton) {
            adminProfile adprof = new adminProfile(this);
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(adprof, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
        if (e.getSource() == createAccount) {
        	newAccount addAcc = new newAccount(this);
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(addAcc, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
        if (e.getSource() == deleteButton) {
        	deleteAccount delAcc = new deleteAccount(this);
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(delAcc, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
        if (e.getSource() == GradeButton) {
        	adminGrades Agrade = new adminGrades(this);
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(Agrade, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
        if (e.getSource() == ClearanceButton) {
        	adminClearance AClearance = new adminClearance(this);
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(AClearance, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
        if (e.getSource() == ScheduleButton) {
        	adminSchedule ASchedule = new adminSchedule(this);
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(ASchedule, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
        if (e.getSource() == EnrollmentButton) {
            adminEnrollment AEnrollment = new adminEnrollment(this);
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(AEnrollment, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }

    }

}
