package main;

import java.io.IOException;
import java.util.Scanner;

import data.GitCommands;
import data.GitProject;
import exeptions.RunCommandExeption;
import exeptions.UrlMalFormedExeption;


public class CodacyExercise {

	public static void main(String [] args) throws RunCommandExeption, IOException, InterruptedException, ClassNotFoundException {
		
		new GitCommands();	
		Scanner scan = new Scanner(System.in);
		
		try {
		    System.out.println("To list the commits, please enter the GitHub url.");
		    String url = scan.nextLine();
		    
		    GitProject gitProj = new GitProject(url);
		    gitProj.seeCommitLogs();
		}
		catch (UrlMalFormedExeption e) {
			e.printStackTrace();
		}
		finally {
			scan.close();
		}
	}
}
