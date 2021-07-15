package data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import exeptions.FailedHTTTPResponseExeption;
import exeptions.RunCommandExeption;
import exeptions.UrlMalFormedExeption;

public class GitProject {
	
	private static final String USER_DIRECTORY ="user.dir";
	private static final String CACHED_COMMITS_FILENAME ="commits.ser";
	private static final String GIT_EXTENSION =".git";
	
	private static final String NEW_LINE ="\n";
	private static final String COMMA =",";
	
	private static final String URL_SPLIT_CHAR ="/";
	private static final int URL_PROJECT_INDEX = 0;
	private static final int URL_USERNAME_INDEX = 3;
	
	private static final int LOG_SHA_INDEX = 0;
	private static final int LOG_AUTHOR_INDEX = 1;
	private static final int LOG_DATE_INDEX = 2;
	private static final int LOG_MESSAGE_INDEX = 3;
	
	String url;
	String projectPath;
	File projectDir;
	String projectName;
	String userName;
	File cachedCommitsFile;
	

	public GitProject(String url) throws UrlMalFormedExeption {
		
		this.url = url;
		setVars();
	}
	
	
	private void setVars() throws UrlMalFormedExeption {
		
		setGitProjectAndUserName();
		this.projectPath = System.getProperty(USER_DIRECTORY) + File.separator + projectName;
		this.projectDir = new File(projectPath);
		this.cachedCommitsFile = new File(projectPath + File.separator + CACHED_COMMITS_FILENAME);
	}
	
	
	private void setGitProjectAndUserName() throws UrlMalFormedExeption {
		
		try {
			String[] urlParts = url.split(URL_SPLIT_CHAR);
			String lastPart = urlParts[urlParts.length-1];
			
			this.projectName = lastPart.substring(URL_PROJECT_INDEX, lastPart.indexOf(GIT_EXTENSION));
			this.userName = urlParts[URL_USERNAME_INDEX];
		}
		catch (Exception e){
			throw new UrlMalFormedExeption("Wrong URL. ");
		}
	}

	
	public void seeCommitLogs() throws IOException, InterruptedException, RunCommandExeption, ClassNotFoundException  {    

		List<Commit> commits = new ArrayList<>();	
		if (!projectDir.exists() || (projectDir.exists() && !cachedCommitsFile.exists())) {
			logParseAndSerialize();
		}
		else {
			commits = ReadAndWriteOperations.readCachedCommits(cachedCommitsFile);
			printCommits(commits);
		}	
	}
	
	
	private void logParseAndSerialize() throws RunCommandExeption, IOException, InterruptedException {		

		List<Commit> commits = new ArrayList<>();
		GitHubAPI gitHubAPI = new GitHubAPI();			
		try {
			commits = getApiResponse(gitHubAPI);
		}
		catch (FailedHTTTPResponseExeption e){
			commits = getFallbackResponse();	
		}

		ReadAndWriteOperations.cacheCommitLogs(commits, projectDir, cachedCommitsFile);
	}


	private List<Commit> getFallbackResponse() throws IOException, InterruptedException, RunCommandExeption {

		List<Commit> commits = new ArrayList<>();
		String commitLogs;
		System.out.println(NEW_LINE + "ERROR invoking GitHub API. Using fallback procedure..." + NEW_LINE);
		if (projectDir.exists()) {
			FileUtils.forceDelete(projectDir);
		}
		commitLogs = GitCommands.gitCloneAndLog(url, projectPath);			
		commits = parseAndPrintCommits(commitLogs);
		return commits;
	}


	private List<Commit> getApiResponse(GitHubAPI gitHubAPI) throws FailedHTTTPResponseExeption {
		
		List<Commit> commits = new ArrayList<>();
		String jsonResponse = GitHubAPI.getCommitsFromAPI(projectName, userName);
		JsonProcessor jsonProc = new JsonProcessor(jsonResponse);
		commits = jsonProc.parseJsonCommitLogs();
		System.out.println(NEW_LINE + "The List of Commits got from API is: " + NEW_LINE);
		printCommits(commits);
		return commits;
	}
	

	private List<Commit> parseAndPrintCommits(String commitLogs) {

		String[] commitArray = splitByLine(commitLogs);
		List<Commit> commits = new ArrayList<>();
		
		System.out.println("The List of Commits is: " + NEW_LINE);
		for (int i = 0; i < commitArray.length; i++) {
			String[] log = commitArray[i].split(COMMA);
			Commit commit = new Commit(log[LOG_SHA_INDEX], log[LOG_MESSAGE_INDEX], log[LOG_DATE_INDEX], log[LOG_AUTHOR_INDEX]);
			commits.add(commit);				
			System.out.println(commit.toString());
		}
		return commits;	
	}


	private String[] splitByLine(String commitLogs) {
		
		commitLogs = commitLogs.substring(commitLogs.indexOf(NEW_LINE)+1);
		String[] commitArray = commitLogs.split(NEW_LINE);
		return commitArray;
	}
	

	public void printCommits(List<Commit> commits) {
		
		for (Commit commit : commits) {
			System.out.println(commit.toString());
		}
	}


	public String getProjectPath() {
		return projectPath;
	}
	

	public File getProjectDir() {
		return projectDir;
	}

	public String getProjectName() {
		return projectName;
	}


	public String getUserName() {
		return userName;
	}

	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	

	public File getCachedCommitsFile() {
		return cachedCommitsFile;
	}
}
