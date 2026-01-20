package TP;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

class GamePanel extends JPanel implements Runnable{
	
    public static final int BUT_W = 200;
    public static final int BUT_H = 80;
    public static final int FPS = 60;
    public static boolean GameRun = true;
    public static ArrayList<GameObject> InGameObject;
    public PlayerObject player1;
    public PlayerObject player2;
    private MainFrame frame;
    private PlayerObject playerOfPanel;
    private PlayerObject playerOfOther;
    public GameServer gServer = null;
    public GameClient gClient = null;
    public boolean serverMode = true;
    public Image backgroundImg;
   
    
    public GamePanel(MainFrame frame){
    	this.frame = frame;
    	try {
    		backgroundImg = ImageIO.read(new File("BackGround2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        setSize(MainFrame.WIDTH,MainFrame.HEIGHT);
        setLayout(null);
        setBounds(0,0,MainFrame.WIDTH,MainFrame.HEIGHT);
        setFocusable(true);
    }
    
    @Override
    public void run() {
    	InGameObject = new ArrayList<GameObject>();
    	GameRun = true;
        player1 = new PlayerObject(100,100,1);
    	player2 = new PlayerObject(MainFrame.WIDTH-150,100,2);
    	GraphicRenderer gr = new GraphicRenderer(this);
    	if (gServer != null) {
    		gServer.playerThis = player1;
    		playerOfPanel = player1;
    		playerOfOther = player2;
    		serverMode = true;
    	}
    	else {
    		gClient.playerThis = player2;
    		playerOfPanel = player2;
    		playerOfOther = player1;
    		serverMode = false;
    	}

		new Thread(playerOfPanel).start();
		
    	InGameObject.add(player1);
    	InGameObject.add(player2);
    	
			GroundObject g1 = new GroundObject(0,MainFrame.HEIGHT-100,1000,10);
			InGameObject.add(g1);
			GroundObject g2 = new GroundObject(100,200,100,10);
			InGameObject.add(g2);
			GroundObject g3 = new GroundObject(MainFrame.WIDTH-100-100,200,100,10);
			InGameObject.add(g3);
			GroundObject g4 = new GroundObject(300,400,100,10);
			InGameObject.add(g4);
			GroundObject g5 = new GroundObject(MainFrame.WIDTH-300-100,400,100,10);
			InGameObject.add(g5);
    	
    	PlayerKeyMove pkm = new PlayerKeyMove(playerOfPanel);
		PlayerKeyCharge pkc = new PlayerKeyCharge(playerOfPanel);
		boolean keyAdded = false;
		int prevPhase = 0;
		while(GameRun) 
		{
			if (serverMode) {
				player2.copy(gServer.playerOther);
			}
			else {
				player1.copy(gClient.playerOther);
			}
			
			if (prevPhase != playerOfPanel.phase) {keyAdded = false;}
			prevPhase = playerOfPanel.phase;
			if (playerOfPanel.phase == 1) {
				if (!keyAdded) { 
					addKeyListener(pkm); 
					keyAdded = true;
				}
			}
			else if (playerOfPanel.phase == 2) {
				if (!keyAdded) {
					removeKeyListener(pkm);
    				addKeyListener(pkc); 
    				keyAdded = true;
    			}
			}
			else if (playerOfPanel.phase == 5) {
				if (!keyAdded) {
					removeKeyListener(pkc);
    				keyAdded = true;
    			}
				if (playerOfOther.phase == 1) {
					playerOfPanel.phase = 6;
				}
			}
			else if (playerOfPanel.phase == 6) {
				if (playerOfOther.phase == 5) {
					if (!keyAdded) {
						playerOfOther.makeBullet();
	    			}
					playerOfPanel.phase = 1;
				}
			}
			
			if (playerOfPanel.hp <= 0) {
				GameObject endLose = new GameObject(0,MainFrame.HEIGHT/3,MainFrame.WIDTH,MainFrame.HEIGHT/3,"endLose.png");
				InGameObject.add(endLose);
				endPhase();
			}
			else if(playerOfOther.hp <= 0) {
				GameObject endWin = new GameObject(0,MainFrame.HEIGHT/3,MainFrame.WIDTH,MainFrame.HEIGHT/3,"endWin.png");
				InGameObject.add(endWin);
				endPhase();
			}
			
    		try {
    			Thread.sleep(1000/FPS);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
	    }
    }
    
    public void endPhase() {
    	for (GameObject o: InGameObject) {
        	o.state = false;
        }
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	GameRun = false;
    	System.exit(0);
    }
    
    
    @Override
    public void paint(Graphics g) {
        Image img = createImage(MainFrame.WIDTH,MainFrame.HEIGHT);
        Graphics imgGraphics = img.getGraphics();
        paintComponents(imgGraphics);
        imgGraphics.drawImage(backgroundImg,0,0,MainFrame.WIDTH,MainFrame.HEIGHT,null);
        int i=0;
        try {
        	for (GameObject o: InGameObject) {
            	o.printObject(imgGraphics);
            }
        } catch (ConcurrentModificationException e) {
        	
        }
        g.drawImage(img, 0, 0, null);
    }
}

class GraphicRenderer implements Runnable{
	private GamePanel gp;
	public GraphicRenderer(GamePanel gp) {
		this.gp = gp;
		new Thread(this).start();
	}
	public void run() {
		try {
			while(GamePanel.GameRun) {
				gp.repaint();
                Thread.sleep(1000/GamePanel.FPS);
            }
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

