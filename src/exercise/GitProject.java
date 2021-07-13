package exercise;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FileUtils;

import exeptions.FailedHTTTPResponseExeption;
import exeptions.InvalidInputExeption;
import exeptions.RunCommandExeption;
import exeptions.UrlMalFormedExeption;

public class GitProject {
	
	String url;
	String projectPath;
	File projectDir;
	String projectName;
	String userName;
	File cachedCommitsFile;
	
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
	
	public GitProject() {}
	
	
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
			throw new UrlMalFormedExeption(e.getMessage());
		}
	}

	
	public void seeCommitLogs() throws IOException, InterruptedException, RunCommandExeption, InvalidInputExeption, ClassNotFoundException  {    
		Commit[] commits;		
		
		if (!projectDir.exists() || (projectDir.exists() && !cachedCommitsFile.exists())) {
			logParseAndSerialize();
		}
		else {
			commits = readCachedCommits();
			printCommits(commits);
		}	
	}
	
	
	private void logParseAndSerialize() throws RunCommandExeption, IOException, InterruptedException, InvalidInputExeption {		
		Commit[] commits;
		GitHubAPI gitHubAPI = new GitHubAPI();	
		
		try {
			commits = getApiResponse(gitHubAPI);
		}
		catch (FailedHTTTPResponseExeption e){
			commits = getFallbackResponse();	
		}
		cacheCommitLogs(commits);
	}


	private Commit[] getFallbackResponse() throws IOException, InterruptedException, RunCommandExeption {
		
		Commit[] commits;
		String commitLogs;
		System.out.println("ERROR invoking GitHub API. Using fallback procedure...");
		if (projectDir.exists()) {
			FileUtils.forceDelete(projectDir);
		}
		commitLogs = GitCommands.gitCloneAndLog(url, projectPath);			
		commits = parseAndPrintCommits(commitLogs);
		return commits;
	}


	private Commit[] getApiResponse(GitHubAPI gitHubAPI) throws FailedHTTTPResponseExeption {
		
		Commit[] commits;
		String jsonResponse = gitHubAPI.getCommitsFromAPI(projectName, userName);
		commits = gitHubAPI.parseJsonCommitLogs(jsonResponse);
		System.out.println("The List of Commits got from API is: ");
		printCommits(commits);
		return commits;
	}
	

	public Commit[] parseAndPrintCommits(String commitLogs) {

		String[] commitArray = spliter(commitLogs);
		Commit[] commits = new Commit[commitArray.length];
		
		System.out.println("The List of Commits is: ");
		for (int i = 0; i < commitArray.length; i++) {
			String[] log = commitArray[i].split(COMMA);
			commits[i] = new Commit(log[LOG_SHA_INDEX], log[LOG_MESSAGE_INDEX], log[LOG_DATE_INDEX], log[LOG_AUTHOR_INDEX]);				
			System.out.println(commits[i].toString());
		}
		return commits;	
	}


	private String[] spliter(String commitLogs) {
		
		commitLogs = commitLogs.substring(commitLogs.indexOf(NEW_LINE)+1);
		String[] commitArray = commitLogs.split(NEW_LINE);
		return commitArray;
	}
	

	public void printCommits(Commit[] commits) {

		for (int i = 0; i < commits.length; i++) {
			System.out.println(commits[i].toString());
		}
	}


	public void cacheCommitLogs(Commit[] commits) throws FileNotFoundException, IOException {
		
		if (!projectDir.exists()) {
			projectDir.mkdir();
        }			
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(cachedCommitsFile))){
			out.writeObject(commits);
		}
	    System.out.println("Serialized data is saved in " + cachedCommitsFile);
	}
	
	
	public Commit[] readCachedCommits() throws IOException ,FileNotFoundException, ClassNotFoundException  {
		
		Commit[] commits = null;
		//List<Commit> commitsss = new ArrayList<>();
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(cachedCommitsFile) )){
			commits = (Commit[])in.readObject();
			//commitsss.addAll((Collection<? extends Commit>) in.readObject());
		}                   
        System.out.println("Showing cached commit list...");

		return commits;
	}
	
	
	public String getProjectPath() {
		return projectPath;
	}
	

	public File getProjectDir() {
		return projectDir;
	}
	

	public String getUrl() {
		return url;
	}


	public String getProjectName() {
		return projectName;
	}


	public String getUserName() {
		return userName;
	}
	
	
	public void setUrl(String url) {
		this.url = url;
	}


	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}


	public void setProjectDir(File projectDir) {
		this.projectDir = projectDir;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
	

	public File getCachedCommitsFile() {
		return cachedCommitsFile;
	}
	
	
	public void setCachedCommitsFile(File cachedCommitsFile) {
		this.cachedCommitsFile = cachedCommitsFile;
	}
}
