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
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class profile extends JPanel implements ActionListener {

    private mainPageStudent SMain;
    JLabel header;
    JPanel addheader;
    JPanel addCenter;
    JLabel sname;
    JTextField name;
    JLabel syear;
    JTextField year;
    JLabel sstatus;
    JTextField status;
    JLabel saddress;
    JTextField address;
    JLabel scontact;
    JTextField contact;
    JLabel sId;
    JTextField Id;
    JButton Eaddress;
    JButton Econtact;

   

    public profile (mainPageStudent MStudent){
        this.SMain = MStudent;
        setBackground(Color.white);
        setLayout(new BorderLayout());

        header = new JLabel("PROFILE");
            header.setForeground(Color.WHITE);
            header.setBounds(10,65,500,20);
            header.setFont(new Font("arial", Font.BOLD, 26));
            

        addheader = new JPanel();
            addheader.setLayout(null);
            addheader.setBackground(new Color(0,0,98));
            addheader.setPreferredSize(new Dimension(0, 150));
            addheader.add(header);

        sname = new JLabel("NAME");
            sname.setBounds(100, 30, 200, 100);
            sname.setForeground(Color.white);

        name = new JTextField();
            name.setBounds(100, 100, 250, 30);
            name.setBorder(null);

        syear = new JLabel("YEAR");
            syear.setBounds(100, 100, 200, 100);
            syear.setForeground(Color.white);

        year = new JTextField();
            year.setBounds(100, 170, 250, 30);
            year.setBorder(null);

        sstatus = new JLabel("STATUS");
            sstatus.setBounds(100, 170, 200, 100);
            sstatus.setForeground(Color.white);

        status = new JTextField();
            status.setBounds(100, 240, 250, 30);
            status.setBorder(null);

        saddress = new JLabel("ADDRESS");
            saddress.setBounds(600, 30, 200, 100);
            saddress.setForeground(Color.white);

        address = new JTextField();
            address.setBounds(600, 100, 250, 30);
            address.setBorder(null);

        scontact = new JLabel("CONTACT NUMBER");
            scontact.setBounds(600, 100, 200, 100);
            scontact.setForeground(Color.white);

        contact = new JTextField();
            contact.setBounds(600, 170, 250, 30);
            contact.setBorder(null);
            
        sId = new JLabel("ID NUMBER");
        sId.setBounds(600, 170, 200, 100);
        sId.setForeground(Color.white);

        Id = new JTextField();
        Id.setBounds(600, 240, 250, 30);
        Id.setBorder(null);

        Eaddress = new JButton("EDIT");
            Eaddress.setBackground(new Color(0,0,98));
            Eaddress.setBorder(null);
            Eaddress.setForeground(Color.white);
            Eaddress.setFocusable(false);
            Eaddress.setBounds(855, 100, 60, 30);
            Eaddress.setFont(new Font(null, Font.BOLD, 10));
            Eaddress.addActionListener(this);
            
        Econtact = new JButton("EDIT");
            Econtact.setBackground(new Color(0,0,98));
            Econtact.setBorder(null);
            Econtact.setFocusable(false);
            Econtact.setForeground(Color.white);
            Econtact.setBounds(855, 170, 60, 30);
            Econtact.setFont(new Font(null, Font.BOLD, 10));
            Econtact.addActionListener(this);


        addCenter = new JPanel();
            addCenter.setLayout(null);
            addCenter.setBackground(new Color(120,0,98));
            addCenter.setPreferredSize(new Dimension(0, 200));
            addCenter.add(sname);   
            addCenter.add(name);
            addCenter.add(syear);
            addCenter.add(year);
            addCenter.add(sstatus);
            addCenter.add(status);
            addCenter.add(saddress);
            addCenter.add(address);
            addCenter.add(scontact);
            addCenter.add(contact);
            addCenter.add(sId);
            addCenter.add(Id);
            addCenter.add(Eaddress);
            addCenter.add(Econtact);
            
            
      
        
        add(addheader, BorderLayout.NORTH);
        add(addCenter, BorderLayout.CENTER);

        // name, year, status and ID are only shown (the admin sets them). The white background stays the same.
        name.setEditable(false);
        year.setEditable(false);
        status.setEditable(false);
        Id.setEditable(false);
        name.setBackground(Color.WHITE);
        year.setBackground(Color.WHITE);
        status.setBackground(Color.WHITE);
        Id.setBackground(Color.WHITE);
        // address and contact number can only be changed with the EDIT buttons
        address.setEditable(false);
        contact.setEditable(false);
        address.setBackground(Color.WHITE);
        contact.setBackground(Color.WHITE);

        // read the profile of the logged-in student from the text files
        loadProfile();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Eaddress) {
            if (Eaddress.getText().equals("EDIT")) {
                address.setEditable(true);
                Eaddress.setText("SAVE");
            } else if (saveProfile()) {
                JOptionPane.showMessageDialog(this, "Address saved!", "SAVED", JOptionPane.INFORMATION_MESSAGE);
                address.setEditable(false);
                Eaddress.setText("EDIT");
            }
        }

        if (e.getSource() == Econtact) {
            if (Econtact.getText().equals("EDIT")) {
                contact.setEditable(true);
                Econtact.setText("SAVE");
            } else if (saveProfile()) {
                JOptionPane.showMessageDialog(this, "Contact Number saved!", "SAVED", JOptionPane.INFORMATION_MESSAGE);
                contact.setEditable(false);
                Econtact.setText("EDIT");
            }
        }
    }

    // ================= BACKEND (text file) =================
    // accounts.txt (written by the admin):  id|email|password|role|name|year|status   -> name, year, status, ID
    // profiles.txt (the owner can edit):    id|address|contact                         -> address, contact
    final String ACCOUNTS_FILE = "data/accounts.txt";
    final String PROFILE_FILE = "data/profiles.txt";

    // create the data folder and profiles.txt if they do not exist yet
    public void makeFileIfMissing() {
        try {
            File folder = new File("data");
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(PROFILE_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot create the file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // read the profile of the logged-in user and show it in the text fields
    public void loadProfile() {
        makeFileIfMissing();
        String myId = SMain.studentId;

        // the name, year, status and ID come from accounts.txt
        try (Scanner input = new Scanner(new File(ACCOUNTS_FILE))) {
            while (input.hasNextLine()) {
                String[] parts = input.nextLine().trim().split("\\|", -1);
                if (parts.length >= 7 && parts[0].equals(myId)) {
                    Id.setText(parts[0]);
                    name.setText(parts[4]);
                    year.setText(parts[5]);
                    status.setText(parts[6]);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            // no accounts file, so the fields stay empty
        }

        // the address and contact number come from profiles.txt
        try (Scanner input = new Scanner(new File(PROFILE_FILE))) {
            while (input.hasNextLine()) {
                String[] parts = input.nextLine().trim().split("\\|", -1);
                if (parts.length >= 3 && parts[0].equals(myId)) {
                    address.setText(parts[1]);
                    contact.setText(parts[2]);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            // no profile saved yet, so the fields stay empty
        }
    }

    // check the address and contact number, then save them in profiles.txt.
    // Only the line of this user is changed. Returns true if it was saved.
    public boolean saveProfile() {
        String myId = SMain.studentId;
        String newAddress = address.getText().trim();
        String newContact = contact.getText().trim();

        if (newAddress.contains("|")) {
            JOptionPane.showMessageDialog(this, "The character | is not allowed in the address.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        // the contact number can only have digits, spaces and + - ( )
        for (int i = 0; i < newContact.length(); i++) {
            char c = newContact.charAt(i);
            if (!Character.isDigit(c) && c != ' ' && c != '+' && c != '-' && c != '(' && c != ')') {
                JOptionPane.showMessageDialog(this, "The contact number can only have numbers, spaces, + - ( ).", "Input Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        makeFileIfMissing();
        ArrayList<String> lines = new ArrayList<String>();
        String newLine = myId + "|" + newAddress + "|" + newContact;
        boolean found = false;

        // copy all profiles, replace the line of this user
        try (Scanner input = new Scanner(new File(PROFILE_FILE))) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                if (parts[0].equals(myId)) {
                    lines.add(newLine);
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Cannot read the file: " + PROFILE_FILE, "File Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!found) {
            lines.add(newLine);   // first time this user saves a profile
        }

        try (PrintWriter output = new PrintWriter(new FileWriter(PROFILE_FILE))) {
            for (int i = 0; i < lines.size(); i++) {
                output.println(lines.get(i));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot save the file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
