package client;
import java.sql.*;

public class Main {
	public static void main(String[] args){
		
		Video vod = new Video();
		int[] qq =  vod.searchVideo("s06s06南");
		
		
		for(int i = 0; i < qq.length; i++){
			System.out.println(qq[i]);
		}
		
	}
}
