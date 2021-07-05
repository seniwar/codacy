package exercise;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Git {

	//https://github.com/seniwar/codacy.git
	public String getLogs(String url){

		String gitProjectFolder = getGitProjectName(url);
		File f = new File(System.getProperty("user.dir") + "\\" + gitProjectFolder);
		Path directory = Paths.get(System.getProperty("user.dir") + "\\" + gitProjectFolder);

		System.out.println("Please wait while we fetch the information...");
		//colocar output do clone e do git pull
		
		if (!f.exists()) {
			runCommand("git clone " + url);
		}
		else {
			runCommand("git -C " + directory.toString() + " pull" );
		}
		
		return runCommand("git -C " + directory.toString() + " log --pretty=format:'{%n  \"commit\": \"%H\",%n   \"subject\": \"%s\",%n    \"author\": {%n    \"name\": \"%aN\",%n    \"date\": \"%aD\"%n  }%n}" ); 
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
				throw new AssertionError(String.format("Error running command: ", command));
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
