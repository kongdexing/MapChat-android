package cn.gdeveloper.mapchat.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HTTP instance factory. 
 * 	execute HTTP request in thread pool . default 2 thread in pool.
 */
public final class HttpFactory  {

    /** core thread pool */
	private static final int CORE_POOL_SIZE 			= 3;

    /** thread factory*/
    private final ThreadFactory mThreadFactory = new ThreadFactory() {
        //
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(r, "#HTTP Work Thread" + mCount.getAndIncrement());
            thread.setDaemon(true);// set the thread is daemon thread
            return thread;
        }
    };
    
    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    /*public Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, sPoolWorkQueue, mThreadFactory,
            new ThreadPoolExecutor.DiscardOldestPolicy());*/
    
    /** work thread*/
    private final HttpRequestWorkThread   mWorker = new HttpRequestWorkThread() ;
    
    /** server thread pool instance*/
    private ExecutorService mExecutorService ;
    private ExecutorService mSingleService;

    /**
     *
     */
    private HttpFactory() {
    	
    }

    /**
     * create a new HTTP Factory
     * 
     * @return
     */
    protected static HttpFactory createHttpFactory() {
        return new HttpFactory();
    }

    /**
     * create request object return index of data
     */
    public synchronized void pushHttpRequest(Request request,IResponse response) {
    	
    	// start execute the HTTP request target
    	final HttpRequestWorkThread thread = mWorker.cloneRequestThread() ;
    	if(thread == null){
    		return ;
    	}
    	
    	// if thread pool is null
    	if(mExecutorService == null){
    		mExecutorService = Executors.newFixedThreadPool(CORE_POOL_SIZE, mThreadFactory);;
    	}
    	
    	final ExecutorService	executor = mExecutorService ;
    	
    	// push thread to pool
    	thread.setParameter(request, response) ;
    	executor.execute(thread);
    }
    
    /**
     * 
     * @param task
     */
    protected void executeRunnable(Runnable task){
    	
    	if(task == null) return ;
    	
    	if(mSingleService == null){
    		mSingleService = Executors.newSingleThreadExecutor();
    	}
    	
    	mSingleService.execute(task);
    }
    
    /**
     * 
     * @param request
     * @param response
     * @return
     */
    protected Runnable buildRunnable(Request request,IResponse response){
    	
    	// start execute the HTTP request target
    	final HttpRequestWorkThread thread = mWorker.cloneRequestThread() ;
    	if(thread == null){
    		return null ;
    	}
    	
    	// push thread to pool
    	thread.setParameter(request, response) ;
    	
    	return thread ;
    }
    
    /**
     * shut down all HTTP request
     */
    protected void shutDown(){
    	
    	if(mExecutorService != null) {
    		mExecutorService.shutdownNow() ;
    		mExecutorService = null ;
    	}
    	
    	if(mSingleService != null){
    		mSingleService.shutdownNow() ;
    		mSingleService	= null ;
    	}
    }

    /**
     * 
     * @param url
     * @param resultType
     * @return
     */
    public static Request getHttpGetRequest(String url, int resultType) {
		return new HttpGetRequest(url, resultType);
	}

    /**
     * 
     * @param url
     * @param parameter
     * @param resultType
     * @return
     */
	public static Request getHttpPostRequest(String url,String parameter, int resultType) {
		return new HttpPostRequest(url, parameter, resultType);
	}

	/**
	 * get http connection
	 * @param flag
	 * @param requestUrl
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	private static HttpURLConnection getHttpConnection(int flag ,String requestUrl,String parameter) throws Exception {
        HttpURLConnection connection = null;
        final URL url = new URL(requestUrl);
        // open connection
        connection = (HttpURLConnection) url.openConnection();
        // connection read time out
        connection.setReadTimeout(Request.REQUEST_TIME_OUT);
        connection.setConnectTimeout(Request.REQUEST_TIME_OUT);
        // set connection
        if (flag == Request.REQUEST_METHOD_POST) {
            connection.setRequestMethod("POST");
            // application/x-www-form-urlencoded // application/json
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            // write parameter
			OutputStream output = connection.getOutputStream();
			output.write(parameter.getBytes());
			output.flush();
			output.close();
			output = null;
        } else if (flag == Request.REQUEST_METHOD_GET) {
            connection.setRequestMethod("GET");
        }
        // connnection
		connection.connect();
        // result
        return connection;
    }
	
    /**
	 * 
	 * @author zhouwei
	 * 
	 */
	private static class HttpGetRequest extends Request {

		private String mUrl;

		public HttpGetRequest(String url, int resultType) {
			super(resultType);
			mUrl = url != null ? url.trim() : "";
		}

		@Override
		public InputStream getResquestInputStream() throws Exception {
			// get server inputstream
			return getHttpConnection(REQUEST_METHOD_GET, mUrl,null).getInputStream();
		}
	}

	/**
	 * 
	 * @author zhouwei
	 * 
	 */
	private static class HttpPostRequest extends Request {

		private String mUrl;

		private String mParameter;

		public HttpPostRequest(String url, String parameter, int resultType) {
			super(resultType);
			mUrl = url != null ? url.trim() : "";
			mParameter = (parameter == null ? "" : parameter);
		}

		@Override
		public InputStream getResquestInputStream() throws Exception {
			// get server inputstream
			return getHttpConnection(REQUEST_METHOD_POST, mUrl,mParameter).getInputStream();
		}
	}
}
