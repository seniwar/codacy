package exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import exeptions.RunCommandExeption;


public class GitCommands {
	
	private static final String GIT_CLONE = "git clone ";
	private static final String GIT_LOG = " log --pretty=format:\"%H,%an,%ad,%s\" --date=format:'%Y-%m-%dT%H:%M:%SZ'";
	private static final String GIT_CHOOSE_DIR = "git -C ";
	private static final String NEW_LINE = "\n";
	
	
	public static String gitCloneAndLog(String url, String path) throws IOException, InterruptedException, RunCommandExeption{			
		runCommand(GIT_CLONE + url);	
		return runCommand(GIT_CHOOSE_DIR + path + GIT_LOG);	
	}
	
	
	public static String runCommand(String command) throws RunCommandExeption, IOException, InterruptedException {
		int exit;
		String output = "";
		String line = "";
		String error = "";
		Process pr;

		pr = Runtime.getRuntime().exec(command);		
		exit = pr.waitFor();
		
		//make this a function and put insde try with resources. 
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));  
		BufferedReader stdError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
 
        while ((line = stdInput.readLine()) != null) {
        	output = output + NEW_LINE + line;
		}
        
        while ((line = stdError.readLine()) != null) {
        	error = error + NEW_LINE + line;
        }
    
		if (exit != 0) {
			throw new RunCommandExeption("Error Running command: " + command + error);
		}	
		
		stdInput.close();
		stdError.close();

		return output;
	}
	
}
