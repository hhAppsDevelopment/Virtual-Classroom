package behaviour;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class MainClass {
	
	JFrame frame;

	JPanel mainPanel;
	JPanel newTestPanel;
	JPanel examineePanel;
	JPanel gradeTestPanel;
	
	JButton logIn;
	JButton createAcc;
	JButton checkList;
	JButton backButton;
	
	JLabel titleLabel;
	JLabel questionLabel;
	JLabel timerLabel;
	JLabel automaticPercentage;
	JLabel manual;
	JTextField manualPercentage; 
	JTextArea answerField;	
	JButton finishButton;
	
	JButton saveButton;
	JPanel inputPanel;
	JTextField titleField;
	JTextField questionField;
	JTextField timeField;
	JTextField factField;
	
	Timer timer;
	int interval,remaining;
	
	model.Question q;
	String fileName;
	model.User u;
	StorageManager sm;
	
	
	public static void main(String[] args){
		
		MainClass mc = new MainClass();
		mc.setupGui();
		mc.sm = new StorageManager();
	}
	
	
	public void setupGui(){
		
		frame = new JFrame("Virtual examination room");
		frame.setSize(400, 300);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-(frame.getSize().width/2), dim.height/2-frame.getSize().height/2);
		
		setupMain();		
		frame.setContentPane(mainPanel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);	
		frame.setVisible(true);
	}
	
	public void changePane(String text) {
		JPanel panel = mainPanel;
		
		switch(text){
		case "Log in": 
			u = sm.checkUser();
			int user = u.getLevel();
			if (user == 2){
				
				Object[] options = {"Create new test","Grade submitted work","Cancel"};
				int n = JOptionPane.showOptionDialog(null,"What would you like to do?","Choosing path",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, options, options[2]);
				if(n == JOptionPane.YES_OPTION){
					 
					setupNewTest();	
					panel = newTestPanel;
				}else if(n == JOptionPane.NO_OPTION){
					String filename = (String)JOptionPane.showInputDialog(null, "Choose which exam to grade", "Tests",
							 JOptionPane.DEFAULT_OPTION, null, sm.getList(2).toArray(),"");
					fileName = filename;
					q = sm.readQuestion(filename);
					setupGradeTest(sm.readQuestion(filename));	
					panel = gradeTestPanel; 
				}else{
					
				}
							
			}else if(user == 1){
				String filename = (String)JOptionPane.showInputDialog(null, "Choose which exam to open", "Tests",
						JOptionPane.DEFAULT_OPTION, null, sm.getList(1).toArray(),"");
				fileName = filename;
				q = sm.readQuestion(filename);
				setupExaminee(sm.readQuestion(filename));
				panel = examineePanel; 
				if(q.getSolved()){
					answerField.setEditable(false);
					timer.cancel();
					timeField.setText(q.getGrade());
					
				}
				
			}else{
				setupMain();
			}
			
			
			break;
			
		case "Back":
			
			setupMain();
			
			break;
			
		case "Add final grade":
			String answ = JOptionPane.showInputDialog(null, "Add final grade\n(take automatic percentage (" + 
					Math.round(q.getRate()*100) +"%) into consideration) \nwrite in any text", "Rating", JOptionPane.DEFAULT_OPTION);
			if(answ.equals(null))return;
			
			q.setGrade(answ);
			
			sm.saveQuestion(q, fileName);
			
			
			break;
		}
		
		
		
		
		frame.setContentPane(panel);
		frame.validate();
		frame.repaint();
		
	}
	
	public void setupMain(){
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		logIn = new JButton("Log in");
		createAcc = new JButton("Create account");
		checkList = new JButton("Test summary");
		
		logIn.addActionListener(new onChangeListener());
		createAcc.addActionListener(new onOutputListener());
		checkList.addActionListener(new onOutputListener());
		
		logIn.setAlignmentX(Component.CENTER_ALIGNMENT);
		createAcc.setAlignmentX(Component.CENTER_ALIGNMENT);
		checkList.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(logIn);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(createAcc);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(checkList);
		mainPanel.add(Box.createVerticalGlue());
		
	}
	public void setupNewTest(){
		
		newTestPanel = new JPanel();
		newTestPanel.setLayout(null);
		
		backButton = new JButton("Back");		
		backButton.addActionListener(new onChangeListener());
		backButton.setSize(90,30);
		backButton.setLocation(300,235);
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new onOutputListener());
		saveButton.setSize(90,30);
		saveButton.setLocation(200,235);
		
		titleField = new JTextField();
		questionField = new JTextField();
		timeField = new JTextField();
		factField = new JTextField();
		
		inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(0,2));
		inputPanel.add(new JLabel("Title of question:"));
		inputPanel.add(titleField);
		inputPanel.add(new JLabel("Question's text:"));
		inputPanel.add(questionField);
		inputPanel.add(new JLabel("Available time (in seconds):"));
		inputPanel.add(timeField);
		inputPanel.add(new JLabel("Facts (separator: space):"));
		inputPanel.add(factField);
		
		inputPanel.setSize(380,200);
		inputPanel.setLocation(5,5);

		newTestPanel.add(inputPanel);
		newTestPanel.add(saveButton);
		newTestPanel.add(backButton);
		
	}
	
	public void setupGradeTest(model.Question q){
		gradeTestPanel = new JPanel();
		gradeTestPanel.setLayout(null);
		
		backButton = new JButton("Back");
		backButton.addActionListener(new onChangeListener());
		backButton.setSize(120,40);
		backButton.setLocation(270,215);

		titleLabel = new JLabel();
		titleLabel.setText("Title: " + q.getTitle());
		titleLabel.setSize(400,30);
		titleLabel.setLocation(5,5);

		questionLabel = new JLabel();
		questionLabel.setText("Question: " + q.getQuestion());
		questionLabel.setSize(400,30);
		questionLabel.setLocation(5,40);
		
		automaticPercentage = new JLabel();
		automaticPercentage.setText("Facts: "+Math.round(q.getRate()*100)+"%");		
		automaticPercentage.setSize(120,40);
		automaticPercentage.setLocation(5,215);
		
		answerField = new JTextArea();
		answerField.setLocation(5,75);
		answerField.setSize(380,100);
		answerField.setText(q.ans);
		answerField.setEditable(false);
		
		finishButton = new JButton("Add final grade");
		finishButton.addActionListener(new onChangeListener());
		finishButton.setSize(120,40);
		finishButton.setLocation(140,215);
		
		gradeTestPanel.add(titleLabel);
		gradeTestPanel.add(questionLabel);
		gradeTestPanel.add(answerField);
		gradeTestPanel.add(automaticPercentage);
		gradeTestPanel.add(finishButton);
		gradeTestPanel.add(backButton);
		
	}
	public void setupExaminee(model.Question q){
		
		examineePanel = new JPanel();
		examineePanel.setLayout(null);
		
		backButton = new JButton("Back");
		backButton.addActionListener(new onChangeListener());
		backButton.setSize(120,40);
		backButton.setLocation(270,215);

		titleLabel = new JLabel();
		titleLabel.setText(q.getTitle());
		titleLabel.setSize(400,30);
		titleLabel.setLocation(5,5);
		titleLabel.setFont(new Font(titleLabel.getFont().getFontName(),
				Font.BOLD,titleLabel.getFont().getSize()));

		questionLabel = new JLabel();
		questionLabel.setText(q.getQuestion());
		questionLabel.setSize(400,30);
		questionLabel.setLocation(5,40);
		
		timerLabel = new JLabel();
		timerLabel.setText("Time left: "+q.getTimeGiven()+"s");		
		timerLabel.setSize(150,40);
		timerLabel.setLocation(5,215);
		
		answerField = new JTextArea();
		answerField.setLocation(5,75);
		answerField.setSize(380,100);
		
		finishButton = new JButton("Finish test");
		finishButton.addActionListener(new onOutputListener());
		finishButton.setSize(120,40);
		finishButton.setLocation(140,215);
		
		examineePanel.add(titleLabel);
		examineePanel.add(questionLabel);
		examineePanel.add(answerField);
		examineePanel.add(timerLabel);
		examineePanel.add(finishButton);
		examineePanel.add(backButton);
		
		timer(q.getTimeGiven());
	}
	
	public void timer(int i){
		interval = 0;
		remaining = i;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){
			public void run() {
				timerLabel.setText("Time left: "+Integer.toString(remaining--)+"s");
				if(remaining<=0){
					
					JOptionPane.showConfirmDialog(null, "You ran out of time.",
							"Warning", JOptionPane.DEFAULT_OPTION);
					
					q.answer(answerField.getText(),u.getUsername());
					q.setSolved(true);
					sm.saveQuestion(q, fileName);
					
					timer.cancel();
					
					setupMain();
					frame.setContentPane(mainPanel);
					frame.validate();
					frame.repaint();
				}
			}
		}, 1000, 1000);
	}

	
	class onChangeListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			MainClass.this.changePane(e.getActionCommand());
		}
		
	}
	
	class onOutputListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()){
					
			case "Test summary":
				JOptionPane.showConfirmDialog(null, sm.getAvailable(),"Available tests",JOptionPane.DEFAULT_OPTION);
				break;
				
			case "Create account":
				JPanel input = new JPanel();
				input.setLayout(new GridLayout(0,2));
				
				JTextField username = new JTextField();
				JPasswordField password = new JPasswordField();
				
				input.add(new JLabel("Username:"));
				input.add(username);
				input.add(new JLabel("Password:"));
				input.add(password);
				input.add(new JLabel("User type:"));
				input.add(new JLabel("Student"));
				
				username.requestFocus();
				
				int answer = JOptionPane.showConfirmDialog(null, input,"Create account",JOptionPane.DEFAULT_OPTION);
				if(answer == JOptionPane.OK_OPTION){
				
					sm.createUser(new model.User(username.getText(),new String(password.getPassword()),false,1));
					
				}
				
				break;
				
			case "Finish test":
				
				MainClass.this.timer.cancel();
				
				q.answer(answerField.getText(),u.getUsername());
				q.setSolved(true);
				sm.saveQuestion(q, fileName);
				
				
				setupMain();
				frame.setContentPane(mainPanel);
				frame.validate();
				frame.repaint();
				
				break;
				
				
			case "Save":
				
				String title = titleField.getText();
				String question = questionField.getText();
				String fact = factField.getText();
				int time;
				
				try{
					time = Integer.parseInt(timeField.getText());
				}catch(Exception exc){					
					JOptionPane.showConfirmDialog(null, "Please give a number for the time, without dimension!\n" + 
							exc,"Error",JOptionPane.DEFAULT_OPTION);
					break;
				}
				
				titleField.setEditable(false);
				questionField.setEditable(false);
				timeField.setEditable(false);
				factField.setEditable(false);
								
				int answ = JOptionPane.showConfirmDialog(null, inputPanel,"Approval",
						JOptionPane.OK_CANCEL_OPTION);				
				
				
				switch(answ){
				case JOptionPane.OK_OPTION:
								
					ArrayList<String> als = new ArrayList<>();
					char[] facts = factField.getText().toCharArray();
					String currentFact = "";
					
					for(int i = 0; i<facts.length; i++){
						
						if(facts[i] == ' '){
							als.add(currentFact);
						}else{
							currentFact = currentFact + facts[i];
						}
						
					}
					
					String fileName = JOptionPane.showInputDialog(null,"Please enter a filename: ");
					sm.saveQuestion(new model.Question(title,question,als,time),fileName);
					
					setupMain();
					frame.setContentPane(mainPanel);
					frame.validate();
					frame.repaint();
					
					break;
				case JOptionPane.CANCEL_OPTION:					
				default:
					titleField.setEditable(true);
					questionField.setEditable(true);
					timeField.setEditable(true);
					factField.setEditable(true);					
					
					MainClass.this.setupNewTest();
					frame.setContentPane(newTestPanel);
										
					titleField.setText(title);
					questionField.setText(question);
					timeField.setText(time + "");
					factField.setText(fact);
					
					frame.revalidate();
					frame.repaint();
					
					break;
				}
				
				break;
			}
			
			
		}
		
	}
	
}
