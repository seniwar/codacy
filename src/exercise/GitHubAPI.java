package exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import exeptions.FailedHTTTPResponseExeption;

public class GitHubAPI {

	public String getCommitsFromAPI(String projectName, String userName) throws FailedHTTTPResponseExeption {
		String output = "";
		String line = "";
		URL url;
		HttpURLConnection conn = null;
		
		try {
			url = new URL("https://api.github.com/repos/" + userName + "/" + projectName + "/commits");
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if (conn.getResponseCode() != 200) {
	            throw new FailedHTTTPResponseExeption("Failed : HTTP error code : " + conn.getResponseCode() + " " + conn.getResponseMessage());
	        }
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));  

	        while ((line = br.readLine()) != null) {
	        	output = output + "\n" + line;
			}
	        
	        return output;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			conn.disconnect();
		}
		return output;	
	}
	
	
	public Commit[] parseJsonCommitLogs(String jsonResponse) {
		
		jsonResponse = "{\"commitList\":" + jsonResponse + "}";

		JSONObject jsonObj = new JSONObject(jsonResponse);
		JSONArray commitList = jsonObj.getJSONArray("commitList");
		int commitsNumb = commitList.length();
		
		Commit[] commits = new Commit[commitsNumb];
		
	    for (int i = 0; i < commitsNumb; ++i) {
	    	JSONObject log = commitList.getJSONObject(i);
	    	JSONObject commit = log.getJSONObject("commit");
	    	JSONObject author = commit.getJSONObject("author");

	    	commits[i] = new Commit(log.getString("sha"), commit.getString("message"), author.get("date").toString(), author.get("name").toString()  );
	    }
	    return commits;
	}

}
