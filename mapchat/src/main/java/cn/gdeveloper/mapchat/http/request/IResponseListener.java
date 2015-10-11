package cn.gdeveloper.mapchat.http.request;

public interface IResponseListener {

    public void onStart();

    public void onRequestResponse(int code, Object value);
}
