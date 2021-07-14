package exercise;

import java.io.IOException;

import exeptions.RunCommandExeption;


public class GitCommands {
	
	private static final String GIT_CLONE = "git clone ";
	private static final String GIT_LOG = " log --pretty=format:\"%H,%an,%ad,%s\" --date=format:'%Y-%m-%dT%H:%M:%SZ'";
	private static final String GIT_CHOOSE_DIR = "git -C ";

	
	public static String gitCloneAndLog(String url, String path) throws IOException, InterruptedException, RunCommandExeption{			
		runCommand(GIT_CLONE + url);	
		return runCommand(GIT_CHOOSE_DIR + path + GIT_LOG);	
	}
	
	
	private static String runCommand(String command) throws RunCommandExeption, IOException, InterruptedException {

		Process pr = Runtime.getRuntime().exec(command);		
		String output = ReadAndWriteOperations.readStream(pr.getInputStream());
		String error = ReadAndWriteOperations.readStream(pr.getErrorStream());
		
		if (pr.waitFor() != 0) {
			throw new RunCommandExeption("Error Running command: " + command + error);
		}	
		return output;
	}
	
	
	
	
}
