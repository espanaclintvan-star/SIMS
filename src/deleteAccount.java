import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class deleteAccount extends JPanel implements ActionListener {

    private mainPageAdmin AMain;
    JLabel header;
    JPanel deleteheader;
    JPanel deleteCenter;
    JLabel student;
    JLabel admin;
    JTable studentTable;
    JTable adminTable;
    DefaultTableModel studentModel;
    DefaultTableModel adminModel;
    JScrollPane Spane;
    JScrollPane Apane;
    JButton sDelete;
    JButton aDelete;

    public deleteAccount(mainPageAdmin MAdmin) {
        this.AMain = MAdmin;
        setBackground(Color.white);
        setLayout(new BorderLayout(2, 2));

        header = new JLabel("DELETE ACCOUNT");
            header.setForeground(Color.WHITE);
            header.setBounds(10, 65, 500, 20);
            header.setFont(new Font("arial", Font.BOLD, 26));

        deleteheader = new JPanel();
            deleteheader.setLayout(null);
            deleteheader.setBackground(new Color(0, 0, 98));
            deleteheader.setPreferredSize(new Dimension(0, 150));
            deleteheader.add(header);

        // --- STUDENT SECTION ---
        student = new JLabel("EXISTING STUDENT ACCOUNTS");
            student.setBounds(50, 10, 250, 30);
            student.setForeground(Color.white);
            student.setFont(new Font("arial", Font.BOLD, 15));

        String[] studentCols = {"ID", "Name", "Year", "Status", "Email"};
        studentModel = new DefaultTableModel(studentCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(studentModel);
            studentTable.setRowHeight(25);

        Spane = new JScrollPane(studentTable);
            Spane.setBounds(30, 45, 420, 320);

        sDelete = new JButton("DELETE");
            sDelete.setBounds(460, 45, 85, 35);
            sDelete.setBackground(new Color(0, 0, 98));
            sDelete.setFocusable(false);
            sDelete.setForeground(Color.WHITE);
            sDelete.setFont(new Font("arial", Font.BOLD, 11));
            sDelete.setBorder(null);
            sDelete.addActionListener(this);

        // --- ADMIN SECTION ---
        admin = new JLabel("EXISTING ADMIN ACCOUNTS");
            admin.setBounds(600, 10, 250, 30);
            admin.setForeground(Color.white);
            admin.setFont(new Font("arial", Font.BOLD, 15));

        String[] adminCols = {"ID", "Name", "Status", "Email"};
        adminModel = new DefaultTableModel(adminCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        adminTable = new JTable(adminModel);
            adminTable.setRowHeight(25);

        Apane = new JScrollPane(adminTable);
            Apane.setBounds(580, 45, 400, 320);

        aDelete = new JButton("DELETE");
            aDelete.setBounds(990, 45, 85, 35);
            aDelete.setBackground(new Color(0, 0, 98));
            aDelete.setFocusable(false);
            aDelete.setForeground(Color.WHITE);
            aDelete.setFont(new Font("arial", Font.BOLD, 11));
            aDelete.setBorder(null);
            aDelete.addActionListener(this);

        deleteCenter = new JPanel();
            deleteCenter.setLayout(null);
            deleteCenter.setBackground(new Color(120, 0, 98));
            deleteCenter.add(student);
            deleteCenter.add(Spane);
            deleteCenter.add(sDelete);
            deleteCenter.add(admin);
            deleteCenter.add(Apane);
            deleteCenter.add(aDelete);

        add(deleteheader, BorderLayout.NORTH);
        add(deleteCenter, BorderLayout.CENTER);

        // show the accounts that are saved in the text file
        loadAccounts();
      
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sDelete) {
            deleteSelected(studentTable, studentModel);
        }
        if (e.getSource() == aDelete) {
            deleteSelected(adminTable, adminModel);
        }
    }

    // ================= BACKEND (text file) =================
    // Every line of data/accounts.txt is one account:  id|email|password|role|name|year|status
    final String ACCOUNTS_FILE = "data/accounts.txt";

    // These files also have records that belong to an account (the first value of each line is the ID).
    // When an account is deleted, its records are deleted too, so they will not show up for a new account with the same ID.
    final String[] RECORD_FILES = {"data/grades.txt", "data/enrollment.txt", "data/clearance.txt", "data/schedule.txt", "data/profiles.txt"};

    // read all lines of a text file (empty lines are skipped). A missing file gives an empty list.
    public ArrayList<String> readLines(String fileName) {
        ArrayList<String> lines = new ArrayList<String>();
        File file = new File(fileName);
        if (!file.exists()) {
            return lines;
        }
        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot read the file: " + fileName, "File Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return lines;
    }

    // write all lines to a text file
    public boolean writeLines(String fileName, ArrayList<String> lines) {
        try (PrintWriter output = new PrintWriter(new FileWriter(fileName))) {
            for (int i = 0; i < lines.size(); i++) {
                output.println(lines.get(i));
            }
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot save the file: " + fileName, "File Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // read accounts.txt and show the students and the admins in their own table
    public void loadAccounts() {
        studentModel.setRowCount(0);
        adminModel.setRowCount(0);

        ArrayList<String> lines = readLines(ACCOUNTS_FILE);
        if (lines == null) {
            return;
        }
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", -1);
            if (parts.length < 7) {
                continue;
            }
            // parts: 0=id 1=email 2=password 3=role 4=name 5=year 6=status
            if (parts[3].equals("student")) {
                studentModel.addRow(new Object[] { parts[0], parts[4], parts[5], parts[6], parts[1] });
            } else if (parts[3].equals("admin")) {
                adminModel.addRow(new Object[] { parts[0], parts[4], parts[6], parts[1] });
            }
        }
    }

    // delete the account that is selected in the given table
    public void deleteSelected(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an account first.", "Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = model.getValueAt(row, 0).toString();
        String name = model.getValueAt(row, 1).toString();

        // an admin cannot delete the account that is logged in
        if (id.equals(AMain.adminId)) {
            JOptionPane.showMessageDialog(this, "You cannot delete the account you are logged in with.", "Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int answer = JOptionPane.showConfirmDialog(this, "Delete the account of " + name + " (" + id + ")?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        if (deleteAccountFromFile(id)) {
            loadAccounts();
            JOptionPane.showMessageDialog(this, "Account deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // remove the account with this ID from accounts.txt. All other accounts are kept.
    // The records of this ID in the other text files are removed too.
    public boolean deleteAccountFromFile(String id) {
        if (!removeLinesOfId(ACCOUNTS_FILE, id)) {
            return false;
        }
        for (int i = 0; i < RECORD_FILES.length; i++) {
            removeLinesOfId(RECORD_FILES[i], id);
        }
        return true;
    }

    // rewrite a file without the lines whose first value is the given ID
    public boolean removeLinesOfId(String fileName, String id) {
        ArrayList<String> lines = readLines(fileName);
        if (lines == null) {
            return false;
        }
        ArrayList<String> newLines = new ArrayList<String>();
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", -1);
            if (!parts[0].equals(id)) {
                newLines.add(lines.get(i));
            }
        }
        if (newLines.size() == lines.size()) {
            return true;   // nothing to delete, so the file does not need to be rewritten
        }
        return writeLines(fileName, newLines);
    }
}
