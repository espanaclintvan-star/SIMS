
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
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class adminClearance extends JPanel implements ActionListener{
    private mainPageAdmin AMain;
	JTable cTable;
    JPanel cpanel;
    JPanel cheader;
    JPanel average;
    JLabel header;
    JTextField searchbar;
    JButton searchbutton;
    JLabel Ssearch;
    JScrollPane scrollPane;
    DefaultTableModel cModel;
    
    public adminClearance(mainPageAdmin MAdmin){
        this.AMain = MAdmin;
        setBackground(Color.white);
        setLayout(new BorderLayout(2, 2));

        String [] cColumn = {"clearance", "Status"};
        // the table starts with empty rows. The records are loaded after the admin searches for a student.
        cModel = new DefaultTableModel(cColumn, 0);
        for (int i = 0; i < 12; i++) {
            addEmptyRow();
        }

         cTable = new JTable(cModel);
            cTable.setFont(new Font("arial", Font.PLAIN, 15));
            cTable.setEnabled(true);
            cTable.setRowHeight(25);
            cTable.getColumnModel().getColumn(0).setPreferredWidth(800);
            cTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            
        
        scrollPane = new JScrollPane(cTable);
            scrollPane.setBounds(100, 0, 900, 325);
            scrollPane.setBorder(null);

        cpanel = new JPanel();
            cpanel.setLayout(null);
            cpanel.setPreferredSize(new Dimension(200, 100));
            cpanel.setBackground(new Color(120,0,98));
            cpanel.add(scrollPane);

        searchbar = new JTextField();
            searchbar.setBounds(730, 100, 250, 30);
            searchbar.setBorder(null);

        searchbutton = new JButton("SEARCH");
            searchbutton.setBounds(990, 100, 80, 30);
            searchbutton.setBackground(new Color(120,0,98));
            searchbutton.setForeground(Color.WHITE);
            searchbutton.setFont(new Font(null, Font.PLAIN, 10));
            searchbutton.setFocusable(false);
            searchbutton.setBorder(null);
        
        Ssearch = new JLabel("Search Students");
            Ssearch.setBounds(730, 125, 100, 30);
            Ssearch.setForeground(Color.WHITE);
            Ssearch.setFont(new Font("null", Font.BOLD, 12));
        
        header = new JLabel("CLEARANCE");
            header.setForeground(Color.WHITE);
            header.setBounds(10,65,500,20);
            header.setFont(new Font("arial", Font.BOLD, 26));
            
        cheader = new JPanel();
            cheader.setLayout(null);
            cheader.setBackground(new Color(0,0,98));
            cheader.setPreferredSize(new Dimension(0, 150));
            cheader.add(header);
            cheader.add(searchbar);
            cheader.add(searchbutton);
            cheader.add(Ssearch);
        
        average = new JPanel();
            average.setLayout(null);
            average.setBackground(new Color(0,0,98));
            average.setPreferredSize(new Dimension(0, 50));    
        
        add(cpanel, BorderLayout.CENTER);
        add(cheader, BorderLayout.NORTH); 
        add(average, BorderLayout.SOUTH);

        // connect the page to the text file
        setupBackend();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // SEARCH button (or Enter in the search bar): find the student and show the records
        if (e.getSource() == searchbutton || e.getSource() == searchbar) {
            String text = searchbar.getText().trim();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a student ID, email or name to search.", "Search", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String id = findStudentId(text);
            if (id.isEmpty()) {
                return;
            }
            currentStudentId = id;
            loadClearance(id);
        }
    }


    // ================= BACKEND (text file) =================
    // Every line of data/clearance.txt is one record:  studentId|clearance|status
    // How to use this page:
    //   1. Type a student ID, email or name in the search bar and click SEARCH (or press Enter).
    //   2. Type in an empty row to ADD a record, or change a cell to UPDATE it. It saves automatically.
    //   3. Select a row and press the DELETE key on the keyboard to DELETE the record.
    final String ACCOUNTS_FILE = "data/accounts.txt";
    final String RECORD_FILE = "data/clearance.txt";
    String currentStudentId = "";    // the student that was searched
    String foundName = "";           // the name of that student
    boolean loading = false;         // true while the program (not the admin) is changing the table
    ArrayList<String> savedCourse = new ArrayList<String>();   // what is saved in the file for each table row
    ArrayList<String> savedValue = new ArrayList<String>();

    // connect the search bar, the table and the DELETE key to the backend
    public void setupBackend() {
        makeFileIfMissing();
        searchbutton.addActionListener(this);
        searchbar.addActionListener(this);

        // when the admin finishes editing a cell, the row is validated and saved
        cModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (loading) {
                    return;
                }
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() != TableModelEvent.ALL_COLUMNS
                        && e.getFirstRow() >= 0 && e.getFirstRow() < cModel.getRowCount()) {
                    updateClearance(e.getFirstRow());
                }
            }
        });

        // the DELETE key deletes the selected row
        cTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE && !cTable.isEditing()) {
                    deleteClearance(cTable.getSelectedRow());
                }
            }
        });
    }

    // create the data folder and the text file if they do not exist yet
    public void makeFileIfMissing() {
        try {
            File folder = new File("data");
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(RECORD_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot create the file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // read ALL lines of the record file (empty lines are skipped). Returns null if the file cannot be read.
    public ArrayList<String> readAllLines() {
        makeFileIfMissing();
        ArrayList<String> lines = new ArrayList<String>();
        try (Scanner input = new Scanner(new File(RECORD_FILE))) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Cannot read the file: " + RECORD_FILE, "File Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return lines;
    }

    // write ALL lines back to the record file
    public boolean writeAllLines(ArrayList<String> lines) {
        try (PrintWriter output = new PrintWriter(new FileWriter(RECORD_FILE))) {
            for (int i = 0; i < lines.size(); i++) {
                output.println(lines.get(i));
            }
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot save the file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // look in accounts.txt for a STUDENT account. The text can be the ID, the email or (part of) the name.
    // Returns the student ID, or an empty text if nothing (or more than one student) was found.
    public String findStudentId(String text) {
        String search = text.trim().toLowerCase();
        String foundId = "";
        String firstName = "";
        int matches = 0;

        try (Scanner input = new Scanner(new File(ACCOUNTS_FILE))) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                // accounts.txt format: id|email|password|role|name|year|status
                String[] parts = line.split("\\|", -1);
                if (parts.length < 7 || !parts[3].equals("student")) {
                    continue;
                }
                if (parts[0].toLowerCase().equals(search) || parts[1].toLowerCase().equals(search)) {
                    foundName = parts[4];
                    return parts[0];   // exact ID or email
                }
                if (parts[4].toLowerCase().contains(search)) {
                    foundId = parts[0];
                    firstName = parts[4];
                    matches++;
                }
            }
        } catch (FileNotFoundException e) {
            // no accounts file yet, so no student can be found
        }

        if (matches == 1) {
            foundName = firstName;
            return foundId;
        }
        if (matches > 1) {
            JOptionPane.showMessageDialog(this, "More than one student matches. Please type the student ID.", "Search", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No student found.", "Search", JOptionPane.WARNING_MESSAGE);
        }
        return "";
    }

    // read the records of one student from the file and show them in the table
    public void loadClearance(String studentId) {
        ArrayList<String> lines = readAllLines();
        if (lines == null) {
            return;
        }
        loading = true;
        cModel.setRowCount(0);
        savedCourse.clear();
        savedValue.clear();
        int count = 0;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", -1);
            if (parts.length >= 3 && parts[0].equals(studentId)) {
                cModel.addRow(new Object[] { parts[1], parts[2] });
                savedCourse.add(parts[1]);
                savedValue.add(parts[2]);
                count++;
            }
        }
        addBlankRows();
        loading = false;


        if (count == 0) {
            JOptionPane.showMessageDialog(this, "No clearance records yet for " + foundName + " (" + studentId + ").\nType in an empty row to add one.", "Search", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // add one empty row to the table (and to the "saved" lists so they stay the same size)
    public void addEmptyRow() {
        cModel.addRow(new Object[] { "", "" });
        savedCourse.add("");
        savedValue.add("");
    }

    // keep at least 12 rows and always one empty row at the end so a new record can be typed
    public void addBlankRows() {
        while (cModel.getRowCount() < 12) {
            addEmptyRow();
        }
        int last = cModel.getRowCount() - 1;
        if (!cellText(last, 0).isEmpty() || !cellText(last, 1).isEmpty()) {
            addEmptyRow();
        }
    }

    // get the text of one cell without spaces at the start and end
    public String cellText(int row, int column) {
        Object value = cModel.getValueAt(row, column);
        if (value == null) {
            return "";
        }
        return value.toString().trim();
    }

    // put the row back to what is saved in the file (used when the input is not valid)
    public void restoreRow(int row) {
        loading = true;
        cModel.setValueAt(savedCourse.get(row), row, 0);
        cModel.setValueAt(savedValue.get(row), row, 1);
        loading = false;
    }

    // called after the admin edits a row: check the input, then save it (add or update)
    public void updateClearance(int row) {
        if (currentStudentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Search for a student first.", "Input Error", JOptionPane.WARNING_MESSAGE);
            restoreRow(row);
            return;
        }

        String course = cellText(row, 0);
        String value = cellText(row, 1);
        String oldCourse = savedCourse.get(row);

        // a record needs both columns. A new row may still be half typed, so just wait for the other cell.
        if (course.isEmpty() || value.isEmpty()) {
            if (!oldCourse.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Clearance and Status cannot be empty.\nTo remove a record, select the row and press the DELETE key.", "Input Error", JOptionPane.WARNING_MESSAGE);
                restoreRow(row);
            }
            return;
        }

        // the | character is used to separate the values in the file
        if (course.contains("|") || value.contains("|")) {
            JOptionPane.showMessageDialog(this, "The character | is not allowed.", "Input Error", JOptionPane.WARNING_MESSAGE);
            restoreRow(row);
            return;
        }

        // the same student cannot have the same clearance twice
        for (int i = 0; i < cModel.getRowCount(); i++) {
            if (i != row && cellText(i, 0).equalsIgnoreCase(course)) {
                JOptionPane.showMessageDialog(this, course + " is already in the list of this student.", "Input Error", JOptionPane.WARNING_MESSAGE);
                restoreRow(row);
                return;
            }
        }

        if (saveClearance(oldCourse, course, value)) {
            savedCourse.set(row, course);
            savedValue.set(row, value);
            addBlankRows();
        } else {
            restoreRow(row);
        }
    }

    // write one record of the current student to the file.
    // oldCourse is empty for a NEW record, otherwise the old record is replaced. Other lines are not changed.
    public boolean saveClearance(String oldCourse, String course, String value) {
        ArrayList<String> lines = readAllLines();
        if (lines == null) {
            return false;
        }
        ArrayList<String> newLines = new ArrayList<String>();
        String newRecord = currentStudentId + "|" + course + "|" + value;
        boolean replaced = false;

        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", -1);
            if (!replaced && !oldCourse.isEmpty() && parts.length >= 3
                    && parts[0].equals(currentStudentId) && parts[1].equalsIgnoreCase(oldCourse)) {
                newLines.add(newRecord);   // update the old record
                replaced = true;
            } else {
                newLines.add(lines.get(i));   // keep every other record
            }
        }
        if (!replaced) {
            newLines.add(newRecord);   // new record
        }
        return writeAllLines(newLines);
    }

    // delete the selected row (and its line in the file) after the admin confirms
    public void deleteClearance(int row) {
        if (row < 0 || row >= cModel.getRowCount()) {
            JOptionPane.showMessageDialog(this, "Select a row first.", "Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (currentStudentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Search for a student first.", "Delete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String oldCourse = savedCourse.get(row);
        if (!oldCourse.isEmpty()) {
            int answer = JOptionPane.showConfirmDialog(this, "Delete " + oldCourse + " of " + foundName + " (" + currentStudentId + ")?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) {
                return;
            }
            ArrayList<String> lines = readAllLines();
            if (lines == null) {
                return;
            }
            // keep every line except the one that is being deleted
            ArrayList<String> newLines = new ArrayList<String>();
            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split("\\|", -1);
                boolean isThisRecord = parts.length >= 3 && parts[0].equals(currentStudentId) && parts[1].equalsIgnoreCase(oldCourse);
                if (!isThisRecord) {
                    newLines.add(lines.get(i));
                }
            }
            if (!writeAllLines(newLines)) {
                return;
            }
        }

        // remove the row from the table
        loading = true;
        cModel.removeRow(row);
        savedCourse.remove(row);
        savedValue.remove(row);
        addBlankRows();
        loading = false;

    }

}
