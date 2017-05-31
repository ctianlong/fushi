package springboot.errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

	//针对controller中发生的异常，404，模板解析错误等不包括在内。
	@ExceptionHandler(value = Exception.class)
	public void defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws Exception {
		ex.printStackTrace();
		response.setCharacterEncoding("UTF-8");
		if (StringUtils.isBlank(response.getHeader("X-Requested-With"))) {
			response.sendRedirect("/error");
		} else {
			response.getWriter().println("error:" + ex.getMessage());
		}
	}

	@ExceptionHandler(MyException.class)
	@ResponseBody
	public ResponseEntity<?> jsonErrorHandler(HttpServletRequest request, Throwable ex) {
		HttpStatus status = getStatus(request);
		ErrorInfo error = new ErrorInfo<>();
		error.setCode(status.value());
		error.setMessage(ex.getMessage());
		error.setUrl(request.getRequestURI());
		return new ResponseEntity<>(error, status);
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}

}
