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

import data.GitCommands;
import exeptions.RunCommandExeption;


public class GitCommandsTests{

	private static String url = "https://github.com/seniwar/justForTestRepo.git";
	
	private static String projectPath = System.getProperty("user.dir") + "\\justForTestRepo";
	static File projectDir = new File(projectPath);

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
		String expectedLogs =  "\n1202de66253946fc200842c7ffcfb5c7d5ce594a,iguerra,'2021-07-14T00:30:09Z',this commit is just for test";
		assertEquals(expectedLogs, GitCommands.gitCloneAndLog(url, projectPath));
		assertTrue(projectDir.exists() && projectDir.isDirectory() && !isDirEmpty(projectPath));
	}
}
