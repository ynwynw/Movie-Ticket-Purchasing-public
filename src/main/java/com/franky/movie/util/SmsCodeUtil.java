package com.franky.movie.util;

import java.util.Random;

/**
 * 短信验证码操作类
 * @author frank
 *
 */
public class SmsCodeUtil {

	public final static int sms_code_length = 6;//默认短信验证码长度
	
	/**
	 * 生成随机验证码
	 * @return
	 */
	public static String generateCode(){
		Random random = new Random();
		String ret = "";
		for(int i=0; i< sms_code_length; i++){
			ret += random.nextInt(10);
		}
		return ret;
	}
	
	/**
	 * 发送短信接口
	 * @param mobile
	 * @param code
	 * @return
	 */
	public static boolean sendSmsCode(String mobile,String code){
		//此处调用第三方短信接口的sdk进行发送短信
		return IhuySmsUtil.sendSms(mobile, code);
		//return true;
	}
	
	/**
	 * 发送短信接口
	 * @param mobile
	 * @return
	 */
	public static boolean sendSmsCode(String mobile){
		return sendSmsCode(mobile, generateCode());
	}
}
