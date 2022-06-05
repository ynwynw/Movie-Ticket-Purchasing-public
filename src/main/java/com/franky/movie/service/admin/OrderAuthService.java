package com.franky.movie.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.franky.movie.dao.admin.OrderAuthDao;
import com.franky.movie.entity.admin.OrderAuth;

/**
 * 后台操作类 数据库操作service
 * @author frank
 *
 */
@Service
public class OrderAuthService {
	
	@Autowired
	private OrderAuthDao orderAuthDao;
	
	/**
	 * 添加/修改操作日志，当id不为空时，修改，id为空时自动新增一条记录
	 * @param orderAuth
	 * @return
	 */
	public OrderAuth save(OrderAuth orderAuth){
		return orderAuthDao.save(orderAuth);
	}
	
	/**
	 * 获取一条记录
	 * @return
	 */
	public OrderAuth findOne(){
		List<OrderAuth> findAll = orderAuthDao.findAll(Sort.by(Direction.DESC, "createTime"));
		if(findAll == null || findAll.size() < 1)return null;
		return findAll.get(0);
	}
}
