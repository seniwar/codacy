package exercise;

/*
 * import java.io.*;
public class SerializeDemo {

   public static void main(String [] args) {
      Employee e = new Employee();
      e.name = "Reyan Ali";
      e.address = "Phokka Kuan, Ambehta Peer";
      e.SSN = 11122333;
      e.number = 101;
      
      try {
         FileOutputStream fileOut =
         new FileOutputStream("/tmp/employee.ser");
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(e);
         out.close();
         fileOut.close();
         System.out.printf("Serialized data is saved in /tmp/employee.ser");
      } catch (IOException i) {
         i.printStackTrace();
      }
   }
}
 */

//ainda pensei escrever o output num ficheiro de texto mas preferi fazer assim pois 
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
