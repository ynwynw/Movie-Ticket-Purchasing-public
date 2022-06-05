package com.franky.movie.controller.home;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.franky.movie.bean.CodeMsg;
import com.franky.movie.bean.PaymentType;
import com.franky.movie.bean.Result;
import com.franky.movie.constant.SessionConstant;
import com.franky.movie.entity.common.Account;
import com.franky.movie.entity.common.Order;
import com.franky.movie.entity.common.OrderItem;
import com.franky.movie.entity.common.PayLog;
import com.franky.movie.service.common.AccountService;
import com.franky.movie.service.common.CinemaCommentService;
import com.franky.movie.service.common.MovieCommentService;
import com.franky.movie.service.common.OrderItemService;
import com.franky.movie.service.common.OrderService;
import com.franky.movie.service.common.PayLogService;
import com.franky.movie.util.SessionUtil;
import com.franky.movie.util.StringUtil;

/**
 * 前台用户中心
 * @author frank
 *
 */
@RequestMapping("/home/account")
@Controller
public class HomeAccountController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private PayLogService payLogService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private MovieCommentService movieCommentService;
	@Autowired
	private CinemaCommentService cinemaCommentService;
	
	/**
	 * 前台用户中心首页
	 * @param model
	 * @return
	 */
	@RequestMapping("/user-center")
	public String index(Model model){
		Account account = (Account)SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
		model.addAttribute("orderList", orderService.findAllPaid(account.getId()));
		return "home/account/user-center";
	}
	
	/**
	 * 个人资料页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/user-info",method=RequestMethod.GET)
	public String userInfo(Model model){
		return "home/account/user-info";
	}
	
	/**
	 * 修改密码页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/update-pwd",method=RequestMethod.GET)
	public String updatePwd(Model model){
		return "home/account/update-pwd";
	}
	
	/**
	 * 修改头像页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/update-head-pic",method=RequestMethod.GET)
	public String updateHeadPic(Model model){
		return "home/account/update-head-pic";
	}
	
	/**
	 * 账户余额
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/user-account",method=RequestMethod.GET)
	public String userAccount(Model model){
		Account account = (Account)SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
		account = accountService.findById(account.getId());
		SessionUtil.set(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY, account);
		model.addAttribute("payLogList", payLogService.findAll(account.getId()));
		return "home/account/user-account";
	}
	
	/**
	 * 用户充值页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/user-pay-to-account",method=RequestMethod.GET)
	public String userPayToAccount(Model model){
		model.addAttribute("payments", PaymentType.values());
		return "home/account/user-pay-to-account";
	}
	
	/**
	 * 修改个人资料
	 * @param account
	 * @return
	 */
	@RequestMapping(value="/update_info",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> updateInfo(Account account){
		Account loginedAccount = (Account)SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
		loginedAccount.setNickname(account.getNickname());
		loginedAccount.setSex(account.getSex());
		if(accountService.save(loginedAccount) == null){
			return Result.error(CodeMsg.SAVE_ERROR);
		}
		//表示用户已经修改成功，放入session
		SessionUtil.set(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY, loginedAccount);
		return Result.success(true);
	}
	
	/**
	 * 修改图片
	 * @param account
	 * @return
	 */
	@RequestMapping(value="/update_head_pic",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> updateHeadPic(Account account){
		Account loginedAccount = (Account)SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
		loginedAccount.setHeadPic(account.getHeadPic());
		if(accountService.save(loginedAccount) == null){
			return Result.error(CodeMsg.SAVE_ERROR);
		}
		//表示用户已经修改成功，放入session
		SessionUtil.set(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY, loginedAccount);
		return Result.success(true);
	}
	
	/**
	 * 修改密码
	 * @param password
	 * @param checkCode
	 * @return
	 */
	@RequestMapping(value="/update_pwd",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> updatePwd(
			@RequestParam(name="password",required=true)String password,
			@RequestParam(name="checkCode",required=true)String checkCode
			){
		//首先检查短信验证码是否正确
		String smsCode = (String)SessionUtil.get("user_update_pwd_sms_code");
		if(smsCode == null){
			return Result.error(CodeMsg.SESSION_EXPIRED);
		}
		if(!smsCode.equalsIgnoreCase(checkCode)){
			return Result.error(CodeMsg.SMS_CODE_ERROR);
		}
		//短信验证码正确
		SessionUtil.set("user_update_pwd_sms_code", null);
		Account account = (Account)SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
		account.setPassword(password);
		accountService.save(account);
		//放入session
		SessionUtil.set(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY, account);
		return Result.success(true);
	}
	
	/**
	 * 生成支付记录
	 * @param payLog
	 * @return
	 */
	@RequestMapping(value="/generate_pay_log",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> generatePayLog(PayLog payLog){
		Account account = (Account)SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
		payLog.setAccount(account);
		payLog.setSn(StringUtil.generateSn());
		if(payLog.getMoney() == null){
			return Result.error(CodeMsg.DATA_ERROR);
		}
		if(payLog.getMoney().compareTo(new BigDecimal(0)) <= 0){
			return Result.error(CodeMsg.DATA_ERROR);
		}
		payLogService.save(payLog);
		return Result.success(payLog.getSn());
	}
	
	/**
	 * 订单列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/user-order-list",method=RequestMethod.GET)
	public String userOrderList(Model model){
		Account account = (Account)SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
		model.addAttribute("orderList", orderService.findAll(account.getId()));
		return "home/account/user-order-list";
	}
	
	
	/**
	 * 用户评价列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/user-comment",method=RequestMethod.GET)
	public String userComment(Model model,@RequestParam(name="type",defaultValue="0")Integer type){
		Account account = (Account)SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
		if(type != 0){
			model.addAttribute("cinemaCommentList", cinemaCommentService.findByAccount(account.getId()));
		}else{
			model.addAttribute("movieCommentList", movieCommentService.findByAccount(account.getId()));
		}
		model.addAttribute("type", type);
		return "home/account/user-comment";
	}
	
	/**
	 * 获取订单子项
	 * @param sn
	 * @return
	 */
	@RequestMapping(value="/get_order_item",method=RequestMethod.POST)
	@ResponseBody
	public Result<List<OrderItem>> getOrderItem(@RequestParam(name="order_sn",required=true)String sn){
		Order order = orderService.find(sn);
		if(order == null){
			return Result.error(CodeMsg.HOME_ORDER_NO_EXIST);
		}
		return Result.success(orderItemService.find(order.getId()));
	}
	
	/**
	 * 删除订单
	 * @param sn
	 * @return
	 */
	@RequestMapping(value="/delete_order",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> deleteOrder(@RequestParam(name="order_sn",required=true)String sn){
		Order order = orderService.find(sn);
		if(order == null){
			return Result.error(CodeMsg.HOME_ORDER_NO_EXIST);
		}
		if(order.getStatus() == Order.status_paid){
			return Result.error(CodeMsg.HOME_ORDER_UNABLE_DELETE);
		}
		//先删除订单子项、再删除订单
		orderService.delete(order);
		return Result.success(true);
	}
}
