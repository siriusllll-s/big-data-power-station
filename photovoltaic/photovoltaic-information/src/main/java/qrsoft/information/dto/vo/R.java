package qrsoft.information.dto.vo;

import lombok.Data;

import java.util.Map;

@Data
public class R {
	private boolean success;
	private String message;
	private Map<String, Object> payloadMap;

	public static R ok(Map<String, Object> payload) {
		R r = new R();
		r.setSuccess(true);
		r.setPayloadMap(payload);
		return r;
	}

	public static R fail(String message) {
		R r = new R();
		r.setSuccess(false);
		r.setMessage(message);
		return r;
	}

	public boolean isSuccess() {
		return success;
	}
}
