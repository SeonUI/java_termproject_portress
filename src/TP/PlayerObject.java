package TP;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class PlayerObject extends GameObject implements Runnable{
	public boolean r, l, space, powerOver, angleOver;
	public final int speed = 3;
	public GameObject bullet;
	private Image cannonImg;
	private Image gaugeImg;
	private Image hpImg1;
	private Image hpImg2;
	public int playerNum;
	
	public boolean movePhase = false;
	public boolean powerPhase = false;
	public boolean anglePhase = false;
	public boolean waitPhase = false;
	// 1 = move, 2=power, 3=angle, 4=shot, 5=wait, 6=myTurn!
	public int phase = 1;
	
	public int hp = 100;
	private final int powerLimit = 50;
	private int power = 1;
	private int powerGauge = 1;
	
	private final int angleLimit = 90;
	private int angle = 2;
	private int angleGauge = 2;
	
	PlayerObject(int x, int y,int playerNum) {
		super(x, y); 
		this.playerNum = playerNum;
		if (playerNum == 1) {
			phase = 1;
		}
		else if (playerNum == 2) {
			phase = 5;
		}
		try {
			img = ImageIO.read(new File("TankBody"+Integer.toString(playerNum)+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cannonImg = ImageIO.read(new File("TankCannon"+Integer.toString(playerNum)+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			gaugeImg = ImageIO.read(new File("TankGauge.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			hpImg1 = ImageIO.read(new File("TankHP1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			hpImg2 = ImageIO.read(new File("TankHP2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xSize = 50;
		ySize = 50;
		isMove = true;
		img = img.getScaledInstance(xSize,ySize,java.awt.Image.SCALE_SMOOTH);
		cannonImg = cannonImg.getScaledInstance((int)(xSize*0.4),(int)(ySize*0.8),java.awt.Image.SCALE_SMOOTH);
		gaugeImg = gaugeImg.getScaledInstance((int)(200),(int)(200),java.awt.Image.SCALE_SMOOTH);
		hpImg1 = hpImg1.getScaledInstance(xSize+30,20,java.awt.Image.SCALE_SMOOTH);
		hpImg2 = hpImg2.getScaledInstance(xSize+30,20,java.awt.Image.SCALE_SMOOTH);
		bullet = null;
	}
	public PlayerObject(PlayerObject src) {
		super(src);
		copy(src);
	}
	public void makeBullet() {
		double sin = Math.sin(Math.toRadians(angleGauge));
		double cos = Math.cos(Math.toRadians(angleGauge));
		if (playerNum == 1) bullet = (GameObject) new BulletObject((int)(x+cos*xSize*0.8),(int)(y-sin*ySize*0.8),powerGauge,angleGauge,this);
		else bullet = (GameObject) new BulletObject((int)(x-cos*xSize*0.8),(int)(y-sin*ySize*0.8),powerGauge,angleGauge,this);
		GamePanel.InGameObject.add(bullet);
	}
	public void copy(PlayerObject src) {
		if (src == null) return;
		x = src.x;
		y = src.y;
		angleGauge = src.angleGauge;
		powerGauge = src.powerGauge;
		phase = src.phase;
		hp = src.hp;
	}
	
	@Override
	public void printObject(Graphics g){
		BufferedImage bimg = new BufferedImage((int)(xSize*2),(int)(ySize*2), BufferedImage.TYPE_INT_ARGB);
	    Graphics bGr = bimg.createGraphics();
		
	    double radian;
		if (playerNum == 1) {
			radian =  Math.toRadians(90-angleGauge);
			bGr.drawImage(cannonImg,0,0, null);
		    bGr.dispose();
		    AffineTransform tx = AffineTransform.getRotateInstance(radian, xSize*0.2, ySize*0.8);
		    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter(bimg, null), (int) (x+xSize/2), y-ySize/2, null);
		}
		else {
			radian = Math.toRadians(270+angleGauge);
			bGr.drawImage(cannonImg,ySize,0, null);
		    bGr.dispose();
		    AffineTransform tx = AffineTransform.getRotateInstance(radian, xSize*0.2+ySize, ySize*0.8);
		    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter(bimg, null), (int) (x-ySize*6/7), y-ySize/2, null);
		}
		
		if (powerGauge != 1){
			AffineTransform tx2 = AffineTransform.getRotateInstance(Math.PI,50,50);
			AffineTransformOp op2 = new AffineTransformOp(tx2, AffineTransformOp.TYPE_BILINEAR);
			BufferedImage bimg2 = new BufferedImage(200,200, BufferedImage.TYPE_INT_ARGB);
		    Graphics bGr2 = bimg2.createGraphics();
		    bGr2.drawImage(gaugeImg, 0, 0,20,powerGauge*2, null);
		    bGr2.dispose();
			g.drawImage(op2.filter(bimg2, null), x,y-100, null);
		}
		g.drawImage(hpImg2,x-10,y+ySize+25,xSize+30,20,null);
		if (hp<0) hp=0;
		g.drawImage(hpImg1,x-10,y+ySize+25,(xSize+30)*hp/100,20,null);
		g.drawImage(img, x, y, null);
	}
	
	@Override
    public void run() {
        while(state) {
        	try {
				Thread.sleep(1000/GamePanel.FPS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	boolean ground = false;
        	ArrayList<GameObject> colObjs =  collisionCheck();
        	for (GameObject o : colObjs) {
        		if (o.getClass().getName() == "TP.GroundObject") {yVel = 0; ground = true;}
        	}
        	if (!ground) yVel += (yVel<=10)? 0.2:0;
        	x += xVel;
        	y += yVel;
        	
        	if  (phase == 1) {
        		if (r) xVel = speed;
            	if (l) xVel = -speed;
            	if (!r && !l) xVel = 0;
        	}
        	else if (phase == 2) {
        		xVel = 0;
	        	if (space) {
	    			if(powerGauge == powerLimit) {
	    				power = -power;
	    			}
	    			else if (powerGauge == 0) {
	    				power = -power;
	    			}
	    			powerGauge += power;
	    		}
	        }
        	else if (phase == 3) {
        		if (space) {
					if(angleGauge == angleLimit) {
						angle = -angle;
					}
					else if (angleGauge == 0) {
						angle = -angle;
					}
					angleGauge += angle;
        		}	
        	}
        	
        	else if (phase == 4) {
        		if (space) {
					makeBullet();
					phase = 5;
        		}	
        	}
        	else if (phase == 6) {
        		power = Math.abs(power); powerGauge = power;
        		angle = Math.abs(angle); angleGauge = angle;
        	}
        }
    }
}

class PlayerKeyMove extends KeyAdapter {
	private PlayerObject player;
	
	public PlayerKeyMove(PlayerObject player) {
		this.player = player;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {       
            case KeyEvent.VK_LEFT:
            	player.l = true;
                break;
            case KeyEvent.VK_RIGHT:
            	player.r = true;
                break;
            case KeyEvent.VK_SPACE:
            	player.phase++;
            	player.l = false;
            	player.r = false;
            	player.space = true;
            	break;
        }
	}
	@Override
	public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {       
            case KeyEvent.VK_LEFT:
            	player.l = false;
                break;
            case KeyEvent.VK_RIGHT:
            	player.r = false;
                break;
        }
	}
}

class PlayerKeyCharge extends KeyAdapter {
	private PlayerObject player;
	
	public PlayerKeyCharge(PlayerObject player) {
		this.player = player;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {       
            case KeyEvent.VK_SPACE:
            	player.space = true;
                break;
        }
	}
	@Override
	public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
            	player.space = false;
            	if (player.phase != 5)
            		player.phase++;
            	break;
            
        }
	}
}