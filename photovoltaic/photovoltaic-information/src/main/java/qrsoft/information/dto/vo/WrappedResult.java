package qrsoft.information.dto.vo;

import lombok.Data;

@Data
public class WrappedResult {
	private boolean successful;
	private String resultHint;
	private Object resultValue;
	private String errorCode;

	public static WrappedResult successWrappedResult(Object value) {
		WrappedResult r = new WrappedResult();
		r.setSuccessful(true);
		r.setResultValue(value);
		r.setResultHint("success");
		return r;
	}

	public static WrappedResult failedWrappedResult(String hint) {
		WrappedResult r = new WrappedResult();
		r.setSuccessful(false);
		r.setResultHint(hint);
		return r;
	}

	public static WrappedResult failedWrappedResult(String hint, String errorCode) {
		WrappedResult r = failedWrappedResult(hint);
		r.setErrorCode(errorCode);
		return r;
	}
}
