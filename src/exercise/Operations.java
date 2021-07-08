package exercise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import exeptions.InvalidInputExeption;
import exeptions.RunCommandExeption;

public class Operations {
	
	String projectPath;
	File projectDir;
	String cachedCommitsPath;
	File cachedCommitsDir;
	
	public Operations() {}
	
	public Operations(String projectPath) {
		this.projectPath = projectPath;
		this.projectDir = new File(projectPath);
		this.cachedCommitsPath = projectPath + "\\cachedLogs";
		this.cachedCommitsDir = new File(cachedCommitsPath);
	}
	
	public String getProjectPath() {
		return projectPath;
	}

	public File getProjectDir() {
		return projectDir;
	}

	public String getCachedCommitsPath() {
		return cachedCommitsPath;
	}

	public File getCachedCommitsDir() {
		return cachedCommitsDir;
	}

	
	public void seeCommitLogs(String url) throws IOException, InterruptedException, RunCommandExeption, InvalidInputExeption, ClassNotFoundException  {    
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

	
	public boolean isDirEmpty(String path) throws IOException {
		Path cachedCommitsPathObj = Paths.get(path);
	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(cachedCommitsPathObj)) {
	        return !dirStream.iterator().hasNext();
	    }
	}
	
	
	public void logParseAndSerialize(String command, String url) throws RunCommandExeption, IOException, InterruptedException, InvalidInputExeption {		
		Commit[] commits;
		String commitLogs = null;
		if(command.equals("clone")) {
			commitLogs = Git.gitCloneAndLog(url, projectPath);		
		}
		else if(command.equals("pull")) {
			commitLogs = Git.gitPullAndLog(url, projectPath);
		}
		else {
			throw new InvalidInputExeption("Invalid input: " + command);
		}
		commits = parseAndPrintCommits(commitLogs);
		cacheCommitLogs(commits);
	}
	

	public Commit[] parseAndPrintCommits(String commitLogs) {

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
	

	public void printCommits(Commit[] commits) {
		
		for (int i = 0; i < commits.length; i++) {
			printCommit(commits[i]);
		}
	}


	public void printCommit(Commit commit) {
		
		System.out.println("Commit: " + commit.getSha());
		System.out.println("Author: " + commit.getAuthor());
		System.out.println("Date: " + commit.getDate());
		System.out.println("Message: " + commit.getMessage() + "\n");
	}
	

	public void cacheCommitLogs(Commit[] commits) {

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
	
	
	public Commit[] readCachedCommits() throws IOException , FileNotFoundException, ClassNotFoundException {
		
		String cachedCommitsFileName = cachedCommitsPath + "\\commits.ser";
		Commit[] commits = null;

        FileInputStream file = new FileInputStream(cachedCommitsFileName);
        ObjectInputStream in = new ObjectInputStream(file);   
        commits = (Commit[])in.readObject();
        //colocar em finally
        in.close();
        file.close();
          
        System.out.println("\nShowing cached commit list...\n");
		
		return commits;
	}
}
