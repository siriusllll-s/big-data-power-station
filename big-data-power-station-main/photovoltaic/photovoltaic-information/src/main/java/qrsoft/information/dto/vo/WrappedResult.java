package qrsoft.information.dto.vo;

import lombok.Data;

@Data
public class WrappedResult<T> {
	private boolean successful;
	private String resultHint;
	private T resultValue;
	private String errorCode;

	public static <T> WrappedResult<T> successWrappedResult(T value) {
		WrappedResult<T> r = new WrappedResult<>();
		r.setSuccessful(true);
		r.setResultValue(value);
		r.setResultHint("success");
		return r;
	}

	public static <T> WrappedResult<T> failedWrappedResult(String hint) {
		WrappedResult<T> r = new WrappedResult<>();
		r.setSuccessful(false);
		r.setResultHint(hint);
		return r;
	}

	public static <T> WrappedResult<T> failedWrappedResult(String hint, String errorCode) {
		WrappedResult<T> r = failedWrappedResult(hint);
		r.setErrorCode(errorCode);
		return r;
	}
}
