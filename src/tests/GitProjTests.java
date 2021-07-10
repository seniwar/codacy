package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import exeptions.InvalidInputExeption;
import exeptions.RunCommandExeption;
import exercise.Commit;
import exercise.GitProject;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GitProjTests  {

	private static String url = "https://github.com/Coveros/helloworld.git";
	
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	
	static GitProject ops = new GitProject(url);
	
	String projectPath = ops.getProjectPath();
	File projectDir = ops.getProjectDir();
	String cachedCommitsPath = ops.getCachedCommitsPath();
	File cachedCommitsDir = ops.getCachedCommitsDir();
	
	String cachedCommitsFileName = cachedCommitsPath + "\\commits.ser";
	
	private static String expectedLogs = "\n649d8b1484235ea186b060fae08c5ca1598a8327,Gene Gotimer,Sun Sep 15 15:52:57 2019 -0400,Added time stamp to make each run unique (sign of life)\n"
			+ "e3d42c1d8557e26692759b66516bf93b64cae7e6,Gene Gotimer,Sat Sep 8 15:19:29 2018 -0400,Added a README\n"
			+ "c51f661a26bd223ff9bca44cac253d0c314c0401,Gene Gotimer,Sat Sep 8 15:15:35 2018 -0400,Simple Java application";
	
	private static String fullCloneConsoleMessage = "\nClonning the project...\n"
			+ "Done.\n\n"
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
			+ "Serialized data is saved in C:\\Users\\iguerra\\Desktop\\ines\\codacy\\codacy\\helloworld\\cachedLogs\\commits.ser";
	
	private static String fullCacheConsoleMessage = "\nShowing cached commit list...\n\n"
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
			+ "Message: Simple Java application\n\n";			
	
	private static String fullPullConsoleMessage = "\nProject exists but there are no cached logs.\n\n"
			+ "Pulling the project...\n\n"
			+ "Done.\n\n"
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
			+ "Serialized data is saved in C:\\Users\\iguerra\\Desktop\\ines\\codacy\\codacy\\helloworld\\cachedLogs\\commits.ser";
	
	
	@BeforeAll
	static private void init(){
		try {
			if (ops.getProjectDir().exists()) {
	            FileUtils.forceDelete(ops.getProjectDir()); 
			}
        } 
		catch (IOException e) {
            e.printStackTrace();
        } 
	}

	
	@AfterAll
	static public void cleanUp() throws IOException{		
		FileUtils.forceDelete(ops.getProjectDir());
	}
	
	
	@BeforeEach
	public void setUp() {
	    System.setOut(new PrintStream(outputStreamCaptor));
	}
	
	
	@AfterEach
	public void tearDown() {
	    System.setOut(standardOut);
	}
	
	
	@Test
	@Order(1)
	public void failReadCachedCommitsTest() {
		Throwable exception = assertThrows(FileNotFoundException.class, () -> ops.readCachedCommits());
		assertEquals(cachedCommitsFileName + " (The system cannot find the path specified)", exception.getMessage());
	}
	
	
	@Test
	@Order(2)
	public void seeCommitLogsWithCloneTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {
		ops.seeCommitLogs();
		assertTrue(projectDir.exists() && projectDir.isDirectory() && !ops.isDirEmpty(projectPath));
		assertTrue(cachedCommitsDir.exists() && cachedCommitsDir.isDirectory() && !ops.isDirEmpty(cachedCommitsPath));
	    assertEquals(fullCloneConsoleMessage, outputStreamCaptor.toString().replaceAll("[\\r\\t]", ""));
	    
	}
	
	
	@Test
	@Order(3)
	public void seeCommitLogsWithCacheTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {
		ops.seeCommitLogs();
	    assertEquals(fullCacheConsoleMessage, outputStreamCaptor.toString().replaceAll("[\\r\\t]", ""));
	}
	
	
	@Test
	@Order(4)
	public void seeCommitLogsWithPullTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {
		FileUtils.forceDelete(ops.getCachedCommitsDir()); 
		ops.seeCommitLogs();
		assertTrue(cachedCommitsDir.exists() && cachedCommitsDir.isDirectory() && !ops.isDirEmpty(cachedCommitsPath));
	    assertEquals(fullPullConsoleMessage, outputStreamCaptor.toString().replaceAll("[\\r\\t]", ""));
	}
	
	
	@Test
	@Order(5)
	public void falseIsDirEmptyTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {
		assertTrue(!ops.isDirEmpty(cachedCommitsPath));  
	}
	
	
	@Test
	@Order(6)
	public void trueIsDirEmptyTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {
		File f = new File(cachedCommitsFileName);
		f.delete();
		assertTrue(ops.isDirEmpty(cachedCommitsPath));  
	}
	
	
	@Test
	@Order(7)
	public void logParseAndSerializeWithCloneTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {	
		FileUtils.forceDelete(ops.getProjectDir());
		ops.logParseAndSerialize("clone");
		assertTrue(projectDir.exists() && projectDir.isDirectory() && !ops.isDirEmpty(projectPath));
	    assertEquals(fullCloneConsoleMessage, outputStreamCaptor.toString().replaceAll("[\\r\\t]", ""));
	}
	
	
	@Test
	@Order(8)
	public void logParseAndSerializeWithPullTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {	
		ops.logParseAndSerialize("pull");
		assertTrue(cachedCommitsDir.exists() && cachedCommitsDir.isDirectory() && !ops.isDirEmpty(cachedCommitsPath));
	    assertEquals(fullPullConsoleMessage, outputStreamCaptor.toString().replaceAll("[\\r\\t]", ""));
	}
	
	
	@Test
	@Order(9)
	public void faillogParseAndSerializeTest() throws ClassNotFoundException, IOException, InterruptedException, RunCommandExeption, InvalidInputExeption {	
		Throwable exception = assertThrows(InvalidInputExeption.class, () -> ops.logParseAndSerialize("xxx"));
		assertEquals("Invalid input: xxx", exception.getMessage());
	}
	
	
	@Test
	@Order(10)
	public void parseAndPrintCommitsTest() throws IOException, InterruptedException, RunCommandExeption {
		
		Commit[] expectedCommits = new Commit[3];
		expectedCommits[0] = new Commit("649d8b1484235ea186b060fae08c5ca1598a8327", "Added time stamp to make each run unique (sign of life)", "Sun Sep 15 15:52:57 2019 -0400", "Gene Gotimer");
		expectedCommits[1] = new Commit("e3d42c1d8557e26692759b66516bf93b64cae7e6", "Added a README", "Sat Sep 8 15:19:29 2018 -0400", "Gene Gotimer");
		expectedCommits[2] = new Commit("c51f661a26bd223ff9bca44cac253d0c314c0401", "Simple Java application", "Sat Sep 8 15:15:35 2018 -0400", "Gene Gotimer");
			
		Commit[] testComits = ops.parseAndPrintCommits(expectedLogs);
		for(int i = 0; i < 3; i++) {
			assertEquals(expectedCommits[i].getSha(), testComits[i].getSha());
			assertEquals(expectedCommits[i].getAuthor(), testComits[i].getAuthor());
			assertEquals(expectedCommits[i].getMessage(), testComits[i].getMessage());
			assertEquals(expectedCommits[i].getDate(), testComits[i].getDate());
		}

	}
	
	
	@Test
	@Order(11)
	public void readCachedCommitsTest() throws FileNotFoundException, ClassNotFoundException, IOException {
		
		Commit[] expectedCommits = new Commit[3];
		expectedCommits[0] = new Commit("649d8b1484235ea186b060fae08c5ca1598a8327", "Added time stamp to make each run unique (sign of life)", "Sun Sep 15 15:52:57 2019 -0400", "Gene Gotimer");
		expectedCommits[1] = new Commit("e3d42c1d8557e26692759b66516bf93b64cae7e6", "Added a README", "Sat Sep 8 15:19:29 2018 -0400", "Gene Gotimer");
		expectedCommits[2] = new Commit("c51f661a26bd223ff9bca44cac253d0c314c0401", "Simple Java application", "Sat Sep 8 15:15:35 2018 -0400", "Gene Gotimer");
		

		Commit[] testComits =  ops.readCachedCommits();
		for(int i = 0; i < 3; i++) {
			assertEquals(expectedCommits[i].getSha(), testComits[i].getSha());
			assertEquals(expectedCommits[i].getAuthor(), testComits[i].getAuthor());
			assertEquals(expectedCommits[i].getMessage(), testComits[i].getMessage());
			assertEquals(expectedCommits[i].getDate(), testComits[i].getDate());
		}
	}
	
}
