package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import exercise.Commit;
import exercise.ReadAndWriteOperations;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReadAndWriteOperationsTests {

	
	private static String projectPath = System.getProperty("user.dir") + "\\justForTestRepo";
	private static File projectDir = new File(projectPath);
	private static String cachedCommitsFileName = projectPath + "\\commits.ser";
	private static File cachedCommitsFile = new File(cachedCommitsFileName);
	
	public boolean isDirEmpty(String path) throws IOException {
		Path cachedCommitsPathObj = Paths.get(path);
	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(cachedCommitsPathObj)) {
	        return !dirStream.iterator().hasNext();
	    }
	}
	
	
	@BeforeAll
	private static void deleteProjectIfExists(){
		
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
	@Order(1)
	public void failReadCachedCommitsTest() {
		Throwable exception = assertThrows(FileNotFoundException.class, () -> ReadAndWriteOperations.readCachedCommits(cachedCommitsFile));
		assertEquals(cachedCommitsFileName + " (The system cannot find the path specified)", exception.getMessage());
	}
	
	
	@Test
	@Order(2)
	public void failCacheCommitLogsTest() throws FileNotFoundException, IOException {
		
		List<Commit> commits = new ArrayList<>();
		commits.add(new Commit("sha", "message", "date", "author"));
		
		String wrongCachedCommitsFileName = projectPath + "\\a\\x\\a";
		File wrongCachedCommitsFile = new File(wrongCachedCommitsFileName);

		Throwable exception = assertThrows(FileNotFoundException.class, () -> ReadAndWriteOperations.cacheCommitLogs(commits, projectDir, wrongCachedCommitsFile));
		assertEquals(projectPath + "\\a\\x\\a (The system cannot find the path specified)", exception.getMessage());
	}
	
	
	@Test
	@Order(3)
	public void cacheCommitLogsTest() throws FileNotFoundException, IOException {
		
		List<Commit> commits = new ArrayList<>();
		commits.add(new Commit("sha", "message", "date", "author"));
		ReadAndWriteOperations.cacheCommitLogs(commits, projectDir, cachedCommitsFile);

		assertTrue(projectDir.exists() && projectDir.isDirectory() && !isDirEmpty(projectPath));
		assertTrue(cachedCommitsFile.exists() && cachedCommitsFile.canRead());	
	}
	
	
	@Test
	@Order(4)
	public void readCachedCommitsTest() throws FileNotFoundException, ClassNotFoundException, IOException {

		List<Commit> commits = ReadAndWriteOperations.readCachedCommits(cachedCommitsFile);
		assertEquals(1, commits.size());		
		assertEquals("sha", commits.get(0).getSha());
		assertEquals("author", commits.get(0).getAuthor());
		assertEquals("message", commits.get(0).getMessage());
		assertEquals("date", commits.get(0).getDate());	
	}

	
	@Test
	@Order(5)
	public void readStreamTest() throws IOException {
		
		InputStream stubInputStream = IOUtils.toInputStream("test data for my input stream", "UTF-8");
		String output = ReadAndWriteOperations.readStream(stubInputStream);
		assertTrue(output.contains("test data for my input stream"));	
	}
}
