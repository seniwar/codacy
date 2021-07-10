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

import org.apache.commons.io.FileUtils;

import exeptions.FailedHTTTPResponseExeption;
import exeptions.InvalidInputExeption;
import exeptions.RunCommandExeption;

public class GitProject {
	
	String url;
	String projectPath;
	File projectDir;
	String cachedCommitsPath;
	File cachedCommitsDir;
	String projectName;
	String userName;
	
	
	public GitProject() {}
	
	public GitProject(String url) {
		this.url = url;
		setVars();
	}
	
	
	public void setVars() {
		setGitProjectAndUserName();
		this.projectPath = System.getProperty("user.dir") + "\\" + projectName;
		this.projectDir = new File(projectPath);
		this.cachedCommitsPath = projectPath + "\\cachedLogs";
		this.cachedCommitsDir = new File(cachedCommitsPath);
	}
	
	
	public void setGitProjectAndUserName() {
		String[] urlParts = url.split("/");
		String lastPart = urlParts[urlParts.length-1];
		int gitIndex = lastPart.indexOf(".git");
		projectName = lastPart.substring(0, gitIndex);
		userName = urlParts[3];
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
	
	
	public void seeCommitLogs() throws IOException, InterruptedException, RunCommandExeption, InvalidInputExeption, ClassNotFoundException  {    
		Commit[] commits;		
		
		if (!projectDir.exists()) {
			logParseAndSerialize();
		}
		else if (projectDir.exists() && (!cachedCommitsDir.exists() || isDirEmpty(cachedCommitsPath))) {
			FileUtils.forceDelete(projectDir);
			logParseAndSerialize();
		}
		else {
			commits = readCachedCommits();
			printCommits(commits);
		}	
	}
	
	public void logParseAndSerialize() throws RunCommandExeption, IOException, InterruptedException, InvalidInputExeption {		
		Commit[] commits;
		String commitLogs = null;
		GitHubAPI gitHubAPI = new GitHubAPI();	
		
		try {
			String jsonResponse = gitHubAPI.getCommitsFromAPI(projectName, userName);
			commits = gitHubAPI.parseJsonCommitLogs(jsonResponse);
			System.out.println("\nThe List of Commits got from API is: \n");
			printCommits(commits);
		}
		catch (FailedHTTTPResponseExeption e){
			System.out.println("\nERROR invoking GitHub API. Using fallback procedure...");
			commitLogs = GitCommands.gitCloneAndLog(url, projectPath);			
			commits = parseAndPrintCommits(commitLogs);	
		}
		cacheCommitLogs(commits);
	}
	
	
	public boolean isDirEmpty(String path) throws IOException {
		Path cachedCommitsPathObj = Paths.get(path);
	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(cachedCommitsPathObj)) {
	        return !dirStream.iterator().hasNext();
	    }
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
		
		if (!projectDir.exists()) {
			projectDir.mkdir();
        }
		
		if (!cachedCommitsDir.exists()) {
			cachedCommitsDir.mkdir();
        }
		
	    try {
			FileOutputStream file = new FileOutputStream(cachedCommitsFileName);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(commits);
			//put on finally?
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
      //put on finally?
        in.close();
        file.close();
          
        System.out.println("\nShowing cached commit list...\n");
		
		return commits;
	}
}
