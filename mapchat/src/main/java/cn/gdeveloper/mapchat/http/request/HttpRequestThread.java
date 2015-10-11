package cn.gdeveloper.mapchat.http.request;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class HttpRequestThread implements Runnable, Cloneable {

    private static final String TAG = HttpRequestThread.class.getSimpleName();

    private static final int MSG_START = 0;
    private static final int MSG_RESPONSE = 1;

    private static final int BUFFER_SIZE = (1 << 10) * 4;// 4k
    private HttpRequest mResquest;

    private RequestListener mListener;

    private Context mContext;

    Object result = null;
    int responseCode = RequestListener.NETWORK_UNCONNECTED;

    private Handler mHandler;

    public void setParameter(Context context, HttpRequest request) {
        mContext = context;
        mResquest = request;
        mListener = request.getRequestListener();

        mHandler = new Handler(mContext.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_START:
                        Log.i(TAG, "MSG_START");
                        mListener.onStart();
                        break;
                    case MSG_RESPONSE:
                        Log.i(TAG, "MSG_RESPONSE, code : " + responseCode+" result "+result.toString());
                        mListener.onResponse(responseCode, result);
                        result = null;
                        break;
                }
            }
        };
    }

    public HttpRequestThread cloneRequestThread() {
        try {
            return (HttpRequestThread) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    private byte[] readInputStream(InputStream input)
            throws Exception {

        byte[] out_buffer;
        byte[] buffer;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            int read = -1;
            buffer = new byte[BUFFER_SIZE];
            while ((read = input.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out_buffer = out.toByteArray();
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null) {
                out.close();
                out = null;
            }
            if (input != null) {
                input.close();
                input = null;
            }
            buffer = null;
        }
        return out_buffer;
    }

    @Override
    public void run() {
        final HttpRequest resquest = mResquest;
        final RequestListener listener = mListener;
        // before excue
        if (listener != null) {
            mHandler.sendEmptyMessage(MSG_START);
            // result value
            InputStream input = null;
            HttpURLConnection connect = null;

            try {
                // start send request to server
                connect = resquest.getHttpConnection();
                // input = resquest.getResquestInputStream();
                if (connect != null) {
                    // return byte[]
                    responseCode = connect.getResponseCode();
                    if (responseCode == 200) {
                        input = connect.getInputStream();
                    } else {
                        input = connect.getErrorStream();
                    }
                    result = readInputStream(input);

                    connect.disconnect();
                } else {
                    responseCode = RequestListener.NETWORK_UNCONNECTED;
                    result = ErrorText.ERROR_CONNECT;
                }
            } catch (Exception e) {
                result = "";
            } finally {
                // response
                mHandler.sendEmptyMessage(MSG_RESPONSE);
                input = null;
                connect = null;
            }
        }
    }


}
