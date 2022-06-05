package com.franky.movie.service.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.franky.movie.dao.common.OrderItemDao;
import com.franky.movie.entity.common.Order;
import com.franky.movie.entity.common.OrderItem;

/**
 * 订单子项信息service层
 * @author frank
 *
 */
@Service
public class OrderItemService {
	
	@Autowired
	private OrderItemDao orderItemDao;
	
	/**
	 * 当order的id不为空时进行编辑，当id为空时则进行添加
	 * @param order
	 * @return
	 */
	public OrderItem save(OrderItem orderItem){
		return orderItemDao.save(orderItem);
	}
	
	/**
	 * 根据orderId查找
	 * @param orderId
	 * @return
	 */
	public List<OrderItem> find(Long orderId){
		return orderItemDao.findByOrderId(orderId);
	}
	
	/**
	 * 根据订单id列表查询
	 * @param orderIds
	 * @return
	 */
	public List<OrderItem> findList(List<Long> orderIds){
		return orderItemDao.findList(orderIds);
	}
	
	/**
	 * 根据订单id列表查询
	 * @param orderIds
	 * @return
	 */
	public List<OrderItem> findListByOrders(List<Order> orders){
		List<Long> orderIds = new ArrayList<Long>();
		for(Order order : orders){
			orderIds.add(order.getId());
		}
		return findList(orderIds);
	}
	
	/**
	 * 获取订单子项座位的id
	 * @param orders
	 * @return
	 */
	public List<Long> findOrderItemSeatIds(List<Order> orders){
		List<Long> orderItemIds = new ArrayList<Long>();
		if(orders == null || orders.size() == 0)return orderItemIds;
		List<OrderItem> orderItems = findListByOrders(orders);
		for(OrderItem orderItem : orderItems){
			orderItemIds.add(orderItem.getCinemaHallSeat().getId());
		}
		return orderItemIds;
	}
	
	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public OrderItem findById(Long id){
		return orderItemDao.find(id);
	}
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(Long id){
		orderItemDao.deleteById(id);
	}
	
	/**
	 * 批量删除
	 * @param orderItems
	 */
	public void deleteAll(List<OrderItem> orderItems){
		orderItemDao.deleteInBatch(orderItems);
	}
	
	
}
