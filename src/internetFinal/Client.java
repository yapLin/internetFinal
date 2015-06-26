package internetFinal;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {        
        try { 
            System.out.println("簡易檔案傳送..."); 

            String remoteHost = "127.0.0.1";
            int port = 5000;
            File file = new File("IMG_2964.flv"); 
            
            System.out.printf("遠端主機: %s%n", remoteHost); 
            System.out.printf("遠端主機連接埠: %d%n", port);
            System.out.printf("傳送檔案: %s%n", file.getName());

            Socket skt = new Socket(remoteHost, port); 

            System.out.println("連線成功！嘗試傳送檔案....");

            PrintStream printStream = 
                new PrintStream(skt.getOutputStream()); 
//            printStream.println("IMG_2964_receive.mp4"); 

            System.out.print("OK! 傳送檔案...."); 
            
            BufferedInputStream inputStream = 
                new BufferedInputStream( 
                        new FileInputStream(file)); 

            int readin; 
//            int s = 0;
            
            while((readin = inputStream.read()) != -1) { 
                 printStream.write(readin); 
//                 Thread.yield();
//                 System.out.println(readin);
//                 s++;
//                 if(s> 300)break;
//                 printStream.flush();
            } 

            printStream.flush();
            printStream.close();
            inputStream.close(); 
            
            skt.close();
            
            System.out.println("\n檔案傳送完畢！"); 
        } 
        catch(Exception e) { 
            e.printStackTrace(); 
        } 
    } 
}