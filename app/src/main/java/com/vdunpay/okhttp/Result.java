package com.vdunpay.okhttp;

public class Result {
	private boolean flag; //系统层的标志位。如果调用接口失败就返回false，例如 : 后台系统格式化json失败、后台系统无法获取卡号。
	private String message;  //提示信息
	private String errorCode;//业务层的代码，表示系统层已经通过，到达业务层查询数据库出现问题， 000000表示调用成功。1006 登录失败！V盾号或密码错误。
	private String data;     //业务层返回的数据
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public Result() {
		// TODO Auto-generated constructor stub
	}
	public Result(boolean flag, String message, String errorCode, String data) {
		super();
		this.flag = flag;
		this.message = message;
		this.errorCode = errorCode;
		this.data = data;
	}

	@Override
	public String toString() {
		return "Result{" +
				"flag=" + flag +
				", message='" + message + '\'' +
				", errorCode='" + errorCode + '\'' +
				", data='" + data + '\'' +
				'}';
	}
}
