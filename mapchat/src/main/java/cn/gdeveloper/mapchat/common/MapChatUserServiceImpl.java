package cn.gdeveloper.mapchat.common;

import android.util.Log;

import org.json.JSONObject;

import cn.gdeveloper.mapchat.http.BaseProxy;
import cn.gdeveloper.mapchat.http.ErrorText;
import cn.gdeveloper.mapchat.http.HttpVisitor;
import cn.gdeveloper.mapchat.http.MapChatMessageID;
import cn.gdeveloper.mapchat.http.WebResponse;
import cn.gdeveloper.mapchat.model.Result;

/**
 * Created by Administrator on 2015/7/5.
 */
public class MapChatUserServiceImpl extends BaseProxy implements IMapChatUserService,MapChatMessageID {

    private final String TAG = "MapChatHttp";
    final Result mResult = new Result() ;

    protected MapChatUserServiceImpl(HttpVisitor service) {
        super(service);
    }

    @Override
    public void register(String email, String username, String mobile, String password,final WebResponse response) {
        final Result error	= mResult.cloneResult() ;
        error.setCode("-1");
        String param = null ;
        try{
            JSONObject json = new JSONObject() ;
            json.put("Email",email) ;
            json.put("UserName",username) ;
            json.put("Password", password) ;
            json.put("Phone", mobile) ;
            param = json.toString() ;
        }catch(Exception e){
            error.setValue(ErrorText.ERROR_BUILD_PARAMETER);
            response.sendMessage(MSG_MEMBER_REGISTER_FAILED,error);
            return ;
        }

        final StringBuilder url = new StringBuilder(256) ;
        url.append(HttpHost.HOST+HttpHost.DEMO_REQ);
        Log.i(TAG,"url "+url+"  param  "+param);
        httpService.sendHttpPostResponseByteArray(url.toString(), param, new WebResponse(response) {
            @Override
            public void dispatchResponse(Object value) {
                super.dispatchResponse(value);
                final Result result = mResult.cloneResult() ;
                if(analyseResult(result,value) == CODE_OK){
                    sendMessage(MSG_MEMBER_REGISTER_SUCCESS,result);
                } else {
                    sendMessage(MSG_MEMBER_REGISTER_FAILED,result);
                }
                // dispatch
                response.dispatchResponse(result);
            }
        });
    }
}
