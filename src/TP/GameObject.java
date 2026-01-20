package TP;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GameObject implements Serializable{
	public int x;
	public int y;
	public double xVel = 0;
	public double yVel = 0;
	public int xSize= 100;
	public int ySize= 100;
	public double rad = 0;
	public Image img; //= new ImageIcon("default.png").getImage();
	public boolean isMove;
	public boolean state = true;
	
	GameObject(int x,int y){
		try {
			img = ImageIO.read(new File("default.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//img = img.getScaledInstance(size,size,java.awt.Image.SCALE_SMOOTH);
		this.x = x;
		this.y = y;
	}
	GameObject(int x,int y, int xSize,int ySize,String file){
		try {
			img = ImageIO.read(new File(file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		img = img.getScaledInstance(xSize,ySize,java.awt.Image.SCALE_SMOOTH);
		this.x = x;
		this.y = y;
		this.xSize = xSize;
		this.ySize = ySize;
	}
	public GameObject(GameObject src) {
		if (src == null) {return;}
		this.x = src.x;
		this.y = src.y;
	}
	public int xMid() {
		return x+xSize/2;
	}
	public int yMid() {
		return y+ySize/2;
	}
	public ArrayList<GameObject> collisionCheck() {
		ArrayList<GameObject> collided = new ArrayList<GameObject>();
		try {
			for (GameObject obj: GamePanel.InGameObject) {
				if(this == obj) continue;
				if(this.x>MainFrame.WIDTH || this.x<0 ||
						this.y>MainFrame.HEIGHT ) {
					collided.add(new GameObject(0,0)); 
				}
				int xdiff = this.xMid()-obj.xMid();
				int ydiff = this.yMid()-obj.yMid();
				if(Math.abs(xdiff)<(obj.xSize+xSize)/2 && Math.abs(ydiff)<(obj.ySize+ySize)/2) {
					collided.add(obj);
				}
			}
		}
		catch (ConcurrentModificationException e) {
        }
		return collided;
		
	}
	
	public void printObject(Graphics g){
		AffineTransform tx = AffineTransform.getRotateInstance(rad, xSize/2, ySize/2);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage bimg = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
	    Graphics bGr = bimg.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();
	    
		//g.drawImage(bimg, x, y, size, size, null);
		g.drawImage(op.filter(bimg, null), x, y, null);
	    //g.drawImage(img, x, y, null);
	    //g.drawImage(img, x, y,size,size, null);
	}
}
