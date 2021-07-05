package exercise;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

//melhorar o error handeling
public class Git {

	//https://github.com/seniwar/codacy.git
	public String getLogs(String url){
		
		String gitProjectFolder = getGitProjectName(url);
		File f = new File(System.getProperty("user.dir") + "\\" + gitProjectFolder);
		Path directory = Paths.get(System.getProperty("user.dir") + "\\" + gitProjectFolder);

		System.out.println("\nPlease wait while we fetch the information...\n");
		//colocar output do clone e do git pull
		
		if (!f.exists()) {
			System.out.println("Clonning the project...");
			runCommand("git clone " + url);
			System.out.println("Done.\n");
			
		}
		else {
			System.out.println("Pulling the project...");
			runCommand("git -C " + directory.toString() + " pull" );
			System.out.println("Done.\n");
			
		}
		//https://github.com/seniwar/codacy.git
		System.out.println("Getting Commit List for the project...\n");
		return runCommand("git -C " + directory.toString() + " log --pretty=format:\"%H,%an,%ad,%s\"" );	
	}
	
	public static String getGitProjectName(String url) {
		String[] urlParts = url.split("/");
		String[] tmpArray = urlParts[urlParts.length-1].split(".git");
		return tmpArray[0];
	}
	
	public static String runCommand(String command) {
		//talvez colocar uma especie de loading 
		int exit;
		Process pr;
		String output = "";
		String line = "";
		
		try {
			pr = Runtime.getRuntime().exec(command);			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));  ;
     
	        while ((line = stdInput.readLine()) != null) {
	        	output = output + "\n" + line;
			}
	        
	        exit = pr.waitFor();
			if (exit != 0) {
				throw new AssertionError(String.format("Error running command: " + command));
			}	
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}	
		return output;
	}
	

	
	
}
