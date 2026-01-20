package TP;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class GameServer implements Runnable {
	public boolean connected = false;
	public boolean serverTurn;
	public GamePanel gp;
	public PlayerObject playerThis;
	public PlayerObject playerOther;
	
	public GameServer(GamePanel gp) {
		this.gp = gp;
	}
	public void run() {
		ServerSocket ss;
		Scanner scn = new Scanner(System.in);
		try {
			ss = new ServerSocket(5000);
			Socket soc = ss.accept();
			connected = true;
			OutputStream outputStream = soc.getOutputStream();
	        ObjectOutputStream ooStream = new ObjectOutputStream(outputStream);
	        InputStream InputStream = soc.getInputStream();
	        ObjectInputStream oiStream = new ObjectInputStream(InputStream);
			while(GamePanel.GameRun) {
		        ooStream.writeObject(new PlayerObject(playerThis)); 
		        playerOther = new PlayerObject((PlayerObject)oiStream.readObject());
		        Thread.sleep(1000/GamePanel.FPS);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
