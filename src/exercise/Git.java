package exercise;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import exeptions.RunCommandExeption;

//melhorar o error handeling
public class Git {

	public static String getGitProjectName(String url) {
		String[] urlParts = url.split("/");
		String lastPart = urlParts[urlParts.length-1];
		int gitIndex = lastPart.indexOf(".git");
		return lastPart.substring(0, gitIndex);
	}
	
	public static String gitCloneAndLog(String url, String path) throws IOException, InterruptedException, RunCommandExeption{
	
		System.out.println("\nClonning the project...");
		runCommand("git clone " + url);
		System.out.println("Done.\n");
				
		//System.out.println("Getting Commit List for the project...\n");
		return runCommand("git -C " + path + " log --pretty=format:\"%H,%an,%ad,%s\"" );	
	}
	
	
	public static String gitPullAndLog(String url, String path) throws IOException, InterruptedException, RunCommandExeption{
		
		System.out.println("\nProject exists but there are no cached logs.\n");
		System.out.println("Pulling the project...\n");
		runCommand("git -C " + path + " pull" );
		System.out.println("Done.\n");
				
		//System.out.println("Getting Commit List for the project...\n");
		return runCommand("git -C " + path + " log --pretty=format:\"%H,%an,%ad,%s\"" );	
	}	
	
	
	public static String runCommand(String command) throws RunCommandExeption, IOException, InterruptedException {
		//talvez colocar uma especie de loading 
		int exit;
		String output = "";
		String line = "";
		String error = "";
		Process pr;

		pr = Runtime.getRuntime().exec(command);			
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));  
		BufferedReader stdError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
 
        while ((line = stdInput.readLine()) != null) {
        	output = output + "\n" + line;
		}
        
        while ((line = stdError.readLine()) != null) {
        	error = error + "\n" + line;
        }

        exit = pr.waitFor();
		if (exit != 0) {
			throw new RunCommandExeption("Error Running command: " + command + error);
		}	
		
		stdInput.close();
		stdError.close();

		return output;
	}
	
}
