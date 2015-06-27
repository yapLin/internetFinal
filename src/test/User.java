package test;
import java.sql.*;

public class User {
	private MySql sql;
	private ResultSet row;
	private Statement stm;
	
	public User(){
		sql = new MySql();
		stm = sql.connectSql();
		row = null;
	}
	
	//check account whether exist
	public int searchUser(String acc){
		try {
			String str = String.format(
					"SELECT * FROM account WHERE acc = '%s'",
					acc
			);
			row = stm.executeQuery(str);
			
			if(row.next()){
				return 0;
			}
			else{
				return 1;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch b lock
			e.printStackTrace();
		}
		return 0;
	}
	
	//new user
	public void newUser(String acc, String psw, String mail){
		try {
			String str = String.format(
					"INSERT INTO account (acc, psw, mail) VALUES ('%s', '%s', '%s')",
					acc, psw, mail
			);
			stm.executeUpdate(str);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//login
	public int logIn(String acc, String psw){
		try {
			String str = String.format(
					"SELECT * FROM account WHERE acc = '%s'",
					acc
			);
			row = stm.executeQuery(str);
			
			if(row.next()){
				String pswC = row.getString("psw");
				
				if(pswC.equals(psw))	return 1;
				else					return 0;
			}
			else{
				return 0;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
}
