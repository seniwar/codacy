package data;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import exeptions.FailedHTTTPResponseExeption;


public class GitHubAPI {
	
	private static final int SUCESS_RESPONSE_CODE = 200;
	private static final String SPACE = " ";
	private static final String URL_SPLIT_CHAR ="/";
	private static final String API_URL = "https://api.github.com/repos/";
	private static final String API_GET = "GET";
	private static final String API_GET_COMMITS = "/commits";
	private static final String API_ACCEPT = "Accept";
	private static final String API_APP_JSON = "application/json";
	
	
	public static String getCommitsFromAPI(String projectName, String userName) throws FailedHTTTPResponseExeption {
		
		String output = "";
		HttpURLConnection conn = null;
		
		try {
			conn = setConnection(projectName, userName);			
			if (conn.getResponseCode() != SUCESS_RESPONSE_CODE) {
	            throw new FailedHTTTPResponseExeption("Failed : HTTP error code : " + conn.getResponseCode() + SPACE + conn.getResponseMessage());
	        }
			output = ReadAndWriteOperations.readStream(conn.getInputStream());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			conn.disconnect();	
		}
		return output;
	}
	
	
	protected static HttpURLConnection setConnection(String projectName, String userName) throws MalformedURLException, IOException, ProtocolException {
		
		URL url;
		HttpURLConnection conn;
		url = new URL(API_URL + userName + URL_SPLIT_CHAR + projectName + API_GET_COMMITS);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(API_GET);
		conn.setRequestProperty(API_ACCEPT, API_APP_JSON);
		return conn;
	}
}
