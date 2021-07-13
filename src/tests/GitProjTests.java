package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
import exercise.Commit;
import exercise.GitProject;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GitProjTests  {
	
	private String url = "https://github.com/Coveros/helloworld.git";
	
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	
	private GitProject gitProj;

	private String expectedLogs = "\n649d8b1484235ea186b060fae08c5ca1598a8327,Gene Gotimer,Sun Sep 15 15:52:57 2019 -0400,Added time stamp to make each run unique (sign of life)\n"
			+ "e3d42c1d8557e26692759b66516bf93b64cae7e6,Gene Gotimer,Sat Sep 8 15:19:29 2018 -0400,Added a README\n"
			+ "c51f661a26bd223ff9bca44cac253d0c314c0401,Gene Gotimer,Sat Sep 8 15:15:35 2018 -0400,Simple Java application";

	
	private String fullApiConsoleMessage = "\nThe List of Commits got from API is: \n\n"
			+ "Commit: 649d8b1484235ea186b060fae08c5ca1598a8327\n"
			+ "Author: Gene Gotimer\n"
			+ "Date: 2019-09-15T19:52:57Z\n"
			+ "Message: Added time stamp to make each run unique (sign of life)\n\n"
			+ "Commit: e3d42c1d8557e26692759b66516bf93b64cae7e6\n"
			+ "Author: Gene Gotimer\n"
			+ "Date: 2018-09-08T19:19:29Z\n"
			+ "Message: Added a README\n\n"
			+ "Commit: c51f661a26bd223ff9bca44cac253d0c314c0401\n"
			+ "Author: Gene Gotimer\n"
			+ "Date: 2018-09-08T19:15:35Z\n"
			+ "Message: Simple Java application\n\n"
			+ "Serialized data is saved in C:\\Users\\iguerra\\Desktop\\ines\\codacy\\codacy\\helloworld\\commits.ser";
	
	
	
	private String fullCloneConsoleMessage = "\nERROR invoking GitHub API. Using fallback procedure...\n"
			+ "The List of Commits is: \n\n"
			+ "Commit: 649d8b1484235ea186b060fae08c5ca1598a8327\n"
			+ "Author: Gene Gotimer\n"
			+ "Date: Sun Sep 15 15:52:57 2019 -0400\n"
			+ "Message: Added time stamp to make each run unique (sign of life)\n\n"
			+ "Commit: e3d42c1d8557e26692759b66516bf93b64cae7e6\n"
			+ "Author: Gene Gotimer\n"
			+ "Date: Sat Sep 8 15:19:29 2018 -0400\n"
			+ "Message: Added a README\n\n"
			+ "Commit: c51f661a26bd223ff9bca44cac253d0c314c0401\n"
			+ "Author: Gene Gotimer\n"
			+ "Date: Sat Sep 8 15:15:35 2018 -0400\n"
			+ "Message: Simple Java application\n\n"
			+ "Serialized data is saved in C:\\Users\\iguerra\\Desktop\\ines\\codacy\\codacy\\helloworld\\commits.ser";
	
	private String fullCacheConsoleMessage = "\nShowing cached commit list...\n\n"
			+ "Commit: 649d8b1484235ea186b060fae08c5ca1598a8327\n"
			+ "Author: Gene Gotimer\n"
			+ "Date: 2019-09-15T19:52:57Z\n"
			+ "Message: Added time stamp to make each run unique (sign of life)\n\n"
			+ "Commit: e3d42c1d8557e26692759b66516bf93b64cae7e6\n"
			+ "Author: Gene Gotimer\n"
			+ "Date: 2018-09-08T19:19:29Z\n"
			+ "Message: Added a README\n\n"
			+ "Commit: c51f661a26bd223ff9bca44cac253d0c314c0401\n"
			+ "Author: Gene Gotimer\n"
			+ "Date: 2018-09-08T19:15:35Z\n"
			+ "Message: Simple Java application\n\n";			
	
	
	private String projectPath = System.getProperty("user.dir") + "\\helloworld";
	private File projectDir = new File(projectPath);
	private String cachedCommitsFileName = projectPath + "\\commits.ser";
	
	
	public boolean isDirEmpty(String path) throws IOException {
		Path cachedCommitsPathObj = Paths.get(path);
	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(cachedCommitsPathObj)) {
	        return !dirStream.iterator().hasNext();
	    }
	}
	
	
	@BeforeAll
	private void deleteProjectIfExists(){
		
		try {
			gitProj = new GitProject(url);
			if (projectDir.exists()) {
	            FileUtils.forceDelete(projectDir); 
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
	
	
	
	//fazer teste para lançar UrlMalFormedExeption - criar obj errado
	
	@Test
	@Order(1)
	public void createGitProjctObjTest() {	

		assertEquals(gitProj.getProjectPath(), projectPath);
		assertEquals(gitProj.getProjectDir().getAbsolutePath(), projectPath);
		assertEquals(gitProj.getCachedCommitsFile().getAbsolutePath(), projectPath + "\\commits.ser");			
		assertEquals(gitProj.getUserName(), "Coveros");
		assertEquals(gitProj.getProjectName(), "helloworld");
	}
	
	
	@Test
	@Order(2)
	public void failReadCachedCommitsTest() {
		Throwable exception = assertThrows(FileNotFoundException.class, () -> gitProj.readCachedCommits());
		assertEquals(cachedCommitsFileName + " (The system cannot find the path specified)", exception.getMessage());
	}
	
	
	
	@Test
	@Order(3)
	public void seeCommitLogsUsingApiTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {
		gitProj.seeCommitLogs();
		assertTrue(gitProj.getProjectDir().exists() && gitProj.getProjectDir().isDirectory() && !isDirEmpty(gitProj.getProjectPath()));
		assertTrue(gitProj.getCachedCommitsFile().exists());
	    assertEquals(fullApiConsoleMessage, outputStreamCaptor.toString().replaceAll("[\\r\\t]", ""));    
	}
	
	
	@Test
	@Order(4)
	public void seeCommitLogsWithCacheTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {
		gitProj.seeCommitLogs();
	    assertEquals(fullCacheConsoleMessage, outputStreamCaptor.toString().replaceAll("[\\r\\t]", ""));
	}
	

	@Test
	@Order(5)
	public void logParseAndSerializeCloneTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption  {	
		gitProj.setUserName("xxx");
		gitProj.seeCommitLogs();
		assertTrue(projectDir.exists() && projectDir.isDirectory() && !isDirEmpty(projectPath));
	    assertEquals(fullCloneConsoleMessage, outputStreamCaptor.toString().replaceAll("[\\r\\t]", ""));
	    gitProj.setUserName("Coveros");
	}
	
	//usar tb funçao acima
	@Test
	@Order(6)
	public void parseAndPrintCommitsTest() throws IOException, InterruptedException, RunCommandExeption {
		
		Commit[] expectedCommits = new Commit[3];
		expectedCommits[0] = new Commit("649d8b1484235ea186b060fae08c5ca1598a8327", "Added time stamp to make each run unique (sign of life)", "Sun Sep 15 15:52:57 2019 -0400", "Gene Gotimer");
		expectedCommits[1] = new Commit("e3d42c1d8557e26692759b66516bf93b64cae7e6", "Added a README", "Sat Sep 8 15:19:29 2018 -0400", "Gene Gotimer");
		expectedCommits[2] = new Commit("c51f661a26bd223ff9bca44cac253d0c314c0401", "Simple Java application", "Sat Sep 8 15:15:35 2018 -0400", "Gene Gotimer");
			
		Commit[] testComits = gitProj.parseAndPrintCommits(expectedLogs);
		for(int i = 0; i < 3; i++) {
			assertEquals(expectedCommits[i].getSha(), testComits[i].getSha());
			assertEquals(expectedCommits[i].getAuthor(), testComits[i].getAuthor());
			assertEquals(expectedCommits[i].getMessage(), testComits[i].getMessage());
			assertEquals(expectedCommits[i].getDate(), testComits[i].getDate());
		}

	}

//add test to see if objects from api and from log are equal in content. for that dates need to be normalized	
	
}
