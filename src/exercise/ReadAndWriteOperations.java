package exercise;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadAndWriteOperations {

	private static final String NEW_LINE = "\n";
	
	
	public static String readStream(InputStream inputStream) {
		
		String output = "";
		String line = "";
		try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(inputStream))) {
			while ((line = stdInput.readLine()) != null) {
				output = output + NEW_LINE + line;
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	
	@SuppressWarnings("unchecked")
	public static List<Commit> readCachedCommits(File cachedCommitsFile) throws FileNotFoundException ,IOException, ClassNotFoundException  {
		
		List<Commit> commits = new ArrayList<>();
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(cachedCommitsFile) )){
			commits = (List<Commit>)in.readObject();
		}                   
        System.out.println("Showing cached commit list...");
		return commits;
	}
	
	
	public static void cacheCommitLogs(List<Commit> commits, File projectDir, File cachedCommitsFile) throws FileNotFoundException, IOException {
		
		if (!projectDir.exists()) {
			projectDir.mkdir();
        }			
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(cachedCommitsFile))){
			out.writeObject(commits);
		}
	    System.out.println("Serialized data is saved in " + cachedCommitsFile);
	}

}
