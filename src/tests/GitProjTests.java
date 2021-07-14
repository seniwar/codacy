package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import exeptions.InvalidInputExeption;
import exeptions.RunCommandExeption;
import exeptions.UrlMalFormedExeption;
import exercise.GitProject;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GitProjTests  {
	
	private static String url = "https://github.com/seniwar/justForTestRepo.git";
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();	
	private static GitProject gitProj;

	
	public boolean isDirEmpty(String path) throws IOException {
		Path cachedCommitsPathObj = Paths.get(path);
	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(cachedCommitsPathObj)) {
	        return !dirStream.iterator().hasNext();
	    }
	}
	
	
	@BeforeAll
	private static void deleteProjectIfExists(){
		
		try {
			gitProj = new GitProject(url);
			if (gitProj.getProjectDir().exists()) {
	            FileUtils.forceDelete(gitProj.getProjectDir()); 
			}
        } 
		catch (UrlMalFormedExeption | IOException e) {
            e.printStackTrace();
        } 
	}

	
	@BeforeEach
	public void setUp() {
	    System.setOut(new PrintStream(outputStreamCaptor));
	}
	
	
	@AfterEach
	public void tearDown() {
	    System.setOut(standardOut);
	}
	
	
	/**/
	@Test
	@Order(1)
	public void createGitProjctObjTest() {	

		String repo = System.getProperty("user.dir") + "\\justForTestRepo";
		assertEquals(repo, gitProj.getProjectPath());
		assertEquals(repo, gitProj.getProjectDir().getAbsolutePath());
		assertEquals(repo + "\\commits.ser", gitProj.getCachedCommitsFile().getAbsolutePath());			
		assertEquals("seniwar", gitProj.getUserName());
		assertEquals("justForTestRepo", gitProj.getProjectName());
	}
	
	
	@Test
	@Order(2)
	public void failTocreateGitProjctObjTest() throws UrlMalFormedExeption {	
		
		Throwable exception = assertThrows(UrlMalFormedExeption.class, () -> new GitProject("potato"));
		assertEquals("Wrong URL. ", exception.getMessage());
	}
	
	
	@Test
	@Order(3)
	public void seeCommitLogsUsingApiTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {
		gitProj.seeCommitLogs();
		assertTrue(gitProj.getProjectDir().exists() && gitProj.getProjectDir().isDirectory() && !isDirEmpty(gitProj.getProjectPath()));
		assertTrue(gitProj.getCachedCommitsFile().exists() && gitProj.getCachedCommitsFile().canRead());
		assertTrue(outputStreamCaptor.toString().contains("The List of Commits got from API is:"));
		assertTrue(outputStreamCaptor.toString().contains("Commit: 1202de66253946fc200842c7ffcfb5c7d5ce594a"));
		assertTrue(outputStreamCaptor.toString().contains("Author: iguerra"));
		assertTrue(outputStreamCaptor.toString().contains("Date: 2021-07-13T23:30:09Z"));
		assertTrue(outputStreamCaptor.toString().contains("Message: this commit is just for test"));
		assertTrue(outputStreamCaptor.toString().contains("Serialized data is saved in " + gitProj.getCachedCommitsFile().getAbsolutePath()));
	}
	
	
	@Test
	@Order(4)
	public void seeCommitLogsWithCacheTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {
		gitProj.seeCommitLogs();
		assertTrue(outputStreamCaptor.toString().contains("Showing cached commit list..."));
		assertTrue(outputStreamCaptor.toString().contains("Commit: 1202de66253946fc200842c7ffcfb5c7d5ce594a"));
	}
	

	@Test
	@Order(5)
	public void seeCommitLogsWithCloneTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption  {	
		gitProj.setUserName("xxx");
		FileUtils.forceDelete(gitProj.getProjectDir()); 	
		gitProj.seeCommitLogs();
		assertTrue(gitProj.getProjectDir().exists() && gitProj.getProjectDir().isDirectory() && !isDirEmpty(gitProj.getProjectPath()));
		assertTrue(gitProj.getCachedCommitsFile().exists() && gitProj.getCachedCommitsFile().canRead());
		assertTrue(outputStreamCaptor.toString().contains("ERROR invoking GitHub API. Using fallback procedure..."));
		assertTrue(outputStreamCaptor.toString().contains("The List of Commits is:"));
		assertTrue(outputStreamCaptor.toString().contains("Commit: 1202de66253946fc200842c7ffcfb5c7d5ce594a"));
		assertTrue(outputStreamCaptor.toString().contains("Serialized data is saved in " + gitProj.getCachedCommitsFile().getAbsolutePath()));
	}	
}
