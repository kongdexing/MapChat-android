package cn.gdeveloper.mapchat.http.impl;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.gdeveloper.mapchat.common.StatusCode;
import cn.gdeveloper.mapchat.http.request.ErrorText;
import cn.gdeveloper.mapchat.model.Result;

public class BaseProxy {

    public String result = "";
    protected static boolean DEBUG = true;
    protected static final int CODE_OK = 1;
    protected static final int CODE_FAILED = 2;

//    public String handleResponseValue(Object value) {
//        result = ErrorText.ERROR_PARSER_JSON;
//        if (value instanceof byte[]) {
//            byte[] data = (byte[]) value;
//            result = new String(data);
//        } else if (value instanceof String) {
//            result = value.toString();
//        }
//        Log.i("HttpRequestThread", "responseValue:" + result);
//        return result;
//    }

    /**
     * 分析返回值. 如果返回  CODE_OK 成功, 反之失败
     */
    protected final int analyseResult(Result res, Object value) {
        if (value instanceof byte[]) {
            //
            byte[] data = (byte[]) value;
            // debug
            debug(data);
            // build result
            try {
                String str = new String(data);
                JSONObject jo = new JSONObject(str);
                final int code = jo.getInt("StatusCode");

                res.setCode(code);
                res.setValue(jo.getString("RetValue"));

                if (code == StatusCode.CODE_1) {
                    return CODE_OK;
                } else {
                    return CODE_FAILED;
                }
            } catch (JSONException e) {
                res.setCode(-1);
                res.setValue("格式错误!");
                return CODE_FAILED;
            }
        } else {
            res.setCode(-1);
            res.setValue(ErrorText.ERROR_CONNECT);
            return CODE_FAILED;
        }
    }

    protected final int analyseJSONObject(Result res, Object value) {

        if (value instanceof byte[]) {
            //
            byte[] data = (byte[]) value;
            // debug
            debug(data);
            // build result // "RetValue":"验证失败","StatusCode":"2"
            try {
                String str = new String(data);
                JSONObject jo = new JSONObject(str);
                //
                res.setJSONObject(jo);
                res.setValue(jo.getString("RetValue"));
                int code = jo.getInt("StatusCode");
                res.setCode(code);
                if (code == StatusCode.CODE_1) {
                    return CODE_OK;
                } else {
                    return CODE_FAILED;
                }
            } catch (JSONException e) {
                res.setCode(-1);
                res.setValue("格式错误!");
                return CODE_FAILED;
            }
        } else {
            res.setCode(-1);
            res.setValue(ErrorText.ERROR_CONNECT);
            return CODE_FAILED;
        }
    }

    /**
     * debug
     */
    protected final void debug(byte[] data) {
        if (!DEBUG || data == null) return;
        Log.i("MapChatHttp", new String(data));
    }

}
