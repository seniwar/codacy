package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import exeptions.FailedHTTTPResponseExeption;
import exercise.Commit;
import exercise.GitHubAPI;


public class GitHubApiTests {

	GitHubAPI api = new GitHubAPI();
	private static String expectedLogs = "\n[{\"sha\":\"649d8b1484235ea186b060fae08c5ca1598a8327\",\"node_id\":\"MDY6Q29tbWl0MTQ3OTYyNzkyOjY0OWQ4YjE0ODQyMzVlYTE4NmIwNjBmYWUwOGM1Y2ExNTk4YTgzMjc=\",\"commit\":{\"author\":{\"name\":\"Gene Gotimer\",\"email\":\"gene.gotimer@coveros.com\",\"date\":\"2019-09-15T19:52:57Z\"},\"committer\":{\"name\":\"Gene Gotimer\",\"email\":\"gene.gotimer@coveros.com\",\"date\":\"2019-09-15T19:52:57Z\"},\"message\":\"Added time stamp to make each run unique (sign of life)\",\"tree\":{\"sha\":\"fe54bf4757120d7731b80bd42b3aff56cafc56db\",\"url\":\"https://api.github.com/repos/Coveros/helloworld/git/trees/fe54bf4757120d7731b80bd42b3aff56cafc56db\"},\"url\":\"https://api.github.com/repos/Coveros/helloworld/git/commits/649d8b1484235ea186b060fae08c5ca1598a8327\",\"comment_count\":0,\"verification\":{\"verified\":false,\"reason\":\"unsigned\",\"signature\":null,\"payload\":null}},\"url\":\"https://api.github.com/repos/Coveros/helloworld/commits/649d8b1484235ea186b060fae08c5ca1598a8327\",\"html_url\":\"https://github.com/Coveros/helloworld/commit/649d8b1484235ea186b060fae08c5ca1598a8327\",\"comments_url\":\"https://api.github.com/repos/Coveros/helloworld/commits/649d8b1484235ea186b060fae08c5ca1598a8327/comments\",\"author\":null,\"committer\":null,\"parents\":[{\"sha\":\"e3d42c1d8557e26692759b66516bf93b64cae7e6\",\"url\":\"https://api.github.com/repos/Coveros/helloworld/commits/e3d42c1d8557e26692759b66516bf93b64cae7e6\",\"html_url\":\"https://github.com/Coveros/helloworld/commit/e3d42c1d8557e26692759b66516bf93b64cae7e6\"}]},{\"sha\":\"e3d42c1d8557e26692759b66516bf93b64cae7e6\",\"node_id\":\"MDY6Q29tbWl0MTQ3OTYyNzkyOmUzZDQyYzFkODU1N2UyNjY5Mjc1OWI2NjUxNmJmOTNiNjRjYWU3ZTY=\",\"commit\":{\"author\":{\"name\":\"Gene Gotimer\",\"email\":\"gene.gotimer@coveros.com\",\"date\":\"2018-09-08T19:19:29Z\"},\"committer\":{\"name\":\"Gene Gotimer\",\"email\":\"gene.gotimer@coveros.com\",\"date\":\"2018-09-08T19:19:29Z\"},\"message\":\"Added a README\",\"tree\":{\"sha\":\"3d77da5995cc8d16349373c94815b48d21905465\",\"url\":\"https://api.github.com/repos/Coveros/helloworld/git/trees/3d77da5995cc8d16349373c94815b48d21905465\"},\"url\":\"https://api.github.com/repos/Coveros/helloworld/git/commits/e3d42c1d8557e26692759b66516bf93b64cae7e6\",\"comment_count\":0,\"verification\":{\"verified\":false,\"reason\":\"unsigned\",\"signature\":null,\"payload\":null}},\"url\":\"https://api.github.com/repos/Coveros/helloworld/commits/e3d42c1d8557e26692759b66516bf93b64cae7e6\",\"html_url\":\"https://github.com/Coveros/helloworld/commit/e3d42c1d8557e26692759b66516bf93b64cae7e6\",\"comments_url\":\"https://api.github.com/repos/Coveros/helloworld/commits/e3d42c1d8557e26692759b66516bf93b64cae7e6/comments\",\"author\":null,\"committer\":null,\"parents\":[{\"sha\":\"c51f661a26bd223ff9bca44cac253d0c314c0401\",\"url\":\"https://api.github.com/repos/Coveros/helloworld/commits/c51f661a26bd223ff9bca44cac253d0c314c0401\",\"html_url\":\"https://github.com/Coveros/helloworld/commit/c51f661a26bd223ff9bca44cac253d0c314c0401\"}]},{\"sha\":\"c51f661a26bd223ff9bca44cac253d0c314c0401\",\"node_id\":\"MDY6Q29tbWl0MTQ3OTYyNzkyOmM1MWY2NjFhMjZiZDIyM2ZmOWJjYTQ0Y2FjMjUzZDBjMzE0YzA0MDE=\",\"commit\":{\"author\":{\"name\":\"Gene Gotimer\",\"email\":\"gene.gotimer@coveros.com\",\"date\":\"2018-09-08T19:15:35Z\"},\"committer\":{\"name\":\"Gene Gotimer\",\"email\":\"gene.gotimer@coveros.com\",\"date\":\"2018-09-08T19:15:35Z\"},\"message\":\"Simple Java application\",\"tree\":{\"sha\":\"601aa134423c87451bbed9ad8e5884a26ff612c3\",\"url\":\"https://api.github.com/repos/Coveros/helloworld/git/trees/601aa134423c87451bbed9ad8e5884a26ff612c3\"},\"url\":\"https://api.github.com/repos/Coveros/helloworld/git/commits/c51f661a26bd223ff9bca44cac253d0c314c0401\",\"comment_count\":0,\"verification\":{\"verified\":false,\"reason\":\"unsigned\",\"signature\":null,\"payload\":null}},\"url\":\"https://api.github.com/repos/Coveros/helloworld/commits/c51f661a26bd223ff9bca44cac253d0c314c0401\",\"html_url\":\"https://github.com/Coveros/helloworld/commit/c51f661a26bd223ff9bca44cac253d0c314c0401\",\"comments_url\":\"https://api.github.com/repos/Coveros/helloworld/commits/c51f661a26bd223ff9bca44cac253d0c314c0401/comments\",\"author\":null,\"committer\":null,\"parents\":[]}]";
	 
	private static String projectPath = System.getProperty("user.dir") + "\\helloworld";
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
	    Throwable exception = assertThrows(FailedHTTTPResponseExeption.class, () -> api.getCommitsFromAPI("xxxx", "yyy"));
	    assertEquals("Failed : HTTP error code : 404 Not Found", exception.getMessage());
	}
	
	
	@Test
	public void getCommitsFromAPITest() throws FailedHTTTPResponseExeption {		
	    assertEquals(api.getCommitsFromAPI("helloworld", "Coveros"), expectedLogs);
	}
	
	
	@Test
	public void parseJsonCommitLogsTest(){		
		
		Commit[] expectedCommits = new Commit[3];
		expectedCommits[0] = new Commit("649d8b1484235ea186b060fae08c5ca1598a8327", "Added time stamp to make each run unique (sign of life)", "2019-09-15T19:52:57Z", "Gene Gotimer");
		expectedCommits[1] = new Commit("e3d42c1d8557e26692759b66516bf93b64cae7e6", "Added a README", "2018-09-08T19:19:29Z", "Gene Gotimer");
		expectedCommits[2] = new Commit("c51f661a26bd223ff9bca44cac253d0c314c0401", "Simple Java application", "2018-09-08T19:15:35Z", "Gene Gotimer");
			
		Commit[] testComits = api.parseJsonCommitLogs(expectedLogs);
		for(int i = 0; i < 3; i++) {
			assertEquals(expectedCommits[i].getSha(), testComits[i].getSha());
			assertEquals(expectedCommits[i].getAuthor(), testComits[i].getAuthor());
			assertEquals(expectedCommits[i].getMessage(), testComits[i].getMessage());
			assertEquals(expectedCommits[i].getDate(), testComits[i].getDate());
		}			
	}	
	
}
