package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import exeptions.RunCommandExeption;
import exercise.CodacyExercise;
import exercise.Commit;
import exercise.Git;


//separar testes por classe?

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CodacyExerciseTests {

	private static String url = "https://github.com/Coveros/helloworld.git";
	private static String path = System.getProperty("user.dir") + "\\helloworld";
	static File f = new File(path);
	
	private static String expectedLogs = "\n649d8b1484235ea186b060fae08c5ca1598a8327,Gene Gotimer,Sun Sep 15 15:52:57 2019 -0400,Added time stamp to make each run unique (sign of life)\n"
			+ "e3d42c1d8557e26692759b66516bf93b64cae7e6,Gene Gotimer,Sat Sep 8 15:19:29 2018 -0400,Added a README\n"
			+ "c51f661a26bd223ff9bca44cac253d0c314c0401,Gene Gotimer,Sat Sep 8 15:15:35 2018 -0400,Simple Java application";
	 
	@BeforeAll
	private static void init(){
		try {
			if (f.exists()) {
	            FileUtils.forceDelete(f); 
			}
        } 
		catch (IOException e) {
            e.printStackTrace();
        } 
	}
	
	@AfterAll
	public static void cleanUp() throws IOException{
		FileUtils.forceDelete(f);
	}
	
	
	
/*
	@Test
	@Order(5)
	public void parseLogsTest() throws IOException, InterruptedException, RunCommandExeption {
		
		Commit[] expectedCommits = new Commit[3];
		expectedCommits[0] = new Commit("649d8b1484235ea186b060fae08c5ca1598a8327", "Added time stamp to make each run unique (sign of life)", "Sun Sep 15 15:52:57 2019 -0400", "Gene Gotimer");
		expectedCommits[1] = new Commit("e3d42c1d8557e26692759b66516bf93b64cae7e6", "Added a README", "Sat Sep 8 15:19:29 2018 -0400", "Gene Gotimer");
		expectedCommits[2] = new Commit("c51f661a26bd223ff9bca44cac253d0c314c0401", "Simple Java application", "Sat Sep 8 15:15:35 2018 -0400", "Gene Gotimer");
			
		Commit[] testComits = CodacyExercise.parseAndPrintLogs(expectedLogs);
		for(int i = 0; i < 3; i++) {
			assertEquals(expectedCommits[i].getSha(), testComits[i].getSha());
			assertEquals(expectedCommits[i].getAuthor(), testComits[i].getAuthor());
			assertEquals(expectedCommits[i].getMessage(), testComits[i].getMessage());
			assertEquals(expectedCommits[i].getDate(), testComits[i].getDate());
		}

	}
	
	//acrescentar teste que valida input 1 ou 2
	//acrescentar teste que valida o case
	*/
}
