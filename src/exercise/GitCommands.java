package exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import exeptions.RunCommandExeption;


public class GitCommands {
	
	public static String gitCloneAndLog(String url, String path) throws IOException, InterruptedException, RunCommandExeption{
		
		System.out.println("\nClonning the project...");
		runCommand("git clone " + url);
		System.out.println("Done.\n");

		return runCommand("git -C " + path + " log --pretty=format:\"%H,%an,%ad,%s\"" );	
	}
	
	public static String gitPullAndLog(String url, String path) throws IOException, InterruptedException, RunCommandExeption{
		
		System.out.println("\nProject exists but there are no cached logs.\n");
		System.out.println("Pulling the project...\n");
		runCommand("git -C " + path + " pull" );
		System.out.println("Done.\n");
				
		return runCommand("git -C " + path + " log --pretty=format:\"%H,%an,%ad,%s\"" );	
	}	
	
	
	public static String runCommand(String command) throws RunCommandExeption, IOException, InterruptedException {
		//Maybe put some kind of loading 
		int exit;
		String output = "";
		String line = "";
		String error = "";
		Process pr;

		pr = Runtime.getRuntime().exec(command);		
		exit = pr.waitFor();
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));  
		BufferedReader stdError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
 
        while ((line = stdInput.readLine()) != null) {
        	output = output + "\n" + line;
		}
        
        while ((line = stdError.readLine()) != null) {
        	error = error + "\n" + line;
        }

        
		if (exit != 0) {
			throw new RunCommandExeption("Error Running command: " + command + error);
		}	
		
		stdInput.close();
		stdError.close();

		return output;
	}
	
}
