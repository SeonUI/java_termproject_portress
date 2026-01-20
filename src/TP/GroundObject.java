package TP;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GroundObject extends GameObject{
	public GroundObject(int x,int y,int xSize,int ySize) {
		super(x,y);
		this.xSize = xSize;
		this.ySize = ySize;
		try {
			img = ImageIO.read(new File("ground.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		img = img.getScaledInstance(xSize,ySize,java.awt.Image.SCALE_SMOOTH);
	}
}
