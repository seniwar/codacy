package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import exeptions.FailedHTTTPResponseExeption;
import exercise.GitHubAPI;


public class GitHubApiTests {

	private static String expectedLogs = "\n[{\"sha\":\"1202de66253946fc200842c7ffcfb5c7d5ce594a\",\"node_id\":\"MDY6Q29tbWl0Mzg1NzU5NTAwOjEyMDJkZTY2MjUzOTQ2ZmMyMDA4NDJjN2ZmY2ZiNWM3ZDVjZTU5NGE=\",\"commit\":{\"author\":{\"name\":\"iguerra\",\"email\":\"ines.guerra@nokia.com\",\"date\":\"2021-07-13T23:30:09Z\"},\"committer\":{\"name\":\"iguerra\",\"email\":\"ines.guerra@nokia.com\",\"date\":\"2021-07-13T23:30:09Z\"},\"message\":\"this commit is just for test\",\"tree\":{\"sha\":\"94b4ffe62f3e0b96280f5bf5bdd4c21a238396b9\",\"url\":\"https://api.github.com/repos/seniwar/justForTestRepo/git/trees/94b4ffe62f3e0b96280f5bf5bdd4c21a238396b9\"},\"url\":\"https://api.github.com/repos/seniwar/justForTestRepo/git/commits/1202de66253946fc200842c7ffcfb5c7d5ce594a\",\"comment_count\":0,\"verification\":{\"verified\":false,\"reason\":\"unsigned\",\"signature\":null,\"payload\":null}},\"url\":\"https://api.github.com/repos/seniwar/justForTestRepo/commits/1202de66253946fc200842c7ffcfb5c7d5ce594a\",\"html_url\":\"https://github.com/seniwar/justForTestRepo/commit/1202de66253946fc200842c7ffcfb5c7d5ce594a\",\"comments_url\":\"https://api.github.com/repos/seniwar/justForTestRepo/commits/1202de66253946fc200842c7ffcfb5c7d5ce594a/comments\",\"author\":null,\"committer\":null,\"parents\":[]}]"; 
	private static String projectPath = System.getProperty("user.dir") + "\\justForTestRepo";
	static File projectDir = new File(projectPath);
	
	
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
	public void getCommitsFromAPIBadResponseTest() {
	    Throwable exception = assertThrows(FailedHTTTPResponseExeption.class, () -> GitHubAPI.getCommitsFromAPI("xxxx", "yyy"));
	    assertEquals("Failed : HTTP error code : 404 Not Found", exception.getMessage());
	}
	
	
	@Test
	public void getCommitsFromAPITest() throws FailedHTTTPResponseExeption {		
	    assertEquals(expectedLogs, GitHubAPI.getCommitsFromAPI("justForTestRepo", "seniwar"));
	}
}
