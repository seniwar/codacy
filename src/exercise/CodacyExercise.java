package exercise;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;


public class CodacyExercise {
	//https://github.com/seniwar/codacy.git
	
	public static void main(String [] args) throws IOException{
		Scanner scan = new Scanner(System.in);
		try {
		    System.out.println("To list the commits, please enter the GitHub url.");
		    String url = scan.nextLine();
		    String jsonLogs = new Git().getLogs(url);
			System.out.println("The Commit history is the following: \n" + jsonLogs);

		    parseLogs(jsonLogs);
		    
		} 
		finally {
			scan.close();
		}
	}

	
	public static Commit[] parseLogs(String jsonLogs) {
		

		
		return null;
		
	}

	
	

}
