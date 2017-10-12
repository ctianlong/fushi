package springboot.errors;

public class MyException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public MyException(String message) {
		super(message);
	}

}
