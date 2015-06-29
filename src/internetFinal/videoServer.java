package internetFinal;

import internetFinal.VerySimpleCharServer.ClientHandler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;

import test.User;
import test.Video;

import com.xuggle.xuggler.IContainer;

public class videoServer {
	Socket sock;
	BufferedReader reader;
	PrintWriter writer;
	static int currentPortNum = 5001;
	
	public class ClientHandler implements Runnable{
		public ClientHandler(Socket clientSocket){
			try{
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
				writer = new PrintWriter(clientSocket.getOutputStream());
				
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
//			sock = clientSocket;
		}
		public void run(){
			
			//讀一行判斷是哪種
			String threadType;
			try {
				
				while(true){
				
					threadType = reader.readLine();
					System.out.println("threadType: "+ threadType);
					if(threadType == null) break;
					if(threadType != null){
						if(threadType.startsWith("fileData")){
							System.out.println("get fileData Request!");
							sendFileData(threadType, "notAd");
						}
						else if(threadType.startsWith("adFileData")){
							System.out.println("get adFileData Request!");
							sendFileData(threadType, "ad");
						}
						else if(threadType.startsWith("login")){
							//切割fileName出來
							String[] stringArray = threadType.split(" ");
							
							String getAccount = stringArray[1].toString();	//帳號
							System.out.print("getAccount: "+getAccount);
							
							String getPassword = stringArray[2].toString();	//密碼
							System.out.print("getPassword: "+getPassword);
							
							User user = new User();
							int checkLogin = user.logIn(getAccount, getPassword);
							if(checkLogin == 0){
								//失敗
								writer.println("loginFailed");
								writer.flush();
							}
							else{
								//成功
								writer.println("loginSuccess");
								writer.flush();
							}
							
						}
						else if(threadType.startsWith("register")){
							System.out.println("register~~~");
							//切割fileName出來
							String[] stringArray = threadType.split(" ");
							
							String getAccount = stringArray[1].toString();	//帳號
							System.out.print("getAccount: "+getAccount);
							
							String getPassword = stringArray[2].toString();	//密碼
							System.out.print("getPassword: "+getPassword);
							
							String getMail = stringArray[3].toString();	//信箱
							System.out.print("getMail: "+getMail);
							
							User user = new User();
							int checkDuplicate = user.searchUser(getAccount);
							if(checkDuplicate == 0){
								//重複
								writer.println("registerFailed");
								writer.flush();
							}
							else{
								//ok
								user.newUser(getAccount, getPassword, getMail);
								writer.println("registerSuccess");
								writer.flush();
							}
							
						}
						else if(threadType.startsWith("fileList")){
							System.out.println("checkfileList~~~");
							
							String[] ans = null;
							int i, check;
							String sendString = "fileListData ";
							
							Video vod = new Video();
							ans = vod.getVideo();
							check = Integer.parseInt(ans[0]);
							System.out.println(check);
							
							for(i = 0; i < check; i++){
								System.out.println(ans[i+1]);
//								sendString.concat(ans[i+1]);
//								sendString.concat(" ");
								sendString = sendString + ans[i+1] + " ";
								
							}
							
							System.out.println("sendString: "+sendString);
							writer.println(sendString);
							writer.flush();
							
							
						}
						else if(threadType.startsWith("fileSize")){
							
						}
						
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//傳遞fileData用的
		public void sendFileData(String threadType, String type){
			//切割fileName出來
			String[] stringArray = threadType.split(" ");
			System.out.print("stringArray: "+stringArray[1].toString());
			String getFileName = stringArray[1].toString();	//檔名
			
			//若為ad則從server端決定
			if(type.equals("ad")){
				getFileName = "youtube_h264_mp3.flv";
			}
			
			
			int fileSizeAns = getFileSize(getFileName);
			
			
			int nextPort = currentPortNum;
			currentPortNum = currentPortNum + 1;
			
			Thread t = new Thread(new sendBinary(getFileName, nextPort));
			t.start();
			System.out.println("ad or not type: "+type);
			if(type.equals("ad")){
				//告訴client我assign的port號
				writer.println("getFilePortNumForAd "+nextPort+" "+Integer.toString(fileSizeAns)+" "+stringArray[1].toString()+" ");
				writer.flush();
				System.out.println("in ad if");
			}
			else{
				//告訴client我assign的port號
				writer.println("getFilePortNum "+nextPort+" "+Integer.toString(fileSizeAns)+" "+getFileName+" ");
				writer.flush();
				System.out.println("in notAd if");
			}
			
			
			
//			try{
//				int size = getFileSize(getFileName);
//				
//				File file = new File(getFileName);
////				PrintWriter printWriter = 
////	                    new PrintWriter(sock.getOutputStream());
//				PrintStream printStream = 
//	                    new PrintStream(sock.getOutputStream());
//				
////	                printStream.println(size);
////	                printStream.flush();
////	                Thread.sleep(3000);
//
//                System.out.print("OK! 傳送檔案...."); 
//                
//                BufferedInputStream inputStream = 
//                    new BufferedInputStream( 
//                            new FileInputStream(file));
//
//                int readin;
////	                int s = 0;
//                
//                while((readin = inputStream.read()) != -1) {
//                     printStream.write(readin);
//                }
//
//                printStream.flush();
//                printStream.close();
//                inputStream.close();
//                
//                sock.close();
//                
//                System.out.println("\n檔案傳送完畢！");
//			}catch(Exception ex){
//				ex.printStackTrace();
//			}
		}
		
	}
	
	
	//inner class for sending binary data
	public class sendBinary implements Runnable{
		String sendFileName;
		int oneOfPort2;
		public sendBinary(String fileName, int portNum){
			sendFileName = fileName;
			oneOfPort2 = portNum;
		}
		public void run() {
			try { 
				
				File file = new File(sendFileName);
				File file2 = new File(sendFileName);
				
				
				System.out.println("簡易檔案接收..."); 
	            System.out.printf("將接收檔案於連接埠: %d%n", oneOfPort2);
	            ServerSocket serverSkt = new ServerSocket(oneOfPort2);
	            
	            //只接一個！
	            Socket clientSkt = serverSkt.accept();
	            System.out.printf("與 %s 建立連線%n", 
                        clientSkt.getInetAddress().toString());
	            
	            PrintStream printStream = 
	                    new PrintStream(clientSkt.getOutputStream());
	            System.out.print("OK! 傳送檔案...."); 
	            
	            BufferedInputStream inputStream = 
	                    new BufferedInputStream( 
	                            new FileInputStream(file));
	            
	            int readin;
	            while((readin = inputStream.read()) != -1) { 
	                 printStream.write(readin); 
//	                 System.out.println(readin);
	            }
//	            BufferedInputStream inputStream2 = 
//	                    new BufferedInputStream( 
//	                            new FileInputStream(file2));
//	            
//	            while((readin = inputStream2.read()) != -1) { 
//	                 printStream.write(readin); 
//	                 System.out.println(readin);
//	            }
	            printStream.flush();
	            printStream.close();
	            inputStream.close();
	            
//	            clientSkt.close();
	            
	            System.out.println("\n檔案傳送完畢！"); 
	            
	            
	            
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
		
	}
	
	
	public int getFileSize(String filename){
		IContainer container = IContainer.make();
		int result = container.open(filename, IContainer.Type.READ, null);
		System.out.println("getFileSizeFileName = "+ filename);
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
