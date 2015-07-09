package cn.gdeveloper.mapchat.http;

/**
 * Get a interceptor from the service layer is generally achieved by the activity
 */
public interface IRequestListener{

	// interceptor state
    public static final int INTERCEPTOR_SUCCEED 		= 1 << 6 ;
    // interceptor state
    public static final int INTERCEPTOR_FAILED 			= 1 << 7 ;
    
    /**
     * start service interceptor
     * 
     * @return state : INTERCEPTOR_SUCCEED | INTERCEPTOR_FAILED ; if is INTERCEPTOR_SUCCEED , service thread start execute
     * 
     */
    public int startRequest();

    /**
     * when work thread finish, it will notify service.
     * @param Object : return value : byte[] | Object[] | Exception
     */
    public Object requestProcess(Object value);
    
    /**
     * RESPONSE_STATE_CODE_INTERCEPTOR_FAILED : interceptor failed
     * RESPONSE_STATE_CODE_FAILED : server or parser exception
     * RESPONSE_STATE_CODE_OK :  ok
     * end service interceptor, the method run before baseServiceFinished
     */
    public void endRequest(int code, Object value);
}
