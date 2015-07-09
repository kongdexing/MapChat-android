package cn.gdeveloper.mapchat.common;

import java.util.HashMap;

/**
 * 卡多拉业务操作状态码
 */
public final class StatusCode {

	public static final String CODE_0 		= "0" ;
	/** 成功 */
	public static final String CODE_1 		= "1" ;
	public static final String CODE_2 		= "2" ;
	public static final String CODE_3 		= "3" ;

	public static final String CODE_100 	= "100" ;
	public static final String CODE_101 	= "101" ;
	public static final String CODE_102 	= "102" ;
	public static final String CODE_103 	= "103" ;
	public static final String CODE_104 	= "104" ;
	public static final String CODE_105 	= "105" ;
	public static final String CODE_106 	= "106" ;
	public static final String CODE_107 	= "107" ;

	public static final String CODE_200 	= "200" ;
	public static final String CODE_201	 	= "201" ;
	public static final String CODE_202 	= "202" ;
	public static final String CODE_203 	= "203" ;
	public static final String CODE_204 	= "204" ;
	public static final String CODE_205 	= "205" ;
	public static final String CODE_206 	= "206" ;
	public static final String CODE_207 	= "207" ;

	public static final String CODE_300 	= "300" ;
	public static final String CODE_301 	= "301" ;
	public static final String CODE_302 	= "302" ;
	public static final String CODE_303 	= "303" ;

	public static final String CODE_400 	= "400" ;
	public static final String CODE_401 	= "401" ;
	public static final String CODE_402 	= "402" ;
	public static final String CODE_403 	= "403" ;

	public static final String CODE_500 	= "500" ;
	public static final String CODE_501 	= "501" ;
	public static final String CODE_502 	= "503" ;

	public static final String CODE_600 	= "600" ;
	public static final String CODE_601 	= "601" ;
	public static final String CODE_602 	= "602" ;

	public static final String CODE_700 	= "700" ;
	public static final String CODE_701 	= "701" ;
	public static final String CODE_702 	= "702" ;

	public static final HashMap<String, String> StatusMsg = new HashMap<String, String>() ;
	
	static {
		
		StatusMsg.put(CODE_0, "未知错误");
		StatusMsg.put(CODE_1, "成功");
		StatusMsg.put(CODE_2, "验证失败");
		StatusMsg.put(CODE_3, "Token为空");
		
		StatusMsg.put(CODE_100, "手机号或邮箱为空");
		StatusMsg.put(CODE_101, "密码为空");
		StatusMsg.put(CODE_102, "用户名或密码错误");
		StatusMsg.put(CODE_103, "该用户已存在");
		StatusMsg.put(CODE_104, "验证码为空");
		StatusMsg.put(CODE_105, "余额不足");
		StatusMsg.put(CODE_106, "充值失败");
		StatusMsg.put(CODE_107, "会员新增失败");
		
		StatusMsg.put(CODE_200, "该用户已经存在相同类型的卡片");
		StatusMsg.put(CODE_201, "会员卡增加失败");
		StatusMsg.put(CODE_202, "付费失败");
		StatusMsg.put(CODE_203, "卡过期");
		StatusMsg.put(CODE_204, "卡余额不足");
		StatusMsg.put(CODE_205, "卡不存在");
		StatusMsg.put(CODE_206, "该卡没激活");
		StatusMsg.put(CODE_207, "退卡申请失败");
		
		StatusMsg.put(CODE_300, "商家登录失败");
		StatusMsg.put(CODE_301, "商家代充值失败");
		StatusMsg.put(CODE_302, "商家代充值余额不足");
		StatusMsg.put(CODE_303, "商家修改失败");
		
		StatusMsg.put(CODE_400, "账单已结账");
		StatusMsg.put(CODE_401, "账单已注销");
		StatusMsg.put(CODE_402, "账单检查客户失败");
		StatusMsg.put(CODE_403, "账单金额不正确");
		
		StatusMsg.put(CODE_500, "促销信息添加失败");
		StatusMsg.put(CODE_501, "促销信息无发送对象");
		StatusMsg.put(CODE_502, "促销信息修改失败");
		
		StatusMsg.put(CODE_600, "账单促销添加失败");
		StatusMsg.put(CODE_601, "账单促销无法发送类型");
		StatusMsg.put(CODE_602, "账单促销修改失败");
		
		StatusMsg.put(CODE_700, "信息已标记为已读");
		StatusMsg.put(CODE_701, "信息修改失败");
		StatusMsg.put(CODE_702, "没有未读消息");
	}
}
