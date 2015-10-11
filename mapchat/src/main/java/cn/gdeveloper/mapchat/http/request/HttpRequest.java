package cn.gdeveloper.mapchat.http.request;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {

    private final String TAG = HttpRequest.class.getSimpleName();
    public static final int REQUEST_TIME_OUT = 20 * 1000;
    public static final int REQUEST_METHOD_GET = 1;
    public static final int REQUEST_METHOD_POST = 2;
    public static final int REQUEST_METHOD_PUT = 3;
    public static final int REQUEST_METHOD_DELETE = 4;

    private RequestListener listener;

    private int mRequestType;
    private String mRequestUrl;
    private String mParameters;

    public HttpRequest(int mRequestType, String mRequestUrl, String parameters) {
        this.mRequestType = mRequestType;
        this.mRequestUrl = mRequestUrl != null ? mRequestUrl.trim() : "";
        mParameters = parameters;
    }

    public RequestListener getRequestListener() {
        return listener;
    }

    public void setRequestListener(RequestListener listener) {
        this.listener = listener;
    }

    public HttpURLConnection getHttpConnection() {
        try {
            Log.i("URL", mRequestUrl);
            URL url = new URL(mRequestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(REQUEST_TIME_OUT);
            conn.setConnectTimeout(REQUEST_TIME_OUT);
            conn.setDoInput(true);

            if (mRequestType == REQUEST_METHOD_POST
                    || mRequestType == REQUEST_METHOD_PUT) {

                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                if (mRequestType == REQUEST_METHOD_POST) {
                    conn.setRequestMethod("POST");
                } else
                    conn.setRequestMethod("PUT");

                DataOutputStream os = new DataOutputStream(
                        conn.getOutputStream());
                if (mParameters != null) {
                    Log.i(TAG, "parameter json :" + mParameters);
                }

                if (mParameters != null) {
                    os.write(mParameters.getBytes());
                }

//                if (mParameters.getmImgStream() != null) {
//                    String imgType = mParameters.getmImgType() + "\r\n";
//                    String imgContentEncoding = "Content-Encoding-Type:Binary\r\n\r\n";
//
//                    os.write(boundary.getBytes());
//                    os.write(imgType.getBytes());
//                    os.write(imgContentEncoding.getBytes());
//                    byte[] bts = InputStreamToByte(mParameters.getmImgStream());
//
//                    os.write(bts);
//                }

                os.flush();
            } else if (mRequestType == REQUEST_METHOD_GET) {
                conn.setRequestMethod("GET");
            } else if (mRequestType == REQUEST_METHOD_DELETE) {
                conn.setRequestMethod("DELETE");
            }
            conn.connect();
            // result
            return conn;
        } catch (IOException e) {
            Log.i("network", "connect error :" + e.getMessage());
            return null;
        }
    }

    private byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte data[] = bytestream.toByteArray();
        bytestream.close();
        return data;
    }

}
