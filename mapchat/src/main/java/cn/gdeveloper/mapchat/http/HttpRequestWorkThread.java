package cn.gdeveloper.mapchat.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * http Server returns data in response to the thread to achieve.
 */
public final class HttpRequestWorkThread implements Runnable , Cloneable {

    /** buffer size*/
    private static final int BUFFER_SIZE = (1 << 10) * 4;// 4k

    /** http request*/
    private Request 	mResquest ;
    
    /** http response*/
    private IResponse 	mResponse ;
    
    /** http request listener*/
    private IRequestListener mListener ;
    
    protected HttpRequestWorkThread(){
    	
    }
    
    /**
     * http request core thread
     * 
     * @param request
     * @param response
     */
    public void setParameter(Request request,IResponse response){
    	mResquest 	= request ;
    	mResponse 	= response ;
    	mListener 	= request.getRequestListener() ;
    }
    
    /**
     * clone request thread
     * @return
     */
	public HttpRequestWorkThread cloneRequestThread(){
		try{
			return (HttpRequestWorkThread)super.clone() ;
		}catch(CloneNotSupportedException e){
			return null ;
		}
	}
    
    /**
     * down byte[] from server to local memory
     * 
     * @param input : http response stream
     * @return: byte[]
     * @throws Exception : exception
     */
    private static final byte[] readInputStream(InputStream input) throws Exception {
        byte[] out_buffer;
        byte[] buffer ;
        ByteArrayOutputStream out = null;
        try {
            // temp buffer
            out = new ByteArrayOutputStream();
            // read byte[]
            int read = -1;
            buffer = new byte[BUFFER_SIZE];
            // start read
            while ((read = input.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            // get byte[]
            out_buffer = out.toByteArray();
        } catch (Exception e) {
            throw e;
        } finally {
            // release resource
            if (out != null) {
                out.close();
                out = null;
            }
            //release input stream
            if(input != null){
                input.close();
                input = null ;
            }
            // release temp buffer
            buffer = null ;
        }
        // return byte[]
        return out_buffer;
    }

    /**
     * runnable target
     */
    public void run() {
    	final Request resquest 			= mResquest;
    	final IResponse response 		= mResponse ;
    	final IRequestListener listener	= mListener ;
    	// before excue
    	if(listener.startRequest() == IRequestListener.INTERCEPTOR_SUCCEED) {
	        // result value
	        Object result = null;
	        int code = IResponse.RESPONSE_CODE_FAILED;
	        InputStream input = null;
	        byte[] buffer = null;
	        try {
	            // start send request to server
	            input = resquest.getResquestInputStream() ;
	            if(input != null){
		            // doing
		            switch (resquest.getResultType()) {
		            case Request.RESULT_TYPE_STREAM:
		                result = input ;
		                break ;
		            case Request.RESULT_TYPE_BYTE_ARRAY:
		                // return byte[] 
		            	result = readInputStream(input);
		                break;
		            case Request.RESULT_TYPE_OBJECT:
		                buffer = readInputStream(input);
		                // Depending on the parsing engine to parse the object
		                result = response != null ? response.wrapperObject(new ByteArrayInputStream(buffer)) : null ;
		                break;
		            default :
		            	System.out.println("Unkown result type !");
		                break ;
		            }
		            // finish and ok
		            code = IResponse.RESPONSE_CODE_OK;
	            } else {
	            	code = IResponse.RESPONSE_CODE_FAILED;
	            	result = new Exception("InputStream is null!") ;
	            }
	        } catch (Exception e) {
	        	result = e;
	        } finally {
	            // response
	            if(code == IResponse.RESPONSE_CODE_OK)
	            	result = listener.requestProcess(result);
	            // end request
	            listener.endRequest(code ,result);
	            // release resource
	            result = null ;
	            buffer = null ;
	            input = null ;
	        }
        }else{
        	listener.endRequest(IResponse.RESPONSE_CODE_FAILED, "StartRequest return failed.") ;
        }
    }
}
