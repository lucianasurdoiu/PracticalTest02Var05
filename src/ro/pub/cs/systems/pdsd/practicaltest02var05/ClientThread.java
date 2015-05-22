package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread{
	private String   address;
	private int      port;
	private String   comanda;
	TextView display_page;
	private Socket socket;
	
	public ClientThread(String address,	int port, String comanda, TextView display_page){
		this.address = address;
		this.port = port;
		this.comanda = comanda;
		this.display_page = display_page;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(address, port);
			if (socket == null) {
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
			}
			
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter    printWriter    = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(comanda);
				printWriter.flush();
			}
			String pageContent = "";
			String partial_page;
			while ((partial_page = bufferedReader.readLine()) != null) {
				pageContent = pageContent + partial_page;
			}
			final String pageSourceCode = pageContent;
			display_page.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					display_page.setText(pageSourceCode);
				}
			});
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
			
}
