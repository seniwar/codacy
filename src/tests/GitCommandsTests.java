package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import exeptions.RunCommandExeption;
import exercise.GitCommands;


public class GitCommandsTests {

	private static String url = "https://github.com/Coveros/helloworld.git";
	
	private static String projectPath = System.getProperty("user.dir") + "\\helloworld";
	static File projectDir = new File(projectPath);
	

	private static String expectedLogs = "\n649d8b1484235ea186b060fae08c5ca1598a8327,Gene Gotimer,Sun Sep 15 15:52:57 2019 -0400,Added time stamp to make each run unique (sign of life)\n"
			+ "e3d42c1d8557e26692759b66516bf93b64cae7e6,Gene Gotimer,Sat Sep 8 15:19:29 2018 -0400,Added a README\n"
			+ "c51f661a26bd223ff9bca44cac253d0c314c0401,Gene Gotimer,Sat Sep 8 15:15:35 2018 -0400,Simple Java application";
	 
	
	public boolean isDirEmpty(String path) throws IOException {
		Path cachedCommitsPathObj = Paths.get(path);
	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(cachedCommitsPathObj)) {
	        return !dirStream.iterator().hasNext();
	    }
	}
	
	@BeforeAll
	static private void deleteProjectIfExists(){
		try {
			if (projectDir.exists()) {
	            FileUtils.forceDelete(projectDir); 
			}
        } 
		catch (IOException e) {
            e.printStackTrace();
        } 
	}
	
	
	@Test
	public void wrongGitHubUrlTest() {
	    Throwable exception = assertThrows(RunCommandExeption.class, () -> GitCommands.gitCloneAndLog("xxxx", projectPath));
	    assertEquals("Error Running command: git clone xxxx\nfatal: repository 'xxxx' does not exist", exception.getMessage());
	}
	
	
	@Test
	public void gitCloneAndLogTest() throws RunCommandExeption, IOException, InterruptedException {				
		assertEquals(expectedLogs, GitCommands.gitCloneAndLog(url, projectPath));
		assertTrue(projectDir.exists() && projectDir.isDirectory() && !isDirEmpty(projectPath));
	}
}
