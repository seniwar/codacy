package exercise;

public class Commit {
	String sha;
	String message;
	String date;
	String author;
	
	public Commit(String sha, String message, String date, String author) {
	    this.sha = sha;
	    this.message = message;
	    this.date = date;
	    this.author = author;
	  }
	
	public String getSha() {
		return sha;
	}
	public String getMessage() {
		return message;
	}
	public String getDate() {
		return date;
	}
	public String getAuthor() {
		return author;
	} 
	
}
