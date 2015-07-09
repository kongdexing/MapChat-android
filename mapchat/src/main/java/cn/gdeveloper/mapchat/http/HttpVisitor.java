package cn.gdeveloper.mapchat.http;

/**
 * HTTP Access service. The class contain multithread request
 */
public class HttpVisitor {

    /** HTTP instance factory */
    private final HttpFactory mHttpFactory = HttpFactory.createHttpFactory();

    /**
     * default constructor
     */
    public HttpVisitor() {
    	
    }

    /**
     * execute request
     * 
     * @param request
     * @param listener
     * @param response
     * @return
     */
    private final int sendRequest(final Request request,final IRequestListener listener,final IResponse response) {
        
    	/**
         * callback not null
         * When the request returns an array of objects of type time, xml
         * parsing engine can not be null
         */
    	if(listener == null || request == null) 
    		return -1;
    	
    	// HTTP factory
    	final HttpFactory http = mHttpFactory ;
    	// init
    	request.setRequestListener(listener);
        // start execute http request thread
    	http.pushHttpRequest(request,response);
        //
        return 1;
    }

    /**
     * get http get byte[]
     * 
     * @param url				: url address : http://xxxx
     * @param listener 			: request listener
     * @return 					
     */
    public final int sendHttpGetResponseByteArray(String url, IRequestListener listener) {
        return sendRequest(HttpFactory.getHttpGetRequest(url, Request.RESULT_TYPE_BYTE_ARRAY), listener , null);
    }

    /**
     * get http get input stream
     * 
     * @param url 				: url address : http://xxxx
     * @param listener 			: request listener
     * @return				
     */
    public final int sendHttpGetResponseStream(String url, IRequestListener listener){
    	return sendRequest(HttpFactory.getHttpGetRequest(url, Request.RESULT_TYPE_STREAM), listener , null);
    }
    
    /**
     * get http post byte[]
     * 
     * @param url : url address
     * @param parameter : post parameter
     * @param listener : request listener
     * @param response : response
     * @return
     */
    public final int sendHttpPostRequest(String url, String parameter, IRequestListener listener,IResponse response) {
        return sendRequest(HttpFactory.getHttpPostRequest(url, parameter,Request.RESULT_TYPE_OBJECT), listener,response);
    }

    /**
     * get http post input stream
     * 
     * @param url
     * @param parameter
     * @param listener
     * @return
     */
    public final int sendHttpPostResponseByteArray(String url, String parameter, IRequestListener listener) {
        return sendRequest(HttpFactory.getHttpPostRequest(url, parameter,Request.RESULT_TYPE_BYTE_ARRAY), listener,null);
    }
    
    /**
     * 
     * @param url
     * @param parameter
     * @param listener
     * @return
     */
    public final int sendHttpPostResponseStream(String url, String parameter, IRequestListener listener) {
        return sendRequest(HttpFactory.getHttpPostRequest(url, parameter,Request.RESULT_TYPE_STREAM), listener,null);
    }
  
    /**
     * 
     * @param request
     * @param listener
     * @param response
     * @return
     */
    public final int sendSoapRequest(Request request, IRequestListener listener,IResponse response){
    	return sendRequest(request, listener ,response);
    }
    
    /**
     * 
     * @param url
     * @param resultType
     * @return
     */
    public final Request getHttpRequest(String url,int resultType){
    	return HttpFactory.getHttpGetRequest(url, resultType) ;
    }
    
    /**
     * 
     * @param request
     * @param listener
     * @param response
     * @return
     */
    public final int execute(final Request request,final IRequestListener listener){
    	
    	if(listener == null || request == null) 
    		return -1;
    	
    	// HTTP factory
    	final HttpFactory http = mHttpFactory ;
    	// init
    	request.setRequestListener(listener);
        // start execute http request thread
    	http.executeRunnable(http.buildRunnable(request, null)) ;
    	
    	return 1 ;
    }
    
    /**
     * Stop All Current HTTP request
     */
    public final void shutDown(){
    	
    	if(mHttpFactory != null) 
    		mHttpFactory.shutDown() ;
    }
}
