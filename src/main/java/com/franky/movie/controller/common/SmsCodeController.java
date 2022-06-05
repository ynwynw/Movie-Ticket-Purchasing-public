package com.franky.movie.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.franky.movie.bean.CodeMsg;
import com.franky.movie.bean.Result;
import com.franky.movie.util.SessionUtil;
import com.franky.movie.util.SmsCodeUtil;

/**
 * 系统验证码公用控制器
 * @author frank
 *
 */
@Controller
@RequestMapping("/sms_code")
public class SmsCodeController {

	private Logger log = LoggerFactory.getLogger(SmsCodeController.class);
	
	/**
	 * 统一短信验证码发送接口
	 * @param mobile
	 * @param valilCode
	 * @return
	 */
	@RequestMapping(value="/send_sms_code",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> generateCpacha(
			@RequestParam(name="mobile",required=true)String mobile,//手机号
			@RequestParam(name="valilCode",required=true)String valilCode,//图形验证码
			@RequestParam(name="img_code_method",defaultValue="home_login_img_code")String imgCodeMethod,//图形验证码调用方法名
			@RequestParam(name="sms_code_method",defaultValue="home_login_sms_code")String smsCodeMethod//短信验证码调用方法名
			){
		//验证图形验证码是否正确
		String imgCode = (String)SessionUtil.get(imgCodeMethod);
		if(imgCode == null){
			return Result.error(CodeMsg.SESSION_EXPIRED);
		}
		if(!imgCode.equalsIgnoreCase(valilCode)){
			return Result.error(CodeMsg.CPACHA_ERROR);
		}
		SessionUtil.set(imgCodeMethod, null);
		//表示验证码对比成功，此时生成短信验证码并放入session
		String smsCode = SmsCodeUtil.generateCode();
		if(SmsCodeUtil.sendSmsCode(mobile, smsCode)){
			log.info("短信验证码【" + smsCode + "】发送成功");
			SessionUtil.set(smsCodeMethod, smsCode);
		}
		return Result.success(true);
	}
}
