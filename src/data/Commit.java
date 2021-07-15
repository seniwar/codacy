package data;

public class Commit implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
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
	
	
	@Override
	public String toString() {
		return  "Commit: " + this.sha + "\n" + 
				"Author: " + this.author + "\n" + 
				"Date: " + this.date + "\n" + 
				"Message: " + this.message + "\n";
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
