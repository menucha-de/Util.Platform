package havis.util.platform;

public class PlatformException extends Exception {

	private static final long serialVersionUID = 4868975267942714566L;

	public PlatformException(String message) {
		super(message);
	}

	public PlatformException(String message, Throwable e) {
		super(message, e);
	}
}
