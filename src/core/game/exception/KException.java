package core.game.exception;

public class KException extends Exception{
	
	
	private static final long serialVersionUID = 1L;

	public KException(String message, Throwable cause){
		super(message, cause);
	}

	public KException(Throwable cause) {
		super(cause);
	}

	public KException(String message) {
		super(message);
	}

}
