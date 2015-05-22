package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;

public class ServerThread extends Thread{

	private int    port = 0;
	private ServerSocket serverSocket = null;

	private HashMap<String, Myclass> data = null;

	public ServerThread(int port) {
		this.port = port;
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException ioException) {
			Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
		this.data = new HashMap<String, Myclass>();
	}

	public synchronized int setData(Myclass comanda, String pageContent) {
		
		Iterator<Map.Entry<String,Myclass>> iter = this.data.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String,Myclass> entry = iter.next();
			if(comanda.getStr().equals(entry.getValue().getStr())){
				return -1;
			}
		
		}
		
		this.data.put(pageContent, comanda);
		return 1;
	}
	

	public synchronized HashMap<String, Myclass> getData() {
		return data;
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Log.i(Constants.TAG, "[SERVER] Waiting for a connection...");
				Socket socket;

				socket = serverSocket.accept();

				Log.i(Constants.TAG, "[SERVER] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
				CommunicationThread communicationThread = new CommunicationThread(this, socket);
				communicationThread.start();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void stopThread() {
		if (serverSocket != null) {
			interrupt();
			try {
				serverSocket.close();
			} catch (IOException ioException) {
				Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}				
			}
		}
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}
}
