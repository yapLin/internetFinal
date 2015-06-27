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
	
	public String[] searchVideo(String key){
		int i = 0;
		int j = 0;
		String[] ans = null;
		String str = "SELECT * FROM video WHERE name LIKE '%"+key+"%'";
		
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

}
