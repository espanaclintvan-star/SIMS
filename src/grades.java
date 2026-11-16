
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class grades extends JPanel implements ActionListener {

    @SuppressWarnings("unused")
    private mainPageStudent SMain;
    JTable gTable;
    JTable BTable;
    JPanel Gpanel;
    JPanel Gheader;
    JPanel average;
    JLabel header;
    JScrollPane scrollPane;
    DefaultTableModel gModel;

    public grades(mainPageStudent MStudent) {
        this.SMain = MStudent;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        String[] TColumn = {"Course", "Grade"};
        // the table model is read-only for students. The rows come from data/grades.txt
        gModel = new DefaultTableModel(TColumn, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] BColumn = {"Gr1", "Gr2"};
        Object[][] Bdata = {
            {"Average: ", "0.00"},
            {"Total Course Taken: ", 0}};

        gTable = new JTable(gModel);
            gTable.setFont(new Font("arial", Font.PLAIN, 15));
            gTable.setEnabled(true);
            gTable.setRowHeight(25);
            gTable.getColumnModel().getColumn(0).setPreferredWidth(800);
            gTable.getColumnModel().getColumn(1).setPreferredWidth(100);

        BTable = new JTable(Bdata, BColumn);
            BTable.setBounds(100, 0, 900, 50);
            BTable.setFont(new Font("arial", Font.PLAIN, 15));
            BTable.setEnabled(false);
            BTable.setRowHeight(25);

        scrollPane = new JScrollPane(gTable);
            scrollPane.setBounds(100, 0, 900, 325);
            scrollPane.setBorder(null);

        Gpanel = new JPanel();
            Gpanel.setLayout(null);
            Gpanel.setPreferredSize(new Dimension(200, 100));
            Gpanel.setBackground(new Color(120,0,98));
            Gpanel.add(scrollPane);


        header = new JLabel("GRADES");
            header.setForeground(Color.WHITE);
            header.setBounds(10, 65, 500, 20);
            header.setFont(new Font("arial", Font.BOLD, 26));

        Gheader = new JPanel();
            Gheader.setLayout(null);
            Gheader.setBackground(new Color(0,0,98));
            Gheader.setPreferredSize(new Dimension(0, 150));
            Gheader.add(header);

        average = new JPanel();
            average.setLayout(null);
            average.setBackground(new Color(0,0,98));
            average.setPreferredSize(new Dimension(0, 50));
            average.add(BTable);

        add(Gpanel, BorderLayout.CENTER);
        add(Gheader, BorderLayout.NORTH);
        add(average, BorderLayout.SOUTH);

        // read the logged-in student's records from the text file
        loadGrades();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


    // ================= BACKEND (text file) =================
    // Every line of data/grades.txt is one record:  studentId|course|grade
    final String RECORD_FILE = "data/grades.txt";

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

    // read the file and show ONLY the records of the logged-in student in the table
    public void loadGrades() {
        makeFileIfMissing();
        gModel.setRowCount(0);
        String myId = SMain.studentId;

        try (Scanner input = new Scanner(new File(RECORD_FILE))) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) {
                    continue;   // skip empty lines
                }
                String[] parts = line.split("\\|", -1);   // -1 keeps empty values at the end
                if (parts.length >= 3 && parts[0].equals(myId)) {
                    gModel.addRow(new Object[] { parts[1], parts[2] });
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Cannot read the file: " + RECORD_FILE, "File Error", JOptionPane.ERROR_MESSAGE);
        }

        // keep the table the same size as before by adding empty rows
        while (gModel.getRowCount() < 10) {
            gModel.addRow(new Object[] { "", "" });
        }
        showAverage();
    }

    // compute the average and the number of courses, then show them in the small table below
    public void showAverage() {
        double total = 0;
        int numbers = 0;
        int courses = 0;
        for (int i = 0; i < gModel.getRowCount(); i++) {
            String course = gModel.getValueAt(i, 0).toString().trim();
            String grade = gModel.getValueAt(i, 1).toString().trim();
            if (!course.isEmpty()) {
                courses++;
                try {
                    total += Double.parseDouble(grade);
                    numbers++;
                } catch (NumberFormatException e) {
                    // a grade that is not a number is ignored in the average
                }
            }
        }
        double ave = 0;
        if (numbers > 0) {
            ave = total / numbers;
        }
        BTable.setValueAt(String.format("%.2f", ave), 0, 1);
        BTable.setValueAt(courses, 1, 1);
    }

}
