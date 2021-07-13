package exercise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import exeptions.FailedHTTTPResponseExeption;

public class GitHubAPI {
	
	private static final int SUCESS_RESPONSE_CODE = 200;
	private static final String NEW_LINE = "\n";
	private static final String SPACE = " ";
	private static final String URL_SPLIT_CHAR ="/";
	private static final String API_URL = "https://api.github.com/repos/";
	private static final String API_GET = "GET";
	private static final String API_GET_COMMITS = "/commits";
	private static final String API_ACCEPT = "Accept";
	private static final String API_APP_JSON = "application/json";
	
	private static final String COMMITLIST_OBJ = "commitList";
	private static final String ADD_COMMITlIST_TOJSON = "{\"" + COMMITLIST_OBJ + "\":";
	private static final String CLOSE_JSON_OBJ = "}";
	private static final String COMMIT_OBJ = "commit";
	private static final String AUTHOR_OBJ = "author";
	private static final String SHA_VAL = "sha";
	private static final String MESSAGE_VAL = "message";
	private static final String DATE_VAL = "date";
	private static final String AUTHOR_NAME_VAL = "name";
	
	
	public String getCommitsFromAPI(String projectName, String userName) throws FailedHTTTPResponseExeption {
		String output = "";
		HttpURLConnection conn = null;
		
		try {
			conn = setConnection(projectName, userName);
			
			if (conn.getResponseCode() != SUCESS_RESPONSE_CODE) {
	            throw new FailedHTTTPResponseExeption("Failed : HTTP error code : " + conn.getResponseCode() + SPACE + conn.getResponseMessage());
	        }
			
			output = readResponse(conn.getInputStream());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			conn.disconnect();	
		}
		
		return output;
	}
	

	private String readResponse(InputStream inputStream) throws IOException {
		
		String line = "";
		String output = "";
		try (BufferedReader br = new BufferedReader(new InputStreamReader((inputStream)))) {
	        while ((line = br.readLine()) != null) {
	        	output = output + NEW_LINE + line;
			}
		}
		return output;
	}

	
	private HttpURLConnection setConnection(String projectName, String userName) throws MalformedURLException, IOException, ProtocolException {
		
		URL url;
		HttpURLConnection conn;
		url = new URL(API_URL + userName + URL_SPLIT_CHAR + projectName + API_GET_COMMITS);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(API_GET);
		conn.setRequestProperty(API_ACCEPT, API_APP_JSON);
		return conn;
	}
	
	
	public Commit[] parseJsonCommitLogs(String jsonResponse) {
		
		jsonResponse = ADD_COMMITlIST_TOJSON + jsonResponse + CLOSE_JSON_OBJ;

		JSONObject jsonObj = new JSONObject(jsonResponse);
		JSONArray commitList = jsonObj.getJSONArray(COMMITLIST_OBJ);
		int commitsNumb = commitList.length();
		
		Commit[] commits = new Commit[commitsNumb];
		
	    for (int i = 0; i < commitsNumb; ++i) {
	    	JSONObject log = commitList.getJSONObject(i);
	    	JSONObject commit = log.getJSONObject(COMMIT_OBJ);
	    	JSONObject author = commit.getJSONObject(AUTHOR_OBJ);

	    	commits[i] = new Commit(log.getString(SHA_VAL), commit.getString(MESSAGE_VAL), 
	    			author.get(DATE_VAL).toString(), author.get(AUTHOR_NAME_VAL).toString() );
	    }
	    return commits;
	}

}
