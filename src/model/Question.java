package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{

	String questionString;
	ArrayList<String> factList;
	int timeGiven;
	public String ans;
	String title;
	String grade;
	boolean solved;
	String solverStudent;
	
	public Question(String title,String question,ArrayList<String> factList,int i){
		questionString = question;
		this.factList = factList;
		timeGiven = i;
		this.title = title;
		this.grade = "N/A";
		this.solved = false;
	}
	
	public boolean getSolved(){
		return solved;
	}
	public void setSolved(boolean solve){
		solved = solve;
	}
	
	public String getGrade(){
		return grade;
	}
	
	public String getQuestion(){
		return questionString;
	}
	
	public ArrayList<String> getFactList(){
		return factList;
	}
	
	public int getTimeGiven(){
		return timeGiven;
	}
	
	public double getRate(){
		return (double)getCorrectNum(ans)/(double)factList.size();
	}
	
	public int getCorrectNum(String answer){
		int x = 0;
		for(int i = 0;i<factList.size();i++){
			if(answer.toLowerCase().contains(factList.get(i).toLowerCase())) x++;
		}
		return x;
	}	
	
	public void answer(String s,String username){
		ans = s;
		solverStudent = username;
		solved = true;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setGrade(String s){
		grade = s;
	}
	
	public String getSolver(){
		return solverStudent;
	}
}
