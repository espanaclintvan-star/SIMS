import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class newAccount extends JPanel implements ActionListener {

    private mainPageAdmin AMain;
    JLabel header;
    JPanel addheader;
    JPanel addCenter;
    JTextField Semail;
    JTextField StemporaryPass;
    JTextField Aemail;
    JTextField AtemporaryPass;
    JLabel student;
    JLabel admin;
    JLabel eStudent;
    JLabel tStudent;
    JLabel eAdmin;
    JLabel tAdmin;
    JButton sAdd;
    JButton aAdd;


    public newAccount (mainPageAdmin MAdmin){
        this.AMain = MAdmin;
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());

        header = new JLabel("CREATE NEW ACCOUNT");
            header.setForeground(Color.WHITE);
            header.setBounds(10,65,500,20);
            header.setFont(new Font("arial", Font.BOLD, 26));
            

        addheader = new JPanel();
            addheader.setLayout(null);
            addheader.setBackground(Color.GRAY);
            addheader.setPreferredSize(new Dimension(0, 150));
            addheader.add(header);

        student = new JLabel("NEW STUDENT ACCOUNT");
            student.setBounds(100, 30, 200, 100);
            student.setForeground(Color.black);

        eStudent = new JLabel("EMAIL:");
            eStudent.setBounds(50, 64, 100, 100);
            eStudent.setForeground(Color.black);
        
        tStudent = new JLabel("PASSWORD:");
            tStudent.setBounds(15, 114, 200, 100);
            tStudent.setForeground(Color.black);

        sAdd = new JButton("ADD STUDENT");
            sAdd.setBounds(100, 200, 75, 30);
            sAdd.setBackground(new Color(0,0,98));
            sAdd.setFocusable(false);
            sAdd.setForeground(Color.WHITE);
            sAdd.setFont(new Font("arial", Font.BOLD, 9));
            sAdd.setBorder(null);
            sAdd.addActionListener(this);

        admin = new JLabel("NEW ADMIN ACCOUNT");
            admin.setBounds(600, 30, 200, 100);
            admin.setForeground(Color.BLACK);

        eAdmin = new JLabel("EMAIL:");
            eAdmin.setBounds(550, 64, 100, 100);
            eAdmin.setForeground(Color.black);
        
        tAdmin = new JLabel("PASSWORD:");
            tAdmin.setBounds(515, 114, 200, 100);
            tAdmin.setForeground(Color.black);

        aAdd = new JButton("ADD ADMIN");
            aAdd.setBounds(600, 200, 75, 30);
            aAdd.setBackground(new Color(0,0,98));
            aAdd.setFocusable(false);
            aAdd.setForeground(Color.WHITE);
            aAdd.setFont(new Font("arial", Font.BOLD, 9));
            aAdd.setBorder(null);
            aAdd.addActionListener(this);

        Semail = new JTextField();
            Semail.setBounds(100, 100, 250, 30);
            Semail.setBorder(null);

        StemporaryPass = new JTextField();
            StemporaryPass.setBounds(100, 150, 250, 30);
            StemporaryPass.setBorder(null);
         
        Aemail = new JTextField();
            Aemail.setBounds(600, 100, 250, 30);
            Aemail.setBorder(null);

        AtemporaryPass = new JTextField();
            AtemporaryPass.setBounds(600, 150, 250, 30);
            AtemporaryPass.setBorder(null);

        addCenter = new JPanel();
            addCenter.setLayout(null);
            addCenter.setBackground(Color.LIGHT_GRAY);
            addCenter.setPreferredSize(new Dimension(0, 150));
            addCenter.add(Semail);   
            addCenter.add(StemporaryPass);
            addCenter.add(Aemail);   
            addCenter.add(AtemporaryPass);
            addCenter.add(student);
            addCenter.add(admin);
            addCenter.add(eStudent);
            addCenter.add(tStudent);
            addCenter.add(eAdmin);
            addCenter.add(tAdmin);
            addCenter.add(sAdd);
            addCenter.add(aAdd);
      
        
        add(addheader, BorderLayout.NORTH);
        add(addCenter, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    String StudentE = Semail.getText();
    String StudentP = StemporaryPass.getText();
    String AdminE = Aemail.getText();
    String AdminP = AtemporaryPass.getText();

    if(e.getSource()==sAdd){
        if(StudentE.isEmpty()&&StudentP.isEmpty()){
            System.out.println("add email and password");
        }else if(StudentE.isEmpty()){
            System.out.println("add email");
        }else if(StudentP.isEmpty()){
            System.out.println("add password");
        }else{
            addNewStudent(StudentE, StudentP, "student");
            System.out.println("success");
            Semail.setText("");
            StemporaryPass.setText("");
        }
    }

    if(e.getSource()==aAdd){
        if(AdminE.isEmpty()&&AdminP.isEmpty()){
            System.out.println("add email and password");
        }else if(AdminE.isEmpty()){
            System.out.println("add email");
        }else if(AdminP.isEmpty()){
            System.out.println("add password");
        }else{
            addNewStudent(AdminE, AdminP, "admin");
            System.out.println("success");
            Aemail.setText("");
            AtemporaryPass.setText("");
        }
    }

}

public void addNewStudent(String StudentE, String StudentP, String role) {
        String query = "INSERT INTO login (username, passwords, roles) values (?, ?, ? )";
        
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, StudentE);
            pstmt.setString(2, StudentP);
            pstmt.setString(3, role);
            
            int rowsAffected = pstmt.executeUpdate(); 
            

        } catch (SQLException e) {
            System.out.println("Database Error: Account was not created.");
            e.printStackTrace();
        }

    }

}
