package data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonProcessor {
	
	private static final String COMMITLIST_OBJ = "commitList";
	private static final String ADD_COMMITlIST_TOJSON = "{\"" + COMMITLIST_OBJ + "\":";
	private static final String CLOSE_JSON_OBJ = "}";
	private static final String COMMIT_OBJ = "commit";
	private static final String AUTHOR_OBJ = "author";
	private static final String SHA_VAL = "sha";
	private static final String MESSAGE_VAL = "message";
	private static final String DATE_VAL = "date";
	private static final String AUTHOR_NAME_VAL = "name";
	
	String jsonResponse;
	JSONObject log;
	JSONObject commit;
	JSONObject author;
	
	
	public JsonProcessor(String jsonResponse) {
		this.jsonResponse = ADD_COMMITlIST_TOJSON + jsonResponse + CLOSE_JSON_OBJ;
	}
	
	
	public List<Commit> parseJsonCommitLogs() {
		
		List<Commit> commits = new ArrayList<>();
		JSONObject jsonObj = new JSONObject(jsonResponse);
		JSONArray commitList = jsonObj.getJSONArray(COMMITLIST_OBJ);

	    for (int i = 0; i < commitList.length(); ++i) {
	    	 log = commitList.getJSONObject(i);
	    	 commit = log.getJSONObject(COMMIT_OBJ);
	    	 author = commit.getJSONObject(AUTHOR_OBJ);
	    	 commits.add(new Commit(log.getString(SHA_VAL), commit.getString(MESSAGE_VAL), 
	     			author.get(DATE_VAL).toString(), author.get(AUTHOR_NAME_VAL).toString()));
	    }
	    return commits;
	}
}
