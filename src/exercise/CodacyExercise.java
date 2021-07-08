package exercise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import exeptions.InvalidInputExeption;
import exeptions.RunCommandExeption;


public class CodacyExercise {
	
	//devo usar static? ha problema em usar variaveis globais?
	static String projectPath;
	static File projectDir;
	
	static String cachedCommitsPath;
	static File cachedCommitsDir;
	
	/*
	https://github.com/seniwar/codacy.git
	https://github.com/Coveros/helloworld.git
	*/
	
	public static void main(String [] args) throws RunCommandExeption, IOException, InterruptedException, InvalidInputExeption {
		
		Scanner scan = new Scanner(System.in);
		try {
		    System.out.println("To list the commits, please enter the GitHub url.");
		    String url = scan.nextLine();
		    //ha problema em inicializar a class git assim? static?
		    new Git();		    
		    setFilesAndPaths(url);
		    seeCommitLogs(url);
		} 
		finally {
			scan.close();
		}
	}

	//colocar funçoes abaixo numa classe LogsOperations?
	public static void seeCommitLogs(String url) throws IOException, InterruptedException, RunCommandExeption, InvalidInputExeption  {    
		Commit[] commits;
		
				
		if (!projectDir.exists()) {
			logParseAndSerialize("clone", url);
		}
		else if (projectDir.exists() && (!cachedCommitsDir.exists() || isDirEmpty(cachedCommitsPath))) {
			logParseAndSerialize("pull", url);
		}
		else {
			commits = readCachedCommits();//ler logs
			printCommits(commits);//print logs
		}	
	}

	public static void setFilesAndPaths(String url) {
		projectPath = System.getProperty("user.dir") + "\\" + Git.getGitProjectName(url);
	    projectDir = new File(projectPath);
		
		cachedCommitsPath = projectPath + "\\cachedLogs";
		cachedCommitsDir = new File(cachedCommitsPath);
	}
	
	public static boolean isDirEmpty(String path) throws IOException {
		Path cachedCommitsPathObj = Paths.get(path);
	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(cachedCommitsPathObj)) {
	        return !dirStream.iterator().hasNext();
	    }
	}
	
	
	public static void logParseAndSerialize(String command, String url) throws RunCommandExeption, IOException, InterruptedException {		
		Commit[] commits;
		String commitLogs;
		if(command.equals("clone")) {
			commitLogs = Git.gitCloneAndLog(url, projectPath);		
		}
		else {
			commitLogs = Git.gitPullAndLog(url, projectPath);
		}
		commits = parseAndPrintCommits(commitLogs);
		cacheCommitLogs(commits);
	}
	

	public static Commit[] parseAndPrintCommits(String commitLogs) {

		commitLogs = commitLogs.substring(commitLogs.indexOf("\n")+1);
		String[] commitArray = commitLogs.split("\n");
		
		int commitsNumb = commitArray.length;
		Commit[] commits = new Commit[commitsNumb];
		
		System.out.println("The List of Commits is: \n");
		for (int i = 0; i < commitsNumb; i++) {
			String[] log = commitArray[i].split(",");
			commits[i] = new Commit(log[0], log[3], log[2], log[1]);
				
			printCommit(commits[i]);
		}
		
		return commits;	
	}
	
	//devo colocar isto na classe commit?
	public static void printCommits(Commit[] commits) {
		
		for (int i = 0; i < commits.length; i++) {
			printCommit(commits[i]);
		}
	}


	public static void printCommit(Commit commit) {
		
		System.out.println("Commit: " + commit.getSha());
		System.out.println("Author: " + commit.getAuthor());
		System.out.println("Date: " + commit.getDate());
		System.out.println("Message: " + commit.getMessage() + "\n");
	}
	
	
	
	
	/*
	https://github.com/seniwar/codacy.git
	https://github.com/Coveros/helloworld.git
	*/
	
	public static void cacheCommitLogs(Commit[] commits) {

		String cachedCommitsFileName = cachedCommitsPath + "\\commits.ser";
		
		if (!cachedCommitsDir.exists()) {
			cachedCommitsDir.mkdir();
        }
		
	    try {
			FileOutputStream file = new FileOutputStream(cachedCommitsFileName);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(commits);
			//colocar em finally
			out.close();
			file.close();
			System.out.printf("Serialized data is saved in " + cachedCommitsFileName);
	    } 
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	
	public static Commit[] readCachedCommits() {
		
		String cachedCommitsFileName = cachedCommitsPath + "\\commits.ser";
		Commit[] commits = null;
		
		try {   
	        FileInputStream file = new FileInputStream(cachedCommitsFileName);
	        ObjectInputStream in = new ObjectInputStream(file);   
	        commits = (Commit[])in.readObject();
	        //colocar em finally
	        in.close();
	        file.close();
	          
	        System.out.println("\nShowing cached commit list...\n");
	        return commits;
		}
		catch (IOException | ClassNotFoundException e) {
	    	e.printStackTrace();
	    }
		return commits;
	}
}
