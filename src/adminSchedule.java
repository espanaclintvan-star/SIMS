
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class adminSchedule extends JPanel implements ActionListener{
    private mainPageAdmin AMain;
	JTable sTable;
    JPanel cpanel;
    JPanel cheader;
    JPanel average;
    JLabel header;
    JTextField searchbar;
    JButton searchbutton;
    JLabel Ssearch;
    JScrollPane scrollPane;
    DefaultTableModel sModel;
    
    public adminSchedule(mainPageAdmin MAdmin){
        this.AMain = MAdmin;
        setBackground(Color.white);
        setLayout(new BorderLayout(2, 2));

        String [] cColumn = {"Time", "Course"};
        Object[][] cdata = {
            {"7:00am - 7:30am", ""}, 
            {"7:30am - 8:00am", ""},
            {"8:00am - 8:30am", ""}, 
            {"8:30am - 9:00am", ""},
            {"9:00am - 9:30am", ""},
            {"9:30am - 10:00am", ""},
            {"10:00am - 10:30am", ""},
            {"10:30am - 11:00am", ""},
            {"11:00am - 11:30am", ""},
            {"11:30am - 12:00pm", ""},
            {"12:00pm - 12:30pm", ""},
            {"12:30pm - 1:00pm", ""},
            {"1:00pm - 1:30pm", ""},
            {"1:30pm - 2:00pm", ""},
            {"2:00pm - 2:30pm", ""},
            {"2:30pm - 3:00pm", ""},
            {"3:00pm - 3:30pm", ""},
            {"3:30pm - 4:00pm", ""},
            {"4:00pm - 4:30pm", ""},
            {"4:30pm - 5:00pm", ""},
            {"5:00pm - 5:30pm", ""},
            {"5:30pm - 6:00pm", ""},
            {"6:00pm - 6:30pm", ""},
            {"6:30pm - 7:00pm", ""}};

         // the Time column cannot be edited. The Course column is edited by the admin.
         sModel = new DefaultTableModel(cdata, cColumn) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
         sTable = new JTable(sModel);
            sTable.setFont(new Font("arial", Font.PLAIN, 15));
            sTable.setEnabled(true);
            sTable.setRowHeight(25);
            sTable.getColumnModel().getColumn(0).setPreferredWidth(150);
            sTable.getColumnModel().getColumn(1).setPreferredWidth(600);
            sTable.setBorder(null);
            
        scrollPane = new JScrollPane(sTable);
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
        
        header = new JLabel("SCHEDULE");
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
        // SEARCH button (or Enter in the search bar)
        if (e.getSource() == searchbutton || e.getSource() == searchbar) {
            searchSchedule();
        }
    }

    // ================= BACKEND (text file) =================
    // Every line of data/schedule.txt is one class:  studentId|day|time|course
    // How to use this page:
    //   1. Type a student ID, email or name in the search bar and click SEARCH (or press Enter).
    //      The Monday schedule is shown. To see another day, type the day after the student,
    //      example:  2024-0001 Tuesday    (typing only  Tuesday  changes the day of the same student).
    //   2. Type a course in the Course column to ADD or UPDATE a class. It saves automatically.
    //   3. Delete the text of a Course cell to DELETE that class.
    final String ACCOUNTS_FILE = "data/accounts.txt";
    final String SCHEDULE_FILE = "data/schedule.txt";
    final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    String currentStudentId = "";    // the student that was searched
    String currentDay = "Monday";    // the day that is shown
    String foundName = "";           // the name of that student
    boolean loading = false;         // true while the program (not the admin) is changing the table

    // connect the search bar and the table to the backend
    public void setupBackend() {
        makeFileIfMissing();
        searchbutton.addActionListener(this);
        searchbar.addActionListener(this);

        // when the admin finishes editing a Course cell, the class is saved or deleted
        sModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (loading) {
                    return;
                }
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1
                        && e.getFirstRow() >= 0 && e.getFirstRow() < sModel.getRowCount()) {
                    updateSchedule(e.getFirstRow());
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
            File file = new File(SCHEDULE_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot create the file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // read ALL lines of the schedule file (empty lines are skipped). Returns null if the file cannot be read.
    public ArrayList<String> readAllLines() {
        makeFileIfMissing();
        ArrayList<String> lines = new ArrayList<String>();
        try (Scanner input = new Scanner(new File(SCHEDULE_FILE))) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Cannot read the file: " + SCHEDULE_FILE, "File Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return lines;
    }

    // write ALL lines back to the schedule file
    public boolean writeAllLines(ArrayList<String> lines) {
        try (PrintWriter output = new PrintWriter(new FileWriter(SCHEDULE_FILE))) {
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

    // returns the full day name if the text is a day (example: "tue" is not accepted, "tuesday" is), otherwise an empty text
    public String matchDay(String text) {
        for (int i = 0; i < DAYS.length; i++) {
            if (DAYS[i].equalsIgnoreCase(text.trim())) {
                return DAYS[i];
            }
        }
        return "";
    }

    // read the search bar, find the student (and the day) and show the schedule
    public void searchSchedule() {
        String text = searchbar.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a student ID, email or name to search.", "Search", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // the day can be typed after the student. If there is no day, Monday is shown.
        String day = "Monday";
        String studentText = text;
        int lastSpace = text.lastIndexOf(' ');
        String lastWord = text.substring(lastSpace + 1);   // the whole text if there is no space
        if (!matchDay(lastWord).isEmpty()) {
            day = matchDay(lastWord);
            if (lastSpace >= 0) {
                studentText = text.substring(0, lastSpace).trim();
            } else {
                studentText = "";   // only a day was typed
            }
        }

        String id = currentStudentId;
        if (studentText.isEmpty()) {
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Type a student ID, email or name first.\nExample: 2024-0001 Tuesday", "Search", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            id = findStudentId(studentText);
            if (id.isEmpty()) {
                return;
            }
        }

        currentStudentId = id;
        currentDay = day;
        loadSchedule(id, day);
        JOptionPane.showMessageDialog(this, "Showing the " + day + " schedule of " + foundName + " (" + id + ").\nTo see another day, type the day after the student.\nExample: " + id + " Tuesday",
                "Search", JOptionPane.INFORMATION_MESSAGE);
    }

    // read the classes of one student for one day and show them in the table
    public void loadSchedule(String studentId, String day) {
        ArrayList<String> lines = readAllLines();
        if (lines == null) {
            return;
        }
        loading = true;
        // clear the Course column first
        for (int i = 0; i < sModel.getRowCount(); i++) {
            sModel.setValueAt("", i, 1);
        }
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", -1);
            // parts: 0=studentId 1=day 2=time 3=course
            if (parts.length >= 4 && parts[0].equals(studentId) && parts[1].equalsIgnoreCase(day)) {
                for (int r = 0; r < sModel.getRowCount(); r++) {
                    if (sModel.getValueAt(r, 0).toString().equals(parts[2])) {
                        sModel.setValueAt(parts[3], r, 1);
                    }
                }
            }
        }
        loading = false;
    }

    // called after the admin edits a Course cell: add / update the class, or delete it if the cell is empty
    public void updateSchedule(int row) {
        String time = sModel.getValueAt(row, 0).toString();
        String course = "";
        if (sModel.getValueAt(row, 1) != null) {
            course = sModel.getValueAt(row, 1).toString().trim();
        }

        if (currentStudentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Search for a student first.", "Input Error", JOptionPane.WARNING_MESSAGE);
            loading = true;
            sModel.setValueAt("", row, 1);
            loading = false;
            return;
        }
        if (course.contains("|")) {
            JOptionPane.showMessageDialog(this, "The character | is not allowed.", "Input Error", JOptionPane.WARNING_MESSAGE);
            loadSchedule(currentStudentId, currentDay);   // put back what is saved
            return;
        }

        boolean done;
        if (course.isEmpty()) {
            done = deleteSchedule(time);
        } else {
            done = saveSchedule(time, course);
        }
        if (!done) {
            loadSchedule(currentStudentId, currentDay);   // saving failed, so show what is really saved
        }
    }

    // save one class (student + day + time). An old class at the same time is replaced, all other lines are kept.
    public boolean saveSchedule(String time, String course) {
        ArrayList<String> lines = readAllLines();
        if (lines == null) {
            return false;
        }
        ArrayList<String> newLines = new ArrayList<String>();
        String newRecord = currentStudentId + "|" + currentDay + "|" + time + "|" + course;
        boolean replaced = false;

        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", -1);
            boolean sameSlot = parts.length >= 4 && parts[0].equals(currentStudentId)
                    && parts[1].equalsIgnoreCase(currentDay) && parts[2].equals(time);
            if (sameSlot && !replaced) {
                newLines.add(newRecord);   // update
                replaced = true;
            } else if (!sameSlot) {
                newLines.add(lines.get(i));   // keep
            }
        }
        if (!replaced) {
            newLines.add(newRecord);   // new class
        }
        return writeAllLines(newLines);
    }

    // delete the class of the current student, day and time. All other lines are kept.
    public boolean deleteSchedule(String time) {
        ArrayList<String> lines = readAllLines();
        if (lines == null) {
            return false;
        }
        ArrayList<String> newLines = new ArrayList<String>();
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", -1);
            boolean sameSlot = parts.length >= 4 && parts[0].equals(currentStudentId)
                    && parts[1].equalsIgnoreCase(currentDay) && parts[2].equals(time);
            if (!sameSlot) {
                newLines.add(lines.get(i));
            }
        }
        if (newLines.size() == lines.size()) {
            return true;   // nothing was saved for this time, so nothing to delete
        }
        return writeAllLines(newLines);
    }
}
