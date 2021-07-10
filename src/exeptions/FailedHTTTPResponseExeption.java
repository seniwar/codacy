package exeptions;

public class FailedHTTTPResponseExeption extends Exception  {

	public FailedHTTTPResponseExeption(String string) {
        super(string);
    }
}