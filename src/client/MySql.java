package client;

import java.sql.*;


public class MySql {
	private Connection con = null;
	private Statement str = null;
	
	
	public Statement connectSql(){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String url = "jdbc:mysql://114.36.76.84:3306/yap";
		String username = "yap";
		String password = "456121";
		
		
	    System.out.println("Connecting database...");
	    try {
			con = DriverManager.getConnection(url, username, password);
			str = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			System.out.println("Database connected!");
			return str;
			
		} catch (SQLException e) {
			throw new RuntimeException("Cannot connect the database!", e);
		}
	}
	
	public void closeSql(){
		System.out.println("Closing the connection.");
	    if (con != null) try { con.close(); } catch (SQLException ignore) {}
	}
}
