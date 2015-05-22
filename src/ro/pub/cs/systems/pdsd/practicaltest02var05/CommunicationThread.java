package ro.pub.cs.systems.pdsd.practicaltest02var05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class CommunicationThread extends Thread{
	private ServerThread serverThread;
	private Socket       socket;

	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket       = socket;
	}

	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter    printWriter    = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (URL)!");
					String comanda = bufferedReader.readLine();
					String pageContent = "";
					if(comanda != null && !comanda.isEmpty()){
						
						String[] com = comanda.split("\\,");
						if(com[0].equals("put")){
							HttpClient httpClient = new DefaultHttpClient();
							HttpGet httpGet = new HttpGet("http://www.timeapi.org/utc/now");
							
							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							String pageSourceCode = httpClient.execute(httpGet, responseHandler);
							if(pageSourceCode != null){
								Myclass structure = new Myclass();
								
								String[] string = pageSourceCode.split("[-T:+]");
								structure.setStr(com[1].toString());
								DateTime time = new DateTime(Integer.parseInt(string[0]), Integer.parseInt(string[1]), Integer.parseInt(string[2]), Integer.parseInt(string[3]), Integer.parseInt(string[4]), Integer.parseInt(string[5]));
								structure.setTimeStamp(time);
								
								if(serverThread.setData( structure, pageContent) < 0){					
									pageContent = pageContent + "modified";	
								}else{
									pageContent = pageContent + "inserted";	
								}		
								
							}				
						}else if(com[0].equals("give")){
							DateTime timeHash = new DateTime();
							
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
							HttpClient httpClient = new DefaultHttpClient();
							HttpGet httpGet = new HttpGet("http://www.timeapi.org/utc/now");

							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							String pageSourceCode = httpClient.execute(httpGet, responseHandler);
							if(pageSourceCode != null){
								
								HashMap<String, Myclass> dataFromHash = serverThread.getData();
								Myclass str = new Myclass();
								str.setStr(com[1]);
								
								Iterator<Map.Entry<String,Myclass>> iter = dataFromHash.entrySet().iterator();
								while (iter.hasNext()) {
									Map.Entry<String,Myclass> entry = iter.next();
									if(str.getStr().equals(entry.getValue().getStr())){
										 timeHash = entry.getValue().getTimeStamp();
									}
								
								}
								
								String[] string = pageSourceCode.split("[-T:+]");
								//structure.setStr(string);
								DateTime time = new DateTime(Integer.parseInt(string[0]), Integer.parseInt(string[1]), Integer.parseInt(string[2]), Integer.parseInt(string[3]), Integer.parseInt(string[4]), Integer.parseInt(string[5]));
										
								
								if(timeHash.toLong() + 60 >= time.toLong()){
									pageContent = pageContent + time.toString();	
									
								}else{
									
									pageContent = pageContent + "none";
								}
						}
							
						}else{
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
							HttpClient httpClient = new DefaultHttpClient();
							HttpGet httpGet = new HttpGet("http://www.timeapi.org/utc/now");

							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							String pageSourceCode = httpClient.execute(httpGet, responseHandler);
							if(pageSourceCode != null){
								Myclass structure = new Myclass();
								serverThread.setData( structure, pageContent);
							}

						}
					
					if(pageContent != null){

						printWriter.println(pageContent);
						printWriter.flush();
					}

				
					}
				}
					
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
