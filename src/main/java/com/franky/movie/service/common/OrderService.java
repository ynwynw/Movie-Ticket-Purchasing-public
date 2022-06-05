package com.franky.movie.service.common;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.franky.movie.bean.PageBean;
import com.franky.movie.constant.SessionConstant;
import com.franky.movie.dao.common.OrderDao;
import com.franky.movie.entity.common.Account;
import com.franky.movie.entity.common.Movie;
import com.franky.movie.entity.common.Order;
import com.franky.movie.entity.common.OrderItem;
import com.franky.movie.util.SessionUtil;

/**
 * 订单信息service层
 * @author frank
 *
 */
@Service
public class OrderService {
	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private MovieService movieService;
	/**
	 * 当order的id不为空时进行编辑，当id为空时则进行添加
	 * @param order
	 * @return
	 */
	public Order save(Order order){
		return orderDao.save(order);
	}
	
	/**
	 * 根据编号查找
	 * @param sn
	 * @return
	 */
	@Transactional
	public Order find(String sn){
		return orderDao.findBySn(sn);
	}
	
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public Order findById(Long id){
		return orderDao.find(id);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Long id){
		orderDao.deleteById(id);
	}
	
	/**
	 * 根据用户id查询
	 * @param accountId
	 * @return
	 */
	public List<Order> findAll(Long accountId){
		return orderDao.findByAccountIdOrderByUpdateTimeDesc(accountId);
	}
	
	/**
	 * 获取指定账号的已支付订单
	 * @param accountId
	 * @return
	 */
	public List<Order> findAllPaid(Long accountId){
		return orderDao.findByAccountIdAndStatusOrderByUpdateTimeDesc(accountId,Order.status_paid);
	}
	
	/**
	 * 根据场次id查找
	 * @param cinemaHallSessionId
	 * @return
	 */
	public List<Order> findByCinemaHallSession(Long cinemaHallSessionId){
		return orderDao.findByCinemaHallSessionIdAndStatusNot(cinemaHallSessionId,Order.status_cancel);
	}
	
	/**
	 * 获取所有的超时且未支付的订单
	 * @param outTime
	 * @return
	 */
	public List<String> findTimeOutList(Date outTime){
		return orderDao.findSnList(outTime, Order.status_unpay);
	}
	
	/**
	 * 生成订单
	 * @param order
	 * @param orderItems
	 * @return
	 */
	@org.springframework.transaction.annotation.Transactional
	public boolean generateOrder(Order order,List<OrderItem> orderItems){
		try {
			order = save(order);
			for(OrderItem orderItem : orderItems){
				orderItem.setOrder(order);
				orderItemService.save(orderItem);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 取消订单
	 * @param sn
	 * @return
	 */
	@org.springframework.transaction.annotation.Transactional
	public boolean cancelOrder(String sn){
		return orderDao.updateOrderStatus(sn, Order.status_unpay, Order.status_cancel) > 0;
	}
	
	/**
	 * 修改订单状态
	 * @param sn
	 * @param newStatus
	 * @param oldStatus
	 * @return
	 */
	@org.springframework.transaction.annotation.Transactional
	public boolean updateOrderStatus(String sn,int oldStatus,int newStatus){
		return orderDao.updateOrderStatus(sn, oldStatus,newStatus) > 0;
	}

	/**
	 * 支付成功
	 * @param order
	 * @return
	 */
	@org.springframework.transaction.annotation.Transactional
	public boolean paySuccess(Order order) {
		if(updateOrderStatus(order.getSn(), Order.status_unpay, Order.status_paid)){
			//扣除用户余额
			Account account = order.getAccount();
			account.setBalance(account.getBalance().subtract(order.getNewMoney()));
			accountService.save(account);
			SessionUtil.set(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY, account);
			//修改电影的总票房
			Movie movie = order.getCinemaHallSession().getMovie();
			movie.setTotalMoney(movie.getTotalMoney().add(order.getNewMoney()));
			movieService.save(movie);
		}
		return true;
	}
	
	/**
	 * 删除订单
	 * @param order
	 * @return
	 */
	@org.springframework.transaction.annotation.Transactional
	public boolean delete(Order order) {
		List<OrderItem> orderItems = orderItemService.find(order.getId());
		orderItemService.deleteAll(orderItems);
		delete(order.getId());
		return true;
	}
	
	/**
	 * 分页获取订单列表
	 * @param order
	 * @param pageBean
	 * @return
	 */
	public PageBean<Order> findPage(Order order,PageBean<Order> pageBean){
		ExampleMatcher withMatcher = ExampleMatcher.matching().withMatcher("sn", GenericPropertyMatchers.contains());
		withMatcher = withMatcher.withIgnorePaths("oldMoney","newMoney","status");
		Example<Order> example = Example.of(order, withMatcher);
		Sort sort = Sort.by(Direction.DESC, "updateTime");
		Pageable pageable = PageRequest.of(pageBean.getCurrentPage()-1, pageBean.getPageSize(),sort);
		Page<Order> findAll = orderDao.findAll(example, pageable);
		pageBean.setContent(findAll.getContent());
		pageBean.setTotal(findAll.getTotalElements());
		pageBean.setTotalPage(findAll.getTotalPages());
		return pageBean;
	}
	
	/**
	 * 返回总数
	 * @return
	 */
	public long count(){
		return orderDao.count();
	}
}
