package df.learn.MySpringFramework.commons;

public class JsonException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1291121461826849625L;
	protected String message;
	protected Exception ex;

	public JsonException(String message) {
		super(message);
		this.message = message;
	}

	public JsonException(Exception ex) {
		this(ex.getMessage(), ex);
	}

	public JsonException(String message, Exception ex) {
		// super(message, ex.getCause()); //Don't to create the exception trace.
		this.message = message;
		this.ex = ex;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public final Exception getEx() {
		if (null != ex) {
			if (ex instanceof CommonException) {
				return ((CommonException) ex).getEx();
			} else {
				return ex;
			}
		} else {
			return this;
		}
	}

	public final void setEx(Exception ex) {
		this.ex = ex;
	}

	@Override
	public String toString() {
		return message == null ? getEx().getMessage() : message;
	}
}
