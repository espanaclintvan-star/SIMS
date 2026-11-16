
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class schedule extends JPanel implements ActionListener{
    private mainPageStudent SMain;
	JTable sTable;
    JPanel cpanel;
    JPanel cheader;
    JPanel footer;
    JLabel header;
    JScrollPane scrollPane;
    JComboBox<String> days;
    DefaultTableModel sModel;
    
    public schedule(mainPageStudent MStudent){
        this.SMain = MStudent;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

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

         // the table model is read-only for students. The Course column is filled from data/schedule.txt
         sModel = new DefaultTableModel(cdata, cColumn) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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
        
        header = new JLabel("SCHEDULE");
            header.setForeground(Color.WHITE);
            header.setBounds(10,65,500,20);
            header.setFont(new Font("arial", Font.BOLD, 26));

        String[] roles = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        days = new JComboBox<>(roles);
            days.setBounds(100,110,216,25);
            days.addActionListener(this);
            
        cheader = new JPanel();
            cheader.setLayout(null);
            cheader.setBackground(new Color(0,0,98));
            cheader.setPreferredSize(new Dimension(0, 150));
            cheader.add(header);
            cheader.add(days);
        
        footer = new JPanel();
            footer.setLayout(null);
            footer.setBackground(new Color(0,0,98));
            footer.setPreferredSize(new Dimension(0, 50));    
            
        add(cpanel, BorderLayout.CENTER);
        add(cheader, BorderLayout.NORTH); 
        add(footer, BorderLayout.SOUTH);

        // show the schedule of the first day in the list (Monday)
        loadSchedule((String) days.getSelectedItem());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == days) {
    	    String selected = (String) days.getSelectedItem();
    	    
    	    loadSchedule(selected);   // show the schedule of the selected day
    	}
        
    }

    // ================= BACKEND (text file) =================
    // Every line of data/schedule.txt is one class:  studentId|day|time|course
    // The time must be the same text as in the Time column of the table, example: 7:00am - 7:30am
    final String SCHEDULE_FILE = "data/schedule.txt";

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

    // read the file and show the classes of the logged-in student for the chosen day
    public void loadSchedule(String day) {
        makeFileIfMissing();

        // clear the Course column first
        for (int i = 0; i < sModel.getRowCount(); i++) {
            sModel.setValueAt("", i, 1);
        }

        String myId = SMain.studentId;
        try (Scanner input = new Scanner(new File(SCHEDULE_FILE))) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", -1);
                // parts: 0=studentId 1=day 2=time 3=course
                if (parts.length >= 4 && parts[0].equals(myId) && parts[1].equalsIgnoreCase(day)) {
                    // find the row with the same time and write the course there
                    for (int i = 0; i < sModel.getRowCount(); i++) {
                        if (sModel.getValueAt(i, 0).toString().equals(parts[2])) {
                            sModel.setValueAt(parts[3], i, 1);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Cannot read the file: " + SCHEDULE_FILE, "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
