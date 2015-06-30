package test;

import java.sql.*;

public class Video {
	private MySql sql;
	private ResultSet row;
	private Statement stm;
	
	public Video(){
		sql = new MySql();
		stm = sql.connectSql();
		row = null;
	}
	
	public int[] searchVideo(String key){
		int i = 0;
		int j = 0;
		int[] ans = null;
		String str = "SELECT * FROM video WHERE name LIKE '%"+key+"%'";
		
		try {
			row = stm.executeQuery(str);
			row.last();
			i = row.getRow();
			row.first();
			
			
			if(i == 0){
				ans = new int[1];
			}
			else{
				ans = new int[i];
			}
			
			
			if(i > 0){
				do{
					
					ans[j] = Integer.parseInt(row.getString("id"));
					j++;
				}
				while(row.next());
			}
			else{
				ans[0] = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ans;
	}
	
	public String[] getVideo(){
		int i = 0;
		int j = 0;
		String[] ans = null;
		String str = "SELECT * FROM video ORDER BY id";
		
		try {
			row = stm.executeQuery(str);
			row.last();
			i = row.getRow();
			row.first();
			ans = new String[i+1];
			
			if(i > 0){
				ans[0] = Integer.toString(i);

				do{
					j++;
					ans[j] = row.getString("name");
				}
				while(row.next());
			}
			else{
				ans[0] = "0";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ans;
	}

	public void numAdd(int key){
		int i = 0;
		String str = "SELECT * FROM video WHERE id = '"+key+"'";
		
		try {
			row = stm.executeQuery(str);
			
			if(row.next()){
				System.out.println("ok");
				i = Integer.parseInt(row.getString("num"));

				str = "UPDATE video SET num = '"+(i+1)+"' WHERE id = '"+key+"'";
				stm.executeUpdate(str);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int[] getTop3(){
		int[] ans = new int[3];
		String str = "SELECT * FROM video ORDER BY num DESC";
		
		try {
			row = stm.executeQuery(str);
	
			if(row.next()){
				for(int i = 0; i < 3; i++){
					ans[i] = Integer.parseInt(row.getString("id"));
					row.next();
				}
			}	
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ans;
	}

}
