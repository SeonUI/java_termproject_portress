package TP;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.ConnectException;

public class GameClient implements Runnable {
	boolean connected = false;
	public PlayerObject playerThis;
	public PlayerObject playerOther;
	public GamePanel gp;
	private Socket soc;
	
	public GameClient(GamePanel gp, String ServerIP) {
		this.gp = gp;
		try {
			soc = new Socket(ServerIP, 5000);
			connected = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			OutputStream outputStream = soc.getOutputStream();
	        ObjectOutputStream ooStream = new ObjectOutputStream(outputStream);
	        InputStream InputStream = soc.getInputStream();
	        ObjectInputStream oiStream = new ObjectInputStream(InputStream);
			while(GamePanel.GameRun) {
		        ooStream.writeObject(new PlayerObject(playerThis)); 
		        playerOther =  new PlayerObject((PlayerObject)oiStream.readObject());
		        Thread.sleep(1000/GamePanel.FPS);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
