
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class grades extends JPanel implements ActionListener{
    private mainPageStudent SMain;
	JTable gTable;
    JTable BTable;
    JPanel Gpanel;
    JPanel Gheader;
    JPanel average;
    JLabel header;

    public grades(mainPageStudent MStudent){
        this.SMain = MStudent;
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        String [] TColumn = {"COURSE", "GRADE"};
        Object[][] data = {
            {"Programming Fundamentals", 1.25},
            {"Database Management Systems", 3.00},
            {"Web Development", 2.00},
            {"Computer Networks", 4.00}};

        double ave = 0 ;
        for (int i=0; i<data.length; i++) {
            ave += (Double) data[i][1];
        }
        ave = ave / data.length;
        String fAve = String.format("%.2f", ave);

        int units = data.length;

        String [] BColumn = {"Gr1", "Gr2"};
        Object[][] Bdata = {
            {"Average: ",fAve},
            {"Total Course Taken: ", units}};
            
        
        gTable = new JTable(data, TColumn);
            gTable.setBounds(100,100, 900, 200);
            gTable.setFont(new Font("arial", Font.PLAIN, 15));
            gTable.setEnabled(false);
            gTable.setRowHeight(25);
            gTable.getTableHeader().setBounds(100, 75, 900, 25);
        
        BTable = new JTable(Bdata, BColumn);
            BTable.setBounds(100,0, 900, 50);
            BTable.setFont(new Font("arial", Font.PLAIN, 15));
            BTable.setEnabled(false);
            BTable.setRowHeight(25);
                
        Gpanel = new JPanel();
            Gpanel.setLayout(null);
            Gpanel.setPreferredSize(new Dimension(200, 100));
            Gpanel.setBackground(Color.lightGray);
            Gpanel.add(gTable.getTableHeader());
            Gpanel.add(gTable);

        header = new JLabel("GRADES");
            header.setForeground(Color.WHITE);
            header.setBounds(10,65,500,20);
            header.setFont(new Font("arial", Font.BOLD, 26));
        Gheader = new JPanel();
            Gheader.setLayout(null);
            Gheader.setBackground(Color.GRAY);
            Gheader.setPreferredSize(new Dimension(0, 150));
            Gheader.add(header);
        
        average = new JPanel();
            average.setLayout(null);
            average.setBackground(Color.GRAY);
            average.setPreferredSize(new Dimension(0, 50));    
            average.add(BTable);
        
        add(Gpanel, BorderLayout.CENTER);
        add(Gheader, BorderLayout.NORTH); 
        add(average, BorderLayout.SOUTH);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

}
