package paintbrushapp;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.*;


/**
 *
 * @author Mario & Marymnasr
 */
public class PaintBrushApp extends JFrame {
    
    public static void main(String[] args) {
      
        JFrame myFrame = new JFrame("Java Paint App by Mario & Mariam Nasr");
        MainToolBar mainBar = new MainToolBar();
        JPanel toolbarPanel = new JPanel();
        MyPanel myPanel = new MyPanel();
        
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.Y_AXIS));
        toolbarPanel.setBounds(200, 200, 40, 50);
        toolbarPanel.add(mainBar);
        
        myFrame.add(toolbarPanel, BorderLayout.SOUTH);
        myFrame.add(myPanel, BorderLayout.CENTER);
        
    
        myFrame.setSize(900, 500);
        myFrame.setLocationRelativeTo(null); 
        myFrame.setVisible(true);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    }
    
}

    
    

