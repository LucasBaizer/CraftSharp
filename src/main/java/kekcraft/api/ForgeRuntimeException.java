package kekcraft.api;

public class ForgeRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 7887899073431592358L;

	public ForgeRuntimeException(Object msg) {
		super(msg == null ? null : msg.toString());
	}
}
