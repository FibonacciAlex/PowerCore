package core.game.activatecodeTest;

import java.util.HashMap;
import java.util.Map;

public class KRewardDataTemplate {

	private boolean result;
	
	private String msg;
	
	private String code;
	
	private Map<String, Integer> data = new HashMap<String, Integer>();

	public KRewardDataTemplate() {
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, Integer> getData() {
		return data;
	}

	public void setData(Map<String, Integer> data) {
		this.data = data;
	}
	
	
}
