package qrsoft.information.shared.handler;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import qrsoft.information.shared.dto.vo.WrappedResult;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, IllegalArgumentException.class})
	public WrappedResult handleValidation(Exception e) {
		return WrappedResult.failedWrappedResult(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public WrappedResult handleException(Exception e) {
		e.printStackTrace();
		return WrappedResult.failedWrappedResult(e.getMessage() == null ? "系统异常" : e.getMessage());
	}
}
