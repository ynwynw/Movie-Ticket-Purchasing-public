package com.franky.movie.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 系统运行时的常量
 * @author frank
 *
 */
public class RuntimeConstant {

	//后台登录拦截器无需拦截的url
	public static List<String> adminLoginExcludePathPatterns = Arrays.asList(
			"/system/login",
			"/system/auth_order",
			"/admin/css/**",
			"/admin/fonts/**",
			"/admin/js/**",
			"/admin/images/**",
			"/error",
			"/cpacha/generate_cpacha",
			"/sms_code/send_sms_code",
			"/photo/**",
			"/upload/upload_photo",
			"/download/**",
			"/home/**"
		);
	//后台权限拦截器无需拦截的url
	public static List<String> adminAuthorityExcludePathPatterns = Arrays.asList(
			"/system/login",
			"/system/index",
			"/system/no_right",
			"/system/auth_order",
			"/admin/css/**",
			"/admin/fonts/**",
			"/admin/js/**",
			"/admin/images/**",
			"/error",
			"/cpacha/generate_cpacha",
			"/sms_code/send_sms_code",
			"/system/logout",
			"/system/update_userinfo",
			"/system/update_pwd",
			"/photo/**",
			"/upload/upload_photo",
			"/download/download_video",
			"/home/**"
		);
	//前台登录拦截器无需拦截的url
	public static List<String> homeLoginExcludePathPatterns = Arrays.asList(
			"/system/**",
			"/admin/**",
			"/error",
			"/cpacha/generate_cpacha",
			"/sms_code/send_sms_code",
			"/photo/**",
			"/upload/upload_photo",
			"/download/**",
			"/home/index/**",
			"/home/cinema/**",
			"/home/news/**",
			"/home/help/**",
			"/home/images/**",
			"/home/css/**",
			"/home/js/**",
			"/home/pay/alipay_notify",
			"/home/movie/**"
			);
	//前台权限拦截器无需拦截的url
	public static List<String> homeGloablExcludePathPatterns = Arrays.asList(
			"/system/**",
			"/admin/**",
			"/error",
			"/cpacha/generate_cpacha",
			"/sms_code/send_sms_code",
			"/upload/upload_photo",
			"/photo/**",
			"/download/download_video",
			"/home/images/**",
			"/home/css/**",
			"/home/pay/alipay_notify",
			"/home/js/**"
			);
	public final static String RUN_ERROR_VIEW = "error/runtime_error";
}
