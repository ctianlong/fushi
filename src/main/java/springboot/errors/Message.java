package springboot.errors;

public class Message {

	private boolean success;

	private Object data;

	private Integer errorCode;

	private String errorMessage;
	
	public Message() {
	}

	public Message(boolean success) {
		this.success = success;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "Message [success=" + success + ", data=" + data + ", errorCode=" + errorCode + ", errorMessage="
				+ errorMessage + "]";
	}
	
}
