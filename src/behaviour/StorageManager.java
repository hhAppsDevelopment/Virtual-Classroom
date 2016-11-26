package behaviour;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class StorageManager {

	FileInputStream fis;
	FileOutputStream fos;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	public void saveQuestion(model.Question q,String fileName){
		
		try {
			
			Path currentRelativePath = Paths.get("");
			fos = new FileOutputStream(new File(currentRelativePath.toAbsolutePath().toString()+"\\"+fileName + ".test"));
			oos = new ObjectOutputStream(fos);
			
			oos.writeObject(q);
			
			oos.close();
			fos.close();
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		
		
	}

	public Object getAvailable() {
		JPanel output = new JPanel();
		output.setLayout(new GridLayout(0,3));
		output.add(new JLabel("Title"));
		output.add(new JLabel("Grade"));
		output.add(new JLabel("Student"));
		for(int i = 0; i < getList(0).size();i++){
			output.add(new JLabel(readQuestion(getList(0).get(i)).getTitle()));
			output.add(new JLabel(readQuestion(getList(0).get(i)).getGrade()));
			output.add(new JLabel(readQuestion(getList(0).get(i)).getSolver()));
		}

		return output;
		
	}
	public ArrayList<String> getList(int listtype) {
		
		Path currentRelativePath = Paths.get("");
		File folder = new File(currentRelativePath.toAbsolutePath().toString());
		
		File[] listOfFiles = folder.listFiles();
		
		ArrayList<String> als = new ArrayList<>();
				
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".test")) {
		        
		    	  if(listtype == 0){
		    		  als.add(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length()-5));
		    	  }
		    	  else if (listtype == 1 && !readQuestion(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length()-5)).getSolved()){
		    		  als.add(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length()-5));
		    	  }
		    	  else if (listtype == 2 && readQuestion(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length()-5)).getGrade().equals("N/A")&& readQuestion(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length()-5)).getSolved()){
		    		  als.add(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length()-5));
		    	  }
		    	  
		    	  
		      } 
		    }
		  
		
		return als;
	}
		
	
	public model.Question readQuestion(String fileName){
		model.Question q;
		try{
			Path currentRelativePath = Paths.get("");
			fis = new FileInputStream(currentRelativePath.toAbsolutePath().toString()+"\\"+fileName + ".test");
			ois = new ObjectInputStream(fis);
			
			q = (model.Question) ois.readObject();
			
			ois.close();
			fis.close();
			return q;
			
		} catch (Exception e){
			
			return null;
		}
		
	}

	public model.User checkUser() {
		
		
		JPanel input = new JPanel();
		input.setLayout(new GridLayout(0,2));
		
		JTextField username = new JTextField();
		JPasswordField password = new JPasswordField();
		
		input.add(new JLabel("Username:"));
		input.add(username);
		input.add(new JLabel("Password:"));
		input.add(password);
		
		
		
		int answ = JOptionPane.showConfirmDialog(null, input,"Authentication",JOptionPane.DEFAULT_OPTION);
		if(answ == JOptionPane.OK_OPTION){
			
			
		
		model.User u;
		
		try{
					
			
			Path currentRelativePath = Paths.get("");
			fis = new FileInputStream(currentRelativePath.toAbsolutePath().toString()+"\\"+username.getText()+".user");
			ois = new ObjectInputStream(fis);
			
			u = (model.User) ois.readObject();
			
			ois.close();
			fis.close();
			

			if(username.getText().equals(u.getUsername()) && new String(password.getPassword()).equals(u.getPassword()) && u.getExaminer() == true){
				return u;
			}else if(username.getText().equals(u.getUsername()) && new String(password.getPassword()).equals(u.getPassword())){
				return u;
			}else{
				
				JOptionPane.showConfirmDialog(null, "Incorrect login credentials", "Error", JOptionPane.DEFAULT_OPTION);
				u.setLevel(0);
				
				return u;
			}
			
			
			
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		}
		return null;
		
		
	}
	
	public void createUser(model.User u){
		
		try{
			
			Path currentRelativePath = Paths.get("");
			fos = new FileOutputStream(currentRelativePath.toAbsolutePath().toString()+"\\"+u.getUsername()+".user");
			oos = new ObjectOutputStream(fos);
			
			oos.writeObject(u);
			
			oos.close();
			fos.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
}
