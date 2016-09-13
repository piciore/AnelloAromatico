package beer;

@SuppressWarnings("serial")
public class beerException extends Exception {

	public beerException() {
	}

	public beerException(String arg0) {
		super(arg0);
	}

	public beerException(Throwable arg0) {
		super(arg0);
	}

	public beerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public beerException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
