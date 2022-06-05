package com.franky.movie.dao.admin;
/**
 * 后台操作日志类数据库操作层
 */
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.franky.movie.entity.admin.OrderAuth;

@Repository
public interface OrderAuthDao extends JpaRepository<OrderAuth, Long> {
	
}
