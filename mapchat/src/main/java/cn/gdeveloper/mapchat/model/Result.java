package cn.gdeveloper.mapchat.model;

import org.json.JSONObject;

/**
 * 1. code=1 成功, value json字符串
 * 2. code!= 1 失败, value 失败的原因
 */
public final class Result implements Cloneable {

	private int code;

	private String value;

	private JSONObject json ;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public JSONObject getJSONObject() {
		return json;
	}

	public void setJSONObject(JSONObject json) {
		this.json = json;
	}

	public void reset() {
		code 	= -1;
		value 	= "";
		json  	= null ;
	}

	public Result cloneResult() {
		try {
			Result res = (Result) super.clone();
			if (res != null)
				res.reset();
			return res;
		} catch (CloneNotSupportedException e) {
			return new Result();
		}
	}

}
