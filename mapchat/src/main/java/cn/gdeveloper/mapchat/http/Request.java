package cn.gdeveloper.mapchat.http;

import java.io.InputStream;

/**
 * The class is http request define
 */
public abstract class Request {
	
	/** request time out */ 
    public static final int REQUEST_TIME_OUT 			= 5000;
    
    /** Http get method*/ 
    public static final int REQUEST_METHOD_GET 			= 1 << 1;
    
    /** post method*/ 
    public static final int REQUEST_METHOD_POST 		= 1 << 2;
    
    /** soap request*/
    public static final int REQUEST_METHOD_SOAP 		= 1 << 3;
    
    /** object value*/ 
    public static final int RESULT_TYPE_OBJECT 			= 1 << 8;
    
    /** byte array*/ 
    public static final int RESULT_TYPE_BYTE_ARRAY 		= 1 << 9;
    
    /** native http response input stream*/ 
    public static final int RESULT_TYPE_STREAM 			= 1 << 10;
    
	/** result type */
    private int resultType;
    
    /** interceptor */
    private IRequestListener listener ;
    
    /**
     * 
     * @param resultType 	: result Type
     */
    public Request(int resultType){
    	this(resultType,null);
    }
    
    /**
     * 
     * @param resultType	: result Type
     * @param listener		: listener
     */
    public Request(int resultType,IRequestListener listener){
    	this.resultType = resultType ;
    	this.listener = listener ;
    }
    
    /**
     * set request listener
     * 
     * @param listener
     */
    public final void setRequestListener(IRequestListener listener){
    	this.listener = listener ;
    }
    
    /**
     * get request listener
     * 
     * @return
     */
    public final IRequestListener getRequestListener(){
    	return listener ;
    }
    
    /**
     * get result type : 
     * 
     * @return RESULT_TYPE_OBJECT_ARRAY | RESULT_TYPE_BYTE_ARRAY | RESULT_TYPE_HTTP_STREAM
     */
    public final int getResultType(){
    	return resultType ;
    }
    
	/**
	 * get request input stream
	 * 
	 * @return : inputStream
	 */
	public abstract InputStream getResquestInputStream() throws Exception;
}
