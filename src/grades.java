
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class grades extends JPanel implements ActionListener{
    private mainPageStudent SMain;

    public grades(mainPageStudent MStudent){
        this.SMain = MStudent;
        
        setBackground(Color.WHITE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

}
