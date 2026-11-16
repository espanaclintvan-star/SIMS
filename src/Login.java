import java.awt.Color;
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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Login extends JFrame implements ActionListener {
    
    JTextField text1; 
    JButton button;
    JButton showPass;
    JLabel label1;
    JLabel label2;
    JCheckBox checkbox1;
    JCheckBox checkbox2;
    ImageIcon logo;
    JLabel label3;
    JPasswordField passwords;
    

    Login(){
         
    	makeFileIfMissing();   // create data/accounts.txt if it does not exist
    	
    	UIManager.put("OptionPane.background", new Color(120, 0, 98));
    	UIManager.put("Panel.background", new Color(120, 0, 98));
    	UIManager.put("OptionPane.messageForeground", Color.WHITE);
    	UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
    	
    	UIManager.put("Button.background", new Color(0, 0, 98));
    	UIManager.put("Button.foreground", Color.WHITE);
    	UIManager.put("Button.focus", new Color(0, 0, 0, 0));
    	UIManager.put("Button.border", BorderFactory.createEmptyBorder(5, 15, 5, 15));
    		
         text1 = new JTextField();
         button = new JButton("login");
         label1 = new JLabel("Email: ");
         passwords = new JPasswordField();
         label2 = new JLabel("Password: ");
         checkbox1 = new JCheckBox("Student");
         checkbox2 = new JCheckBox("Admin");
         logo = new ImageIcon("logo_edited.png");
         label3 = new JLabel(logo);
         showPass = new JButton("Show");

         setLayout(null);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setResizable(false);
         setSize(500, 300);
         
         text1.setBounds(120, 120, 250, 30);
         passwords.setBounds(120, 170, 250, 30);
         text1.setBorder(null);
         passwords.setBorder(null);

         label1.setBounds(80, 128, 50, 10);
         label2.setBounds(50, 178, 70, 10);
         label3.setBounds(180, -10,130, 130);
         label1.setForeground(Color.WHITE);
         label2.setForeground(Color.WHITE);

         button.setBounds(200, 228, 80, 30);
         button.setFocusable(false);
         button.setForeground(Color.WHITE); 
         button.setBackground(new Color(120, 0, 98));
         button.setBorder(null);
         button.addActionListener(this);
         
         showPass.setBounds(380, 170, 50, 30);
         showPass.setFocusable(false);
         showPass.setForeground(Color.WHITE); 
         showPass.setBackground(new Color(120, 0, 98));
         showPass.setBorder(null);
         showPass.addActionListener(this);
         
         
         checkbox1.setBounds(120, 210, 90, 10);
         checkbox2.setBounds(210, 210, 90, 10);
         checkbox1.setBackground(new Color(0, 0, 98));
         checkbox2.setBackground(new Color(0, 0, 98));
         checkbox1.setForeground(Color.WHITE);
         checkbox2.setForeground(Color.WHITE);

         getContentPane().setBackground(new Color(0, 0, 98));
         setTitle("Student Information Management System");
         setIconImage(logo.getImage());

         add(label1);
         add(text1);
         add(label2);
         add(button);
         add(checkbox1);
         add(checkbox2);
         add(label3);
         add(passwords);
         add(showPass);
         setVisible(true);
    }

    @Override 
    public void actionPerformed (ActionEvent e){
        if(e.getSource()==button){
            String username = text1.getText();
            String password = String.valueOf(passwords.getPassword());

            if(username.isEmpty() && password.isEmpty()){
                JOptionPane.showMessageDialog(this, "Must enter username and password.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }else if(username.isEmpty()){
                JOptionPane.showMessageDialog(this, "Must enter username.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }else if(password.isEmpty()){
                JOptionPane.showMessageDialog(this, "Must enter password.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String checkbox="";
            if(checkbox1.isSelected()){
                checkbox+="student";
            }
            if(checkbox2.isSelected()){
                checkbox+="admin";
            }

            switch(checkbox){
                case "student":
                    if(checkAdminCredentials(username, password, "student")){
                        new mainPageStudent(loggedInId);
                        this.setVisible(false);
                    }else{
                    	JOptionPane.showMessageDialog(this, "Error.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "admin":
                    if(checkAdminCredentials(username, password, "admin")){
                        new mainPageAdmin(loggedInId);
                        this.setVisible(false);
                    }else{
                    	JOptionPane.showMessageDialog(this, "Error.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    }
                    break;
                case "studentadmin":
                    JOptionPane.showMessageDialog(this, "Error, pick one.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Must enter one role.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    
            }
        }
        
        if(e.getSource()==showPass) {
        	if (passwords.getEchoChar() != (char) 0) {
                // Show plain text
                passwords.setEchoChar((char) 0);
                showPass.setText("Hide");
            } else {
                // Hide password again
            	passwords.setEchoChar('•'); 
                showPass.setText("Show");
            }
        }
    }

    // ================= BACKEND (text file) =================
    // Every line of data/accounts.txt is one account:  id|email|password|role|name|year|status
    // role is "student" or "admin".
    // NOTE: passwords are saved as plain text. This is simple for a school project,
    // but it is NOT safe for a real system (real systems save a hash of the password).
    final String ACCOUNTS_FILE = "data/accounts.txt";
    String loggedInId = "";   // ID of the account that logged in

    // create the data folder and accounts.txt if they do not exist (or are empty).
    // A default admin account is added so that somebody can always log in the first time.
    public void makeFileIfMissing() {
        try {
            File folder = new File("data");
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(ACCOUNTS_FILE);
            if (!file.exists() || file.length() == 0) {
                try (PrintWriter output = new PrintWriter(new FileWriter(file))) {
                    output.println("ADMIN001|admin@sims.com|admin123|admin|System Administrator|N/A|Active");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot create the file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // read all accounts from the text file. Each account is an array of 7 values.
    public ArrayList<String[]> loadAccounts() {
        ArrayList<String[]> accounts = new ArrayList<String[]>();
        try (Scanner input = new Scanner(new File(ACCOUNTS_FILE))) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) {
                    continue;   // skip empty lines
                }
                String[] parts = line.split("\\|", -1);   // -1 keeps empty values at the end
                if (parts.length >= 7) {
                    accounts.add(parts);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Cannot read the file: " + ACCOUNTS_FILE, "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return accounts;
    }

    // check the email, password and role against the accounts in the text file
    public boolean checkAdminCredentials(String username, String password, String role) {
        loggedInId = "";
        ArrayList<String[]> accounts = loadAccounts();

        for (int i = 0; i < accounts.size(); i++) {
            String[] account = accounts.get(i);
            // account[0]=id, [1]=email, [2]=password, [3]=role
            if (account[1].equalsIgnoreCase(username.trim()) && account[2].equals(password) && account[3].equals(role)) {
                loggedInId = account[0];   // remember who logged in
                return true;
            }
        }
        return false;
    }
}
