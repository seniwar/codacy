package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import exercise.Commit;
import exercise.JsonProcessor;

public class JsonProcessorTests  {
	
	JsonProcessor jsonProc = new JsonProcessor("\n[{\"sha\":\"asdfgjh\",\"node_id\":\"MDY6Q29tbWl0Mzg1NzU5NTAwOjEyMDJkZTY2MjUzOTQ2ZmMyMDA4NDJjN2ZmY2ZiNWM3ZDVjZTU5NGE=\",\"commit\":{\"author\":{\"name\":\"Morpheus\",\"email\":\"ines.guerra@nokia.com\",\"date\":\"1999-06-25\"},\"committer\":{\"name\":\"iguerra\",\"email\":\"ines.guerra@nokia.com\",\"date\":\"2021-07-13T23:30:09Z\"},\"message\":\"Welcome to the real world\",\"tree\":{\"sha\":\"94b4ffe62f3e0b96280f5bf5bdd4c21a238396b9\",\"url\":\"https://api.github.com/repos/seniwar/justForTestRepo/git/trees/94b4ffe62f3e0b96280f5bf5bdd4c21a238396b9\"},\"url\":\"https://api.github.com/repos/seniwar/justForTestRepo/git/commits/1202de66253946fc200842c7ffcfb5c7d5ce594a\",\"comment_count\":0,\"verification\":{\"verified\":false,\"reason\":\"unsigned\",\"signature\":null,\"payload\":null}},\"url\":\"https://api.github.com/repos/seniwar/justForTestRepo/commits/1202de66253946fc200842c7ffcfb5c7d5ce594a\",\"html_url\":\"https://github.com/seniwar/justForTestRepo/commit/1202de66253946fc200842c7ffcfb5c7d5ce594a\",\"comments_url\":\"https://api.github.com/repos/seniwar/justForTestRepo/commits/1202de66253946fc200842c7ffcfb5c7d5ce594a/comments\",\"author\":null,\"committer\":null,\"parents\":[]}]");
	
	@Test
	public void parseJsonCommitLogsTest(){		

		List<Commit> testComits = jsonProc.parseJsonCommitLogs();
		for (Commit commit : testComits){
			assertEquals(commit.getSha(), "asdfgjh");
			assertEquals(commit.getAuthor(), "Morpheus");
			assertEquals(commit.getMessage(), "Welcome to the real world");
			assertEquals(commit.getDate(), "1999-06-25");
		}		
	}
 
}
