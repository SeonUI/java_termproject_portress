package TP;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainPanel extends JPanel {
	private final int BUT_W = 200;
    private final int BUT_H = 80;
    private final int TEXT_W = 400;
    private final int TEXT_H = 40;
    private final int titleSize = 20;
    public JLabel lab1;
    public JLabel lab2;
    public JLabel lab3;
    public JLabel EnterLab;
    public JLabel EnterLab2;
    public JButton btn1;
    public JButton btn2;
    public JButton EnterBtn;
    public JTextField text;
    
	public MainPanel(MainFrame frame) {
		int WIDTH = MainFrame.WIDTH;
		int HEIGHT = MainFrame.HEIGHT;
		setSize(WIDTH,HEIGHT);
		setLayout(null);
		JLayeredPane lp = new JLayeredPane();
		
		Image titleImg = null;
        try {
			titleImg = ImageIO.read(new File("Fortress.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        titleImg = titleImg.getScaledInstance(30*titleSize,10*titleSize,java.awt.Image.SCALE_SMOOTH);
        
        lab1 = new JLabel(new ImageIcon(titleImg));
        lab1.setBounds((WIDTH)/2-30*titleSize/2, HEIGHT/4-10*titleSize/2, 30*titleSize, 10*titleSize);
        lp.add(lab1, 3, 0);
        
        titleImg = null;
        try {
			titleImg = ImageIO.read(new File("BackGround.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        titleImg = titleImg.getScaledInstance(WIDTH,HEIGHT,java.awt.Image.SCALE_SMOOTH);
        lab2 = new JLabel(new ImageIcon(titleImg));
        lab2.setBounds(0,0,WIDTH,HEIGHT);
        lp.add(lab2,1,0);
        //lab2.setVisible(false);
		
        Font alge = new Font("Algerian", Font.BOLD, 50);
        lab3 = new JLabel("Waiting", JLabel.CENTER);
        lab3.setFont(alge);
        lab3.setForeground(Color.white);
        lab3.setBounds(WIDTH/2-500,HEIGHT/2-100,1000,100);
        lp.add(lab3, 3, 0);
        lab3.setVisible(false);
        
        EnterLab = new JLabel("Please Enter IP address", JLabel.CENTER);
        EnterLab.setFont(alge);
        EnterLab.setForeground(Color.white);
        EnterLab.setBounds(WIDTH/2-500,HEIGHT/2-200,1000,100);
        EnterLab.setVisible(false);
        lp.add(EnterLab, 3, 0);
        
        Font plain = new Font("godic",Font.PLAIN, 20);
        EnterLab2 = new JLabel("Host is not Found", JLabel.CENTER);
        EnterLab2.setFont(plain);
        EnterLab2.setForeground(Color.red);
        EnterLab2.setBounds((WIDTH+BUT_W)/2+50, HEIGHT/2+100, BUT_W, BUT_H);
        EnterLab2.setVisible(false);
        lp.add(EnterLab2, 3, 0);
        
        text = new JTextField(20);
        text.setFont(plain);
        text.setBounds((WIDTH-TEXT_W)/2, HEIGHT/2, TEXT_W, TEXT_H);
        text.setHorizontalAlignment(JTextField.CENTER);
        text.setVisible(false);
        lp.add(text,3,0);
        
        EnterBtn = new JButton("Find!");
        EnterBtn.setBounds((WIDTH-BUT_W)/2, HEIGHT/2+100, BUT_W, BUT_H);
        EnterBtn.addActionListener(new btn3Listener(frame));
        EnterBtn.setVisible(false);
        lp.add(EnterBtn, 3, 0);
        
        btn1 = new JButton("Play as Host");
        btn1.setBounds((WIDTH-BUT_W)/2, HEIGHT/2, BUT_W, BUT_H);
        btn1.addActionListener(new btn1Listener(frame));
        lp.add(btn1,3,0);
        
        btn2 = new JButton("Play as Guest");
        btn2.setBounds((WIDTH-BUT_W)/2, HEIGHT/2+BUT_H*2, BUT_W, BUT_H);
        btn2.addActionListener(new btn2Listener(frame));
        lp.add(btn2,3,0);
        
        lp.setBounds(0,0,WIDTH,HEIGHT);
        add(lp);
        setVisible(true);
	}
}
class btn1Listener implements ActionListener {
	private MainFrame frame;
	public btn1Listener(MainFrame frame){
		this.frame = frame;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.panel1.lab1.setVisible(false);
		frame.panel1.lab3.setVisible(true);
		frame.panel1.btn1.setVisible(false);
		frame.panel1.btn2.setVisible(false);
		new Thread(new ServerConnectThread(frame)).start();
		/*
		frame.panel1.setVisible(false);
		frame.panel2.setVisible(true);
		if(frame.panel2.requestFocusInWindow())
		new Thread(frame.panel2).start();*/
	}
}
class ServerConnectThread implements Runnable {
	private MainFrame frame;
	public ServerConnectThread(MainFrame frame){
		this.frame = frame;
	}
	@Override
	public void run() {
		try {
		GameServer gs = new GameServer(frame.panel2);
		new Thread(gs).start();
		int dot = 0;
		String str = "Waiting";
		while (!gs.connected) {
			Thread.sleep(1000);
			str += '.'; dot++;
			frame.panel1.lab3.setText(str);
			if (dot >= 3) {dot = 0; str = "Waiting"; }
		}
		
		frame.panel1.lab3.setText("Connected!");
		Thread.sleep(3000);
		frame.panel1.setVisible(false);
		frame.panel2.setVisible(true);
		frame.panel2.requestFocusInWindow();
		frame.panel2.gServer = gs;
		new Thread(frame.panel2).start();
	
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		frame.panel1.lab1.setVisible(true);
		frame.panel1.lab3.setVisible(false);
		frame.panel1.btn1.setVisible(true);
		frame.panel1.btn2.setVisible(true);
		frame.panel1.lab3.setText("Waiting");
		
	}	
}

class btn2Listener implements ActionListener {
	private MainFrame frame;
	public btn2Listener(MainFrame frame){
		this.frame = frame;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		frame.panel1.lab1.setVisible(false);
		frame.panel1.btn1.setVisible(false);
		frame.panel1.btn2.setVisible(false);
		
		frame.panel1.EnterBtn.setVisible(true);
		frame.panel1.EnterLab.setVisible(true);
		frame.panel1.text.setVisible(true);
	}
}

class btn3Listener implements ActionListener {
	private MainFrame frame;
	public btn3Listener(MainFrame frame){
		this.frame = frame;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		GameClient gc = new GameClient(frame.panel2, frame.panel1.text.getText());
		if (!gc.connected) {
			frame.panel1.EnterLab2.setVisible(true);
		}
		else {
			frame.panel1.EnterLab2.setVisible(false);
			frame.panel1.EnterLab.setText("Connected!");
			new Thread(new ClientConnectThread(frame,gc)).start();
		}
	}
}
class ClientConnectThread implements Runnable {
	private MainFrame frame;
	private GameClient gc;
	public ClientConnectThread(MainFrame frame,GameClient gc){
		this.frame = frame;
		this.gc = gc;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		frame.panel1.setVisible(false);
		frame.panel2.setVisible(true);
		frame.panel2.requestFocusInWindow();
		frame.panel2.gClient = gc;
		new Thread(gc).start();
		new Thread(frame.panel2).start();
	}	
}

