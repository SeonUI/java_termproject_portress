package TP;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame{
	 public static final int WIDTH  = 1000;
     public static final int HEIGHT = 700;
     public MainPanel panel1;
     public GamePanel panel2;
     public static void main(String[] args) {
    	 MainFrame newgame = new MainFrame();
 		//GraphicPainter GP = new GraphicPainter(newgame);
 	}
	 public MainFrame(){
	        setTitle("Fortress"); 
	        setSize(WIDTH,HEIGHT); 
	        setResizable(false); 
	        setLayout(null);

	        panel1 = new MainPanel(this);
	        panel1.setBounds(0,0,WIDTH,HEIGHT);
	        add(panel1);
	        
	        panel2 = new GamePanel(this);
	        panel2.setBounds(0,0,WIDTH,HEIGHT);
	        add(panel2);
	        panel2.setVisible(false);
	        
	        
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        setVisible(true);
	 }
}

