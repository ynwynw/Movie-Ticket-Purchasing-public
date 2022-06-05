package com.franky.movie.dao.common;
/**
 * 订单信息管理数据库操作层
 */
import java.util.Date;
import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.franky.movie.entity.common.Order;
@Repository
public interface OrderDao extends JpaRepository<Order, Long> {
	
	@Query("select o from Order o where o.id = :id")
	Order find(@Param("id")Long id);
	
	@Lock(value=LockModeType.PESSIMISTIC_WRITE)
	Order findBySn(String sn);
	
	List<Order> findByAccountIdOrderByUpdateTimeDesc(Long accountId);
	
	List<Order> findByAccountIdAndStatusOrderByUpdateTimeDesc(Long accountId,Integer status);
	
	List<Order> findByCinemaHallSessionIdAndStatusNot(Long cinemaHallSessionId,Integer status);
	
	@Query("select o.sn from Order o where o.createTime <= :outTime and o.status = :status")
	List<String> findSnList(@Param("outTime")Date outTime ,@Param("status")Integer status);
	
	@Modifying
	@Query("update Order set status = :newStatus where sn = :sn and status = :oldStatus")
	int updateOrderStatus(@Param("sn")String sn,@Param("oldStatus")Integer oldStatus,@Param("newStatus")Integer newStatus);
}
