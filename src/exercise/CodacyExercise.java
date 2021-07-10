package exercise;

import java.io.IOException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import exeptions.InvalidInputExeption;
import exeptions.RunCommandExeption;


public class CodacyExercise {

	/*
	https://github.com/seniwar/codacy.git
	https://github.com/Coveros/helloworld.git
	*/
	
	
	public static void main(String [] args) throws RunCommandExeption, IOException, InterruptedException, InvalidInputExeption, ClassNotFoundException {
		
		new GitCommands();	
		Scanner scan = new Scanner(System.in);
		
		try {
		    System.out.println("To list the commits, please enter the GitHub url.");
		    String url = scan.nextLine();
		    
		    GitProject gitProj = new GitProject(url);
		    gitProj.seeCommitLogs();
		} 
		finally {
			scan.close();
		}
	}
}
