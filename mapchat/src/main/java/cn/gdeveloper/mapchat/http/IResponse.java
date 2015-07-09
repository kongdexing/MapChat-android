package cn.gdeveloper.mapchat.http;

import java.io.InputStream;

/**
 * http request response 
 */
public interface IResponse {
 
    /** response ok*/ 
    public static final int RESPONSE_CODE_OK 		= 1 << 4;
    
    /** response failed*/ 
    public static final int RESPONSE_CODE_FAILED 	= 1 << 5;
    
    /**
     * get object array from parse xml , Array list<Object>
     * 
     * @param input
     * @return
     * @throws Exception
     */
	public Object wrapperObject(InputStream input)throws Exception ;
}
