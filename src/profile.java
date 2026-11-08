import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
   

    public profile (mainPageStudent MStudent){
        this.SMain = MStudent;
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());

        header = new JLabel("PROFILE");
            header.setForeground(Color.WHITE);
            header.setBounds(10,65,500,20);
            header.setFont(new Font("arial", Font.BOLD, 26));
            

        addheader = new JPanel();
            addheader.setLayout(null);
            addheader.setBackground(Color.GRAY);
            addheader.setPreferredSize(new Dimension(0, 150));
            addheader.add(header);

        sname = new JLabel("NAME");
            sname.setBounds(100, 30, 200, 100);
            sname.setForeground(Color.black);

        name = new JTextField();
            name.setBounds(100, 100, 250, 30);
            name.setBorder(null);

        syear = new JLabel("YEAR");
            syear.setBounds(100, 100, 200, 100);
            syear.setForeground(Color.black);

        year = new JTextField();
            year.setBounds(100, 170, 250, 30);
            year.setBorder(null);

        sstatus = new JLabel("STATUS");
            sstatus.setBounds(100, 170, 200, 100);
            sstatus.setForeground(Color.black);

        status = new JTextField();
            status.setBounds(100, 240, 250, 30);
            status.setBorder(null);

        saddress = new JLabel("ADDRESS");
            saddress.setBounds(600, 30, 200, 100);
            saddress.setForeground(Color.black);

        address = new JTextField();
            address.setBounds(600, 100, 250, 30);
            address.setBorder(null);

        scontact = new JLabel("CONTACT NUMBER");
            scontact.setBounds(600, 100, 200, 100);
            scontact.setForeground(Color.black);

        contact = new JTextField();
            contact.setBounds(600, 170, 250, 30);
            contact.setBorder(null);

        addCenter = new JPanel();
            addCenter.setLayout(null);
            addCenter.setBackground(Color.LIGHT_GRAY);
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
            
      
        
        add(addheader, BorderLayout.NORTH);
        add(addCenter, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


}
