package es.upm.grise.profundizacion.order;

public class IncorrectItemException extends Exception {

	private static final long serialVersionUID = 1L;

	public IncorrectItemException() {
		super();
	}

	public IncorrectItemException(String message) {
		super(message);
	}

}
