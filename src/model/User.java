package model;

public class User implements java.io.Serializable{
	String username;
	String password;
	boolean examiner;
	int level;

	public User(String username, String password, boolean examiner,int l){
		this.username = username;
		this.password = password;
		this.examiner = examiner;
		this.level = l;
	}
	
	public String getUsername(){
		return username;
		
	}
	public String getPassword(){
		return password;
		
	}
	public boolean getExaminer(){
		return examiner;
		
	}
	
	public int getLevel(){
		return level;
	}
	
	public void setLevel(int i){
		if(i==0||i==1||i==2){
			this.level = i;
		}
	}
}
