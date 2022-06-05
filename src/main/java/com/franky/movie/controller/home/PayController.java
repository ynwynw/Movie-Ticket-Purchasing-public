package com.franky.movie.controller.home;
/**
 * 支付统一入口控制器
 */
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.franky.movie.constant.RuntimeConstant;
import com.franky.movie.entity.common.Account;
import com.franky.movie.entity.common.PayLog;
import com.franky.movie.pay.Alipay;
import com.franky.movie.service.common.AccountService;
import com.franky.movie.service.common.PayLogService;

@RequestMapping("/home/pay")
@Controller
public class PayController {
	
	@Autowired
	private PayLogService payLogService;
	@Autowired
	private AccountService accountService;
	
	private Logger log = LoggerFactory.getLogger(PayController.class);
	
	@RequestMapping(value="/to_pay")
	public String toPay(@RequestParam(name="sn",required=true)String sn,Model model){
		PayLog payLog = payLogService.find(sn);
		if(payLog == null){
			model.addAttribute("msg", "支付记录不存在！");
			return RuntimeConstant.RUN_ERROR_VIEW;
		}
		if(payLog.getStatus() == PayLog.status_paid){
			model.addAttribute("msg", "已经支付成功，请勿重复发起支付！");
			return RuntimeConstant.RUN_ERROR_VIEW;
		}
		if(payLog.getMoney().compareTo(new BigDecimal(0)) <= 0){
			model.addAttribute("msg", "支付金额错误！");
			return RuntimeConstant.RUN_ERROR_VIEW;
		}
		//到这表示一切合法，根据支付方式调起支付
		switch (payLog.getPaymentType()) {
			case alipay:{
				String html = Alipay.generatePayHtml(payLog.getSn(), payLog.getMoney(), "用户充值", "用户【" + payLog.getAccount().getMobile() + "】充值【" + payLog.getMoney() + "】");
				model.addAttribute("payHtml", html);
				log.info("进入支付宝电脑网站支付");
				return "home/pay/alipay_pc";
			}
			default:
				break;
		}
		model.addAttribute("msg", "未定义的支付方式！");
		return RuntimeConstant.RUN_ERROR_VIEW;
	}
	
	/**
	 * 支付宝异步通知
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/alipay_notify")
	@ResponseBody
	public String alipayNotify(HttpServletRequest request){
		if(!Alipay.isValid(request)){
			log.error("支付宝异步通知签名验证失败!");
			return "fail";
		}
		//签名验证通过
		//商户订单号
		String sn = request.getParameter("out_trade_no");
		//支付宝交易金额
		String total_amount = request.getParameter("total_amount");
		//交易状态
		String trade_status = request.getParameter("trade_status");
		if("TRADE_SUCCESS".equals(trade_status)){
			//表示是支付成功
			//查询支付记录
			PayLog payLog = payLogService.find(sn);
			if(payLog == null){
				log.error("支付记录未找到sn=" + sn);
				return "fail";
			}
			//检查支付记录的状态
			if(payLog.getStatus() != PayLog.status_unpay){
				log.error("支付记录状态错误，status=" + payLog.getStatus());
				return "fail";
			}
			//检查支付的金额是否与支付记录中相符
			if(payLog.getMoney().compareTo(new BigDecimal(total_amount)) != 0){
				log.error("支付金额错误，支付记录金额=" + payLog.getMoney() + "支付宝通知支付金额=" + total_amount);
				return "fail";
			}
			//所有一切都符合，此时增加用户的余额
			Account account = payLog.getAccount();
			account.setBalance(account.getBalance().add(payLog.getMoney()));
			accountService.save(account);
			payLog.setStatus(PayLog.status_paid);
			payLogService.save(payLog);
			log.info("支付成功，用户最新余额=" + account.getBalance());
		}
		return "success";
	}
}
