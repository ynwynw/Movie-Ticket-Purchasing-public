package com.franky.movie.controller.home;
/**
 * 订单控制器
 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.franky.movie.bean.CodeMsg;
import com.franky.movie.bean.Result;
import com.franky.movie.constant.RuntimeConstant;
import com.franky.movie.constant.SessionConstant;
import com.franky.movie.entity.common.Account;
import com.franky.movie.entity.common.CinemaHallSeat;
import com.franky.movie.entity.common.CinemaHallSession;
import com.franky.movie.entity.common.Order;
import com.franky.movie.entity.common.OrderItem;
import com.franky.movie.service.common.AccountService;
import com.franky.movie.service.common.CinemaHallSeatService;
import com.franky.movie.service.common.CinemaHallSessionService;
import com.franky.movie.service.common.OrderItemService;
import com.franky.movie.service.common.OrderService;
import com.franky.movie.util.SessionUtil;
import com.franky.movie.util.StringUtil;

@RequestMapping("/home/order")
@Controller
public class HomeOrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CinemaHallSessionService cinemaHallSessionService;
	@Autowired
	private CinemaHallSeatService cinemaHallSeatService;
	@Value("${movie.order.timeout}")
	private Integer orderTimeout;//订单过期时间
	private Logger log = LoggerFactory.getLogger(HomeOrderController.class);
	
	/**
	 * 生成订单
	 * @param cinemaHallSessionId
	 * @param cinemaHallSeatIds
	 * @return
	 */
	@RequestMapping(value="/generate_order",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> toPay(
			@RequestParam(name="cinema_hall_session_id",required=true)Long cinemaHallSessionId,
			@RequestParam(name="cinema_hall_seat_ids",required=true)String cinemaHallSeatIds
			){
		List<CinemaHallSeat> cinemaHallSeatList = JSONObject.parseArray(cinemaHallSeatIds, CinemaHallSeat.class);
		Account account = (Account)SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
		CinemaHallSession cinemaHallSession = cinemaHallSessionService.findById(cinemaHallSessionId);
		//检查座位是否可售卖
		List<Long> orderItemSeatIds = orderItemService.findOrderItemSeatIds(orderService.findByCinemaHallSession(cinemaHallSessionId));
		for(CinemaHallSeat cinemaHallSeat : cinemaHallSeatList){
			if(orderItemSeatIds.contains(cinemaHallSeat.getId())){
				//说明该座位状态不可售卖
				return Result.error(CodeMsg.HOME_ADD_ORDER_SEAT_UNABLE);
			}
		}
		//座位检查一切正常
		Order order = new Order();
		order.setAccount(account);
		order.setCinemaHallSession(cinemaHallSession);
		order.setNewMoney(cinemaHallSession.getNewPrice().multiply(new BigDecimal(cinemaHallSeatList.size())));
		order.setOldMoney(cinemaHallSession.getOldPrice().multiply(new BigDecimal(cinemaHallSeatList.size())));
		order.setNum(cinemaHallSeatList.size());
		order.setSn(StringUtil.generateSn());
		//开始组装订单子项
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		for(CinemaHallSeat cinemaHallSeat : cinemaHallSeatList){
			OrderItem orderItem = new OrderItem();
			orderItem.setCinemaHallSeat(cinemaHallSeat);
			orderItem.setMoney(cinemaHallSession.getNewPrice());
			orderItems.add(orderItem);
		}
		if(!orderService.generateOrder(order, orderItems)){
			return Result.error(CodeMsg.HOME_ADD_ORDER_ERROR);
		}
		log.info("订单已经生成");
		return Result.success(order.getSn());
	}
	
	/**
	 * 订单确认支付页面
	 * @param sn
	 * @param model
	 * @return
	 */
	@RequestMapping(value="order_pay",method=RequestMethod.GET)
	public String orderPay(@RequestParam(name="order_sn",required=true)String sn,Model model){
		Order order = orderService.find(sn);
		if(order == null){
			model.addAttribute("msg", "订单编号不存在!");
			return RuntimeConstant.RUN_ERROR_VIEW;
		}
		if(order.getStatus() != Order.status_unpay){
			model.addAttribute("msg", "订单状态不可支付!");
			return RuntimeConstant.RUN_ERROR_VIEW;
		}
		long passTime = System.currentTimeMillis()/1000 - order.getCreateTime().getTime()/1000;
		if(passTime > orderTimeout){
			model.addAttribute("msg", "订单已超时，不可支付!");
			return RuntimeConstant.RUN_ERROR_VIEW;
		}
		//订单状态都合适
		model.addAttribute("leftTime", orderTimeout - passTime);
		model.addAttribute("order", order);
		model.addAttribute("orderItemList", orderItemService.find(order.getId()));
		return "home/order/pay_order";
	}
	
	/**
	 * 订单支付
	 * @param sn
	 * @return
	 */
	@RequestMapping(value="order_pay",method=RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> orderPay(@RequestParam(name="order_sn",required=true)String sn){
		Order order = orderService.find(sn);
		if(order == null){
			return Result.error(CodeMsg.HOME_ORDER_NO_EXIST);
		}
		if(order.getStatus() != Order.status_unpay){
			return Result.error(CodeMsg.HOME_ORDER_STATUS_UNABLE);
		}
		long passTime = System.currentTimeMillis()/1000 - order.getCreateTime().getTime()/1000;
		if(passTime > orderTimeout){
			return Result.error(CodeMsg.HOME_ORDER_STATUS_TIMEOUT);
		}
		if(order.getAccount().getBalance().compareTo(order.getNewMoney()) < 0){
			return Result.error(CodeMsg.HOME_ORDER_BALANCE_OUT);
		}
		//订单状态都合适
		//1、订单状态修改成已支付；2、扣除用户余额
		if(!orderService.paySuccess(order)){
			return Result.error(CodeMsg.HOME_ORDER_PAY_ERROR);
		}
		return Result.success(true);
	}
}
