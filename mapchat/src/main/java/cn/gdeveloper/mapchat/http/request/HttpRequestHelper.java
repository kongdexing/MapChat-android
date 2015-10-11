package cn.gdeveloper.mapchat.http.request;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpRequestHelper {

    // core thread pool
    private static final int CORE_POOL_SIZE = 5;

    private ExecutorService mExecutorService;
    private HttpRequestThread mRequestThread;
    private static HttpRequestHelper mInstance = new HttpRequestHelper();
    private Context mContext;

    // thread factory
    private final ThreadFactory mThreadFactory = new ThreadFactory() {

        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(r, "#HTTP Work Thread" + mCount.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    };

    public static HttpRequestHelper getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        mExecutorService = Executors.newFixedThreadPool(CORE_POOL_SIZE, mThreadFactory);
        mRequestThread = new HttpRequestThread();
    }

    public int sendHttpGetRequest(String url, String parameters, RequestListener listener) {
        return sendRequest(new HttpRequest(HttpRequest.REQUEST_METHOD_GET, url, parameters), listener);
    }

    public int sendHttpPostRequest(String url, String parameters, RequestListener listener) {
        return sendRequest(new HttpRequest(HttpRequest.REQUEST_METHOD_POST, url, parameters), listener);
    }

    public int sendHttpPutRequest(String url, String parameters, RequestListener listener) {
        return sendRequest(new HttpRequest(HttpRequest.REQUEST_METHOD_PUT, url, parameters), listener);
    }

    public int sendHttpDeleteRequest(String url, String parameters, RequestListener listener) {
        return sendRequest(new HttpRequest(HttpRequest.REQUEST_METHOD_DELETE, url, parameters), listener);
    }

    private int sendRequest(HttpRequest request, RequestListener listener) {

        if (listener == null || request == null)
            return -1;
        // init
        request.setRequestListener(listener);
        // start execute http request thread
        pushHttpRequest(request);
        return 1;
    }

    public void pushHttpRequest(HttpRequest request) {
        // start execute the HTTP request target
        final HttpRequestThread thread = mRequestThread.cloneRequestThread();
        if (thread == null) {
            return;
        }
        // push thread to pool
        thread.setParameter(mContext, request);
        mExecutorService.execute(thread);
    }

}
