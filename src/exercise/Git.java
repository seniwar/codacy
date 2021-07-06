package exercise;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

//melhorar o error handeling
public class Git {

	public static String getLogs(String url) throws IOException, InterruptedException, RunCommandExeption{
		
		File f = new File(System.getProperty("user.dir") + "\\" + getGitProjectName(url));
		cloneOrPullProject(url, f);
				
		System.out.println("Getting Commit List for the project...\n");
		return runCommand("git -C " + f.getAbsolutePath() + " log --pretty=format:\"%H,%an,%ad,%s\"" );	
	}
	
	
	public static String getGitProjectName(String url) {
		String[] urlParts = url.split("/");
		String[] tmpArray = urlParts[urlParts.length-1].split(".git");
		return tmpArray[0];
	}
	
	
	public static String cloneOrPullProject(String url, File f) throws RunCommandExeption, IOException, InterruptedException {
		
		if (!f.exists()) {
			System.out.println("Clonning the project...");
			return runCommand("git clone " + url);
		}
		else {
			System.out.println("Pulling the project...");
			return runCommand("git -C " + f.getAbsolutePath() + " pull" );			
		}
	}
	
		
	public static String runCommand(String command) throws RunCommandExeption, IOException, InterruptedException {
		//talvez colocar uma especie de loading 
		int exit;
		Process pr;
		String output = "";
		String line = "";
		String error = "";
		

		pr = Runtime.getRuntime().exec(command);			
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));  
		BufferedReader stdError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
 
        while ((line = stdInput.readLine()) != null) {
        	output = output + "\n" + line;
		}
        
        while ((line = stdError.readLine()) != null) {
        	error = error + "\n" + line;
        }
        //alguns outputs estão a ir para o stderror e nao percebo pq. nao me apetece resolver isto
        
        exit = pr.waitFor();
		if (exit != 0) {
			throw new RunCommandExeption("Error Running command: " + command + error);
		}	
		
		stdInput.close();
		stdError.close();

		return output;
	}
	

	
	
}
