import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class newAccount extends JPanel implements ActionListener {

    private mainPageAdmin AMain;
    JLabel header;
    JPanel addheader;
    JPanel addCenter;
    JPanel SPanel;
    JPanel APanel;
    JTextField Semail;
    JTextField StemporaryPass;
    JTextField Sname;
    JTextField Syear;
    JTextField Sstatus;
    JTextField Sid;
    JLabel student;
    JLabel eStudent;
    JLabel tStudent;
    JLabel nStudent;
    JLabel yStudent;
    JLabel sStudent;
    JLabel iStudent;
    JButton sAdd;
    JLabel admin;
    JTextField Aemail;
    JTextField AtemporaryPass;
    JTextField Aname;
    JTextField Astatus;
    JTextField Aid;
    JLabel eAdmin;
    JLabel tAdmin;
    JLabel nAdmin;
    JLabel sAdmin;
    JLabel iAdmin;
    JButton aAdd;
    JScrollPane Spane;
    JScrollPane Apane;


    public newAccount (mainPageAdmin MAdmin){
        this.AMain = MAdmin;
        setBackground(Color.white);
        setLayout(new BorderLayout(2, 2));
        
        header = new JLabel("CREATE NEW ACCOUNT");
            header.setForeground(Color.WHITE);
            header.setBounds(10,65,500,20);
            header.setFont(new Font("arial", Font.BOLD, 26));

        addheader = new JPanel();
            addheader.setLayout(null);
            addheader.setBackground(new Color(0,0,98));
            addheader.setPreferredSize(new Dimension(0, 150));
            addheader.add(header);

        student = new JLabel("NEW STUDENT ACCOUNT");
            student.setBounds(130, 2, 200, 100);
            student.setForeground(Color.white);
            student.setFont(new Font(null, Font.BOLD, 15));

        eStudent = new JLabel("EMAIL:");
            eStudent.setBounds(50, 64, 100, 100);
            eStudent.setForeground(Color.white);
        
        Semail = new JTextField();
            Semail.setBounds(100, 100, 250, 30);
            Semail.setBorder(null);

        tStudent = new JLabel("PASSWORD:");
            tStudent.setBounds(15, 114, 200, 100);
            tStudent.setForeground(Color.white);

        StemporaryPass = new JTextField();
            StemporaryPass.setBounds(100, 150, 250, 30);
            StemporaryPass.setBorder(null);

        nStudent = new JLabel("NAME:");
            nStudent.setBounds(50, 164, 200, 100);
            nStudent.setForeground(Color.white);

        Sname = new JTextField();
            Sname.setBounds(100, 200, 250, 30);
            Sname.setBorder(null);

        yStudent = new JLabel("YEAR:");
            yStudent.setBounds(50, 214, 200, 100);
            yStudent.setForeground(Color.white);

        Syear = new JTextField();
            Syear.setBounds(100, 250, 250, 30);
            Syear.setBorder(null);

        sStudent = new JLabel("STATUS:");
            sStudent.setBounds(40, 264, 200, 100);
            sStudent.setForeground(Color.white);

        Sstatus = new JTextField();
            Sstatus.setBounds(100, 300, 250, 30);
            Sstatus.setBorder(null);

        iStudent = new JLabel("ID:");
            iStudent.setBounds(75, 314, 200, 100);
            iStudent.setForeground(Color.white);

        Sid = new JTextField();
            Sid.setBounds(100, 350, 250, 30);
            Sid.setBorder(null);

        sAdd = new JButton("ADD STUDENT");
            sAdd.setBounds(100, 400, 75, 30);
            sAdd.setBackground(new Color(0,0,98));
            sAdd.setFocusable(false);
            sAdd.setForeground(Color.WHITE);
            sAdd.setFont(new Font(null, Font.BOLD, 9));
            sAdd.setBorder(null);
            sAdd.addActionListener(this);
            
        SPanel = new JPanel();
        	SPanel.setPreferredSize(new Dimension(380, 460));
        	SPanel.setLayout(null);
        	SPanel.add(student);
        	SPanel.add(Semail); 
        	SPanel.add(StemporaryPass); 
        	SPanel.add(Sname);
        	SPanel.add(Syear);
        	SPanel.add(Sid);
        	SPanel.add(Sstatus);
        	SPanel.add(eStudent);
        	SPanel.add(tStudent);  
        	SPanel.add(nStudent);
            SPanel.add(yStudent);
            SPanel.add(sStudent);
            SPanel.add(iStudent);
            SPanel.add(sAdd);
            
        Spane = new JScrollPane(SPanel);
            Spane.setBounds(100, 0, 400, 373); 
            Spane.setBackground(new Color(120,0,98));  
            Spane.setBorder(null);
        
        admin = new JLabel("NEW ADMIN ACCOUNT");
            admin.setBounds(100, 10, 200, 100);
            admin.setForeground(Color.white);
            admin.setFont(new Font(null, Font.BOLD, 15));

        eAdmin = new JLabel("EMAIL:");
            eAdmin.setBounds(50, 64, 100, 100);
            eAdmin.setForeground(Color.white);

        Aemail = new JTextField();
            Aemail.setBounds(100, 100, 250, 30);
            Aemail.setBorder(null);
        
        tAdmin = new JLabel("PASSWORD:");
            tAdmin.setBounds(15, 114, 200, 100);
            tAdmin.setForeground(Color.white);

        AtemporaryPass = new JTextField();
            AtemporaryPass.setBounds(100, 150, 250, 30);
            AtemporaryPass.setBorder(null);

        nAdmin = new JLabel("NAME:");
            nAdmin.setBounds(50, 164, 200, 100);
            nAdmin.setForeground(Color.white);

        Aname = new JTextField();
            Aname.setBounds(100, 200, 250, 30);
            Aname.setBorder(null);

        sAdmin = new JLabel("STATUS:");
            sAdmin.setBounds(40, 214, 200, 100);
            sAdmin.setForeground(Color.white);

        Astatus = new JTextField();
            Astatus.setBounds(100, 250, 250, 30);
            Astatus.setBorder(null);

        iAdmin = new JLabel("ID:");
            iAdmin.setBounds(75, 264, 200, 100);
            iAdmin.setForeground(Color.white);

        Aid = new JTextField();
            Aid.setBounds(100, 300, 250, 30);
            Aid.setBorder(null);

        aAdd = new JButton("ADD ADMIN");
            aAdd.setBounds(100, 350, 75, 30);
            aAdd.setBackground(new Color(0,0,98));
            aAdd.setFocusable(false);
            aAdd.setForeground(Color.WHITE);
            aAdd.setFont(new Font("arial", Font.BOLD, 9));
            aAdd.setBorder(null);
            aAdd.addActionListener(this);

        APanel = new JPanel();
            APanel.setPreferredSize(new Dimension(380, 460));
            APanel.setLayout(null);
            APanel.add(admin);
            APanel.add(eAdmin);
            APanel.add(Aemail);
            APanel.add(tAdmin);
            APanel.add(AtemporaryPass);
            APanel.add(nAdmin);
            APanel.add(Aname);
            APanel.add(sAdmin);
            APanel.add(Astatus);
            APanel.add(iAdmin);
            APanel.add(Aid);
            APanel.add(aAdd);

        Apane = new JScrollPane(APanel);
            Apane.setBounds(600, 0, 400, 373); 
            Apane.setBackground(new Color(120,0,98));  
            Apane.setBorder(null);
         
        addCenter = new JPanel();
            addCenter.setLayout(null);
            addCenter.setBackground(new Color(0,0,98));
            addCenter.setPreferredSize(new Dimension(0, 150));
            addCenter.add(Spane);
            addCenter.add(Apane);
        
        add(addheader, BorderLayout.NORTH);
        add(addCenter, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    if(e.getSource()==sAdd){    
        String StudentE = Semail.getText();
        String StudentP = StemporaryPass.getText();
        String StudentN = Sname.getText();
        String StudentY = Syear.getText();
        String StudentS = Sstatus.getText();
        String studentIdText = Sid.getText().trim();
        int StudentI;  

        if(StudentE.isEmpty()||StudentP.isEmpty()||StudentN.isEmpty()||StudentY.isEmpty()||studentIdText.isEmpty()||StudentS.isEmpty()){
            JOptionPane.showMessageDialog(this,"Missing Input","Error", JOptionPane.ERROR_MESSAGE);
        }else{
            
            // check the input and save the new account in the text file
            String problem = checkAccount(studentIdText, StudentE, StudentP, StudentN, StudentY, StudentS);
            if (!problem.isEmpty()) {
                JOptionPane.showMessageDialog(this, problem, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!saveAccount(studentIdText, StudentE, StudentP, "student", StudentN, StudentY, StudentS)) {
                return;
            }
            JOptionPane.showMessageDialog(this, "Added New Student", "Success",  JOptionPane.INFORMATION_MESSAGE);
            Semail.setText("");
            StemporaryPass.setText("");
            Sname.setText("");
            Syear.setText("");
            Sstatus.setText("");
            Sid.setText("");
        }
    }

    if(e.getSource()==aAdd){
        String AdminE = Aemail.getText();
        String AdminP = AtemporaryPass.getText();
        String AdminN = Aname.getText();
        String AdminS = Astatus.getText();
        String adminIdText = Aid.getText().trim();
        int AdminI;
        
        if(AdminE.isEmpty()||AdminP.isEmpty()||AdminN.isEmpty()||adminIdText.isEmpty()||AdminS.isEmpty()){
            JOptionPane.showMessageDialog(this, "Missing Input", "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            
            // check the input and save the new account in the text file (admins have no year)
            String problem = checkAccount(adminIdText, AdminE, AdminP, AdminN, "N/A", AdminS);
            if (!problem.isEmpty()) {
                JOptionPane.showMessageDialog(this, problem, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!saveAccount(adminIdText, AdminE, AdminP, "admin", AdminN, "N/A", AdminS)) {
                return;
            }
            JOptionPane.showMessageDialog(this, "Added New Admin", "Success", JOptionPane.INFORMATION_MESSAGE);
            Aemail.setText("");
            AtemporaryPass.setText("");
            Aid.setText("");
            Aname.setText("");
            Astatus.setText("");
        }
    }

}

    // ================= BACKEND (text file) =================
    // Every line of data/accounts.txt is one account:  id|email|password|role|name|year|status
    // NOTE: passwords are saved as plain text. This is simple for a school project,
    // but it is NOT safe for a real system (real systems save a hash of the password).
    final String ACCOUNTS_FILE = "data/accounts.txt";

    // create the data folder and accounts.txt if they do not exist
    public void makeFileIfMissing() {
        try {
            File folder = new File("data");
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(ACCOUNTS_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot create the file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // read all lines of accounts.txt (empty lines are skipped). Returns null if the file cannot be read.
    public ArrayList<String> loadAccounts() {
        makeFileIfMissing();
        ArrayList<String> lines = new ArrayList<String>();
        try (Scanner input = new Scanner(new File(ACCOUNTS_FILE))) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Cannot read the file: " + ACCOUNTS_FILE, "File Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return lines;
    }

    // check the input of a new account. Returns the problem as a text, or an empty text if everything is fine.
    public String checkAccount(String id, String email, String password, String name, String year, String status) {
        id = id.trim();
        email = email.trim();

        // the | character separates the values in the file, so it cannot be used
        String all = id + email + password + name + year + status;
        if (all.contains("|")) {
            return "The character | is not allowed.";
        }
        if (email.indexOf('@') < 1 || email.contains(" ")) {
            return "Please enter a valid email (example: juan@sims.com).";
        }

        // no duplicate ID or email
        ArrayList<String> lines = loadAccounts();
        if (lines == null) {
            return "Cannot read the accounts file.";
        }
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", -1);
            if (parts.length >= 7) {
                if (parts[0].equalsIgnoreCase(id)) {
                    return "This ID is already used.";
                }
                if (parts[1].equalsIgnoreCase(email)) {
                    return "This email is already used.";
                }
            }
        }
        return "";
    }

    // add the new account at the end of accounts.txt. All old accounts are kept.
    public boolean saveAccount(String id, String email, String password, String role, String name, String year, String status) {
        ArrayList<String> lines = loadAccounts();
        if (lines == null) {
            return false;
        }
        lines.add(id.trim() + "|" + email.trim() + "|" + password + "|" + role + "|" + name.trim() + "|" + year.trim() + "|" + status.trim());

        try (PrintWriter output = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (int i = 0; i < lines.size(); i++) {
                output.println(lines.get(i));
            }
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot save the account: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
