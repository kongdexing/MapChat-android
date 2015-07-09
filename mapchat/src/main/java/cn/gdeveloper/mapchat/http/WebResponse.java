package cn.gdeveloper.mapchat.http;

import android.os.Handler;

/**
 * HTTP 请求监听器
 * 
 * 1. 开始请求
 * 2. 结束请求 (覆盖dispatchResponse来处理自己的逻辑)
 */
public class WebResponse implements IRequestListener ,Cloneable {

	/** 请求消息 */
	public static final class HttpRequestMessage {
		
		/** 请求开始 */
		public static final int MSG_REQUEST_START	= 0x0f000000 ;
		/** 请求结束 */
		public static final int MSG_REQUEST_END		= MSG_REQUEST_START + 1 ;
	}
	
	/** 请求模式(前台模式(会发生request消息)|后台模式) */
	public static final class HttpRequestMode {
		
		/** 前台模式 */ 
		public static final int MODE_FOREGROUND	= 1 ;
		/** 后台模式 */
		public static final int MODE_BACKGROUND	= MODE_FOREGROUND + 1 ;
	}
	
	/** 请求模式*/
	private int mode ;
	/** handler */
	private Handler handler ;
	
	public WebResponse(){
		this(HttpRequestMode.MODE_BACKGROUND,null);
	}
	
	public WebResponse(Handler handler){
		this(HttpRequestMode.MODE_FOREGROUND,handler);
	}
	
	public WebResponse(WebResponse response){
		this(response.mode,response.handler);
	}
	
	public WebResponse(int mode, Handler handler){
		this.mode 	  = mode ;
		this.handler  = handler ;
	}
	
	public final void init(WebResponse response){
		init(response.mode,response.handler);
	}
	
	public final void init(int mode, Handler handler){
		this.mode 	  = mode ;
		this.handler  = handler ;
	}
	
	public final boolean sendMessage(int what){
		return sendMessage(what,null) ;
	}
	
	public final boolean sendMessage(int what,Object obj){
		if(handler != null) {
			handler.obtainMessage(what, obj).sendToTarget() ;
			return true ;
		} else {
			return false ;
		}
	}
	
	@Override
	public final int startRequest() {
		
		if(mode == HttpRequestMode.MODE_FOREGROUND){
			sendMessage(HttpRequestMessage.MSG_REQUEST_START) ;
		}
		
		return INTERCEPTOR_SUCCEED;
	}
	
	@Override
	public final void endRequest(int code, Object value) {
		
		if(mode == HttpRequestMode.MODE_FOREGROUND){
			sendMessage(HttpRequestMessage.MSG_REQUEST_END) ;
		}
		
		if(code == IResponse.RESPONSE_CODE_FAILED) {
			requestProcess(value);
		}
	}
	
	@Override
	public final Object requestProcess(Object value) {
		
		// done
		dispatchResponse(value) ;
		// return
		return value ;
	}
	
	/** JSON 响应 .子类覆盖此方法*/
	public void dispatchResponse(Object value){
		
	}
}
