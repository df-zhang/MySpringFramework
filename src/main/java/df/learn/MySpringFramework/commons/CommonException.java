package df.learn.MySpringFramework.commons;


@SuppressWarnings("serial")
public class CommonException extends RuntimeException {

	protected ExceptionCode code;
	protected String message;
	protected Exception ex;

	public CommonException(String message) {
		this(message, null, ExceptionCode.DEFAULT);
	}

	public CommonException(String message, ExceptionCode code) {
		this(message, null, code);
	}

	public CommonException(Exception ex) {
		this(ex.getMessage(), ex);
	}

	public CommonException(Exception ex, ExceptionCode code) {
		this(ex.getMessage(), ex, code);
	}

	public CommonException(String message, Exception ex) {
		this(ex.getMessage(), ex, ExceptionCode.DEFAULT);
	}

	public CommonException(String message, Exception ex, ExceptionCode code) {
		// super(message, ex.getCause()); //Don't to create the exception trace.
		this.message = message;
		this.ex = ex;
		this.code = code;
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
