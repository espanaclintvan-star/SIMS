
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

public class clearance extends JPanel implements ActionListener{
    private mainPageStudent SMain;
	JTable cTable;
    JPanel cpanel;
    JPanel cheader;
    JPanel average;
    JLabel header;
    JScrollPane scrollPane;
    DefaultTableModel cModel;
    
    public clearance(mainPageStudent MStudent){
        this.SMain = MStudent;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        String [] cColumn = {"clearance", "Status"};
        // the table model is read-only for students. The rows come from data/clearance.txt
        cModel = new DefaultTableModel(cColumn, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

         cTable = new JTable(cModel);
            cTable.setFont(new Font("arial", Font.PLAIN, 15));
            cTable.setEnabled(false);
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

        header = new JLabel("CLEARANCE");
            header.setForeground(Color.WHITE);
            header.setBounds(10,65,500,20);
            header.setFont(new Font("arial", Font.BOLD, 26));
            
        cheader = new JPanel();
            cheader.setLayout(null);
            cheader.setBackground(new Color(0,0,98));
            cheader.setPreferredSize(new Dimension(0, 150));
            cheader.add(header);
        
        average = new JPanel();
            average.setLayout(null);
            average.setBackground(new Color(0,0,98));
            average.setPreferredSize(new Dimension(0, 50));    
        
        add(cpanel, BorderLayout.CENTER);
        add(cheader, BorderLayout.NORTH); 
        add(average, BorderLayout.SOUTH);

        // read the logged-in student's records from the text file
        loadClearance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }


    // ================= BACKEND (text file) =================
    // Every line of data/clearance.txt is one record:  studentId|clearance|status
    final String RECORD_FILE = "data/clearance.txt";

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
    public void loadClearance() {
        makeFileIfMissing();
        cModel.setRowCount(0);
        String myId = SMain.studentId;

        try (Scanner input = new Scanner(new File(RECORD_FILE))) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) {
                    continue;   // skip empty lines
                }
                String[] parts = line.split("\\|", -1);   // -1 keeps empty values at the end
                if (parts.length >= 3 && parts[0].equals(myId)) {
                    cModel.addRow(new Object[] { parts[1], parts[2] });
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Cannot read the file: " + RECORD_FILE, "File Error", JOptionPane.ERROR_MESSAGE);
        }

        // keep the table the same size as before by adding empty rows
        while (cModel.getRowCount() < 12) {
            cModel.addRow(new Object[] { "", "" });
        }

    }

}
