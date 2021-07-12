package exercise;

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

	public void setSha(String sha) {
		this.sha = sha;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setAuthor(String author) {
		this.author = author;
	} 

}
