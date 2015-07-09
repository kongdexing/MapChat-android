package cn.gdeveloper.mapchat.http;

/**
 * 交互消息定义
 */
public interface MapChatMessageID {

	public static final int MSG_ERROR_NOT_LOGIN = 0x0ff00000;

	// ////////////////////////////////////////////////////////////////////
	public static final int MSG_MEMBER_REGISTER_FAILED = MSG_ERROR_NOT_LOGIN + 1;
	public static final int MSG_MEMBER_REGISTER_SUCCESS = MSG_MEMBER_REGISTER_FAILED + 1;


}
