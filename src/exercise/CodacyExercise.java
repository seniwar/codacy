package exercise;

import java.io.IOException;
import java.util.Scanner;


public class CodacyExercise {
	//https://github.com/seniwar/codacy.git
	
	public static void main(String [] args) throws IOException{
		Scanner scan = new Scanner(System.in);
		try {
		    System.out.println("To list the commits, please enter the GitHub url.");
		    String url = scan.nextLine();
		    String logs = new Git().getLogs(url);

		    //to use later
		    Commit[] comits = parseAndPrintLogs(logs);
		    
		} 
		finally {
			scan.close();
		}
	}

	//Eu queria ter sacado os logs em formato json e depois parsar directamente para um objecto 
	//json utilizando a biblioteca org.json, mas nao estava a conseguir por isso segui em frente
	public static Commit[] parseAndPrintLogs(String logs) {

		String[] logsArray = logs.split("\n");
		int logsSize = logsArray.length;
		Commit[] comits = new Commit[logsSize-1];
		
		System.out.println("The List of Commits is: \n");
		for (int i = 1; i < logsSize; i++) {
			String[] log = logsArray[i].split(",");
			comits[i-1] = new Commit(log[0], log[1], log[2], log[3]);
				
			System.out.println("Commit: " + comits[i-1].getSha());
			System.out.println("Author: " + comits[i-1].getAuthor());
			System.out.println("Date: " + comits[i-1].getDate());
			System.out.println("Message: " + comits[i-1].getMessage() + "\n");
		}
		
		return comits;	
	}

	
	

}
