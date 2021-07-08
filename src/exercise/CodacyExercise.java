package exercise;

import java.io.IOException;
import java.util.Scanner;
import exeptions.InvalidInputExeption;
import exeptions.RunCommandExeption;


public class CodacyExercise {

	/*
	https://github.com/seniwar/codacy.git
	https://github.com/Coveros/helloworld.git
	*/
	
	public static void main(String [] args) throws RunCommandExeption, IOException, InterruptedException, InvalidInputExeption, ClassNotFoundException {
		
		Scanner scan = new Scanner(System.in);
		try {
		    System.out.println("To list the commits, please enter the GitHub url.");
		    String url = scan.nextLine();
		    new Git();	
		    
		    Operations ops = new Operations(System.getProperty("user.dir") + "\\" + Git.getGitProjectName(url));
		    ops.seeCommitLogs(url);
		} 
		finally {
			scan.close();
		}
	}
}
