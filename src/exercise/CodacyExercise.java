package exercise;

import java.io.IOException;
import java.util.Scanner;


public class CodacyExercise {
	//https://github.com/seniwar/codacy.git
	
	public static void main(String [] args) throws RunCommandExeption, IOException, InterruptedException {
		Scanner scan = new Scanner(System.in);
		try {
		    System.out.println("To list the commits, please enter the GitHub url.");
		    String url = scan.nextLine();
		    new Git();
			String logs = Git.getLogs(url);

		    //to use later maybe
		    Commit[] comits = parseAndPrintLogs(logs);
		    
		} 
		finally {
			scan.close();
		}
	}

	//Eu queria ter sacado os logs em formato json e depois parsar directamente para um objecto 
	//json utilizando a biblioteca org.json, mas nao estava a conseguir por isso segui em frente
	public static Commit[] parseAndPrintLogs(String logs) {

		logs = logs.substring(logs.indexOf("\n")+1);
		String[] logsArray = logs.split("\n");
		
		int logsSize = logsArray.length;
		Commit[] comits = new Commit[logsSize];
		
		System.out.println("The List of Commits is: \n");
		for (int i = 0; i < logsSize; i++) {
			String[] log = logsArray[i].split(",");
			comits[i] = new Commit(log[0], log[3], log[2], log[1]);
				
			System.out.println("Commit: " + comits[i].getSha());
			System.out.println("Author: " + comits[i].getAuthor());
			System.out.println("Date: " + comits[i].getDate());
			System.out.println("Message: " + comits[i].getMessage() + "\n");
		}
		
		return comits;	
	}
}
