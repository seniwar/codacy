package exercise;

import java.io.IOException;
import java.util.Scanner;

import exeptions.InvalidInputExeption;
import exeptions.RunCommandExeption;


public class CodacyExercise {

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
