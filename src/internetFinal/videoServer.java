package internetFinal;

import internetFinal.VerySimpleCharServer.ClientHandler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.xuggle.xuggler.IContainer;

public class videoServer {
	Socket sock;
	BufferedReader reader;
	
	public class ClientHandler implements Runnable{
		public ClientHandler(Socket clientSocket){
			try{
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			}catch(Exception ex){
				ex.printStackTrace();
			}
//			sock = clientSocket;
		}
		public void run(){
			try{
				int size = getFileSize("IMG_2964.flv");
				
				File file = new File("IMG_2964.flv");
//				PrintWriter printWriter = 
//	                    new PrintWriter(sock.getOutputStream());
				PrintStream printStream = 
	                    new PrintStream(sock.getOutputStream());
				
	                printStream.println(size);
	                printStream.flush();
//	                Thread.sleep(3000);

                System.out.print("OK! 傳送檔案...."); 
                
                BufferedInputStream inputStream = 
                    new BufferedInputStream( 
                            new FileInputStream(file));

                int readin;
//	                int s = 0;
                
                while((readin = inputStream.read()) != -1) {
                     printStream.write(readin);
                }

                printStream.flush();
                printStream.close();
                inputStream.close();
                
                sock.close();
                
                System.out.println("\n檔案傳送完畢！");
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	public int getFileSize(String filename){
		IContainer container = IContainer.make();
		int result = container.open(filename, IContainer.Type.READ, null);
		
		// check if the operation was successful
		if (result<0)
			throw new RuntimeException("Failed to open media file");
		// query how many streams the call to open found
        int numStreams = container.getNumStreams();
		System.out.println("numStreams: "+ numStreams+ "(end)");
		int fileSize = (int) container.getFileSize();
		System.out.println("fileSize: "+ fileSize+ "(end)");
		container.close();
		return fileSize;
	}
	public static void main(String[] args) { 
		new videoServer().go();
		
		
	}
	public void go(){
		try { 
	        int port = 5000;
	        System.out.println("簡易檔案接收...");
	        System.out.printf("將接收檔案於連接埠: %d%n", port); 
	
	        ServerSocket serverSkt = new ServerSocket(port); 
	        
	        while(true) {
	        	
	        	
	        	
	        	
	            System.out.println("傾聽中....");
	            
	            Socket clientSkt = serverSkt.accept();
	            
	            System.out.printf("與 %s 建立連線%n", 
	                    clientSkt.getInetAddress().toString()); 
	            
	            Thread t = new Thread(new ClientHandler(clientSkt));
				t.start();
				System.out.println("got a connection");
	            
	        }
		}
		catch(Exception e) { 
            e.printStackTrace(); 
        } 

	}
}
