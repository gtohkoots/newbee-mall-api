package ltd.newbee.mall.newbeemallapi.util;

public class ResultGenerator {
	
	public static Result genSuccessResult() {
		Result result = new Result(200,"SUCCESS");
		return result;
	}
	
	public static Result genSuccessResult(Object data) {
		Result result = new Result();
		result.setMessage("SUCCESS");
		result.setResultCode(200);
		result.setData(data);
		return result;
	}

	public static Result genFailResult(String string) {
		Result result = new Result(500,"ERROR");
		result.setMessage(string);
		return result;
	}

}
