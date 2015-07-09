package cn.gdeveloper.mapchat.http;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.gdeveloper.mapchat.common.StatusCode;
import cn.gdeveloper.mapchat.http.ErrorText;
import cn.gdeveloper.mapchat.http.HttpVisitor;
import cn.gdeveloper.mapchat.http.MapChatMessageID;
import cn.gdeveloper.mapchat.http.WebResponse;
import cn.gdeveloper.mapchat.model.Result;

/**
 * 访问基类
 * 
 * 1. HTTP 访问器
 * 2. debug
 *
 */
public abstract class BaseProxy {

	protected static boolean DEBUG					= false ;
	protected static final int CODE_OK				= 1 ;
	protected static final int CODE_FAILED			= 2 ;
	protected static final int CODE_TOKEN_TIMEOUT	= 3 ;
	
	protected HttpVisitor httpService ;

	protected BaseProxy(HttpVisitor service){
		httpService	= service ;
	}
	
	/** 分析返回值. 如果返回  CODE_OK 成功, 反之失败 */
	protected final int analyseResult(Result res, Object value){
		Log.i("MapChatHttp","analyseResult "+value);
		if(value instanceof byte[]){
			//
			byte[] data = (byte[])value ;
			// debug
			debug(data);
			// build result
			try{
				String str 		= new String(data) ;
				JSONObject jo 	= new JSONObject(str);
				final String code = jo.getString("StatusCode") ;
				
				res.setCode(code);
				res.setValue(jo.getString("RetValue"));
				
				if(code.equals(StatusCode.CODE_1)){
					return CODE_OK ;
				} else if(code.equals(StatusCode.CODE_2)){
					return CODE_TOKEN_TIMEOUT ;
				} else {
					return CODE_FAILED ;
				}
			} catch(JSONException e){
				res.setCode("-1");
				res.setValue("格式错误!");
				return CODE_FAILED ;
			}
		} else {
			res.setCode("-1");
			res.setValue(ErrorText.ERROR_CONNECT);
			return CODE_FAILED;
		}
	}
	/** 分析返回值. 如果返回  CODE_OK 成功, 反之失败 */
	protected final int analyseResult2(Result res, Object value){
		
		if(value instanceof byte[]){
			//
			byte[] data = (byte[])value ;
			// debug
			debug(data);
			// build result
			try{
				String str 		= new String(data) ;
				JSONObject jo 	= new JSONObject(str);
				final String code = jo.getString("StatusCode") ;
				
				res.setCode(code);
				res.setValue(str);
				
				if(code.equals(StatusCode.CODE_1)){
					return CODE_OK ;
				} else if(code.equals(StatusCode.CODE_2)){
					return CODE_TOKEN_TIMEOUT ;
				} else {
					res.setValue(jo.getString("RetValue"));
					return CODE_FAILED ;
				}
			} catch(JSONException e){
				res.setCode("-1");
				res.setValue("格式错误!");
				return CODE_FAILED ;
			}
		} else {
			res.setCode("-1");
			res.setValue(ErrorText.ERROR_CONNECT);
			return CODE_FAILED;
		}
	}	

	/** 分析JSON 字符串*/
	protected final int analyseJSONArray(Result res, Object value){
		
		if(value instanceof byte[]){
			//
			byte[] data = (byte[])value ;
			// debug
			debug(data);
			// build result
			try{
				String str 		= new String(data) ;
				JSONArray array = new JSONArray(str);
				
				res.setCode(StatusCode.CODE_1);
				res.setJSONArray(array);
				res.setValue(str);
				
				return CODE_OK ;
			} catch(JSONException e){
				res.setCode("-1");
				res.setValue("格式错误!");
				return CODE_FAILED ;
			}
		} else {
			res.setCode("-1");
			res.setValue(ErrorText.ERROR_CONNECT);
			return CODE_FAILED;
		}
	}
	
	/** 检测 字符串*/
	protected final boolean checkString(String value){
		return value != null && !value.trim().equals("");
	}
	
	/** debug */
	protected final void debug(byte[] data){
		
		if(!DEBUG || data == null) return ;
		Log.i("MapChatHttp",new String(data));
	}
}
