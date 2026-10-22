import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;






class mainPageStudent extends JFrame implements ActionListener{
    Login loginref;
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
    JButton settingsButton;

    
    public mainPageStudent(Login logINS) {
        this.loginref = logINS;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setMinimumSize(new Dimension(500,500));
        setSize(500, 500);
        setLayout(new BorderLayout(5,5));
        logo = new ImageIcon("logo_edited.png");

        MLogo = new JLabel(logo);
            MLogo.setPreferredSize(new Dimension(100,0));

        name = new JLabel("STUDENT INFORMATION MANAGEMENT SYSTEM (SIMS)");
            name.setForeground(Color.WHITE);
            name.setFont(new Font("Arial", Font.BOLD, 20));

        ProfileButton = new JButton();
            ProfileButton.setBounds(80, 80, 55, 20);
            ProfileButton.setBorder(null);
            ProfileButton.setBackground(new Color(0,0,98));
            ProfileButton.setForeground(Color.WHITE);
            ProfileButton.setFont(new Font("Arial", Font.PLAIN , 10));
            ProfileButton.setFocusable(false);
            ProfileButton.setText("PROFILE");
            ProfileButton.addActionListener(this);

        settingsButton = new JButton();
            settingsButton.setBounds(150, 80, 55, 20);
            settingsButton.setBorder(null);
            settingsButton.setBackground(new Color(0,0,98));
            settingsButton.setForeground(Color.WHITE);
            settingsButton.setFont(new Font("Arial", Font.PLAIN , 10));
            settingsButton.setFocusable(false);
            settingsButton.setText("SETTINGS");
            settingsButton.addActionListener(this);

        logoutButton = new JButton();
            logoutButton.setBounds(220, 80, 55, 20);
            logoutButton.setBorder(null);
            logoutButton.setBackground(new Color(0,0,98));
            logoutButton.setForeground(Color.WHITE);
            logoutButton.setFont(new Font("Arial", Font.PLAIN , 10));
            logoutButton.setFocusable(false);
            logoutButton.setText("LOGOUT");
            logoutButton.addActionListener(this);

        bottomRightNav = new JPanel();
            bottomRightNav.setLayout(null);
            bottomRightNav.setPreferredSize(new Dimension(300, 0));
            bottomRightNav.setBackground(Color.MAGENTA);
            bottomRightNav.add(ProfileButton);
            bottomRightNav.add(settingsButton);
            bottomRightNav.add(logoutButton);


        topNav = new JPanel();
            topNav.setLayout(new BorderLayout());
            topNav.setPreferredSize(new Dimension(0, 120));
            topNav.setBackground(Color.GRAY);
            topNav.add(MLogo, BorderLayout.WEST);
            topNav.add(bottomRightNav, BorderLayout.EAST);
            topNav.add(name, BorderLayout.CENTER);
        add(topNav, BorderLayout.NORTH);
        
        GradeButton = new JButton();
            GradeButton.setBounds(5,35,160,50);
            GradeButton.setBackground(new Color(0,0,98));
            GradeButton.setBorder(null);
            GradeButton.setText("GRADES");
            GradeButton.setFocusable(false);
            GradeButton.setForeground(Color.WHITE);
            GradeButton.setFont(new Font("Arial", Font.BOLD, 18));
            GradeButton.addActionListener(this);

        ClearanceButton = new JButton();
            ClearanceButton.setBounds(5,110,160,50);
            ClearanceButton.setBackground(new Color(0,0,98));
            ClearanceButton.setBorder(null);
            ClearanceButton.setText("CLEARNACE");
            ClearanceButton.setFocusable(false);
            ClearanceButton.setForeground(Color.WHITE);
            ClearanceButton.setFont(new Font("Arial", Font.BOLD, 18));
            ClearanceButton.addActionListener(this);

        ScheduleButton = new JButton();
            ScheduleButton.setBounds(5,185,160,50);
            ScheduleButton.setBackground(new Color(0,0,98));
            ScheduleButton.setBorder(null);
            ScheduleButton.setText("SCHEDULE");
            ScheduleButton.setFocusable(false);
            ScheduleButton.setForeground(Color.WHITE);
            ScheduleButton.setFont(new Font("Arial", Font.BOLD, 18));
            ScheduleButton.addActionListener(this);

        EnrollmentButton = new JButton();
            EnrollmentButton.setBounds(5,260,160,50);
            EnrollmentButton.setBackground(new Color(0,0,98));
            EnrollmentButton.setBorder(null);
            EnrollmentButton.setText("ENROLLMENT");
            EnrollmentButton.setFocusable(false);
            EnrollmentButton.setForeground(Color.WHITE);
            EnrollmentButton.setFont(new Font("Arial", Font.BOLD, 18));
            EnrollmentButton.addActionListener(this);

        leftNav = new JPanel();
            leftNav.setPreferredSize(new Dimension(200, 0));
            leftNav.setBackground(Color.GRAY);
            leftNav.setLayout(null);
            leftNav.add(GradeButton);
            leftNav.add(ClearanceButton);
            leftNav.add(ScheduleButton);
            leftNav.add(EnrollmentButton);
        add(leftNav, BorderLayout.WEST);

        mainPanel = new JPanel();
            mainPanel.setBackground(Color.GREEN);
        add(mainPanel, BorderLayout.CENTER);
        

	    getContentPane().setBackground(new Color(0, 0, 98));
	    setTitle("Student Information Management System");
	    setIconImage(logo.getImage());
        
        
        setVisible(true);

        
    }

    @Override 
    public void actionPerformed (ActionEvent e){
        if(e.getSource()==logoutButton){
            new Login();
            this.setVisible(false);
        }
        if(e.getSource()==ProfileButton){
            System.out.println("profile");
        }
        if(e.getSource()==settingsButton){
            System.out.println("settings");
        }
        if(e.getSource()==GradeButton){
            grades grade = new grades(this);
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(grade, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
        if(e.getSource()==ClearanceButton){
            System.out.println("clearance");
        }
        if(e.getSource()==ScheduleButton){
            System.out.println("sched");
        }
        if(e.getSource()==EnrollmentButton){
            System.out.println("enrollment");
        }
        
    }
    

}
