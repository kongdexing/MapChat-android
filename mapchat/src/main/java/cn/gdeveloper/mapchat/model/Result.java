package cn.gdeveloper.mapchat.model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 卡多拉返回结果 
 * 1. code=1 成功, value json字符串 
 * 2. code!= 1 失败, value 失败的原因
 * 3. json, 对应的json对象
 */
public final class Result implements Cloneable {

	private String code;

	private String value;
	
	private JSONObject json ;
	
	private JSONArray array ;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
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

	public JSONArray getJSONArray() {
		return array;
	}

	public void setJSONArray(JSONArray array) {
		this.array = array;
	}

	public void reset() {
		code 	= "-1";
		value 	= "";
		json  	= null ;
		array	= null ;
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
