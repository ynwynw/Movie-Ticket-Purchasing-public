package com.franky.movie.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 * 短信发送示例
 * @author 互亿无线官方API(Java版)
 *
 */
public class IhuySmsUtil {

	private static String Url = "http://106.ihuyi.com/webservice/sms.php?method=Submit";
	
	public static boolean sendSms(String mobile,String code){
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(Url);

		client.getParams().setContentCharset("GBK");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=GBK");
	    String content = new String("您的验证码是：" + code + "。请不要把验证码泄露给其他人。");
		NameValuePair[] data = {//提交短信
//			    new NameValuePair("account", "01234567890"), //查看账号号码 登录用户中心->验证码通知短信>产品总览->API接口信息->API ID
			    new NameValuePair("account", "C06623633"), //查看API ID 登录用户中心->验证码通知短信>产品总览->API接口信息->API ID
//			    new NameValuePair("password", "frank123456"), //查看密码 登录用户中心->验证码通知短信>产品总览->API接口信息->API KEY
			    new NameValuePair("password", "9953e7f5c78061ff456c4d1c149d2883"), //查看API KEY 登录用户中心->验证码通知短信>产品总览->API接口信息->API KEY
			    //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
			    new NameValuePair("mobile", mobile), 
			    new NameValuePair("content", content),
		};
		method.setRequestBody(data);

		try {
			client.executeMethod(method);
			
			String SubmitResult =method.getResponseBodyAsString();

			System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();

			String rstCode = root.elementText("code");
			String msg = root.elementText("msg");
			String smsid = root.elementText("smsid");

			System.out.println(rstCode);
			System.out.println(msg);
			System.out.println(smsid);

			 if(!"2".equals(rstCode)){
				System.out.println("短信提交失败");
				return false;
			}

		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
