package TP;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class BulletObject extends GameObject implements Runnable{
	private PlayerObject playerShot;
	private Image expImg;
	private int expSize = 100;
	
	BulletObject(int x, int y, int vel, int deg, PlayerObject player) {
		super(x, y);
		try {
			img = ImageIO.read(new File("bullet.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			expImg = ImageIO.read(new File("Explosion.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		playerShot = player;
		rad = (player.playerNum == 1)? Math.toRadians(-deg):Math.toRadians(180+deg);
		vel = vel /= 3;
		xVel = vel*Math.cos(rad);
		yVel = vel*Math.sin(rad);
		xSize = 50;
		ySize = 50;
		img = img.getScaledInstance(xSize,ySize,java.awt.Image.SCALE_SMOOTH);
		expImg = expImg.getScaledInstance(expSize,expSize,java.awt.Image.SCALE_SMOOTH);
		isMove = true;
		new Thread(this).start();
	}
	public void run() {
        while(state) { try {
        	x += xVel;
        	y += yVel;
        	yVel += 0.3;
        	
        	if (xVel == 0) rad = Math.PI/2;
        	else if (xVel<0) rad = Math.PI+Math.atan(yVel/xVel);
        	else	rad = Math.atan(yVel/xVel);
        	
        	boolean isColli = false;
        	ArrayList<GameObject> colObjs =  collisionCheck();
        	for (GameObject o : colObjs) {
        		if (o == playerShot) ;
        		else {
        			isColli = true;
    	    		if (o.getClass().getName() == "TP.PlayerObject") {
    	    			((PlayerObject) o).hp -= 30;
    	    		}
        		}
        	}
    	
        	if (isColli) {
        		rad = 0;
        		img = expImg;
        		x -= 50/2;
    			y -= 50/2;
        		for (xSize = expSize; xSize>5; xSize -= 50) {
        			ySize = xSize;
        			img = img.getScaledInstance(xSize,ySize,java.awt.Image.SCALE_AREA_AVERAGING);
        			x += 50/2;
        			y += 50/2;
        			Thread.sleep(500);
        		}
        		GamePanel.InGameObject.remove(this);
	    		this.state = false;
        	}
        	
			Thread.sleep(1000/GamePanel.FPS);
			
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}
